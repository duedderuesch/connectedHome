package com.duedderuesch.placed.data.entities;

public class Home {
    private Integer id;
    private String name;
    private String address;

    public Home(Integer id, String name, String address){
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
