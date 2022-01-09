package com.plugin;

import com.plugin.beans.Field;
import com.plugin.services.FakerService;
import com.plugin.services.FieldService;

import java.util.List;

public class Test {

    private static FakerService fakerService = new FakerService();

    private static FieldService fieldService = new FieldService();

    public static void main(String[] args) {
        String bean = Person.class.getSimpleName();

        List<Field> fields = fieldService.getFields(Person.class);
        System.out.println(fields);

        String values = fields
                         .stream()
                         .map(field -> {
                             Object value = fakerService.getFakeForField(field.getName());
                             if ("String".equals(field.getType())) {
                                 return "\"" + value + "\"";
                             }
                             else if ("List".equals(field.getType())) {
                                 return "Arrays.asList()";
                             }
                             return value.toString();
                         })
                         .reduce((value1, value2) -> value1 + ", " + value2)
                         .orElse(null);

        System.out.println("new " + bean + "(" + values + ")");

    }
}
