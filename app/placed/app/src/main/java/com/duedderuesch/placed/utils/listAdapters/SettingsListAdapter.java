package com.duedderuesch.placed.utils.listAdapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.duedderuesch.placed.ui.settings.SettingsFragment;

import java.util.ArrayList;

import static com.duedderuesch.placed.R.*;
import static com.duedderuesch.placed.R.drawable.ic_cloud;
import static com.duedderuesch.placed.R.drawable.ic_settings_black_24;
import static com.duedderuesch.placed.R.drawable.ic_settings_info;
import static com.duedderuesch.placed.R.drawable.ic_settings_versioninfo;

public class SettingsListAdapter extends ArrayAdapter<String> {

    private String TAG = "settingsListAdapter";

    public SettingsListAdapter(Context context, ArrayList<String> titleList){
        super(context,0, titleList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layout.list_item_settings, parent, false);
        }

        String title = getItem(position);

        TextView tv_title = convertView.findViewById(id.tv_litem_settings_title);
        ImageView icon = convertView.findViewById(id.iv_litem_settings_icon);


        tv_title.setText(title);
        if(title.equals(SettingsFragment.SettingsHeader.Settings)){
            Drawable drawable = ContextCompat.getDrawable(getContext(), ic_settings_black_24);
            icon.setImageDrawable(drawable);
        } else if (title.equals(SettingsFragment.SettingsHeader.Information)){
            Drawable drawable = ContextCompat.getDrawable(getContext(), ic_settings_info);
            icon.setImageDrawable(drawable);
        } else if (title.equals(SettingsFragment.SettingsHeader.Firebase)){
            Drawable drawable = ContextCompat.getDrawable(getContext(), ic_cloud);
            icon.setImageDrawable(drawable);
        } else if (title.equals(SettingsFragment.SettingsHeader.Version)){
            Drawable drawable = ContextCompat.getDrawable(getContext(), ic_settings_versioninfo);
            icon.setImageDrawable(drawable);
        }

        return convertView;
    }
}
