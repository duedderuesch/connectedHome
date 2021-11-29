package com.duedderuesch.placed.utils.listAdapters;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.duedderuesch.placed.R;
import com.duedderuesch.placed.data.dbhelper.PlantDBHelper;
import com.duedderuesch.placed.data.dbhelper.SensorDataDBHelper;
import com.duedderuesch.placed.data.dbhelper.SensorDeviceDBHelper;
import com.duedderuesch.placed.data.entities.Plant;
import com.duedderuesch.placed.data.entities.SensorData;
import com.duedderuesch.placed.data.entities.SensorDevice;
import com.duedderuesch.placed.utils.Utils;
import com.duedderuesch.placed.utils.views.PlantIconBorder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PlantListAdapter extends ArrayAdapter<Plant> {

    private String TAG = "homePlantsAdapterTry";

    public PlantListAdapter(Context context, ArrayList<Plant> plantList){
        super(context,0, plantList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        SensorDeviceDBHelper mSDeHelper = new SensorDeviceDBHelper(getContext());
        PlantDBHelper mPOHelper = new PlantDBHelper(getContext());
        SensorDataDBHelper mSDaHelper = new SensorDataDBHelper(getContext());
        mSDeHelper.getReadableDatabase();
        mPOHelper.getReadableDatabase();
        mSDaHelper.getReadableDatabase();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_home_plant, parent, false);
        }

        Plant plant = getItem(position);

        TextView tv_plantId = convertView.findViewById(R.id.tv_litem_home_sensor_plantid);
        TextView tv_name = convertView.findViewById(R.id.tv_litem_home_plant_name);
        TextView tv_bioname = convertView.findViewById(R.id.tv_litem_home_plant_biologicalname);
        TextView tv_timestamp = convertView.findViewById(R.id.tv_litem_home_plant_timestamp);
        ImageView iv_plant_icon = convertView.findViewById(R.id.iv_litem_home_plant_icon);
        PlantIconBorder pib = convertView.findViewById(R.id.pib_litem_homee_plant);



        String timestamp;
        String lastDataFromPlant =
                mPOHelper.mostRecentDataTimestamp(plant.getId().toString(), getContext());
        if(lastDataFromPlant.substring(0,10).equals(Utils.today())){
            timestamp = lastDataFromPlant.substring(11);
            if(Integer.parseInt(lastDataFromPlant.substring(11,13)) >=
                    (Integer.parseInt(Utils.time().substring(0,2))) - 3){
                pib.setPlantActiveRecent(true);
            } else pib.setPlantActiveRecent(false);
        }
        else{
            timestamp = lastDataFromPlant;
            pib.setPlantActiveRecent(false);
        }




        tv_name.setText(plant.getName());
        tv_bioname.setText(plant.getBiological_name());
        tv_timestamp.setText(timestamp);
        tv_plantId.setText(String.valueOf(plant.getId()));

        if(plant.getId()==123456789){
            tv_bioname.setVisibility(View.GONE);
            tv_timestamp.setVisibility(View.GONE);
            iv_plant_icon.setVisibility(View.INVISIBLE);
        }

        int nightModeFlags =
                getContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                convertView.setBackgroundColor(ResourcesCompat.getColor(convertView.getResources(), R.color.secondaryBackgroundDarkMode, null));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                convertView.setBackgroundColor(ResourcesCompat.getColor(convertView.getResources(), R.color.white, null));
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }



        return convertView;
    }
}
