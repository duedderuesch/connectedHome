package com.duedderuesch.placed.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.duedderuesch.placed.data.dbhelper.HomeDBHelper;
import com.duedderuesch.placed.data.dbhelper.PlantDBHelper;
import com.duedderuesch.placed.data.dbhelper.SensorDataDBHelper;
import com.duedderuesch.placed.data.dbhelper.SensorDeviceDBHelper;
import com.duedderuesch.placed.data.dbhelper.ServerDBHelper;
import com.duedderuesch.placed.data.entities.Result;
import com.duedderuesch.placed.data.entities.Server;
import com.duedderuesch.placed.ui.login.LoginActivity;

import com.duedderuesch.placed.utils.localDB.get;
import com.duedderuesch.placed.utils.localDB.utils;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class startup extends LoginActivity {

    private static String TAG = "LoginActivity_extending_startup";

    interface RepositoryCallback<T> {
        void onComplete(Result<T> result);
    }
    interface DataGetterCallback<T> {
        void onComplete(Result<T> result);
    }

    public static void getStartup(Context ctx) {

        SensorDataDBHelper dataDBHelper = new SensorDataDBHelper(ctx);
        SensorDeviceDBHelper deviceDBHelper = new SensorDeviceDBHelper(ctx);
        PlantDBHelper plantDBHelper = new PlantDBHelper(ctx);



        Log.d(TAG, "startup");
        boolean localServerReachable = true;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        if(prefs.getBoolean("sw_data_stopDataLocalServer",true)){
            String localServerIP = Utils.getLocalServerIP(ctx);
            if(localServerIP != null) {
                String localServerSubnetMask = "255.255.255.0";
                Log.d(TAG, Networking.getIpAddress(ctx));
                String currentIP = Networking.getIpAddress(ctx);
                String[] localServerIPParts = localServerIP.split("\\.");
                String[] deviceIPParts = currentIP.split("\\.");
                String[] subnetMaskParts = localServerSubnetMask.split("\\.");
                for (int i = 0; i < subnetMaskParts.length; i++) {
                    int block = Integer.parseInt(subnetMaskParts[i]);
                    if (block > 0) {
                        if (!localServerIPParts[i].equals(deviceIPParts[i])) {
                            localServerReachable = false;
                            break;
                        }
                    }
                }
            } else localServerReachable = false;
        }
        if(!localServerReachable){
            LoginActivity.local_db_status = dbStatus.NOTREACHABLE;
            Log.d(TAG, "!localServerReachable");
            String firebaseURL = prefs.getString("et_pref_server_fbdbadress",null);
            if(firebaseURL != null) {
                // only for reference: FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance("https://placed-5b875-default-rtdb.europe-west1.firebasedatabase.app/");
                FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance(firebaseURL);
                ExecutorService singleThreadExecService = Executors.newSingleThreadExecutor(); //TODO: inline Executor and threads
                singleThreadExecService.execute(() -> {
                    FirebaseRealtimeDB.getFBSensorList(fbDatabase, new RepositoryCallback<ArrayList<String>>() {
                        @Override
                        public void onComplete(Result<ArrayList<String>> result) {
                            if (result instanceof Result.Success) {
                                LoginActivity.firebase_db_status = dbStatus.ONLINE;
                                for (int i = 0; i < ((Result.Success<ArrayList<String>>) result).data.size(); i++) {
                                    Log.d("FirebaseGetter", i + ".1");
                                    FirebaseRealtimeDB.getFBSensorData(fbDatabase,
                                            ((Result.Success<ArrayList<String>>) result).data.get(i),
                                            ctx);
                                }
                            } else {

                            }
                        }
                    });
                });
            }
        } else {

            Log.d(TAG, "localServerReachable");
            //TODO:
            get.fetchData(SensorDeviceDBHelper.TABLE_NAME, 0, null, null, null, null,ctx);
            get.fetchData(PlantDBHelper.TABLE_NAME,0,null,null,"25",null,ctx);
            get.fetchData(HomeDBHelper.TABLE_NAME,0,null,null,"25",null,ctx);
            get.fetchData(ServerDBHelper.TABLE_NAME, 0, null, null, "20", new utils.DataGetterCallback<String>() {
                @Override
                public void onComplete(Result<String> result) {
                    if(result != null) {
                        LoginActivity.local_db_status = dbStatus.ONLINE;
                        LoginActivity.firebase_db_status = dbStatus.ONLINE;

                        //Server Settings überprüfen TODO: Choose which settings you want to keep Dialog
                        ServerDBHelper serverDBHelper = new ServerDBHelper(ctx);
                        ArrayList<Server> serverList = serverDBHelper.getAllServer();
                        for (int i = 0; i < serverList.size(); i++) {
                            if (serverList.get(i).getServer_type().equals(Server.ServerType.LOCAL)) {
                                SharedPreferences.Editor prefEditor = prefs.edit();
                                prefEditor.putString("et_pref_server_localip", serverList.get(i).getAddress());
                                prefEditor.commit();
                            } else if (serverList.get(i).getServer_type().equals(Server.ServerType.FIREBASE)) {
                                SharedPreferences.Editor prefEditor = prefs.edit();
                                prefEditor.putString("et_pref_server_fbdbadress", serverList.get(i).getAddress());
                                prefEditor.commit();
                            }
                        }
                    }
                }
            }, ctx);
            get.fetchData(SensorDataDBHelper.TABLE_NAME, 0, null, null, "30", new utils.DataGetterCallback<String>() {
                @Override
                public void onComplete(Result<String> result) {
                    if(result instanceof Result.Success) {
                        //pass
                    }
                }
            }, ctx);
        }
        endStartup(ctx);
    }


    private static void endStartup(Context ctx){
        Log.d(TAG, "endstartup");
        FirebaseRealtimeDB.databaseStatus(ctx, new FirebaseRealtimeDB.IsOnlineCallback<Boolean>() {
            @Override
            public void onComplete(Boolean isOnline) {
                if(isOnline) firebase_db_status = dbStatus.ONLINE;
                else firebase_db_status = dbStatus.NOTREACHABLE;
                finishLogin(ctx);
            }
        });
    }
}
