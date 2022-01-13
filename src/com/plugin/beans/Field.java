package com.plugin.beans;

public class Field {

    private String name;

    private String type;

    private String subClassName;

    public Field() {
    }

    public Field(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Field(String name, String type, String subClassName) {
        this.name = name;
        this.type = type;
        this.subClassName = subClassName;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSubClassName() {
        return subClassName;
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", subClassName='" + subClassName + '\'' +
                '}';
    }

}
