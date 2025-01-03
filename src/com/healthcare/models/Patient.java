package com.healthcare.models;

public class Patient {
    private int id;
    private String name;
    private int age;
    private String contact;

    public Patient(int id, String name, int age, String contact) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.contact = contact;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getContact() { return contact; }
}
