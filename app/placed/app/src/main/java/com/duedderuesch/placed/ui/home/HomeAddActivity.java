package com.duedderuesch.placed.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.duedderuesch.placed.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.duedderuesch.placed.R;

public class HomeAddActivity extends AppCompatActivity {

    EditText home_name;
    EditText home_adress;
    TextView title_wifi;
    EditText home_wifiSSID;
    TextView title_ip;
    TextView home_serverIPstart;
    EditText home_serverIPend;

    String wifi_ssid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        home_name = findViewById(R.id.ti_HomeAdd_name);
        home_adress = findViewById(R.id.ti_HomeAdd_adress);
        title_wifi = findViewById(R.id.tv_HomeAdd_titleWifi);
        home_wifiSSID = findViewById(R.id.ti_HomeAdd_wifissid);
        title_ip = findViewById(R.id.tv_HomeAdd_titleIP);
        home_serverIPstart = findViewById(R.id.tv_HomeAdd_IPstart);
        home_serverIPend = findViewById(R.id.ti_HomeAdd_IPend);

        home_wifiSSID.setKeyListener(null);
        home_serverIPstart.setKeyListener(null);


        home_adress.setVisibility(View.GONE);
        title_wifi.setVisibility(View.GONE);
        home_wifiSSID.setVisibility(View.GONE);
        title_ip.setVisibility(View.GONE);
        home_serverIPstart.setVisibility(View.GONE);
        home_serverIPend.setVisibility(View.GONE);


        home_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                home_adress.setVisibility(View.VISIBLE);
                title_wifi.setVisibility(View.VISIBLE);
                home_wifiSSID.setVisibility(View.VISIBLE);
                title_ip.setVisibility(View.VISIBLE);
                home_serverIPstart.setVisibility(View.VISIBLE);
                home_serverIPend.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
           this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}