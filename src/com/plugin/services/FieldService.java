package com.plugin.services;

import com.plugin.beans.Field;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class FieldService {

    public List<Field> getFields(Class aClass) {
        return Arrays.stream(aClass.getDeclaredFields())
                     .map(field -> {
                         String typeName = field.getType().getSimpleName();
                         if ("List".equals(typeName)) {
                             String actualTypeArgument = field.getGenericType().getTypeName().split("<")[1].split("\\.")[2].replace(">", "");
                             return new Field(field.getName(), field.getType().getSimpleName(), actualTypeArgument);
                         }
                         return new Field(field.getName(), field.getType().getSimpleName());
                     })
                     .collect(toList());
    }

}
