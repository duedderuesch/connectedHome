package com.duedderuesch.placed.data.entities;

import java.io.StringBufferInputStream;

public class Server {
    public static class ServerType{
        public static String LOCAL = "local";
        public static String FIREBASE = "firebase";
    }


    private Integer id;
    private String  name;
    private String  home;
    private String  server_type;
    private String  address;

    public Server(Integer id,
                  String name,
                  String home,
                  String server_type,
                  String address){

        this.id = id;
        this.name = name;
        this.home = home;
        this.server_type = server_type;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHome() {
        return home;
    }

    public String getServer_type() {
        return server_type;
    }

    public String getAddress() {
        return address;
    }
}
