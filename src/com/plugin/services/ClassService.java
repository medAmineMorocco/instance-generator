package com.plugin.services;

import com.plugin.beans.Field;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.*;

import static java.util.Collections.emptyList;

public class ClassService {

    private final FakerService fakerService;

    public ClassService(String localeCode) {
        fakerService = new FakerService(localeCode);
    }

    public String getInstance(String bean, List<Field> parameters) {
        String values = parameters
                .stream()
                .map(field -> {
                    Object value = fakerService.getFakeForField(field.getName());
                    String mappedValue = null;
                    if (field.getType() == null) {
                        mappedValue = value.toString();
                    }
                    if ("String".equals(field.getType())) {
                        mappedValue = "\"" + value + "\"";
                    }
                    else if ("List".equals(field.getType()) && "String".equals(field.getSubClassName())) {
                            mappedValue = "Arrays.asList(\"\")";
                    }
                    else if ("List".equals(field.getType()) && !"String".equals(field.getSubClassName())) {
                        mappedValue = "Arrays.asList(" + "new " + field.getSubClassName() + "()" + ")";
                    }
                    else if (field.getType() != null && !"List".equals(field.getType()) && !"String".equals(field.getType())) {
                        mappedValue = "new " + field.getType() + "()";
                    }
                    return mappedValue;
                })
                .reduce((value1, value2) -> value1 + ", " + value2)
                .orElse("");

        return "new " + bean + "(" + values + ")";
    }

    public String getInstances(String bean, List<Field> parameters, int count) {
        List<String> instances = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            instances.add(getInstance(bean, parameters));
        }
        String result = instances.stream()
                                 .reduce((value1, value2) -> value1 + ",\n" + value2)
                                 .orElse("");
        return "Arrays.asList(\n" + result + "\n)";
    }

    public List<Field> getParametersOfLongestConstructor(Class aClass) throws IOException {
        Optional<Constructor> longestConstructor = Arrays.stream(aClass.getConstructors())
                                                         .max(Comparator.comparingInt(Constructor::getParameterCount));
        if (longestConstructor.isPresent()) {
            return getParameterNames(longestConstructor.get());
        }
        return emptyList();
    }

    private List<Field> getParameterNames(Constructor<?> constructor) throws IOException {
        Class<?> declaringClass = constructor.getDeclaringClass();
        ClassLoader declaringClassLoader = declaringClass.getClassLoader();

        Type declaringType = Type.getType(declaringClass);
        String constructorDescriptor = Type.getConstructorDescriptor(constructor);
        String url = declaringType.getInternalName() + ".class";

        InputStream classFileInputStream = declaringClassLoader.getResourceAsStream(url);
        if (classFileInputStream == null) {
            throw new IllegalArgumentException("The constructor's class loader cannot find the bytecode that defined the constructor's class (URL: " + url + ")");
        }

        ClassNode classNode;
        try {
            classNode = new ClassNode();
            ClassReader classReader = new ClassReader(classFileInputStream);
            classReader.accept(classNode, 0);
        } finally {
            classFileInputStream.close();
        }

        List<MethodNode> methods = classNode.methods;
        for (MethodNode method : methods) {
            if (method.name.equals("<init>") && method.desc.equals(constructorDescriptor)) {
                Type[] argumentTypes = Type.getArgumentTypes(method.desc);
                List<Field> parameterNames = new ArrayList<>(argumentTypes.length);

                List<LocalVariableNode> localVariables = method.localVariables;
                for (int i = 0; i < argumentTypes.length; i++) {
                    String name = localVariables.get(i + 1).name;
                    String type = getType(localVariables.get(i + 1).desc);
                    String subClassName = null;
                    if ("List".equals(type)) {
                        subClassName = getSubClassName(localVariables.get(i + 1));
                    }
                    parameterNames.add(new Field(name, type, subClassName));
                }
                return parameterNames;
            }
        }

        return emptyList();
    }

    private String getType(String description) {
        String type = description.split("/").length == 3
                ?
                description.split("/")[2]
                           .replace(";", "")
                           .replace(">", "")
                :
                null;
        if (type != null && type.contains("$")) {
            type = type.split("\\$")[1];
        }
        return type;
    }

    private String getSubClassName(LocalVariableNode localVariableNode) {
        return getType(localVariableNode.signature.split("<")[1]);
    }

}
