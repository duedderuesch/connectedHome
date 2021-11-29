package com.duedderuesch.placed.ui.edit;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.duedderuesch.placed.R;
import com.duedderuesch.placed.data.dbhelper.PlantDBHelper;
import com.duedderuesch.placed.data.entities.Plant;
import com.duedderuesch.placed.ui.login.LoginActivity;
import com.duedderuesch.placed.utils.localDB.update;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EditPlantActivity extends AppCompatActivity {

    private String TAG = "EditPlantActivityTest";

    private static Plant plant;
    private Boolean changed;
    private PlantDBHelper plantDBHelper;
    private Menu menu;
    FirebaseDatabase fb_db;

    private EditText et_name;
    private EditText et_alivesince;
    private EditText et_note;
    private EditText et_id;
    private String pt_id;

    private AutoCompleteTextView et_pt_name;
    private EditText et_pt_maxhum;
    private EditText et_pt_minhum;
    private EditText et_pt_maxtemp;
    private EditText et_pt_mintemp;

    private Switch sw_saveToFB;

    private ProgressBar pb_loading;


    public static void setPlantObject(Plant po) {
        EditPlantActivity.plant = po;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plant);

        plantDBHelper = new PlantDBHelper(getApplicationContext());

        ConstraintLayout cl_planttype_header =  findViewById(R.id.cl_edplant_planttype_header);
        ConstraintLayout cl_planttype_content = findViewById(R.id.cl_edplant_planttype_content);

        et_name = findViewById(R.id.et_edplant_plant_name);
        et_alivesince = findViewById(R.id.et_edplant_plant_alivesince);
        et_note = findViewById(R.id.et_edplant_plant_note);
        et_id = findViewById(R.id.et_edplant_plant_id);

        et_pt_name = findViewById(R.id.et_edplant_planttype_name);
        et_pt_maxhum = findViewById(R.id.et_edplant_planttype_maxhum);
        et_pt_minhum = findViewById(R.id.et_edplant_planttype_minhum);
        et_pt_maxtemp = findViewById(R.id.et_edplant_planttype_maxtemp);
        et_pt_mintemp = findViewById(R.id.et_edplant_planttype_mintemp);

        sw_saveToFB = findViewById(R.id.sw_edplant_savetofb);

        pb_loading = findViewById(R.id.pb_edplant_loading);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        fb_db = FirebaseDatabase.getInstance(prefs.getString("et_pref_server_fbdbadress", "https://placed-5b875-default-rtdb.europe-west1.firebasedatabase.app/"));


        if(!LoginActivity.local_db_status.equals(LoginActivity.dbStatus.ONLINE) || !LoginActivity.firebase_db_status.equals(LoginActivity.dbStatus.ONLINE)) {
            Toast.makeText(getApplicationContext(), "Server not reachable, no Changes can be made!!!", Toast.LENGTH_LONG).show();
            pb_loading.setIndeterminate(false);
            pb_loading.setProgress(100);
            DrawableCompat.setTint(pb_loading.getProgressDrawable(), getApplicationContext().getColor(R.color.placedIntenseRedColor));
            findViewById(R.id.tv_edplant_servererror).setVisibility(View.VISIBLE);
            et_name.        setEnabled(false);
            et_alivesince.  setEnabled(false);
            et_note.        setEnabled(false);
            et_id.          setEnabled(false);
            et_pt_name.     setEnabled(false);
            et_pt_maxhum.   setEnabled(false);
            et_pt_minhum.   setEnabled(false);
            et_pt_maxtemp.  setEnabled(false);
            et_pt_mintemp.  setEnabled(false);
        } else {
            DatabaseReference fbRef = fb_db.getReference("/plant_objects");
            fbRef.child(plant.getId().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        sw_saveToFB.setChecked(true);
                        pb_loading.setIndeterminate(false);
                        pb_loading.setProgress(0);
                    }
                }
            });
        }

        changed = false;
//        menuItem_save = findViewById(R.id.menuitem_editplant_save);
//        menuItem_save.setVisible(false);


        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if(changed) {
                    confirmSaveChanges();
                }

            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);


        cl_planttype_content.setVisibility(View.GONE);
        cl_planttype_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cl_planttype_content.getVisibility() == View.GONE){
                    cl_planttype_content.setVisibility(View.VISIBLE);
                } else {
                    cl_planttype_content.setVisibility(View.GONE);
                }
            }
        });

        et_pt_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO
            }
        });
        et_pt_name.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                //TODO
            }
        });
        loadDataFromPlantObject();

        ImageView ic_cloudstate_name = findViewById(R.id.iv_edplant_icname);
        ImageView ic_cloudstate_pt_name = findViewById(R.id.iv_edplant_planttype_icname);
        ImageView ic_cloudState_aliveSince = findViewById(R.id.iv_edplant_icalivesince);
        ImageView ic_cloudstate_note = findViewById(R.id.iv_edplant_icnote);
        ImageView ic_cloudstate_id = findViewById(R.id.iv_edplant_icid);

        et_name.addTextChangedListener(textEditWatcher(ic_cloudstate_name));
        et_pt_name.addTextChangedListener(textEditWatcher(ic_cloudstate_pt_name));
        et_alivesince.addTextChangedListener(textEditWatcher(ic_cloudState_aliveSince));
        et_note.addTextChangedListener(textEditWatcher(ic_cloudstate_note));
        et_id.addTextChangedListener(textEditWatcher(ic_cloudstate_id));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editplant,menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuitem_editplant_save) {
            saveChanges();
        }
        return super.onOptionsItemSelected(item);
    }

    private TextWatcher textEditWatcher(ImageView iv){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setIcEdit(iv);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        };
    }

    private void confirmSaveChanges(){
        AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
        alertDialog.setTitle("Save?");
        alertDialog.setMessage("You´ve made some changes. Don´t you want to save them?");
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
////                        onBackPressed();
//
//                    }
//                });
//        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Discard", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
////                onBackPressed();
//            }
//        });
        alertDialog.show();
    }

    private void loadDataFromPlantObject() {
        et_name.setText(plant.getName());
        et_note.setText(plant.getNote());
        et_alivesince.setText(plant.getBirthdate());
        et_id.setText(plant.getId());
        et_pt_maxhum.setText(plant.getMax_humidity());
        et_pt_minhum.setText(plant.getMin_humidity());
        et_pt_maxtemp.setText(plant.getMax_temperature());
        et_pt_mintemp.setText(plant.getMin_temperature());
    }

    private void clearAllTypeFields(){
        et_pt_maxhum.setText(null);
        et_pt_minhum.setText(null);
        et_pt_maxtemp.setText(null);
        et_pt_mintemp.setText(null);
    }

    public void setIcEdit(ImageView iv){
        iv.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_edit_yellow, getTheme()));
        Log.d(TAG, "changed = " + changed);
        if(!changed) somethingChanged();
        changed = true;
    }
    public void setIcSaved(ImageView iv){
        iv.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_cloud_done, getTheme()));
    }

    public void somethingChanged(){
        Log.d(TAG, "something changed" );
        menu.findItem(R.id.menuitem_editplant_save).setVisible(true);
    }

    public boolean saveChanges(){
        ArrayList<String> argList = new ArrayList<>();
        ArrayList<String> valList = new ArrayList<>();
        argList.add("id");
        argList.add("plant_object_name");
        argList.add("type");
        argList.add("alive_since");
        valList.add(et_id.getText().toString());
        valList.add(et_name.getText().toString());
        valList.add(et_pt_name.getText().toString());
        valList.add(et_alivesince.getText().toString());
        new update("placed", "plant_object", argList, valList, plant.getId().toString());

        if(sw_saveToFB.isChecked()){
            DatabaseReference fbRef = fb_db.getReference("/plant_objects");
            fbRef.child(plant.getId().toString()).child("plant_object_name").setValue(et_name.getText().toString());
            fbRef.child(plant.getId().toString()).child("type").setValue(et_pt_name.getText().toString());
            fbRef.child(plant.getId().toString()).child("alive_since").setValue(et_alivesince.getText().toString());
        }
        return true;
    }
}