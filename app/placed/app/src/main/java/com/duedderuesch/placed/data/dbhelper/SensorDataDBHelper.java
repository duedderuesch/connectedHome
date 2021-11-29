package com.duedderuesch.placed.data.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.duedderuesch.placed.data.entities.SensorData;
import com.duedderuesch.placed.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class SensorDataDBHelper extends SQLiteOpenHelper {

    private String TAG = "LocalSensorDataDBHelper";

    public static final String DB_NAME = "placed.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_SENSOR_DATA = "data";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SENSOR = "sensor";
    public static final String COLUMN_IS_GENERIC = "is_generic";
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_DATA_TYPE = "data_type";
    public static final String COLUMN_YEAR      = "year";
    public static final String COLUMN_MONTH     = "month";
    public static final String COLUMN_DAY       = "day";
    public static final String COLUMN_HOUR      = "hour";
    public static final String COLUMN_MINUTE    = "minute";

    public static final String TABLE_NAME = TABLE_SENSOR_DATA;

    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    "(" + COLUMN_ID         + " INTEGER PRIMARY KEY NOT NULL, " +
                    COLUMN_SENSOR           + " TEXT NOT NULL, '" +
                    COLUMN_IS_GENERIC       + "' INTEGER NOT NULL, " +
                    COLUMN_VALUE            + " TEXT NOT NULL, '" +
                    COLUMN_DATA_TYPE        + "' TEXT NOT NULL, " +
                    COLUMN_YEAR             + " INTEGER, " +
                    COLUMN_MONTH            + " INTEGER, " +
                    COLUMN_DAY              + " INTEGER, " +
                    COLUMN_HOUR             + " INTEGER, " +
                    COLUMN_MINUTE           + " INTEGER);";

    public SensorDataDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        onCreate(getWritableDatabase());
        Log.d(TAG, "Database " + getDatabaseName() + " created");
    }

    public void onCreate(SQLiteDatabase db){
        try {
            db.execSQL(SQL_CREATE);
        }
        catch (Exception ex) {
            Log.e(TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersionNr, int newVersionNr){

    }

    public void addSensorData(SensorData sensorData){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID            , sensorData.getId());
        values.put(COLUMN_SENSOR        , sensorData.getSensor());
        values.put(COLUMN_IS_GENERIC    , sensorData.getIs_generic());
        values.put(COLUMN_VALUE         , sensorData.getValue());
        values.put(COLUMN_DATA_TYPE     , sensorData.getData_type());
        values.put(COLUMN_YEAR          , sensorData.getYear());
        values.put(COLUMN_MONTH         , sensorData.getMonth());
        values.put(COLUMN_DAY           , sensorData.getDay());
        values.put(COLUMN_HOUR          , sensorData.getHour());
        values.put(COLUMN_MINUTE        , sensorData.getMinute());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public void addSensorDataIfNotExist(SensorData sensorData){
        if(sensorData != null) {
            SQLiteDatabase db = this.getWritableDatabase();
            String SQL_QUERY = "SELECT * FROM " + TABLE_NAME +
                    " WHERE " + COLUMN_ID + " = '" + sensorData.getId() + "'";
            Cursor cursor = db.rawQuery(SQL_QUERY, null);

            if (!cursor.moveToFirst()) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_ID, sensorData.getId());
                values.put(COLUMN_SENSOR, sensorData.getSensor());
                values.put(COLUMN_IS_GENERIC, sensorData.getIs_generic());
                values.put(COLUMN_VALUE, sensorData.getValue());
                values.put(COLUMN_DATA_TYPE, sensorData.getData_type());
                values.put(COLUMN_YEAR, sensorData.getYear());
                values.put(COLUMN_MONTH, sensorData.getMonth());
                values.put(COLUMN_DAY, sensorData.getDay());
                values.put(COLUMN_HOUR, sensorData.getHour());
                values.put(COLUMN_MINUTE, sensorData.getMinute());


                db.insert(TABLE_NAME, null, values);
                db.close();
            }
            cursor.close();
        }
    }

    public ArrayList<SensorData> getAllSensorData(){
        ArrayList<SensorData> resultList = new ArrayList<>();
        String SQL_QUERY = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY,null);
        while (cursor.moveToNext()){
            Integer     DB_ID            = cursor.getInt(0);
            String      DB_SENSOR        = cursor.getString(1);
            Integer     DB_IS_GENERIC    = cursor.getInt(2);
            String      DB_VALUE         = cursor.getString(3);
            String      DB_DATA_TYPE     = cursor.getString(4);
            Integer     DB_YEAR          = cursor.getInt(5);
            Integer     DB_MONTH         = cursor.getInt(6);
            Integer     DB_DAY           = cursor.getInt(7);
            Integer     DB_HOUR          = cursor.getInt(8);
            Integer     DB_MINUTE        = cursor.getInt(9);

            SensorData querySensorData = new SensorData(DB_ID,
                    DB_SENSOR,
                    DB_IS_GENERIC,
                    DB_VALUE,
                    DB_DATA_TYPE,
                    DB_YEAR,
                    DB_MONTH,
                    DB_DAY,
                    DB_HOUR,
                    DB_MINUTE);
            resultList.add(querySensorData);
        }

        cursor.close();
        db.close();
        return  resultList;
    }

    public ArrayList<SensorData> getAllSensorDataBy(String column, String keyword, String limit){
        ArrayList<SensorData> resultList = new ArrayList<>();
        String SQL_QUERY;
        if(limit == null) {
            SQL_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + column + " = " + "'" + keyword + "'";
        } else {
            SQL_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + column + " = " + "'" + keyword
                    + "' ORDER BY " + COLUMN_ID + " DESC LIMIT " + limit;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY,null);
        while (cursor.moveToNext()){
            Integer     DB_ID            = cursor.getInt(0);
            String      DB_SENSOR        = cursor.getString(1);
            Integer     DB_IS_GENERIC    = cursor.getInt(2);
            String      DB_VALUE         = cursor.getString(3);
            String      DB_DATA_TYPE     = cursor.getString(4);
            Integer     DB_YEAR          = cursor.getInt(5);
            Integer     DB_MONTH         = cursor.getInt(6);
            Integer     DB_DAY           = cursor.getInt(7);
            Integer     DB_HOUR          = cursor.getInt(8);
            Integer     DB_MINUTE        = cursor.getInt(9);

            SensorData querySensorData = new SensorData(DB_ID,
                    DB_SENSOR,
                    DB_IS_GENERIC,
                    DB_VALUE,
                    DB_DATA_TYPE,
                    DB_YEAR,
                    DB_MONTH,
                    DB_DAY,
                    DB_HOUR,
                    DB_MINUTE);
            resultList.add(querySensorData);
        }

        cursor.close();
        db.close();
        return  resultList;
    }

    public ArrayList<SensorData> getSensorDataLastDay(String enddate, String sensor){
        ArrayList<SensorData> resultList = new ArrayList<>();
        //TODO: function: SensorDataDBHelper.getSensorDataLastDay()
//        try {
//            Calendar mCal = Calendar.getInstance();
//            Date date_end = Utils.dateFormatAsSaved.parse(enddate);
//            String whole_day_end = Utils.dateFormatAsSavedLocal.format(date_end);
//            mCal.setTime(Utils.dateFormatAsSavedLocal.parse(whole_day_end));
//            mCal.add(Calendar.DAY_OF_MONTH,-1);
//            String whole_day_start = Utils.dateFormatAsSavedLocal.format(mCal.getTime());
//
//
//            String SQL_QUERY;
//            if (sensor == null) {
//                SQL_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_TIMESTAMP + " < '" + whole_day_end + "' ORDER BY " + COLUMN_ID + " DESC";
//            } else {
//                SQL_QUERY = "SELECT * FROM " + TABLE_NAME +
//                        " WHERE " + COLUMN_SENSOR + " = '" + sensor + "'" +
//                        " AND " + COLUMN_TIMESTAMP + " > '" + whole_day_start + "'" +
//                        " AND " + COLUMN_TIMESTAMP + " < '" + whole_day_end + "'" +
//                        " ORDER BY " + COLUMN_ID + " DESC";
//            }
//
//            SQLiteDatabase db = this.getWritableDatabase();
//            Cursor cursor = db.rawQuery(SQL_QUERY, null);
//            Log.d("SensorGraphActivity", cursor.toString());
//            while (cursor.moveToNext()){
//                Integer     DB_ID            = cursor.getInt(0);
//                String      DB_SENSOR        = cursor.getString(1);
//                Integer     DB_IS_GENERIC    = cursor.getInt(2);
//                String      DB_VALUE         = cursor.getString(3);
//                String      DB_DATA_TYPE     = cursor.getString(4);
//                Integer     DB_YEAR          = cursor.getInt(5);
//                Integer     DB_MONTH         = cursor.getInt(6);
//                Integer     DB_DAY           = cursor.getInt(7);
//                Integer     DB_HOUR          = cursor.getInt(8);
//                Integer     DB_MINUTE        = cursor.getInt(9);
//
//                SensorData querySensorData = new SensorData(DB_ID,
//                        DB_SENSOR,
//                        DB_IS_GENERIC,
//                        DB_VALUE,
//                        DB_DATA_TYPE,
//                        DB_YEAR,
//                        DB_MONTH,
//                        DB_DAY,
//                        DB_HOUR,
//                        DB_MINUTE);
//                resultList.add(querySensorData);
//            }
//
//            cursor.close();
//            db.close();
//        } catch (Exception e){
//            Log.e("SensorGraphActivity", "Error in getting Data: " + e.toString());
//        }
        return  resultList;
    }

    public ArrayList<SensorData> getSensorDataLastSevenDays(String sensor){
        ArrayList<SensorData> resultList = new ArrayList<>();
        try {
            Calendar calendarToday = Calendar.getInstance();
            calendarToday.add(Calendar.DAY_OF_MONTH,-6);



            //TODO: date exceptions
            String SQL_QUERY;
            if (sensor == null) {
                SQL_QUERY = "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_YEAR + " >= '" + calendarToday.get(Calendar.YEAR) + "'" +
                        " AND " + COLUMN_MONTH + " >= '" + calendarToday.get(Calendar.MONTH) + "'" +
                        " AND " + COLUMN_DAY + " > '" + calendarToday.get(Calendar.DAY_OF_MONTH) + "'" +
                        " ORDER BY " + COLUMN_ID + " ASC";
            } else {
                SQL_QUERY = "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_SENSOR + " = '" + sensor + "'" +
                        " AND " + COLUMN_YEAR + " = '" + calendarToday.get(Calendar.YEAR) + "'" +
                        " AND " + COLUMN_MONTH + " >= '" + calendarToday.get(Calendar.MONTH) + "'" +
                        " AND " + COLUMN_DAY + " > '" + calendarToday.get(Calendar.DAY_OF_MONTH) + "'" +
                        " ORDER BY " + COLUMN_ID + " ASC";
            }


            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(SQL_QUERY,null);
            while (cursor.moveToNext()){
                Integer     DB_ID            = cursor.getInt(0);
                String      DB_SENSOR        = cursor.getString(1);
                Integer     DB_IS_GENERIC    = cursor.getInt(2);
                String      DB_VALUE         = cursor.getString(3);
                String      DB_DATA_TYPE     = cursor.getString(4);
                Integer     DB_YEAR          = cursor.getInt(5);
                Integer     DB_MONTH         = cursor.getInt(6);
                Integer     DB_DAY           = cursor.getInt(7);
                Integer     DB_HOUR          = cursor.getInt(8);
                Integer     DB_MINUTE        = cursor.getInt(9);

                SensorData querySensorData = new SensorData(DB_ID,
                        DB_SENSOR,
                        DB_IS_GENERIC,
                        DB_VALUE,
                        DB_DATA_TYPE,
                        DB_YEAR,
                        DB_MONTH,
                        DB_DAY,
                        DB_HOUR,
                        DB_MINUTE);
                resultList.add(querySensorData);
            }

            cursor.close();
            db.close();
            return  resultList;
        } catch (Exception e){
            Log.e("SensorGraphActivity", "Error in getting Data: " + e.toString());
        }
        return  resultList;
    }

    public ArrayList<SensorData> getNewestSensorDataForEverySensor(Context ctx){
        ArrayList<SensorData> resultList = new ArrayList<>();
        ArrayList<String> alreadyGottenIdsList = new ArrayList<>();
        SensorDeviceDBHelper deviceDBHelper = new SensorDeviceDBHelper(ctx);
        int numberOfSensors = deviceDBHelper.numberOfRows();
        String SQL_QUERY = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY,null);
        while (cursor.moveToNext()){
            String      DB_SENSOR        = cursor.getString(1);

            if(!alreadyGottenIdsList.contains(DB_SENSOR)){
                alreadyGottenIdsList.add(DB_SENSOR);

                Integer     DB_ID            = cursor.getInt(0);
                Integer     DB_IS_GENERIC    = cursor.getInt(2);
                String      DB_VALUE         = cursor.getString(3);
                String      DB_DATA_TYPE     = cursor.getString(4);
                Integer     DB_YEAR          = cursor.getInt(5);
                Integer     DB_MONTH         = cursor.getInt(6);
                Integer     DB_DAY           = cursor.getInt(7);
                Integer     DB_HOUR          = cursor.getInt(8);
                Integer     DB_MINUTE        = cursor.getInt(9);

                SensorData querySensorData = new SensorData(DB_ID,
                        DB_SENSOR,
                        DB_IS_GENERIC,
                        DB_VALUE,
                        DB_DATA_TYPE,
                        DB_YEAR,
                        DB_MONTH,
                        DB_DAY,
                        DB_HOUR,
                        DB_MINUTE);
                resultList.add(querySensorData);
            }
            if(alreadyGottenIdsList.size() == numberOfSensors) break;

        }
        cursor.close();
        db.close();
        return  resultList;
    }

    public Integer getLastIdBy(String column, String keyword){
        String SQL_QUERY;
        if(column == null) {
            SQL_QUERY = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1";
        } else {
            SQL_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + column + " = " + "'" + keyword
                    + "' ORDER BY " + COLUMN_ID + " DESC LIMIT 1";
        }

        Integer lastId = null;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY,null);
        if(cursor.moveToFirst()){
            lastId = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return  lastId;

    }



    public void deleteRowByID(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,COLUMN_ID + " = ?", new String[]{id});
        db.close();
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }
}
