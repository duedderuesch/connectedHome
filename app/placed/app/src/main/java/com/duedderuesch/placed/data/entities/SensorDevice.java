package com.duedderuesch.placed.data.entities;

public class SensorDevice {

    public static class SensorType{
        public static String temperature = "temperature";
        public static String soil_humidity = "soil-humidity";
        public static String air_humidity = "air-humidity";
        public static String weather = "weather";
    }

    private Integer id;
    private String  name;
    private String  sensor_type;
    private Integer is_generic;
    private String  plant;
    private String  home;
    private String  server;
    private String  ip;


    public SensorDevice(Integer id,
                        String  name,
                        String  sensor_type,
                        Integer is_generic,
                        String  plant,
                        String  home,
                        String  server,
                        String  ip){

        this.id = id;
        this.name = name;
        this.sensor_type = sensor_type;
        this.is_generic = is_generic;
        this.plant = plant;
        this.home = home;
        this.server = server;
        this.ip = ip;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSensor_type() {
        return sensor_type;
    }

    public Integer getIs_generic() {
        return is_generic;
    }

    public String getPlant() {
        return plant;
    }

    public String getHome() {
        return home;
    }

    public String getServer() {
        return server;
    }

    public String getIp() {
        return ip;
    }
}
