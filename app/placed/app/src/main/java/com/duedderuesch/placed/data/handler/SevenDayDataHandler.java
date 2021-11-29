package com.duedderuesch.placed.data.handler;

import android.content.Context;
import android.util.Log;

import com.duedderuesch.placed.data.comparator.SensorDataComparator;
import com.duedderuesch.placed.data.entities.SensorData;
import com.duedderuesch.placed.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class SevenDayDataHandler {
    private final String TAG = "SevenDayDataHandler";

    private ArrayList<Float> dataList;
    private ArrayList<SensorData> sourceDataList;
    private ArrayList<String> sourceDataDateList;
    private Boolean dataInPercent = true;

    public SevenDayDataHandler(Context ctx, ArrayList<SensorData> sourceDataList){
        super();
        sourceDataList.sort(new SensorDataComparator());
        this.sourceDataList = sourceDataList;
        this.dataList = calculateAverageForDays();

    }

    public ArrayList<Float> getDataList() {
        return dataList;
    }

    public ArrayList<String> getSourceDataDateList() {
        return sourceDataDateList;
    }

    /**
     * creates array of the last 7 dates (the last is today) and an 7 sized array with nulls
     *
     * dates of sensorData is compared to the first array, the index from the comparison
     * is used to fill the second array with the corresponding averaged values
     *
     * @return dataList         (aka. the second array)
     */
    private ArrayList<Float> calculateAverageForDays(){
        ArrayList<Float> queryDataList = new ArrayList<>();
        ArrayList<String> queryDayList = new ArrayList<>();

        try{
            for (int i=0; i<7;i++){
                Calendar calToday = Calendar.getInstance();
                calToday.add(Calendar.DAY_OF_YEAR, (-1*(6-i)));
                queryDayList.add(Utils.dateFormat_OnlyDate.format(calToday.getTime()));
                queryDataList.add(null);
            }

        } catch (Exception e){
            Log.e(TAG, e.toString());
        }


        for (int i=0;i<sourceDataList.size();i++){
            SensorData querySensorData = sourceDataList.get(i);
            try {
                if (queryDayList.contains(Utils.sensorDataDate(querySensorData))) {
                    int index = queryDayList.indexOf(Utils.sensorDataDate(querySensorData));
                    if(queryDataList.get(index) != null) {
                        queryDataList.set(index, (queryDataList.get(index) + Float.parseFloat(querySensorData.getValue())) / 2);
                    } else {
                        queryDataList.set(index, Float.valueOf(querySensorData.getValue()));
                    }
                }
            } catch (Exception e){
                Log.e(TAG, e.toString());
            }

        }

        for(int i=0; i<7; i++){
            if(queryDataList.get(i) == null){
                queryDayList.set(i, null);
            } else {
                String stringValue = queryDataList.get(i).toString();
                Log.d(TAG, "stringValue = " + stringValue);
                if (stringValue.length() > 4){
                    stringValue = stringValue.substring(0,4);
                    Log.d(TAG, "stringValue.substring = " + stringValue);
                    queryDataList.set(i, Float.valueOf(stringValue));
                }

            }
        }


        Log.d(TAG, "querryDayList: " + Arrays.toString(queryDayList.toArray()));
        Log.d(TAG, "querryDataList: " + Arrays.toString(queryDataList.toArray()));
        Log.d(TAG, "sourceDataDateList: " + Arrays.toString(queryDayList.toArray()));

        sourceDataDateList = new ArrayList<>();
        sourceDataDateList = queryDayList;

        ArrayList<Float> dataList = new ArrayList<>();
        dataList = queryDataList;
        return dataList;
    }
}
