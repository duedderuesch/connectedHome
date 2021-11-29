package com.duedderuesch.placed.utils.listAdapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.duedderuesch.placed.R;
import com.duedderuesch.placed.data.dbhelper.SensorDeviceDBHelper;
import com.duedderuesch.placed.data.entities.SensorData;
import com.duedderuesch.placed.data.entities.SensorDevice;
import com.duedderuesch.placed.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeListAdapter extends ArrayAdapter<String> {

    private final String TAG = "HomeListAdapter";

    private final Context ctx;
    private ArrayList<String> homeNameList;

    ArrayList<SensorData> dataList;

    public HomeListAdapter(Context context, ArrayList<String> homeNameList, ArrayList<SensorData> dataList) {
        super(context,0, homeNameList);
        ctx = context;

        this.homeNameList = homeNameList;
        this.dataList = dataList;
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {

        SensorDeviceDBHelper deviceDBHelper = new SensorDeviceDBHelper(ctx);

        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(ctx).inflate(R.layout.list_item_homelist,parent,false);

        TextView tv_name = listItem.findViewById(R.id.tv_litem_start_home_name);
        TextView tv_temp_out = listItem.findViewById(R.id.tv_litem_start_home_out_temp);
        TextView tv_temp_in = listItem.findViewById(R.id.tv_litem_start_home_in_temp);
        ImageView iv_ic_fbstate = listItem.findViewById(R.id.iv_litem_start_home_fbstate);
        ImageView iv_ic_ldbstate = listItem.findViewById(R.id.iv_litem_start_home_ldbstate);
        ImageView iv_weathericon = listItem.findViewById(R.id.iv_litem_start_home_out_weathericon);
        ImageView iv_bag_sun = listItem.findViewById(R.id.iv_litem_start_home_weatherbackg_sun);
        ImageView iv_bag_rain = listItem.findViewById(R.id.iv_litem_start_home_weatherbackg_rain);

        Drawable ic_cloud_red =  AppCompatResources.getDrawable(ctx, R.drawable.ic_cloud_red);
        Drawable ic_cloud_green =  AppCompatResources.getDrawable(ctx, R.drawable.ic_cloud_green);


        if(LoginActivity.firebase_db_status.equals(LoginActivity.dbStatus.ONLINE)){
            iv_ic_fbstate.setImageDrawable(ic_cloud_green);
        } else {
            iv_ic_fbstate.setImageDrawable(ic_cloud_red);
        }

        if(LoginActivity.local_db_status.equals(LoginActivity.dbStatus.NOTREACHABLE) || LoginActivity.local_db_status.equals(LoginActivity.dbStatus.na)){
            iv_ic_ldbstate.setImageDrawable(ic_cloud_red);
        } else {
            iv_ic_ldbstate.setImageDrawable(ic_cloud_green);
        }

        try {
            String temp_outdoor = null;
            String temp_indoor = null;
            Integer weatherIcon = null;

            Log.d(TAG, Arrays.toString(dataList.toArray()));

            for (int i = 0; i < dataList.size(); i++) {
                ArrayList<SensorDevice> queryDeviceList =deviceDBHelper.getSensorDevicesBy(
                        SensorDeviceDBHelper.COLUMN_ID,
                        dataList.get(i).getSensor()
                );
                SensorDevice sensor = null;
                if(queryDeviceList.size() > 0)  sensor = queryDeviceList.get(0);
                if(sensor != null) {
                    if (sensor.getIs_generic() == 1 &&
                            sensor.getSensor_type().equals(SensorDevice.SensorType.temperature) &&
                            sensor.getName().toLowerCase().contains("outdoor")) {
                        temp_outdoor = dataList.get(i).getValue();
                    } else if (sensor.getIs_generic() == 1 &&
                            sensor.getSensor_type().equals(SensorDevice.SensorType.weather)) {
                        weatherIcon = weatherIconSetter(Integer.valueOf(dataList.get(i).getValue()));

                        if (weatherIcon == R.drawable.ic_weather_sun || weatherIcon == R.drawable.ic_weather_cloud) {
                            iv_bag_sun.setVisibility(View.VISIBLE);
                            iv_bag_rain.setVisibility(View.GONE);
                        } else {
                            iv_bag_sun.setVisibility(View.GONE);
                            iv_bag_rain.setVisibility(View.VISIBLE);
                        }
                    } else if (sensor.getIs_generic() == 1 &&
                            sensor.getSensor_type().equals(SensorDevice.SensorType.temperature) &&
                            sensor.getName().toLowerCase().contains("indoor")) {
                        temp_indoor = dataList.get(i).getValue();
                    }
                }
            }


            if (temp_outdoor == null) {
                temp_outdoor = "uk";
            }

            tv_name.setText(homeNameList.get(position));
            tv_temp_out.setText(temperatureAsShown(temp_outdoor));
            tv_temp_in.setText(temperatureAsShown(temp_indoor));
            iv_weathericon.setImageDrawable(AppCompatResources.getDrawable(ctx, weatherIcon));
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, e.toString() + " | " + e.getLocalizedMessage());
        }
        return listItem;
    }

    // It gets a View that displays in the drop down popup the data at the specified position
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // It gets a View that displays the data at the specified position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private Integer weatherIconSetter(Integer weatherId){
        Integer weatherIcon = null;
        if(weatherId == 800){
            weatherIcon = R.drawable.ic_weather_sun;
        } else if(weatherId == 741){
            weatherIcon = R.drawable.ic_weather_fog;
        } else if(weatherId > 699 ){
            weatherIcon = R.drawable.ic_weather_cloud;
        } else if(weatherId > 599){
            weatherIcon = R.drawable.ic_weather_snow;
        } else if(weatherId > 299){
            weatherIcon = R.drawable.ic_weather_rain;
        } else if(weatherId > 199){
            weatherIcon = R.drawable.ic_weather_storm;
        }

        return weatherIcon;
    }

    private String temperatureAsShown(String temp){
        String output;
        String[] originalTempParts = temp.split("\\.");
        if(!originalTempParts[0].equals("uk") && originalTempParts.length > 1) {
            if (Integer.parseInt(originalTempParts[0]) > 9) {
                int convertedValue = Integer.parseInt(originalTempParts[0]);
                if(fiveOrAbove(originalTempParts[1].substring(0,1))) convertedValue ++;
                output = convertedValue + "°C";

            } else if (Integer.parseInt(originalTempParts[0]) < -9) {
                output = originalTempParts[0] + "°C"; //TODO: negative rounding of value
            } else {
                if (originalTempParts[1].length() > 1 && fiveOrAbove(originalTempParts[1].substring(1, 2))) {
                    int roundedTempPart = Integer.parseInt(originalTempParts[1].substring(0, 1)) + 1;
                    if (roundedTempPart == 10) {
                        int roundedTransferPart = Integer.parseInt(originalTempParts[0]) + 1;
                        output = roundedTransferPart + "°C";
                    } else {
                        output = originalTempParts[0] + "," + roundedTempPart + "°C";
                    }
                } else {
                    output = originalTempParts[0] + "," + originalTempParts[1].charAt(0) + "°C";
                }
            }
            return output;
        } else {
            return temp;
        }

    }

    private Boolean fiveOrAbove(String inputNumberAsString){
        int input = Integer.parseInt(inputNumberAsString);
        return input > 4;
    }
}