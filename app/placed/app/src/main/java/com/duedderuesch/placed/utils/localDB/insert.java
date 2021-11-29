package com.duedderuesch.placed.utils.localDB;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;

import static com.duedderuesch.placed.utils.localDB.utils.replaceSpaces;

import com.duedderuesch.placed.utils.ApplicationExecutors;

public class insert {

    private final String TAG = "localDB.insert";

    private String database;
    private String table;
    private ArrayList<String> argList;
    private ArrayList<String> valList;
    private Boolean tryToUpdate = false;

    private String localDBURL;
    private String sqlRequest;
    private String URLString;

    private ApplicationExecutors exec;
    private ArrayList<String> results;

    AsyncTask<?, ?, ?> runningTask;

    public insert(String database, String table, ArrayList<String> argList, ArrayList<String> valList, Boolean tryToUpdate){
        this.database = database;
        this.table = table;
        this.argList = argList;
        this.valList = valList;
        this.tryToUpdate = tryToUpdate;

        localDBURL = "placedserver"; //TODO;

        formatSQLString();
        formatURLString();
    }


    private void formatSQLString(){
        sqlRequest = "INSERT INTO `" + table + "` (`" + argList.get(0) + "`";

        if(argList.size()>1) {
            for (int i = 1; i < argList.size(); i++) {
                sqlRequest = sqlRequest + ",`" + argList.get(i) + "`";
            }
        }

        sqlRequest = sqlRequest + ") VALUES (%27" + valList.get(0) + "%27";

        if(valList.size()>1) {
            for (int i = 1; i < valList.size(); i++) {
                sqlRequest = sqlRequest + ",%27" + valList.get(i) + "%27";
            }
        }

        sqlRequest = sqlRequest + ");";
    }

    private void formatURLString(){
        sqlRequest = replaceSpaces(sqlRequest);

        URLString = "http://" + localDBURL + "/dbaccess/upload.php?";
        URLString = URLString + "db=" + database;
        URLString = URLString + "&sql=" + sqlRequest;

        urlCall();
    }

    private void urlCall(){
        exec = new ApplicationExecutors();
        exec.getBackgroundThread().execute(
                () -> {
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
                            Log.d(TAG, "line read: " + line);
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

                    if (results.size() == 1 && results.get(0).equals("00000")) {
                        Log.d(TAG, "successful");
                    } else {
                        Log.e(TAG, "more than one result / not successful");
                        if (tryToUpdate) {
                            //TODO: update call here
                        }
                    }
                }
        );
    }

//    private static class URLcall extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... arg0) {
//            Log.d(TAG, "call to URL: " + URLString);
//
//            results = new ArrayList<>();
//
//            URL url;
//            InputStream is = null;
//            BufferedReader br;
//            String line;
//
//            try {
//                url = new URL(URLString);
//                is = url.openStream(); // throws an IOException
//                br = new BufferedReader(new InputStreamReader(is));
//
//                while ((line = br.readLine()) != null) {
//                    results.add(line);
//                    Log.d(TAG,"line read: " + line);
//                }
//            } catch (Exception mue) {
//                Log.e(TAG, mue.toString());
//                mue.printStackTrace();
//            } finally {
//                try {
//                    if (is != null)
//                        is.close();
//
//
//                } catch (IOException ioe) {
//                    Log.e(TAG, ioe.toString());
//
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if(results.size() == 1 && results.get(0).equals("00000")) {
//                Log.d(TAG, "successful");
//            } else {
//                Log.e(TAG, "more than one result / not successful");
//                if(tryToUpdate){
//                    //TODO: update call here
//                }
//            }
//        }
//
//    }

}
