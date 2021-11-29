package com.duedderuesch.placed.utils.listAdapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.duedderuesch.placed.R;
import com.duedderuesch.placed.data.dbhelper.PlantDBHelper;
import com.duedderuesch.placed.data.dbhelper.SensorDeviceDBHelper;
import com.duedderuesch.placed.data.entities.Plant;
import com.duedderuesch.placed.data.entities.SensorData;
import com.duedderuesch.placed.data.entities.SensorDevice;
import com.duedderuesch.placed.ui.login.LoginActivity;
import com.duedderuesch.placed.utils.Utils;

import java.util.ArrayList;

public class SensorListAdapter extends ArrayAdapter<SensorData> {

    ProgressBar pb_value;
    ProgressBar pb_min;
    ProgressBar pb_max;
    TextView tv_value;
    int min_hum;
    int max_hum;
    int min_temp;
    int max_temp;
    public SensorListAdapter(Context context, ArrayList<SensorData> sensorDataList){
        super(context,0, sensorDataList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_plant_sensor_value,parent,false);
        }

        SensorData sensorData = getItem(position);

        SensorDeviceDBHelper mSDeHelper = new SensorDeviceDBHelper(getContext());
        PlantDBHelper plantDBHelper = new PlantDBHelper(getContext());

        String sensorId = sensorData.getSensor();
        SensorDevice sensor = mSDeHelper.getSensorDevicesBy(SensorDeviceDBHelper.COLUMN_ID,sensorId).get(0);
        Integer plantId = Integer.valueOf(sensor.getPlant());
        Plant plant =  plantDBHelper.getPlantObjectBy(PlantDBHelper.COLUMN_ID, plantId.toString()).get(0);

        if(plant.getMin_humidity() != null){
            min_hum = plant.getMin_humidity();
            max_hum = plant.getMax_humidity();
            min_temp = plant.getMin_temperature();
            max_temp = plant.getMax_temperature();
        } else {
            min_hum = 30;
            max_hum = 80;
            min_temp = 8;
            max_temp= 27;
        }

        TextView tv_desc            = convertView.findViewById(R.id.tv_litem_plant_desc);
        TextView tv_timestamp       = convertView.findViewById(R.id.tv_litem_plant_timestamp);
        TextView tv_value_min       = convertView.findViewById(R.id.tv_litem_plant_value_min);
        TextView tv_value_value     = convertView.findViewById(R.id.tv_litem_plant_value_current);
        TextView tv_value_max       = convertView.findViewById(R.id.tv_litem_plant_value_max);
        TextView tv_desc_timestamp  = convertView.findViewById(R.id.tv_litem_plant_desc_timestamp);
        TextView tv_desc_min        = convertView.findViewById(R.id.tv_litem_plant_desc_min);
        TextView tv_desc_value      = convertView.findViewById(R.id.tv_litem_plant_desc_current);
        TextView tv_desc_max        = convertView.findViewById(R.id.tv_litem_plant_desc_max);
        TextView tv_sensorId        = convertView.findViewById(R.id.tv_litem_plant_sensorid);

        tv_value    = convertView.findViewById(R.id.tv_litem_plant_value);
        pb_value    = convertView.findViewById(R.id.pb_litem_plant_value);
        pb_min      = convertView.findViewById(R.id.pb_litem_plant_min);
        pb_max      = convertView.findViewById(R.id.pb_litem_plant_max);

        tv_timestamp.setVisibility(View.GONE);
        tv_desc_timestamp.setVisibility(View.GONE);
        tv_desc_min.setVisibility(View.GONE);
        tv_desc_value.setVisibility(View.GONE);
        tv_desc_max.setVisibility(View.GONE);
        tv_value_min.setVisibility(View.GONE);
        tv_value_value.setVisibility(View.GONE);
        tv_value_max.setVisibility(View.GONE);

        String value = sensorData.getValue();
        String hour = sensorData.getHour().toString();
        String minute = sensorData.getMinute().toString();

        if(hour.length() == 1) hour = "0" + hour;
        if(minute.length() == 1) minute = "0" + minute;

        int intValue;
        if(value.length()>5) {
            value = value.substring(0,5);
        } else if(value.equals("None")) value = "1";
        String[] valueParts = value.split("\\.");
        if(valueParts.length > 1)intValue= Integer.parseInt(valueParts[0]);
        else intValue= Integer.parseInt(value);



        tv_value_value.setText(valueParts[0]);
        tv_sensorId.setText(sensorId);

        String today = Utils.dateFormat_OnlyDate.format(LoginActivity.today);
        if(today.equals(sensorData.getDay() + "."+sensorData.getMonth() + "."+sensorData.getYear())){
            tv_timestamp.setText(hour + ":" + minute);
        } else{
            tv_timestamp.setText(sensorData.getDay() + "."+sensorData.getMonth() + ". "+
                    hour + ":" + minute);
        }

        String desc;
        pb_min.setVisibility(View.VISIBLE);
        pb_max.setVisibility(View.VISIBLE);
        if (sensor.getSensor_type().equals(SensorDevice.SensorType.soil_humidity)){
            desc="Bodenfeuchtigkeit:";
            setMinMax(0,100);
            pb_value.setRotation(0);
            pb_min.setRotation(0);
            pb_max.setRotation(180);
            pb_value.setProgress(intValue, true);
            pb_min.setProgress(min_hum);
            pb_max.setProgress(pb_value.getMax()-max_hum);
            tv_value_min.setText(String.valueOf(min_hum));
            tv_value_max.setText(String.valueOf(max_hum));
            tv_value.setText(value + "%");
        } else if (sensor.getSensor_type().equals(SensorDevice.SensorType.temperature)){
            desc = "Temperatur: ";
            tv_value_min.setText(String.valueOf(min_temp));
            tv_value_max.setText(String.valueOf(max_temp));
            tv_value.setText(value + "°C");
            if(intValue>0){
                setMinMax(0,40);
                pb_value.setRotation(0);
                pb_min.setRotation(0);
                pb_max.setRotation(180);
                pb_value.setProgress(intValue, true);
                pb_min.setProgress(min_temp);
                pb_max.setProgress(pb_value.getMax()-max_temp);
            } else {
                setMinMax(0,30);
                pb_value.setRotation(180);
                pb_min.setRotation(180);
                pb_max.setRotation(0);
                pb_value.setProgress(-intValue, true);
                pb_min.setProgress(min_temp);
                pb_max.setProgress(pb_value.getMax()-max_temp);
            }

        } else {
            desc="unbekannt";
            pb_value.setMin(0);
            pb_value.setMax(100);
            pb_min.setVisibility(View.GONE);
            pb_max.setVisibility(View.GONE);
            tv_value.setText(value);
        }
        tv_desc.setText(desc);

        int nightModeFlags =
                getContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                tv_desc.setTextColor(ResourcesCompat.getColor(convertView.getResources(), R.color.descriptiveTextColorDarkMode, null));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                tv_desc.setTextColor(ResourcesCompat.getColor(convertView.getResources(), R.color.descriptiveTextColor, null));
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }


        setColors();
        return convertView;
    }

    private void setMinMax(int value_min, int value_max){
        pb_value.setMin(value_min);
        pb_value.setMax(value_max);
        pb_min.setMin(value_min);
        pb_min.setMax(value_max);
        pb_max.setMin(value_min);
        pb_max.setMax(value_max);

    }

    public void setColors(){
        LayerDrawable layers_min = (LayerDrawable) pb_min.getProgressDrawable();
        Drawable backTint_min = layers_min.getDrawable(0);
        LayerDrawable layers_max = (LayerDrawable) pb_max.getProgressDrawable();
        Drawable backTint_max = layers_max.getDrawable(0);

        //value and min
        if(pb_value.getProgress() > pb_min.getProgress()){
            DrawableCompat.setTint(pb_min.getProgressDrawable(), getContext().getColor(R.color.placedIntenseRedColor));
            DrawableCompat.setTint(backTint_min, getContext().getColor(R.color.progressBarBackgroundTint));
        } else if (pb_value.getProgress() <= pb_min.getProgress()){
            DrawableCompat.setTint(pb_min.getProgressDrawable(), getContext().getColor(R.color.progressBarMaxColor));
            DrawableCompat.setTint(backTint_min, getContext().getColor(R.color.progressBarBackgroundTint));

        }
        //value and max
        if(pb_value.getProgress() < (pb_value.getMax()-pb_max.getProgress())){
            DrawableCompat.setTint(pb_max.getProgressDrawable(), getContext().getColor(R.color.placedIntenseRedColor));
            DrawableCompat.setTint(backTint_max, getContext().getColor(R.color.progressBarBackgroundTint));
        } else if (pb_value.getProgress() >= (pb_value.getMax()-pb_max.getProgress())){
            DrawableCompat.setTint(pb_max.getProgressDrawable(), getContext().getColor(R.color.progressBarMaxColor));
            DrawableCompat.setTint(backTint_max, getContext().getColor(R.color.progressBarBackgroundTint));
        }

        if(pb_min.getProgress() < pb_value.getProgress() && pb_value.getProgress() < (pb_value.getMax()-pb_max.getProgress())){
            tv_value.setTextColor(getContext().getColor(R.color.primaryColorDark));
        } else {
            tv_value.setTextColor(getContext().getColor(R.color.placedIntenseRedColor));
        }

    }

    @Override
    public void clear() {
        super.clear();
    }
}
