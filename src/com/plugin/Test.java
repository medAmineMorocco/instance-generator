package com.plugin;

import com.plugin.beans.Field;
import com.plugin.services.ClassService;

import java.io.IOException;
import java.util.List;

public class Test {

    public static final String LOCALE_CODE = "en";

    private static ClassService classService = new ClassService(LOCALE_CODE);

    public static void main(String[] args) {
        String bean = Person.class.getSimpleName();

        try {
            List<Field> parameters = classService.getParametersOfLongestConstructor(Person.class);

            System.out.println(classService.getInstance(bean, parameters));
            System.out.println("=============");
            System.out.println(classService.getInstances(bean, parameters, 3));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
