package com.duedderuesch.placed.data.entities;

public class User {
    private String username;
    private String email;
    private String birthdate;
    private String full_name;

    public User(String username, String full_name, String email, String birthdate){
        this.username = username;
        this.full_name = full_name;
        this.email = email;
        this.birthdate = birthdate;
    }

    public String getUsername(){
        return this.username;
    }

    public String getFull_name(){ return this.full_name;}

}
