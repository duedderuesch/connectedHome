package com.duedderuesch.placed.data.comparator;

import android.util.Log;

import com.duedderuesch.placed.utils.Utils;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class GraphSensorDataHourComparator implements Comparator<HashMap<String, String>> {


    @Override
    public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
        try {
            Date date_o1 = Utils.specialDateFormat_DateAndHour.parse(o1.get("hour"));
            Date date_o2 = Utils.specialDateFormat_DateAndHour.parse(o2.get("hour"));
            return date_o1.compareTo(date_o2);
        } catch (Exception e) {
            Log.e("GRAPGH_ACTION", e.toString());
            return 0;
        }

    }
}

