package com.plugin;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class Person {

    private String firstName;

    private String lastName;

    private int age;

    private Papa papa;

    private List<String> options;

    private List<Book> books;

    class Papa {
        private String name;
    }

    class Book {
        private String name;
    }

    public Person(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
}
