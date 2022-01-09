package com.plugin;

import java.util.List;

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
}
