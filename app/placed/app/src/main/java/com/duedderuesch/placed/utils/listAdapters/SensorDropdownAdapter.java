package com.duedderuesch.placed.utils.listAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.duedderuesch.placed.R;

import java.util.ArrayList;

public class SensorDropdownAdapter extends ArrayAdapter<String> {

    private final Context mContext;
    private ArrayList<String> sensorList;

    public SensorDropdownAdapter(Context context, ArrayList<String> sensorList) {
        super(context,0, sensorList);
        mContext = context;
        this.sensorList = sensorList;
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {

        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item_dropdw_sensor,parent,false);

        TextView tv_name = (TextView) listItem.findViewById(R.id.tv_downlitem_sensor_name);
        TextView tv_id = listItem.findViewById(R.id.tv_downlitem_sensor_id);

        tv_name.setText(sensorList.get(position));
        //tvLanguage.setTextColor(Color.rgb(75, 180, 225));

        //tv_id.setText();
        ImageView iv_ic = (ImageView) listItem.findViewById(R.id.iv_downlitem_sensor_icon);

        if(sensorList.get(position).toLowerCase().contains("moist")){
            iv_ic.setImageResource(R.drawable.ic_water);
        } else if(sensorList.get(position).toLowerCase().contains("temp")){
            iv_ic.setImageResource(R.drawable.ic_thermostate);
        } else {
            iv_ic.setImageResource(R.drawable.ic_sensor);
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
}