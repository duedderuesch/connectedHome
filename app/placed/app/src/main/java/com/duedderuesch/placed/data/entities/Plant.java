package com.duedderuesch.placed.data.entities;

public class Plant {
    private Integer id;
    private String  name;
    private String  biological_name;
    private String  home;
    private String  birthdate;
    private Integer min_humidity;
    private Integer max_humidity;
    private Integer min_temperature;
    private Integer max_temperature;
    private String  note;

    public Plant(Integer  id,
                 String  name,
                 String  biological_name,
                 String  home,
                 String  birthdate,
                 Integer  min_humidity,
                 Integer   max_humidity,
                 Integer min_temperature,
                 Integer max_temperature,
                 String note){
        this.id = id;
        this.name = name;
        this.biological_name = biological_name;
        this.home = home;
        this.birthdate = birthdate;
        this.min_humidity = min_humidity;
        this.max_humidity = max_humidity;
        this.min_temperature = min_temperature;
        this.max_temperature = max_temperature;
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBiological_name() {
        return biological_name;
    }

    public String getHome() {
        return home;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public Integer getMin_humidity() {
        return min_humidity;
    }

    public Integer getMax_humidity() {
        return max_humidity;
    }

    public Integer getMin_temperature() {
        return min_temperature;
    }

    public Integer getMax_temperature() {
        return max_temperature;
    }

    public String getNote() {
        return note;
    }
}
