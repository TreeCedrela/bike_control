package com.example.autobike.enity;

public class user {

    public int id;
    public String name;
    public String password;
    public String e_mail;
    public int remember;

    public user(){

    }

    public user(String name, String password, String e_mail, int remember) {
        this.name = name;
        this.password = password;
        this.e_mail = e_mail;
        this.remember = remember;
    }

    @Override
    public String toString() {
        return "user{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", e_mail='" + e_mail + '\'' +
                '}';
    }
}
