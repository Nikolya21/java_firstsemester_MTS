package com.mipt.nikolyakhachatryan.model;

public class Human {
    private String name, last_name;
    private int age;
    boolean is_worker;
    public String getName() {
        return name;
    }

    public String getLast_name() {
        return last_name;
    }

    public int getAge() {
        return age;
    }

    public boolean isIs_worker() {
        return is_worker;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setIs_worker(boolean is_worker) {
        this.is_worker = is_worker;
    }

}