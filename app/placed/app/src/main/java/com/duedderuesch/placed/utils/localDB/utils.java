package com.duedderuesch.placed.utils.localDB;

import android.content.Context;
import android.util.Log;

import com.duedderuesch.placed.data.dbhelper.SensorDataDBHelper;
import com.duedderuesch.placed.data.entities.Result;
import com.duedderuesch.placed.data.entities.SensorData;
import com.duedderuesch.placed.ui.PlantActivity;
import com.duedderuesch.placed.utils.Utils;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class utils {

    public interface DataGetterCallback<T>{
        void onComplete(Result<T> result);
    }

    public static String replaceSpaces(String rootString ){
        String finalString;

        finalString = rootString.replace(" ", "@~");

        return finalString;

    }

    public static String replacesBrackets(String rootString ){
        String finalString;

        finalString = rootString.replace("(", "@++");
        finalString = finalString.replace(")", "@--");

        return finalString;

    }


    public static String createSQLWhere(ArrayList<String> whereArgs, ArrayList<String> whereVals){
        if(whereArgs.size() == whereVals.size()){
            String sql = null;
            if(whereArgs.size()==1) sql = whereArgs.get(0) + whereVals.get(0);
            else {
                for (int i = 0; i < whereArgs.size()-1; i++) {
                    if(sql == null) sql = whereArgs.get(i) + whereVals.get(i) + " AND ";
                    else sql = sql + whereArgs.get(i) + whereVals.get(i) + " AND ";
                }
                sql = sql + whereArgs.get(whereArgs.size()-1) + whereVals.get(whereArgs.size()-1);
            }

            return(replaceSpaces(sql));
        } else {
            return null;
        }
    }

    public static void getMissingSensorData(String sensorId, Context ctx, PlantActivity.PlantGetDataCallback<Integer> callbackFromPlant){
        SensorDataDBHelper dataDBHelper = new SensorDataDBHelper(ctx);
        dataDBHelper.getReadableDatabase();
        Integer lastEntryId = dataDBHelper.getLastIdBy(SensorDataDBHelper.COLUMN_SENSOR, sensorId);
        if(lastEntryId != null) getSensorData(sensorId,25, lastEntryId, ctx, callbackFromPlant);
    }

    private static void getSensorData(String sensorId, Integer limit,Integer lastEntryId, Context ctx, PlantActivity.PlantGetDataCallback<Integer> callbackFromPlant){

        ArrayList<String> whereArgs = new ArrayList<>();
        ArrayList<String> whereVals = new ArrayList<>();

        if(lastEntryId != null) {
            lastEntryId++;
            whereArgs.add(SensorDataDBHelper.COLUMN_ID);
            whereVals.add("< " + lastEntryId);
            lastEntryId--;
        }
        if(sensorId != null){
            whereArgs.add(SensorDataDBHelper.COLUMN_SENSOR);
            whereVals.add("= " + sensorId);
        }

        final Integer comparisonId = lastEntryId;
        ExecutorService singleThreadExecService = Executors.newSingleThreadExecutor();
        singleThreadExecService.execute(() -> {

            if(whereArgs.size() > 0) {
                get.fetchData(SensorDataDBHelper.TABLE_NAME, 0, whereArgs, whereVals, limit.toString(), new DataGetterCallback<String>() {
                    @Override
                    public void onComplete(Result<String> result) {
                        if (result instanceof Result.Success) {
                            if(((Result.Success<String>) result).data != null) {
                                Log.d("localDB.get", "result.data: " + ((Result.Success<String>) result).data + " comparisonId: " + comparisonId);
                                if (((Result.Success<String>) result).data.equals(comparisonId.toString())) {
                                    Log.d("localDB.get", "successful result: 1");
                                    Result.Success<Integer> resultForPlant= new Result.Success<Integer>(Utils.StatusCode.successful);
                                    callbackFromPlant.onComplete(resultForPlant);
                                } else {
                                    Log.d("localDB.get", "call with index: "+ ((Result.Success<String>) result).data);
                                    getSensorData(sensorId, limit, Integer.valueOf(((Result.Success<String>) result).data), ctx, callbackFromPlant);
                                }
                            } else {
                                Log.d("localDB.get", "bad result: result data == null");
                                Result.Failure<Integer> resultForPlant= new Result.Failure<Integer>(Utils.StatusCode.failed);
                                callbackFromPlant.onComplete(resultForPlant);
                            }
                        } else {
                            Log.d("localDB.get", "bad result: 2");
                            Result.Failure<Integer> resultForPlant= new Result.Failure<Integer>(Utils.StatusCode.failed);
                            callbackFromPlant.onComplete(resultForPlant);
                        }
                    }
                }, ctx);
            } else {
                get.fetchData(SensorDataDBHelper.TABLE_NAME, 0, null, null, limit.toString(), new DataGetterCallback<String>() {
                    @Override
                    public void onComplete(Result<String> result) {
                        if (result instanceof Result.Success) {
                            if (((Result.Success<String>) result).data.equals(comparisonId.toString())) {
                                Log.d("localDB.get", "successful result: 2");
                                Result.Success<Integer> resultForPlant= new Result.Success<Integer>(Utils.StatusCode.successful);
                                callbackFromPlant.onComplete(resultForPlant);
                            } else {
                                getSensorData(sensorId, limit, Integer.valueOf(((Result.Success<String>) result).data), ctx, callbackFromPlant);
                            }
                        } else {
                            Log.d("localDB.get", "bad result: 3");
                            Result.Failure<Integer> resultForPlant= new Result.Failure<Integer>(Utils.StatusCode.failed);
                            callbackFromPlant.onComplete(resultForPlant);
                        }
                    }
                }, ctx);
            }

        });
    }


}
