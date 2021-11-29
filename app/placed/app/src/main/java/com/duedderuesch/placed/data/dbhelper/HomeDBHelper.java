package com.duedderuesch.placed.data.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.duedderuesch.placed.data.entities.Home;
import com.duedderuesch.placed.data.entities.Plant;

import java.util.ArrayList;

public class HomeDBHelper extends SQLiteOpenHelper {
    private String TAG = "HomeDBHelper";

    public static final String DB_NAME              = "placed.db";
    public static final int DB_VERSION              = 1;
    public static final String TABLE_HOME           = "home";
    public static final String COLUMN_ID            = "id";
    public static final String COLUMN_NAME          = "name";
    public static final String COLUMN_ADDRESS       = "address";


    public static final String TABLE_NAME = TABLE_HOME;

    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    "(" + COLUMN_ID     + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME         + " TEXT NOT NULL, " +
                    COLUMN_ADDRESS      + " TEXT);";

    public HomeDBHelper(Context context){
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

    public void addHome(Home home){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,           home.getId());
        values.put(COLUMN_NAME,         home.getName());
        values.put(COLUMN_ADDRESS,      home.getAddress());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public void addHomeIfNotExist(Home home){
        String SQL_QUERY = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_ID + " = '" + home.getId() + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY,null);

        if(!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID,           home.getId());
            values.put(COLUMN_NAME,         home.getName());
            values.put(COLUMN_ADDRESS,      home.getAddress());

            db.insert(TABLE_NAME,null,values);
        }
        db.close();
        cursor.close();
    }

    public void updateHome(Home home) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,           home.getId());
        values.put(COLUMN_NAME,         home.getName());
        values.put(COLUMN_ADDRESS,      home.getAddress());

        deleteRowByID(home.getName());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }



    public ArrayList<Home> getAllHomes(){
        ArrayList<Home> resultList = new ArrayList<>();
        String SQL_QUERY = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY,null);
        while (cursor.moveToNext()){
            Integer db_ID           = cursor.getInt(0);
            String db_NAME          = cursor.getString(1);
            String db_ADDRESS       = cursor.getString(2);


            Home home = new Home(
                    db_ID,
                    db_NAME,
                    db_ADDRESS);
            resultList.add(home);
        }

        cursor.close();
        db.close();
        return  resultList;
    }

    public ArrayList<Home> getAllHomesBy(String column, String keyword){
        ArrayList<Home> resultList = new ArrayList<>();
        String SQL_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + column + " = " + "'" + keyword + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY,null);
        while (cursor.moveToNext()){
            Integer db_ID           = cursor.getInt(0);
            String db_NAME          = cursor.getString(1);
            String db_ADDRESS       = cursor.getString(2);


            Home home = new Home(
                    db_ID,
                    db_NAME,
                    db_ADDRESS);
            resultList.add(home);
        }

        cursor.close();
        db.close();
        return  resultList;
    }

    public void deleteRowByID(String plantObjectId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_NAME + " = ?", new String[]{plantObjectId});
        db.close();
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }


}

