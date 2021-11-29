package com.duedderuesch.placed.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.duedderuesch.placed.R;
import com.duedderuesch.placed.data.dbhelper.SensorDataDBHelper;
import com.duedderuesch.placed.data.dbhelper.SensorDeviceDBHelper;
import com.duedderuesch.placed.data.entities.Result;
import com.duedderuesch.placed.data.entities.SensorData;
import com.duedderuesch.placed.data.entities.Plant;
import com.duedderuesch.placed.data.entities.SensorDevice;
import com.duedderuesch.placed.data.handler.SevenDayDataHandler;
import com.duedderuesch.placed.ui.edit.EditPlantActivity;
import com.duedderuesch.placed.ui.login.LoginActivity;
import com.duedderuesch.placed.utils.FirebaseRealtimeDB;
import com.duedderuesch.placed.utils.listAdapters.SensorListAdapter;
import com.duedderuesch.placed.utils.localDB.utils;
import com.duedderuesch.placed.utils.views.SevenDayChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PlantActivity extends AppCompatActivity {

    private ListView lv_sensors;
    private TextView tv_title;
    private TextView tv_note;
    private SensorDataDBHelper dataDBHelper;
    private SensorDeviceDBHelper deviceDBHelper;
    private SensorListAdapter sensorListAdapter = null;
    public static Plant plant;
    private Integer plantId;
    private ArrayList<SensorData> sensorDataList;
    private SevenDayChart sdc;
    private ProgressBar pb_loading;

    public interface PlantGetDataCallback<T>{
        void onComplete(Result<T> result);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSensorForNewData();
                Snackbar.make(view, "Updating Sensor Data", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        deviceDBHelper = new SensorDeviceDBHelper(getApplicationContext());
        dataDBHelper = new SensorDataDBHelper(getApplicationContext());

        sdc = findViewById(R.id.sdc_plant);
        dataDBHelper = new SensorDataDBHelper(getApplicationContext());

        tv_title = findViewById(R.id.tv_plant_title);
        tv_note = findViewById(R.id.tv_plant_note);
        Button bt_chart_hum = findViewById(R.id.bt_plant_chart_hum);
        Button bt_chart_temp = findViewById(R.id.bt_plant_chart_temp);
        ImageView iv_icedit = findViewById(R.id.iv_plant_icedit);
        pb_loading = findViewById(R.id.pb_plant_loading);
        lv_sensors = findViewById(R.id.lv_plant_sensor_data);


        pb_loading.setVisibility(View.GONE);
        plantId = plant.getId();

        lv_sensors.setHeaderDividersEnabled(true);
        lv_sensors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_timestamp       = view.findViewById(R.id.tv_litem_plant_timestamp);
                TextView tv_value_min       = view.findViewById(R.id.tv_litem_plant_value_min);
                TextView tv_value_value     = view.findViewById(R.id.tv_litem_plant_value_current);
                TextView tv_value_max       = view.findViewById(R.id.tv_litem_plant_value_max);
                TextView tv_desc_timestamp  = view.findViewById(R.id.tv_litem_plant_desc_timestamp);
                TextView tv_desc_min        = view.findViewById(R.id.tv_litem_plant_desc_min);
                TextView tv_desc_value      = view.findViewById(R.id.tv_litem_plant_desc_current);
                TextView tv_desc_max        = view.findViewById(R.id.tv_litem_plant_desc_max);

                if(tv_timestamp.getVisibility() == View.GONE) {
                    tv_timestamp.setVisibility(View.VISIBLE);
                    tv_value_min.setVisibility(View.VISIBLE);
                    tv_value_value.setVisibility(View.VISIBLE);
                    tv_value_max.setVisibility(View.VISIBLE);
                    tv_desc_timestamp.setVisibility(View.VISIBLE);
                    tv_desc_min.setVisibility(View.VISIBLE);
                    tv_desc_value.setVisibility(View.VISIBLE);
                    tv_desc_max.setVisibility(View.VISIBLE);
                } else {
                    tv_timestamp.setVisibility(View.GONE);
                    tv_value_min.setVisibility(View.GONE);
                    tv_value_value.setVisibility(View.GONE);
                    tv_value_max.setVisibility(View.GONE);
                    tv_desc_timestamp.setVisibility(View.GONE);
                    tv_desc_min.setVisibility(View.GONE);
                    tv_desc_value.setVisibility(View.GONE);
                    tv_desc_max.setVisibility(View.GONE);
                }
            }
        });
        lv_sensors.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_plantId        = view.findViewById(R.id.tv_litem_plant_sensorid);
                //new SensorActivity(tv_plantId.getText().toString());
                Intent i = new Intent(getApplicationContext(), SensorGraphActivity.class);
                startActivity(i);
                return false;
            }
        });


        iv_icedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPlantActivity.setPlantObject(plant);
                Intent intent = new Intent(getApplicationContext(), EditPlantActivity.class);
                startActivity(intent);
            }
        });

        bt_chart_hum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDataType(SensorDevice.SensorType.soil_humidity);
            }
        });

        bt_chart_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDataType(SensorDevice.SensorType.temperature);
            }
        });

        fetchAllDataForPlant(plantId.toString(), getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUi();
    }

    private void updateUi(){
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable mainThreadRunnable = new Runnable() {
            @Override
            public void run() {
                tv_title.setText(plant.getName());
                tv_note.setText(plant.getNote());

                sensorDataList = new ArrayList<>();
                ArrayList<SensorDevice> deviceList = deviceDBHelper.getSensorDevicesBy(deviceDBHelper.COLUMN_PLANT, plantId.toString());
                for (int i = 0; i < deviceList.size(); i++) {
                    ArrayList<SensorData> queryDataList = dataDBHelper.getAllSensorDataBy(dataDBHelper.COLUMN_SENSOR, deviceList.get(i).getId().toString(), "1");
                    sensorDataList.addAll(queryDataList);
                }
                if (sensorListAdapter != null && sensorListAdapter.getCount() > 0)
                    sensorListAdapter.clear();
                sensorListAdapter = new SensorListAdapter(getApplicationContext(), sensorDataList);


                lv_sensors.setAdapter(sensorListAdapter);

                ArrayList<SensorData> rawQuerryDataList =
                        dataDBHelper.getSensorDataLastSevenDays("1"); //TODO: choose initial Sensor
                if (rawQuerryDataList.size() != 0) {
                    sdc.setDataHandler(new SevenDayDataHandler(getApplicationContext(), rawQuerryDataList));
                }
                sdc.invalidate();
            }
        };
        mainHandler.post(mainThreadRunnable);
    }

    private void chooseDataType(String sensorType){
        Integer sensorId = null;
        ArrayList<SensorDevice> deviceList = new ArrayList<>();
        deviceList = deviceDBHelper.getSensorDevicesBy(SensorDeviceDBHelper.COLUMN_PLANT, plantId.toString());
        for(int i = 0; i < deviceList.size();i++){
            if(deviceList.get(i).getSensor_type().equals(sensorType)){
                sensorId = deviceList.get(i).getId();
            }
        }
        if(sensorId != null) {
            ArrayList<SensorData> rawQuerryDataList =
                    dataDBHelper.getSensorDataLastSevenDays(sensorId.toString());
            if (rawQuerryDataList.size() != 0) {
                sdc.setDataHandler(new SevenDayDataHandler(getApplicationContext(), rawQuerryDataList));
            }
            sdc.invalidate();
        }
    }

    private void fetchAllDataForPlant(String plantId, Context ctx){
        pb_loading.setVisibility(View.VISIBLE);
        ArrayList<SensorDevice> deviceList =
                deviceDBHelper.getSensorDevicesBy(SensorDeviceDBHelper.COLUMN_PLANT, plantId);
        if(LoginActivity.local_db_status.equals(LoginActivity.dbStatus.ONLINE)) {
            for (int i = 0; i < deviceList.size(); i++) {
                utils.getMissingSensorData(deviceList.get(i).getId().toString(), ctx, new PlantGetDataCallback<Integer>() {
                    @Override
                    public void onComplete(Result<Integer> result) {
                        pb_loading.setVisibility(View.GONE);

                        updateUi();
                    }
                });
            }
        } else if (LoginActivity.firebase_db_status.equals(LoginActivity.dbStatus.ONLINE)) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
            String firebaseURL = prefs.getString("et_pref_server_fbdbadress",null);
            FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance(firebaseURL);
            for (int i = 0; i < deviceList.size(); i++) {
                FirebaseRealtimeDB.getFBSensorData(fbDatabase, deviceList.get(i).getId().toString(), ctx);
            }
        } else pb_loading.setVisibility(View.GONE);

    }

    private void callSensorForNewData(){
        Log.d("FirebaseGetter", "callSensorForNewData()");
        ArrayList<String> whereArgs = new ArrayList<>();
        ArrayList<String> whereVals = new ArrayList<>();

        whereArgs.add("requestUpdate");
        whereVals.add("1");

        ArrayList<SensorDevice> deviceList = deviceDBHelper.getSensorDevicesBy(deviceDBHelper.COLUMN_PLANT, plantId.toString());
        for(int i=0; i<deviceList.size(); i++){
            FirebaseRealtimeDB.updateDataBySensors(deviceList.get(i).getId(), whereArgs, whereVals, new FirebaseRealtimeDB.SuccessCallback<Boolean>() {
                @Override
                public void onComplete(Result<Boolean> result) {
                    if(result instanceof Result.Success){
                        Log.d("FirebaseGetter", "success");
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("FirebaseGetter", "calling now to get new data");
                                fetchAllDataForPlant(plantId.toString(), getApplicationContext());
                            }
                        }, 8000);
                    } else {
                        Log.d("FirebaseGetter", "failure");
                    }


                }
            });
        }






    }

}