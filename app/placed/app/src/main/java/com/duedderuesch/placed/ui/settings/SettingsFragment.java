package com.duedderuesch.placed.ui.settings;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.duedderuesch.placed.R;
import com.duedderuesch.placed.data.dbhelper.SensorDataDBHelper;
import com.duedderuesch.placed.data.entities.SensorData;
import com.duedderuesch.placed.ui.SettingsActivity;
import com.duedderuesch.placed.utils.listAdapters.SettingsListAdapter;

import java.util.ArrayList;

import static com.duedderuesch.placed.R.drawable.icon_avatar_1;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;

    public static class SettingsHeader{
        public static String Settings = "Settings";
        public static String Information = "Information";
        public static String Firebase = "Firebase Console";
        public static String Version = "Version COntrol";
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        Button bt_delete_LSD = root.findViewById(R.id.bt_setting_deleteLocalSensorData);
        bt_delete_LSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SensorDataDBHelper mLSDHelper = new SensorDataDBHelper(getContext());
                mLSDHelper.getWritableDatabase();
                ArrayList<SensorData> queryList = mLSDHelper.getAllSensorData();
                for(int i = 0;i<queryList.size(); i++) {
                    mLSDHelper.deleteRowByID(String.valueOf(queryList.get(i).getId()));
                }
            }
        });

        ArrayList<String> titleList = new ArrayList<>();
        titleList.add(SettingsHeader.Settings);
        titleList.add(SettingsHeader.Information);
        titleList.add(SettingsHeader.Firebase);
        titleList.add(SettingsHeader.Version);

        ListView lv = root.findViewById(R.id.lv_settings_settings);
        SettingsListAdapter lv_adap = new SettingsListAdapter(getContext(), titleList);
        lv.setAdapter(lv_adap);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView title = view.findViewById(R.id.tv_litem_settings_title);
                if(title.getText().equals("Settings")){
                    Intent i = new Intent(getContext(), SettingsActivity.class);
                    startActivity(i);
                }
            }
        });

        ImageView avatarView = root.findViewById(R.id.iv_settings_avatar);
        Drawable drawable = ContextCompat.getDrawable(getContext(), icon_avatar_1);
        avatarView.setImageDrawable(drawable);

        try {
            PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            TextView tv_version = root.findViewById(R.id.tv_settings_version);
            tv_version.setText("placed: "+ verCode + "  -  "+ version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return root;
    }
}