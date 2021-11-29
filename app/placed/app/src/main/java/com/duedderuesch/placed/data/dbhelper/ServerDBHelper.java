package com.duedderuesch.placed.data.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.duedderuesch.placed.data.entities.Home;
import com.duedderuesch.placed.data.entities.Server;

import java.util.ArrayList;

public class ServerDBHelper extends SQLiteOpenHelper {
    private String TAG = "ServerDBHelper";

    public static final String DB_NAME              = "placed.db";
    public static final int DB_VERSION              = 1;
    public static final String TABLE_SERVER         = "server";
    public static final String COLUMN_ID            = "id";
    public static final String COLUMN_NAME          = "name";
    public static final String COLUMN_HOME          = "home";
    public static final String COLUMN_SERVER_TYPE   = "server_type";
    public static final String COLUMN_ADDRESS       = "address";


    public static final String TABLE_NAME = TABLE_SERVER;

    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    "(" + COLUMN_ID     + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME         + " TEXT NOT NULL, " +
                    COLUMN_HOME         + " TEXT NOT NULL, " +
                    COLUMN_SERVER_TYPE  + " TEXT NOT NULL, " +
                    COLUMN_ADDRESS      + " TEXT NOT NULL);";

    public ServerDBHelper(Context context){
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

    public void addServer(Server server){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID         ,      server.getId());
        values.put(COLUMN_NAME       ,      server.getName());
        values.put(COLUMN_HOME       ,      server.getHome());
        values.put(COLUMN_SERVER_TYPE,      server.getServer_type());
        values.put(COLUMN_ADDRESS    ,      server.getAddress());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public void addServerIfNotExist(Server server){
        String SQL_QUERY = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_ID + " = '" + server.getId() + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY,null);

        if(!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID         ,      server.getId());
            values.put(COLUMN_NAME       ,      server.getName());
            values.put(COLUMN_HOME       ,      server.getHome());
            values.put(COLUMN_SERVER_TYPE,      server.getServer_type());
            values.put(COLUMN_ADDRESS    ,      server.getAddress());

            db.insert(TABLE_NAME,null,values);
        }
        db.close();
        cursor.close();
    }

    public void updateServer(Server server) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID         ,      server.getId());
        values.put(COLUMN_NAME       ,      server.getName());
        values.put(COLUMN_HOME       ,      server.getHome());
        values.put(COLUMN_SERVER_TYPE,      server.getServer_type());
        values.put(COLUMN_ADDRESS    ,      server.getAddress());

        deleteRowByID(server.getId().toString());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }



    public ArrayList<Server> getAllServer(){
        ArrayList<Server> resultList = new ArrayList<>();
        String SQL_QUERY = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY,null);
        while (cursor.moveToNext()){
            Integer db_ID            = cursor.getInt(0);
            String  db_NAME          = cursor.getString(1);
            String  db_HOME          = cursor.getString(2);
            String  db_SERVER_TYPE   = cursor.getString(3);
            String  db_ADDRESS       = cursor.getString(4);


            Server server = new Server(
                    db_ID,
                    db_NAME,
                    db_HOME,
                    db_SERVER_TYPE,
                    db_ADDRESS);
                    resultList.add(server);
        }

        cursor.close();
        db.close();
        return  resultList;
    }

    public ArrayList<Server> getAllServerBy(String column, String keyword){
        ArrayList<Server> resultList = new ArrayList<>();
        String SQL_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + column + " = " + "'" + keyword + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY,null);
        while (cursor.moveToNext()){
            Integer db_ID            = cursor.getInt(0);
            String  db_NAME          = cursor.getString(1);
            String  db_HOME          = cursor.getString(2);
            String  db_SERVER_TYPE   = cursor.getString(3);
            String  db_ADDRESS       = cursor.getString(4);


            Server server = new Server(
                    db_ID,
                    db_NAME,
                    db_HOME,
                    db_SERVER_TYPE,
                    db_ADDRESS);
            resultList.add(server);
        }

        cursor.close();
        db.close();
        return  resultList;
    }

    public void deleteRowByID(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{id});
        db.close();
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }


}

