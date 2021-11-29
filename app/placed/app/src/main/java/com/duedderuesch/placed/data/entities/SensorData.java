package com.duedderuesch.placed.data.entities;

public class SensorData {

    public static class DataType{
        public static String single = "single";
        public static String average_year = "average-year";
        public static String average_month = "average-month";
        public static String average_day = "average-day";
        public static String average_hour = "average-hour";
    }

    private Integer id;
    private String  sensor;
    private Integer is_generic;
    private String  value;
    private String  data_type;
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Integer minute;



    public SensorData(Integer id,
                      String  sensor,
                      Integer is_generic,
                      String  value,
                      String  data_type,
                      Integer year,
                      Integer month,
                      Integer day,
                      Integer hour,
                      Integer minute){
        this.id = id;
        this.sensor = sensor;
        this.is_generic = is_generic;
        this.value = value;
        this.data_type = data_type;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public Integer getId() {
        return id;
    }

    public String getSensor() {
        return sensor;
    }

    public Integer getIs_generic() {
        return is_generic;
    }

    public String getValue() {
        return value;
    }

    public String getData_type() {
        return data_type;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getDay() {
        return day;
    }

    public Integer getHour() {
        return hour;
    }

    public Integer getMinute() {
        return minute;
    }
}
