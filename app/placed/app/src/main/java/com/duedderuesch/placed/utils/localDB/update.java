package com.duedderuesch.placed.utils.localDB;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import static com.duedderuesch.placed.utils.localDB.utils.replaceSpaces;

public class update {

    private final String TAG = "localDB.update";

    private String database;
    private String table;
    private ArrayList<String> argList;
    private ArrayList<String> valList;
    private Boolean tryToUpdate = false;

    private String localDBURL;
    private String sqlRequest;
    private String URLString;
    private String itemID;
    private ArrayList<String> results;

    AsyncTask<?, ?, ?> runningTask;

    public update(String database, String table, ArrayList<String> argList, ArrayList<String> valList, String itemID){
        this.database = database;
        this.table = table;
        this.argList = argList;
        this.valList = valList;
        this.itemID = itemID;

        localDBURL = "192.168.0.50"; //TODO;

        formatSQLString();
        formatURLString();
    }


    private void formatSQLString(){
        if(argList.size() == valList.size() && argList.size() > 0) {
            sqlRequest = "UPDATE `" + table + "` SET";

            for (int i = 0; i < argList.size(); i++) {
                sqlRequest = sqlRequest + " `" + argList.get(i) + "` = %27" + valList.get(i) + "%27";
                if(i != argList.size()-1) sqlRequest = sqlRequest + ",";
            }


            sqlRequest = sqlRequest + " WHERE `id` = %27" + itemID + "%27";
        }
    }

    private void formatURLString(){
        sqlRequest = replaceSpaces(sqlRequest);

        URLString = "http://" + localDBURL + "/dbaccess/update.php?";
        URLString = URLString + "db=" + database;
        URLString = URLString + "&sql=" + sqlRequest;

        //URLString = replacesBrackets(URLString);

        new URLcall().execute();
    }

    private class URLcall extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... arg0) {
            Log.d(TAG, "call to URL: " + URLString);

            results = new ArrayList<>();

            URL url;
            InputStream is = null;
            BufferedReader br;
            String line;

            try {
                url = new URL(URLString);
                is = url.openStream(); // throws an IOException
                br = new BufferedReader(new InputStreamReader(is));

                while ((line = br.readLine()) != null) {
                    results.add(line);
                    Log.d(TAG,"line read: " + line);
                }
            } catch (Exception mue) {
                Log.e(TAG, mue.toString());
                mue.printStackTrace();
            } finally {
                try {
                    if (is != null)
                        is.close();


                } catch (IOException ioe) {
                    Log.e(TAG, ioe.toString());

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(results.size() == 1 && results.get(0).equals("00000")) {
                Log.d(TAG, "successful");
            } else {
                Log.e(TAG, "more than one result / not successful");
                if(tryToUpdate){
                    //TODO: update call here
                }
            }
        }

    }

}
