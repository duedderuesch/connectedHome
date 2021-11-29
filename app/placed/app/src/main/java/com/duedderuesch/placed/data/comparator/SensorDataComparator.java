package com.duedderuesch.placed.data.comparator;

import com.duedderuesch.placed.data.entities.SensorData;
import com.duedderuesch.placed.utils.Utils;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class SensorDataComparator implements Comparator<SensorData> {


    @Override
    public int compare(SensorData sd1, SensorData sd2) {
        try{
            Double date1;
            Double date2;

            String year1 = sd1.getYear().toString();
            String month1 = sd1.getMonth().toString();
            String day1 = sd1.getDay().toString();
            String hour1 = sd1.getHour().toString();
            String minute1 = sd1.getMinute().toString();

            if(month1.length() != 2) month1 = "0"+month1;
            if(day1.length() !=2) day1 = "0"+day1;
            if(hour1.length() !=2) hour1 = "0"+hour1;
            if(minute1.length() !=2) minute1 = "0"+ minute1;

            date1 = Double.valueOf(year1+month1+day1+hour1+minute1);

            String year2 = sd2.getYear().toString();
            String month2 = sd2.getMonth().toString();
            String day2 = sd2.getDay().toString();
            String hour2 = sd2.getHour().toString();
            String minute2 = sd2.getMinute().toString();

            if(month2.length() != 2) month2 = "0"+month2;
            if(day2.length() !=2) day2 = "0"+day2;
            if(hour2.length() !=2) hour2 = "0"+hour2;
            if(minute2.length() !=2) minute2 = "0"+ minute2;

            date2 = Double.parseDouble(year2 + month2 + day2 + hour2 + minute2);

            return date1.compareTo(date2);
        } catch (Exception e){

            return 0;
        }

    }
}
