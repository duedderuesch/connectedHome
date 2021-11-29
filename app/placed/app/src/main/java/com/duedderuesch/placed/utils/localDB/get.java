package com.duedderuesch.placed.utils.localDB;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.duedderuesch.placed.data.dbhelper.HomeDBHelper;
import com.duedderuesch.placed.data.dbhelper.PlantDBHelper;
import com.duedderuesch.placed.data.dbhelper.SensorDataDBHelper;
import com.duedderuesch.placed.data.dbhelper.SensorDeviceDBHelper;
import com.duedderuesch.placed.data.dbhelper.ServerDBHelper;
import com.duedderuesch.placed.data.entities.Home;
import com.duedderuesch.placed.data.entities.Plant;
import com.duedderuesch.placed.data.entities.Result;
import com.duedderuesch.placed.data.entities.SensorData;
import com.duedderuesch.placed.data.entities.SensorDevice;
import com.duedderuesch.placed.data.entities.Server;
import com.duedderuesch.placed.utils.HttpHandler;
import com.duedderuesch.placed.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class get{
    private static final String TAG = "localDB.get";

    public static void fetchData(String table,
                                 Integer getKeys,
                                 ArrayList<String> whereArgs,
                                 ArrayList<String> whereVals,
                                 String limit,
                                 utils.DataGetterCallback<String> callback,
                                 Context ctx){

        if(getKeys == 1 || getKeys == 0) {
            ExecutorService singleThreadExecService = Executors.newSingleThreadExecutor();
            singleThreadExecService.execute(() -> {
                HttpHandler netHandler = new HttpHandler();
                String url = "http://" + Utils.getLocalServerIP(ctx) + "/dbaccess/get.php?table=" +
                        table + "&keys=" + getKeys;
                if (whereArgs != null && whereVals != null) url = url + "&where="
                        + utils.createSQLWhere(whereArgs, whereVals);
                if (limit != null) url = url + "&limit=" + limit;


                String rawJsonAnswer = netHandler.makeServiceCall(url);

                Log.d(TAG, "URL: " + url);
                Log.d(TAG, "Response from url: " + rawJsonAnswer);
                if (rawJsonAnswer != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonAnswer);
                        if (getKeys == 0) {
                            switch (table) {
                                case SensorDataDBHelper.TABLE_NAME:
                                    Result.Success<String> getDataResult= new Result.Success<String>(storeData_SensorData(jsonArray, ctx));
                                    callback.onComplete(getDataResult);
                                    break;
                                case HomeDBHelper.TABLE_NAME:
                                    storeData_Home(jsonArray, ctx);
                                    break;
                                case SensorDeviceDBHelper.TABLE_NAME:
                                    storeData_SensorDevice(jsonArray, ctx);
                                    break;
                                case PlantDBHelper.TABLE_NAME:
                                    storeData_Plant(jsonArray, ctx);
                                    break;
                                case ServerDBHelper.TABLE_NAME:
                                    Result.Success<String> getServerResult = new Result.Success<String>(storeData_Server(jsonArray, ctx));
                                    callback.onComplete(getServerResult);
                                    break;
                            }
                        } else {
                            //TODO: getKeys = 1 usage
                        }


                    } catch (final JSONException e) {
                        Result.Failure<String> result = new Result.Failure<String>(Utils.StatusCode.failed.toString());
                        callback.onComplete(result);
                    }

                } else {
                    Result.Failure<String> result = new Result.Failure<String>(Utils.StatusCode.failed.toString());
                    callback.onComplete(result);
                }
            });
        } else {
            Result.Failure<String> result = new Result.Failure<String>(Utils.StatusCode.wrong_args.toString());
            callback.onComplete(result);
        }

    }

    private static String storeData_SensorData(JSONArray jsonArray, Context ctx){
        String lastId = null;

        SensorDataDBHelper dataDBHelper = new SensorDataDBHelper(ctx);
        dataDBHelper.getWritableDatabase();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                SensorData querySensorData = new SensorData(
                        c.getInt("id"),
                        c.getString("sensor"),
                        c.getInt("is_generic"),
                        c.getString("value"),
                        c.getString("data_type"),
                        c.getInt("year"),
                        c.getInt("month"),
                        c.getInt("day"),
                        c.getInt("hour"),
                        c.getInt("minute"));

                dataDBHelper.addSensorDataIfNotExist(querySensorData);

                lastId = String.valueOf(c.getInt("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lastId;
    }

    private static void storeData_Home(JSONArray jsonArray, Context ctx){
        HomeDBHelper homeDBHelper = new HomeDBHelper(ctx);
        homeDBHelper.getWritableDatabase();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                Home queryHome = new Home(
                        c.getInt("id"),
                        c.getString("name"),
                        c.getString("address"));

                homeDBHelper.addHomeIfNotExist(queryHome);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void storeData_Plant(JSONArray jsonArray, Context ctx){
        PlantDBHelper plantDBHelper = new PlantDBHelper(ctx);
        plantDBHelper.getWritableDatabase();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                Plant queryPlant = new Plant(
                        c.getInt("id"),
                        c.getString("name"),
                        c.getString("biological_name"),
                        c.getString("home"),
                        c.getString("birthdate"),
                        c.getInt("min_humidity"),
                        c.getInt("max_humidity"),
                        c.getInt("min_temperature"),
                        c.getInt("max_temperature"),
                        c.getString("note"));

                plantDBHelper.addPlantIfNotExist(queryPlant);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void storeData_SensorDevice(JSONArray jsonArray, Context ctx){
       SensorDeviceDBHelper deviceDBHelper = new SensorDeviceDBHelper(ctx);
       deviceDBHelper.getWritableDatabase();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                SensorDevice queryDevice = new SensorDevice(
                        c.getInt("id"),
                        c.getString("name"),
                        c.getString("sensor_type"),
                        c.getInt("is_generic"),
                        c.getString("plant"),
                        c.getString("home"),
                        c.getString("server"),
                        c.getString("ip"));

                deviceDBHelper.addSensorDeviceIfNotExist(queryDevice);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static String storeData_Server(JSONArray jsonArray, Context ctx){
        Integer lastId = null;
        ServerDBHelper serverDBHelper = new ServerDBHelper(ctx);
        serverDBHelper.getWritableDatabase();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                Server queryServer = new Server(
                        c.getInt("id"),
                        c.getString("name"),
                        c.getString("home"),
                        c.getString("server_type"),
                        c.getString("address"));

                serverDBHelper.updateServer(queryServer);
                if(lastId == null || lastId>c.getInt("id")) lastId = c.getInt("id");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(lastId != null) return lastId.toString();
        else return null;

    }
}
