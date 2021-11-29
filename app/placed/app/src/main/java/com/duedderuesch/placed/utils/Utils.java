package com.duedderuesch.placed.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.preference.PreferenceManager;

import com.duedderuesch.placed.R;
import com.duedderuesch.placed.data.entities.SensorData;

import java.text.SimpleDateFormat;
import java.util.*;
//import org.apache.http.conn.util.InetAddressUtils;

public class Utils {

    public static SimpleDateFormat dateFormat_OnlyDate = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
    public static SimpleDateFormat dateFormat_Time = new SimpleDateFormat("HH:mm:ss", Locale.GERMAN);

    public static SimpleDateFormat specialDateFormat_DateAndHour = new SimpleDateFormat("dd.MM.yyyy.HH", Locale.GERMAN);
    public static SimpleDateFormat specialDateFormat_DayAndMonth = new SimpleDateFormat("dd.MM.", Locale.GERMAN);
    public static SimpleDateFormat specialDateFormat_Hour = new SimpleDateFormat("HH:00", Locale.GERMAN);

    public static class StatusCode{
        public static Integer successful = 100;
        public static Integer no_error = 101;
        public static Integer not_authorized = 403;
        public static Integer not_found = 404;
        public static Integer wrong_args = 501;
        public static Integer failed = 999;

    }

    public static String getLocalServerIP(Context ctx){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String ip = prefs.getString("et_pref_server_localip", "xxx.xxx.xxx.xxx");
        if(ip != null && !ip.equals("xxx.xxx.xxx.xxx"))return ip;
        else return null;

    }

    public static String sensorDataDatetime(SensorData sensorData){
        String year = sensorData.getYear().toString();
        String month = sensorData.getMonth().toString();
        String day = sensorData.getDay().toString();
        String hour = sensorData.getHour().toString();
        String minute = sensorData.getMinute().toString();

        if(month.length() != 2) month = "0"+month;
        if(day.length() !=2) day = "0"+day;
        if(hour.length() !=2) hour = "0"+hour;
        if(minute.length() !=2) minute = "0"+ minute;

        return day+"."+month+"."+year+" "+hour+":"+minute;
    }

    public static Double sensorDataDatetimeNumber(SensorData sensorData){
        String year = sensorData.getYear().toString();
        String month = sensorData.getMonth().toString();
        String day = sensorData.getDay().toString();
        String hour = sensorData.getHour().toString();
        String minute = sensorData.getMinute().toString();

        if(month.length() != 2) month = "0"+month;
        if(day.length() !=2) day = "0"+day;
        if(hour.length() !=2) hour = "0"+hour;
        if(minute.length() !=2) minute = "0"+ minute;

        return Double.valueOf(year+month+day+hour+minute);
    }

    public static Double DatetimeNumber(String timestamp){
        String year = timestamp.substring(6,10);
        String month = timestamp.substring(3,5);
        String day = timestamp.substring(0,2);
        String hour = timestamp.substring(11,13);
        String minute = timestamp.substring(14,16);

        return Double.valueOf(year+month+day+hour+minute);
    }

    public static String sensorDataDate(SensorData sensorData){
        String year = sensorData.getYear().toString();
        String month = sensorData.getMonth().toString();
        String day = sensorData.getDay().toString();

        if(month.length() != 2) month = "0"+month;
        if(day.length() !=2) day = "0"+day;

        return day+"."+month+"."+year;
    }

    public static int getItemHeightofListView(ListView listView, int items) {
        ListAdapter adapter = listView.getAdapter();
        int UNBOUNDED = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        int grossElementHeight = 0;
        for (int i = 0; i < items; i++) {
            View childView = adapter.getView(i, null, listView);
            childView.measure(UNBOUNDED, UNBOUNDED);
            grossElementHeight += childView.getMeasuredHeight();
        }
        return grossElementHeight;
    }

    public static String today(){
        Calendar cal = Calendar.getInstance();
        return dateFormat_OnlyDate.format(cal.getTime());
    }

    public static String time(){
        Calendar cal = Calendar.getInstance();
        return dateFormat_Time.format(cal.getTime());
    }
}