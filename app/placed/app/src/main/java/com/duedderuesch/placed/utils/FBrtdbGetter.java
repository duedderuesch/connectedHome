package com.duedderuesch.placed.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.duedderuesch.placed.data.dbhelper.SensorDataDBHelper;
import com.duedderuesch.placed.data.entities.Result;
import com.duedderuesch.placed.data.entities.SensorData;
import com.duedderuesch.placed.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FBrtdbGetter {

    private static final String TAG = "FirebaseGetter";

    public static void getFBSensorList(FirebaseDatabase fbDatabase,
                                       final startup.RepositoryCallback<ArrayList<String>> callback){

        ArrayList<String> sensorList = new ArrayList<>();

        DatabaseReference fbDatabaseReference = fbDatabase.getReference();
        fbDatabaseReference.child("/data_by_sensors").
                get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "Result.Error null");
                    Result.Error<ArrayList<String>> result = new Result.Error<ArrayList<String>>(null);
                    callback.onComplete(result);
                } else {
                    DataSnapshot dataSnapshot = task.getResult();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Log.d(TAG, "got child: " + child.getKey());
                        sensorList.add(child.getKey());
                    }
                    Log.d(TAG, "Result.Success List.Size: " + sensorList.size());
                    Result.Success<ArrayList<String>> result = new Result.Success<ArrayList<String>> (sensorList);
                    callback.onComplete(result);
                }
            }
        });
    }

    public static void getFBSensorData(FirebaseDatabase fb_db, String childPath, Boolean withOutput, Context ctx) {
        Log.d(TAG, childPath);
        if (withOutput) {
            LoginActivity.setLoadingInfo("loading Child " + childPath + " from Firebase");
        }
        SensorDataDBHelper dataDBHelper = new SensorDataDBHelper(ctx);
        Log.d(TAG, "getting child");
        Map<String, String> queryMap = new HashMap<>();
        DatabaseReference fb_db_ref = fb_db.getReference("/data_by_sensors");
        //fb_db_ref.child("placed_moist_1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
        fb_db_ref.child(childPath).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d(TAG, "complete");
                if (!task.isSuccessful()) {
                    Log.d(TAG, "error");
                    queryMap.put("error", String.valueOf(Utils.StatusCode.not_found));
                    LoginActivity.firebase_db_status = LoginActivity.dbStatus.NOTREACHABLE;
                } else {
                    Log.d(TAG, "success");
                    LoginActivity.firebase_db_status = LoginActivity.dbStatus.ONLINE;
                    queryMap.put("error", String.valueOf(Utils.StatusCode.no_error));
                    DataSnapshot dataSnapshot = task.getResult();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        queryMap.put(child.getKey(), child.getValue().toString());
                    }
                    dataDBHelper.addSensorDataIfNotExist(MapToSensorData(queryMap));
                }
            }
        });
    }

    public static SensorData MapToSensorData(Map<String, String> map) {
        SensorData sensorData;
        try {
            sensorData = new SensorData(
                    Integer.valueOf(map.get("id")),
                    map.get("sensor"),
                    Integer.valueOf(map.get("is-generic")),
                    map.get("value"),
                    map.get("data-type"),
                    Integer.valueOf(map.get("year")),
                    Integer.valueOf(map.get("month")),
                    Integer.valueOf(map.get("day")),
                    Integer.valueOf(map.get("hour")),
                    Integer.valueOf(map.get("minute")));

                    Log.d(TAG, map.get("id"));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            sensorData = null;
        }
        return sensorData;
    }
}
