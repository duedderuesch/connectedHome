
package com.duedderuesch.placed.data.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.duedderuesch.placed.data.entities.Plant;
import com.duedderuesch.placed.data.entities.SensorDevice;

import java.util.ArrayList;

public class SensorDeviceDBHelper extends SQLiteOpenHelper {

    private String TAG = "sensor_devcieDBHelper";

    public static final String DB_NAME = "placed.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_SENSOR_DEVICE  = "sensor";
    public static final String COLUMN_ID            = "id";
    public static final String COLUMN_NAME          = "name";
    public static final String COLUMN_SENSOR_TYPE   = "sensor_type";
    public static final String COLUMN_IS_GENERIC    = "is_generic";
    public static final String COLUMN_PLANT         = "plant";
    public static final String COLUMN_HOME          = "home";
    public static final String COLUMN_SERVER        = "server";
    public static final String COLUMN_IP            = "ip";

    public static final String TABLE_NAME = TABLE_SENSOR_DEVICE;

    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    "(" + COLUMN_ID      + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME          + " TEXT NOT NULL, '" +
                    COLUMN_SENSOR_TYPE   + "' TEXT NOT NULL, '" +
                    COLUMN_IS_GENERIC    + "' INTEGER NOT NULL, " +
                    COLUMN_PLANT         + " TEXT, " +
                    COLUMN_HOME          + " TEXT, " +
                    COLUMN_SERVER        + " TEXT, " +
                    COLUMN_IP            + " TEXT);";


    public SensorDeviceDBHelper(Context context){
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

    public void addSensorDevice(SensorDevice sensorDevice){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID            , sensorDevice.getId());
        values.put(COLUMN_NAME          , sensorDevice.getName());
        values.put(COLUMN_SENSOR_TYPE   , sensorDevice.getSensor_type());
        values.put(COLUMN_IS_GENERIC    , sensorDevice.getIs_generic());
        values.put(COLUMN_PLANT         , sensorDevice.getPlant());
        values.put(COLUMN_HOME          , sensorDevice.getHome());
        values.put(COLUMN_SERVER        , sensorDevice.getServer());
        values.put(COLUMN_IP            , sensorDevice.getIp());


        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public void addSensorDeviceIfNotExist(SensorDevice sensorDevice){
        String SQL_QUERY = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_ID + " = '" + sensorDevice.getId() + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY,null);

        if(!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();

            values.put(COLUMN_ID, sensorDevice.getId());
            values.put(COLUMN_NAME, sensorDevice.getName());
            values.put(COLUMN_SENSOR_TYPE, sensorDevice.getSensor_type());
            values.put(COLUMN_IS_GENERIC, sensorDevice.getIs_generic());
            values.put(COLUMN_PLANT, sensorDevice.getPlant());
            values.put(COLUMN_HOME, sensorDevice.getHome());
            values.put(COLUMN_SERVER, sensorDevice.getServer());
            values.put(COLUMN_IP, sensorDevice.getIp());

            db.insert(TABLE_NAME, null, values);
        }
        db.close();
        cursor.close();
    }

    public void updateSensorDevice(SensorDevice sensorDevice) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID            , sensorDevice.getId());
        values.put(COLUMN_NAME          , sensorDevice.getName());
        values.put(COLUMN_SENSOR_TYPE   , sensorDevice.getSensor_type());
        values.put(COLUMN_IS_GENERIC    , sensorDevice.getIs_generic());
        values.put(COLUMN_PLANT         , sensorDevice.getPlant());
        values.put(COLUMN_HOME          , sensorDevice.getHome());
        values.put(COLUMN_SERVER        , sensorDevice.getServer());
        values.put(COLUMN_IP            , sensorDevice.getIp());

        deleteRowByID(sensorDevice.getId().toString());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }



    public ArrayList<SensorDevice> getAllSensorDevices(){
        ArrayList<SensorDevice> resultList = new ArrayList<>();
        String SQL_QUERY = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY,null);
        while (cursor.moveToNext()){
            Integer DB_ID           = cursor.getInt(0);
            String  DB_NAME         = cursor.getString(1);
            String  DB_SENSOR_TYPE  = cursor.getString(2);
            Integer DB_IS_GENERIC   = cursor.getInt(3);
            String  DB_PLANT        = cursor.getString(4);
            String  DB_HOME         = cursor.getString(5);
            String  DB_SERVER       = cursor.getString(6);
            String  DB_IP           = cursor.getString(7);


            SensorDevice sensorDevice = new SensorDevice(DB_ID,
                    DB_NAME,
                    DB_SENSOR_TYPE,
                    DB_IS_GENERIC,
                    DB_PLANT,
                    DB_HOME,
                    DB_SERVER,
                    DB_IP);
            resultList.add(sensorDevice);
        }

        cursor.close();
        db.close();
        return  resultList;
    }

    public ArrayList<SensorDevice> getSensorDevicesBy(String column, String keyword){
        ArrayList<SensorDevice> resultList = new ArrayList<>();
        String SQL_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + column + " = " + "'" + keyword + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY,null);
        while (cursor.moveToNext()){
            Integer DB_ID           = cursor.getInt(0);
            String  DB_NAME         = cursor.getString(1);
            String  DB_SENSOR_TYPE  = cursor.getString(2);
            Integer DB_IS_GENERIC   = cursor.getInt(3);
            String  DB_PLANT        = cursor.getString(4);
            String  DB_HOME         = cursor.getString(5);
            String  DB_SERVER       = cursor.getString(6);
            String  DB_IP           = cursor.getString(7);


            SensorDevice sensorDevice = new SensorDevice(DB_ID,
                    DB_NAME,
                    DB_SENSOR_TYPE,
                    DB_IS_GENERIC,
                    DB_PLANT,
                    DB_HOME,
                    DB_SERVER,
                    DB_IP);
            resultList.add(sensorDevice);
        }

        cursor.close();
        db.close();
        return  resultList;
    }

    public void deleteRowByID(String sensorDeviceId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,COLUMN_ID + " = ?", new String[]{sensorDeviceId});
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
