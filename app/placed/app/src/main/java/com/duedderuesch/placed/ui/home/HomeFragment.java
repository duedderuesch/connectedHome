package com.duedderuesch.placed.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.duedderuesch.placed.data.dbhelper.HomeDBHelper;
import com.duedderuesch.placed.data.dbhelper.SensorDataDBHelper;
import com.duedderuesch.placed.data.dbhelper.SensorDeviceDBHelper;
import com.duedderuesch.placed.data.entities.Home;
import com.duedderuesch.placed.data.entities.Plant;
import com.duedderuesch.placed.data.entities.Result;
import com.duedderuesch.placed.ui.PlantActivity;
import com.duedderuesch.placed.R;
import com.duedderuesch.placed.data.dbhelper.PlantDBHelper;
import com.duedderuesch.placed.ui.login.LoginActivity;
import com.duedderuesch.placed.utils.Utils;
import com.duedderuesch.placed.utils.listAdapters.HomeListAdapter;
import com.duedderuesch.placed.utils.listAdapters.PlantListAdapter;
import com.duedderuesch.placed.utils.localDB.get;
import com.duedderuesch.placed.utils.localDB.utils;

import java.util.ArrayList;

public class HomeFragment extends Fragment{
    private static String TAG = "homeFragment";

    private HomeViewModel homeViewModel;
    private ListView lv_sensors;
    private ListView lv_homes;
    private ImageButton bt_sort_list;
    private RadioGroup rg_sort_list;
    private RadioButton rbt_sort_list_alpha;
    private RadioButton rbt_sort_list_recent;
    private HomeDBHelper        homeDBHelper ;
    private PlantDBHelper       plantDBHelper;
    private SensorDataDBHelper  dataDBHelper ;
    private TextView tv_sortheader_home;


    interface callback<T>{
        void onComplete(Result<T> result);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ConstraintLayout cl_home_sortheader = root.findViewById(R.id.cl_home_sortheader);

        lv_homes = root.findViewById(R.id.lv_home_homes);
        lv_sensors = root.findViewById(R.id.lv_home_sensors);
        bt_sort_list = root.findViewById(R.id.bt_home_sortsensorlist);
        rg_sort_list = root.findViewById(R.id.rg_home_sort);
        rbt_sort_list_alpha = root.findViewById(R.id.rbt_home_sort_alpha);
        rbt_sort_list_recent = root.findViewById(R.id.rbt_home_sort_recent);
        tv_sortheader_home = root.findViewById(R.id.tv_home_sortheader_home);

        homeDBHelper    = new HomeDBHelper(getContext());
        plantDBHelper   = new PlantDBHelper(getContext());
        dataDBHelper    = new SensorDataDBHelper(getContext());

        bt_sort_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rg_sort_list.getVisibility() == View.VISIBLE){
                    rg_sort_list.setVisibility(View.GONE);
                } else rg_sort_list.setVisibility(View.VISIBLE);
            }
        });

        rbt_sort_list_alpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortList();
                rg_sort_list.setVisibility(View.GONE);
            }
        });

        rbt_sort_list_recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortList();
                rg_sort_list.setVisibility(View.GONE);
            }
        });

        lv_sensors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_title = view.findViewById(R.id.tv_litem_home_sensor_plantid);
                if (plantDBHelper.getPlantObjectBy(plantDBHelper.COLUMN_ID, tv_title.getText().toString()).size() == 1) {
                    PlantActivity.plant = plantDBHelper.getPlantObjectBy(plantDBHelper.COLUMN_ID, tv_title.getText().toString()).get(0);
                    Intent i = new Intent(getContext(), PlantActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getContext(), "too man or too little elements: " + plantDBHelper.getPlantObjectBy(plantDBHelper.COLUMN_ID, tv_title.getText().toString()).size(), Toast.LENGTH_LONG).show();
                }
            }
        });

        lv_homes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_home_name = view.findViewById(R.id.tv_litem_start_home_name);
                tv_sortheader_home.setText(tv_home_name.getText() + ":");
            }
        });

        int nightModeFlags =
                getContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
//                cl_home_sortheader.setBackgroundColor(ResourcesCompat.getColor(root.getResources(), R.color.primaryColorDark, null));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
//                cl_home_sortheader.setBackgroundColor(ResourcesCompat.getColor(root.getResources(), R.color.primaryColorLight, null));
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                //pass
                break;
        }

        return root;
    }

    private void sortList(){
        boolean sortAlphabetically = false;
        if(rbt_sort_list_alpha.isChecked()) sortAlphabetically = true;

        //TODO: create Sorting Algorithm
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inf) {
        inf.inflate(R.menu.home_fragment_menu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();

        final Context ctx = getContext();

        if(LoginActivity.local_db_status.equals(LoginActivity.dbStatus.ONLINE)) {
            Log.d(TAG, "#online");
            get.fetchData(SensorDataDBHelper.TABLE_NAME, 0, null, null, "30", new utils.DataGetterCallback<String>() {
                @Override
                public void onComplete(Result<String> result) {
                    Log.d(TAG, "resumed");
                    updateUi();
                }
            }, ctx);
        } else updateUi();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuitem_editplant_save) {
            Intent i = new Intent(getContext(), HomeAddActivity.class);
            startActivity(i);
        } else if (id == R.id.menuitem_home_plant) {
            Intent i = new Intent(getContext(), PlantActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUi(){
        ArrayList<Home> homeList = homeDBHelper.getAllHomes();
        if(homeList.size()>0) {
            //setting up Home-List:
            ArrayList<String> homeNameList = new ArrayList<>();
            for(int i=0;i<homeList.size();i++){
                homeNameList.add(homeList.get(i).getName());
            }
            HomeListAdapter homeListAdapter =
                    new HomeListAdapter(getContext(),
                            homeNameList,
                            dataDBHelper.getNewestSensorDataForEverySensor(getContext()));
            lv_homes.setAdapter(homeListAdapter);
            lv_homes.getLayoutParams().height = Utils.getItemHeightofListView(lv_homes,0);

            //setting up Plant-List:
            ArrayList<Plant> plantList = plantDBHelper.getAllPlantObjects();
            for(int i=0;i<plantList.size();i++){
                if(plantList.get(i).getName().toLowerCase().contains("generic")){
                    plantList.remove(i);
                }
            }
            if(plantList.size() == 0){ //TODO: rework
                Plant emptyListPlant = new Plant(123456789,
                        "keine Daten vorhanden",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
                plantList.add(emptyListPlant);
            }
            PlantListAdapter plantListAdapter = new PlantListAdapter(getContext(), plantList);
            lv_sensors.setAdapter(plantListAdapter);

            //finished List-SetUps

            rg_sort_list.setVisibility(View.GONE); //TODO: sensor-sort
            rbt_sort_list_recent.setChecked(true);
            if(homeList.size() ==1 )tv_sortheader_home.setText(homeList.get(0).getName() + ":");
        } else {
            firstRun();
        }
    }

    private void firstRun(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("oopsie - new here?");
        builder.setMessage("It looks like, there is no server. Connect one?");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        input.setHint("ip address of local server");
        builder.setView(input);

        builder.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                m_Text = input.getText().toString();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor prefEditor = prefs.edit();
                prefEditor.putString("et_pref_server_localip", input.getText().toString());
                prefEditor.commit();
                LoadingDialog();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    public void LoadingDialog(){
        Context ctx = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("PLease Wait");
        ProgressBar loadingBar = new ProgressBar(getContext());
        loadingBar.setIndeterminate(true);
        builder.setView(loadingBar);
        builder.setCancelable(false);
        builder.show();

        get.fetchData(SensorDataDBHelper.TABLE_NAME, 0, null, null, "1", new utils.DataGetterCallback<String>() {
            @Override
            public void onComplete(Result<String> result) {
                if(result instanceof Result.Success){
                    Log.d(TAG, "right");
                    ContextCompat.getMainExecutor(ctx).execute(() -> {
                        Intent i = new Intent(getContext(), LoginActivity.class);
                        startActivity(i);
                    });
                } else {
                    Log.d(TAG, "wrong");
                    ContextCompat.getMainExecutor(ctx).execute(() -> {
                        firstRun();
                    });
                }
            }
        }, getContext());
    }
}