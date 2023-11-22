package com.example.lab05_v2;

public class Person {
    private Integer id;
    private String name;
    private String dob;
    private String email;

    private String avatar;

    public Person(String name, String dob, String email, String avatar) {
        this.id = null;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.avatar = avatar;
    }
    public Person(int id, String name, String dob, String email, String avatar) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar(){ return avatar;}
}