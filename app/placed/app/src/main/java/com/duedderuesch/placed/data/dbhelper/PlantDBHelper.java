package com.duedderuesch.placed.data.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.duedderuesch.placed.data.entities.Plant;
import com.duedderuesch.placed.data.entities.SensorData;
import com.duedderuesch.placed.data.entities.SensorDevice;
import com.duedderuesch.placed.utils.Utils;

import java.util.ArrayList;

public class PlantDBHelper extends SQLiteOpenHelper {
    private String TAG = "PlantObjectDBHelper";

    public static final String DB_NAME = "placed.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_PLANT              = "plant";
    public static final String COLUMN_ID                = "id";
    public static final String COLUMN_NAME              = "name";
    public static final String COLUMN_BIOLOGICAL_NAME   = "biological_name";
    public static final String COLUMN_HOME              = "home";
    public static final String COLUMN_BIRTHDATE         = "birthdate";
    public static final String COLUMN_MIN_HUMIDITY      = "min_humidity";
    public static final String COLUMN_MAX_HUMIDITY      = "max_humidity";
    public static final String COLUMN_MIN_TEMPERATURE   = "min_temperature";
    public static final String COLUMN_MAX_TEMPERATURE   = "max_temperature";
    public static final String COLUMN_NOTE              = "note";

    public static final String TABLE_NAME = TABLE_PLANT;

    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    "(" + COLUMN_ID                 + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME                     + " TEXT NOT NULL, '" +
                    COLUMN_BIOLOGICAL_NAME          + "' TEXT, " +
                    COLUMN_HOME                     + " TEXT NOT NULL, " +
                    COLUMN_BIRTHDATE                + " TEXT, '" +
                    COLUMN_MIN_HUMIDITY             + "' INTEGER, '" +
                    COLUMN_MAX_HUMIDITY             + "' INTEGER, '" +
                    COLUMN_MIN_TEMPERATURE          + "' INTEGER, '" +
                    COLUMN_MAX_TEMPERATURE          + "' INTEGER, " +
                    COLUMN_NOTE                     + " TEXT);";

    public PlantDBHelper(Context context){
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

    public void addPlantObject(Plant plant){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID                , plant.getId());
        values.put(COLUMN_NAME              , plant.getName());
        values.put(COLUMN_BIOLOGICAL_NAME   , plant.getBiological_name());
        values.put(COLUMN_HOME              , plant.getHome());
        values.put(COLUMN_BIRTHDATE         , plant.getBirthdate());
        values.put(COLUMN_MIN_HUMIDITY      , plant.getMin_humidity());
        values.put(COLUMN_MAX_HUMIDITY      , plant.getMax_humidity());
        values.put(COLUMN_MIN_TEMPERATURE   , plant.getMin_temperature());
        values.put(COLUMN_MAX_TEMPERATURE   , plant.getMax_temperature());
        values.put(COLUMN_NOTE              , plant.getNote());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public void addPlantIfNotExist(Plant plant){
        String SQL_QUERY = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_ID + " = '" + plant.getId() + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY,null);

        if(!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID                , plant.getId());
            values.put(COLUMN_NAME              , plant.getName());
            values.put(COLUMN_BIOLOGICAL_NAME   , plant.getBiological_name());
            values.put(COLUMN_HOME              , plant.getHome());
            values.put(COLUMN_BIRTHDATE         , plant.getBirthdate());
            values.put(COLUMN_MIN_HUMIDITY      , plant.getMin_humidity());
            values.put(COLUMN_MAX_HUMIDITY      , plant.getMax_humidity());
            values.put(COLUMN_MIN_TEMPERATURE   , plant.getMin_temperature());
            values.put(COLUMN_MAX_TEMPERATURE   , plant.getMax_temperature());
            values.put(COLUMN_NOTE              , plant.getNote());

            db.insert(TABLE_NAME,null,values);
        }
        db.close();
        cursor.close();
    }

    public void updatePlantObject(Plant plant) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID                , plant.getId());
        values.put(COLUMN_NAME              , plant.getName());
        values.put(COLUMN_BIOLOGICAL_NAME   , plant.getBiological_name());
        values.put(COLUMN_HOME              , plant.getHome());
        values.put(COLUMN_BIRTHDATE         , plant.getBirthdate());
        values.put(COLUMN_MIN_HUMIDITY      , plant.getMin_humidity());
        values.put(COLUMN_MAX_HUMIDITY      , plant.getMax_humidity());
        values.put(COLUMN_MIN_TEMPERATURE   , plant.getMin_temperature());
        values.put(COLUMN_MAX_TEMPERATURE   , plant.getMax_temperature());
        values.put(COLUMN_NOTE              , plant.getNote());

        deleteRowByID(plant.getId().toString());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }



    public ArrayList<Plant> getAllPlantObjects(){
        ArrayList<Plant> resultList = new ArrayList<>();
        String SQL_QUERY = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY,null);
        while (cursor.moveToNext()){
            Integer DB_ID                = cursor.getInt(0);
            String  DB_NAME              = cursor.getString(1);
            String  DB_BIOLOGICAL_NAME   = cursor.getString(2);
            String  DB_HOME              = cursor.getString(3);
            String  DB_BIRTHDATE         = cursor.getString(4);
            Integer DB_MIN_HUMIDITY      = cursor.getInt(5);
            Integer DB_MAX_HUMIDITY      = cursor.getInt(6);
            Integer DB_MIN_TEMPERATURE   = cursor.getInt(7);
            Integer DB_MAX_TEMPERATURE   = cursor.getInt(8);
            String  DB_NOTE              = cursor.getString(9);

            Plant plant = new Plant(DB_ID,
                    DB_NAME,
                    DB_BIOLOGICAL_NAME,
                    DB_HOME,
                    DB_BIRTHDATE,
                    DB_MIN_HUMIDITY,
                    DB_MAX_HUMIDITY,
                    DB_MIN_TEMPERATURE,
                    DB_MAX_TEMPERATURE,
                    DB_NOTE);
            resultList.add(plant);
        }

        cursor.close();
        db.close();
        return  resultList;
    }

    public ArrayList<Plant> getPlantObjectBy(String column, String keyword){
        ArrayList<Plant> resultList = new ArrayList<>();
        String SQL_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + column + " = " + "'" + keyword + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY,null);
        while (cursor.moveToNext()){
            Integer DB_ID                = cursor.getInt(0);
            String  DB_NAME              = cursor.getString(1);
            String  DB_BIOLOGICAL_NAME   = cursor.getString(2);
            String  DB_HOME              = cursor.getString(3);
            String  DB_BIRTHDATE         = cursor.getString(4);
            Integer DB_MIN_HUMIDITY      = cursor.getInt(5);
            Integer DB_MAX_HUMIDITY      = cursor.getInt(6);
            Integer DB_MIN_TEMPERATURE   = cursor.getInt(7);
            Integer DB_MAX_TEMPERATURE   = cursor.getInt(8);
            String  DB_NOTE              = cursor.getString(9);

            Plant plant = new Plant(DB_ID,
                    DB_NAME,
                    DB_BIOLOGICAL_NAME,
                    DB_HOME,
                    DB_BIRTHDATE,
                    DB_MIN_HUMIDITY,
                    DB_MAX_HUMIDITY,
                    DB_MIN_TEMPERATURE,
                    DB_MAX_TEMPERATURE,
                    DB_NOTE);
            resultList.add(plant);
        }

        cursor.close();
        db.close();
        return  resultList;
    }

    public String mostRecentDataTimestamp(String plantID, Context ctx){
        String timestamp = null;
        SensorDataDBHelper dataDBHelper = new SensorDataDBHelper(ctx);
        ArrayList<SensorData> queryDataList = dataDBHelper.getNewestSensorDataForEverySensor(ctx);
        SensorDeviceDBHelper deviceDBHelper = new SensorDeviceDBHelper(ctx);
        ArrayList<SensorDevice> queryDeviceList =
                deviceDBHelper.getSensorDevicesBy(SensorDeviceDBHelper.COLUMN_PLANT, plantID);

        for(int i=0; i<queryDataList.size();i++){
            for(int j=0; j<queryDeviceList.size();j++){
                if(queryDataList.get(i).getSensor().
                        equals(queryDeviceList.get(j).getId().toString())){
                    if(timestamp == null) timestamp = Utils.sensorDataDatetime(queryDataList.get(i));
                    else if (Utils.DatetimeNumber(timestamp) <
                            Utils.sensorDataDatetimeNumber(queryDataList.get(i))){
                        timestamp = Utils.sensorDataDatetime(queryDataList.get(i));
                    }

                }
            }

        }
        return timestamp;
    }

    public void deleteRowByID(String plantObjectId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,COLUMN_ID + " = ?", new String[]{plantObjectId});
        db.close();
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }

    public boolean rowExistsById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + "'" + id + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            db.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }


}

