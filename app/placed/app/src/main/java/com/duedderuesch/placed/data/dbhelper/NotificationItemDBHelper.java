package com.duedderuesch.placed.data.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.duedderuesch.placed.data.entities.NotificationItem;

import java.util.ArrayList;

public class NotificationItemDBHelper extends SQLiteOpenHelper {

    private String TAG = "LocalSensorDataDBHelper";

    public static final String DB_NAME = "placed.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_SENSOR_DATA = "notification_item";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CHANNEL = "channel";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TEXT = "text_content";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_HASPROGRESS = "has_progress";
    public static final String COLUMN_PROGRESS = "progress";

    public static final String TABLE_NAME = TABLE_SENSOR_DATA;

    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    "(" + COLUMN_ID         + " INTEGER PRIMARY KEY NOT NULL, " +
                    COLUMN_CHANNEL          + " TEXT NOT NULL, " +
                    COLUMN_TITLE            + " TEXT NOT NULL, " +
                    COLUMN_TEXT             + " TEXT, " +
                    COLUMN_ICON             + " INTEGER, " +
                    COLUMN_PRIORITY         + " INTEGER, " +
                    COLUMN_HASPROGRESS      + " INTEGER, " +
                    COLUMN_PROGRESS         + " INTEGER);";

    public NotificationItemDBHelper(Context context){
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

    public void addSensorData(NotificationItem item){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, item.getId());
        values.put(COLUMN_CHANNEL, item.getChannel());
        values.put(COLUMN_TITLE, item.getTitle());
        values.put(COLUMN_TEXT, item.getText());
        values.put(COLUMN_ICON, item.getIcon());
        values.put(COLUMN_PRIORITY, item.getPriority());

        Integer hasProgress = null;
        if(item.getHasProgressBar()){
            hasProgress = 1;
        } else {
            hasProgress = 0;
        }

        values.put(COLUMN_HASPROGRESS, hasProgress);
        values.put(COLUMN_PROGRESS, item.getProgress());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public ArrayList<NotificationItem> getAllNotificationItems(){
        ArrayList<NotificationItem> resultList = new ArrayList<>();
        String SQL_QUERY = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL_QUERY,null);
        while (cursor.moveToNext()){
            Integer     db_ID           = cursor.getInt(0);
            String      db_CHANNEL      = cursor.getString(1);
            String      db_TITLE        = cursor.getString(2);
            String      db_TEXT         = cursor.getString(3);
            Integer     db_ICON         = cursor.getInt(4);
            Integer     db_PRIORITY     = cursor.getInt(5);
            Integer     db_HASPROGRESS  = cursor.getInt(6);
            Integer     db_PROGRESS     = cursor.getInt(7);

            boolean hasProgress = db_HASPROGRESS != 0;

            NotificationItem query_item = new NotificationItem(db_ID, db_CHANNEL,db_TITLE,db_TEXT, db_ICON, db_PRIORITY, hasProgress,db_PROGRESS);
            resultList.add(query_item);
        }

        cursor.close();
        db.close();
        return  resultList;
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
