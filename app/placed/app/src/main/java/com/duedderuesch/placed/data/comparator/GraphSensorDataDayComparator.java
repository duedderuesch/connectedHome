package com.duedderuesch.placed.data.comparator;

import com.duedderuesch.placed.utils.Utils;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class GraphSensorDataDayComparator implements Comparator<HashMap<String, String>> {


    @Override
    public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
        try{
            Date date_o1 = Utils.dateFormat_OnlyDate.parse(o1.get("day"));
            Date date_o2 = Utils.dateFormat_OnlyDate.parse(o2.get("day"));
            return date_o1.compareTo(date_o2);
        } catch (Exception e){

            return 0;
        }

    }
}
