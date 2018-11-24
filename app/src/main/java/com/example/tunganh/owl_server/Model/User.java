package com.example.tunganh.owl_server.Model;

public class User {
    private String Pass;
    private String Name;
    private String Phone;
    private String Admin;

    public User() {
    }

    public User(String pass, String name, String phone, String admin) {
        Pass = pass;
        Name = name;
        Phone = phone;
        Admin = admin;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAdmin() {
        return Admin;
    }

    public void setAdmin(String admin) {
        Admin = admin;
    }
}
