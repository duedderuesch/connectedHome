package com.duedderuesch.placed.data.entities;

import android.content.Context;
import android.util.Log;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.duedderuesch.placed.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class GraphDataSeries {

    private String TAG = "SensorGraphActivity";

    public static class DaterangeType {
        public static String every = "every";
        public static String average_year = "average-year";
        public static String average_month = "average-month";
        public static String average_day = "average-day";
        public static String average_hour = "average-hour";

        public static ArrayList<String> DaterangePossibilities;

        public static ArrayList<String> getDaterangePossibilities() {
            DaterangePossibilities = new ArrayList<>();
            DaterangePossibilities.add(every);
            DaterangePossibilities.add(average_year);
            DaterangePossibilities.add(average_month);
            DaterangePossibilities.add(average_day);
            DaterangePossibilities.add(average_hour);
            return DaterangePossibilities;
        }
    }
    public static class Type {
        public static String ONLY_YDATA = "only_ydata";
        public static String XDATA_IS_XLABEL = "xdata_is_xlabel";
        public static String ONLY_YDATA_BUT_XLABEL = "ydata_+_xlabel";
        public static String YDATA_XDATA_XLABEL = "all_seperate";
    }

    private static final String PER_MINUTE = "p_minute";
    private static final String PER_HOUR = "p_hour";
    private static final String PER_DAY = "p_day";

    private XYSeries xySeries;
    private ArrayList<Integer> yDataList;
    private ArrayList<Integer> xDataList;
    private ArrayList<String> xLabelList;
    private Integer maxIndex;
    private String daterangeType;
    private String type;
    private Context ctx;


    public XYSeries getXySeries() {
        return xySeries;
    }

    public ArrayList<Integer> getyDataList() {
        return yDataList;
    }

    public ArrayList<Integer> getxDataList() {
        return xDataList;
    }

    public ArrayList<String> getxLabelList() {
        return xLabelList;
    }

    public String getDaterangeType() {
        return daterangeType;
    }

    public String getType() {
        return type;
    }

    public Integer getMaxIndex() {
        return maxIndex;
    }

//    public GraphDataSeries(ArrayList<SensorData> sensorData, String daterangeType, String type){
//        yDataList = new ArrayList<>();
//        xDataList = new ArrayList<>();
//        xLabelList = new ArrayList<>();
//        this.daterangeType = daterangeType;
//        this.type = type;
//        String per = null;
//        if(daterangeType.equals(DaterangeType.DATERANGE_1_H)){
//            per = PER_HOUR;
//        }
////        else if(daterangeType.equals(DaterangeType.DATERANGE_14_D)) {
////            per = PER_DAY;
////        }
//        if(per == null){
//            calculateEveryValue(sensorData);
//        } else {
//            calculateAveragePer(sensorData, per);
//        }
//    }
//
//    private void calculateAveragePer(ArrayList<SensorData> queryData, String per) {
//        Log.d(TAG, "calculation started with per: " + per);
//        queryData.sort(new LocalSensorDataComparator());
//
//        int difference = 0;
//        SensorData firstItem = queryData.get(0);
//        SensorData lastItem = queryData.get(queryData.size() - 1);
//        try {
//            Date firstDate = Utils.dateFormatAsSavedLocal.parse(firstItem.getTimestamp());
//            Date lastDate = Utils.dateFormatAsSavedLocal.parse(lastItem.getTimestamp());
//            difference = getDifferenceWithPer(firstDate, lastDate, per);
//            for (int i = 0; i < queryData.size(); i++) {
//                SensorData queryLSD = queryData.get(i);
//                Integer value = queryLSD.getValue();
//                Date queryLSDDate = Utils.dateFormatAsSavedLocal.parse(queryLSD.getTimestamp());
//                Integer dataDifference = getDifferenceWithPer(firstDate, queryLSDDate, per);
//                String currentLabel = null;
//                if (daterangeType.equals(DaterangeType.DATERANGE_1_H)) {
//                    currentLabel = Utils.specialDateFormat_Hour.format(queryLSDDate);
//                } else if (daterangeType.equals(DaterangeType.DATERANGE_14_D)) {
//                    currentLabel = Utils.specialDateFormat_DayAndMonth.format(queryLSDDate);
//                }
//                if(xLabelList.size() == 0){
//                    xLabelList.add(currentLabel);
//                }
//                if(!xLabelList.contains(currentLabel)){
//                    xLabelList.add(currentLabel);
//                }
//                if (yDataList.size() == 0 || yDataList.size() < (xLabelList.indexOf(currentLabel)+1)) {
//                    yDataList.add(value);
//                } else {
//                    yDataList.set(xLabelList.indexOf(currentLabel),(yDataList.get(xLabelList.indexOf(currentLabel)) + value) / 2);
//                }
//                if(xDataList.size() == 0 || xDataList.size() < (xLabelList.indexOf(currentLabel)+1)){
//                    xDataList.add(dataDifference);
//                }
//            }
//            Log.d("graphDataSeries", "xData: " + xDataList.size() + "|" + Arrays.toString(xDataList.toArray()));
//            Log.d("graphDataSeries", "yData: " + yDataList.size() + "|" + Arrays.toString(yDataList.toArray()));
//            Log.d("graphDataSeries", "xLabel: " + xLabelList.size() + "|");
//
//            xySeries = new SimpleXYSeries(xDataList, yDataList, "title");
//            this.maxIndex = xDataList.get(xDataList.size()-1);
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//            Log.e(TAG, e.getMessage());
//        }
//    }
//
//    private void calculateEveryValue(ArrayList<SensorData> querrySensorDataList){
//        Log.d(TAG, "void: calculateEveryValue (GraphDataSeries|157)");
//        querrySensorDataList.sort(new LocalSensorDataComparator());
//
////        Number[] currentYData = null;
////        Number[] currentXData = null;
////        String[] currentXLabel= null;
////        Integer difference = 0;
////        LocalSensorData firstItem = querrySensorDataList.get(0);
////        LocalSensorData lastItem = querrySensorDataList.get(querrySensorDataList.size() - 1);
//
//        ArrayList<Integer> querryYDataList = new ArrayList<>();
//        if(daterangeType.equals(DaterangeType.DATERANGE_1_EVERY) || daterangeType.equals(DaterangeType.DATERANGE_14_D)) {
//            Log.d(TAG, "everyDifferenceSame: " + everyDifferenceSame(querrySensorDataList,PER_MINUTE));
//            if (everyDifferenceSame(querrySensorDataList, PER_MINUTE)){
//                for(int i=0;i<querrySensorDataList.size();i++){
//                    querryYDataList.add(querrySensorDataList.get(i).getValue());
//                }
//            } else {
//                querryYDataList = null;
//                while(querryYDataList==null) {
//                    querryYDataList = fillValuesToSameDifference(querrySensorDataList);
//                    Log.d(TAG, "querryList == null  --> querryList.size == " + querryYDataList.size());
//                }
//
//            }
////            currentXData = new Number[querrySensorDataList.size()];
////            currentXLabel = new String[querrySensorDataList.size()];
//            int indexOfOriginalSet=0;
//            for (int i=0;i<querryYDataList.size();i++){
//                if(querryYDataList.get(i) == null){
//                    Log.d(TAG, "i: " + i + " == null");
//
//                } else {
//                    yDataList.add(querryYDataList.get(i));
//                    xDataList.add(i);
//                    try{
//
//                        xLabelList.add(Utils.dateFormatTime.format(Utils.dateFormatAsSavedLocal.parse(querrySensorDataList.get(indexOfOriginalSet).getTimestamp())));
//                    } catch (Exception e){
//                        xLabelList.add("error");
//                        Log.e(TAG, "Labelling Error at item " + i + ": " + e.toString());
//                    }
//                    indexOfOriginalSet++;
//                }
//            }
//            Log.d(TAG, "YData...");
////            currentYData = querryYDataList.toArray(new Number[0]);
////            Log.d(TAG, "currentYData: ["+currentYData.length+"] " + Arrays.toString(currentYData));
////            Log.d(TAG, "currentXData: ["+currentXData.length+"] " + Arrays.toString(currentXData));
////            Log.d(TAG, "currentXLabel: ["+currentXLabel.length+"] " + Arrays.toString(currentXLabel));
//
//        }
//
////        yData = currentYData;
////        xData = currentXData;
////        xLabel = currentXLabel;
////        for ( int i=0;i<yData.length;i++){
////            yDataList.add((int)yData[i]);
////            xDataList.add((int)xData[i]);
////            xLabelList.add(xLabel[i]);
////        }
//        xySeries = new SimpleXYSeries(xDataList, yDataList, "title");
//        this.maxIndex = xDataList.get(xDataList.size()-1);
//    }
//
//    private boolean everyDifferenceSame(ArrayList<SensorData> queryData, String per){
//        Integer difference = 0;
//        for(int i=0;i<queryData.size()-1;i++){
//            int queryDifference;
//            try{
//                Date dateOne = Utils.dateFormatAsSavedLocal.parse(queryData.get(i).getTimestamp());
//                Date dateTwo = Utils.dateFormatAsSavedLocal.parse(queryData.get(i+1).getTimestamp());
//                queryDifference = getDifferenceWithPer(dateOne,dateTwo,PER_MINUTE);
//                if(!(queryDifference == difference)){
//                    return false;
//                }
//            } catch (Exception e){
//                Log.e(TAG, e.toString());
//            }
//        }
//        return true;
//    }
//
//    private ArrayList<Integer> fillValuesToSameDifference(ArrayList<SensorData> queryData){
//        ArrayList<Integer> resultList = new ArrayList<>();
//        int smallestDiff = smallestDifference(queryData);
//        boolean isNotSmallest = false;
//        for(int i=0;i<queryData.size()-1;i++){
//            int queryDifference;
//            try{
//                Date dateOne = Utils.dateFormatAsSavedLocal.parse(queryData.get(i).getTimestamp());
//                Date dateTwo = Utils.dateFormatAsSavedLocal.parse(queryData.get(i+1).getTimestamp());
//                if(daterangeType.equals(DaterangeType.DATERANGE_1_EVERY) || daterangeType.equals(DaterangeType.DATERANGE_14_D)){
//                    queryDifference = getDifferenceWithPer(dateOne,dateTwo,PER_MINUTE);
//                    if(queryDifference>smallestDiff){
//                        resultList.add(queryData.get(i).getValue());
//                        resultList.add(null);
//                    } else if(queryDifference<smallestDiff){
//                        isNotSmallest = true;
//                        break;
//                    } else {
//                        resultList.add(queryData.get(i).getValue());
//                    }
//                } else {
//                    queryDifference = getDifferenceWithPer(dateOne,dateTwo,PER_MINUTE);
//                }
//
//            } catch (Exception e){
//                Log.e(TAG, e.toString());
//            }
//        }
//        resultList.add(queryData.get(queryData.size()-1).getValue());
//        if(isNotSmallest){
//            return null;
//        } else {
//            return resultList;
//        }
//
//    }
//
//    private Integer smallestDifference(ArrayList<SensorData> queryData) {
//        int smallestDiff;
//        Date dateOne;
//        Date dateTwo;
//        try {
//            dateOne = Utils.dateFormatAsSavedLocal.parse(queryData.get(0).getTimestamp());
//            dateTwo = Utils.dateFormatAsSavedLocal.parse(queryData.get(1).getTimestamp());
//            //TODO
//            smallestDiff = getDifferenceWithPer(dateOne, dateTwo, PER_MINUTE);
//            for (int i = 1; i < queryData.size() - 1; i++) {
//                int queryDiff;
//
//                dateOne = Utils.dateFormatAsSavedLocal.parse(queryData.get(i).getTimestamp());
//                dateTwo = Utils.dateFormatAsSavedLocal.parse(queryData.get(i + 1).getTimestamp());
//                //TODO
//                queryDiff = getDifferenceWithPer(dateOne, dateTwo, PER_MINUTE);
//                if (queryDiff < smallestDiff) {
//                    smallestDiff = queryDiff;
//                }
//
//            }
//            return smallestDiff;
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//            return 1;
//        }
//    }
//
//    private Integer getDifferenceWithPer(Date dateOne, Date dateTwo, String per){
//        Calendar firstCal = Calendar.getInstance();
//        firstCal.setTime(dateOne);
//        Calendar lastCal = Calendar.getInstance();
//        lastCal.setTime(dateTwo);
//        long diff = 0;
//        String changedate;
//        if(lastCal.getTimeInMillis() > firstCal.getTimeInMillis()){
//            changedate = Utils.dateFormatAsSavedLocal.format(firstCal.getTime());
//        } else {
//            changedate = Utils.dateFormatAsSavedLocal.format(lastCal.getTime());
//        }
//        switch (per) {
//            case PER_HOUR:
//                changedate = changedate.substring(0, 14);
//                changedate = changedate + "00:00";
//                break;
//            case PER_DAY:
//                changedate = changedate.substring(0, 10);
//                changedate = changedate + " 00:00:00";
//                break;
//            case PER_MINUTE:
//                changedate = changedate.substring(0, 17);
//                changedate = changedate + "00";
//                break;
//        }
//        try {
//            if (lastCal.getTimeInMillis() > firstCal.getTimeInMillis()) {
//                firstCal.setTime(Utils.dateFormatAsSavedLocal.parse(changedate));
//                diff = (lastCal.getTimeInMillis() - firstCal.getTimeInMillis()) / (60 * 1000);
//                if (per.equals(PER_HOUR)) {
//                    diff = diff / 60;
//                }
//                if (per.equals(PER_DAY)) {
//                    diff = diff / (60 * 24);
//                }
//            }else {
//                lastCal.setTime(Utils.dateFormatAsSavedLocal.parse(changedate));
//                diff = (firstCal.getTimeInMillis() - lastCal.getTimeInMillis()) / (60 * 1000);
//                if (per.equals(PER_HOUR)) {
//                    diff = diff / 60;
//                }
//                if (per.equals(PER_DAY)) {
//                    diff = diff / (60 * 24);
//                }
//            }
//        }catch (Exception e){
//            Log.e(TAG, e.toString());
//        }
//        return Integer.valueOf(String.valueOf(diff));
//
//    }
}
