package com.duedderuesch.placed.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import com.androidplot.Plot;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PanZoom;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.PointLabeler;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.YValueMarker;
import com.duedderuesch.placed.data.dbhelper.SensorDataDBHelper;
import com.duedderuesch.placed.data.dbhelper.SensorDeviceDBHelper;
import com.duedderuesch.placed.data.entities.GraphDataSeries;
import com.duedderuesch.placed.data.entities.SensorData;
import com.duedderuesch.placed.data.entities.SensorDevice;
import com.duedderuesch.placed.utils.Utils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.duedderuesch.placed.R;
import com.duedderuesch.placed.utils.graphHelpers.dayMarkerCreator;
import com.duedderuesch.placed.utils.listAdapters.SensorDropdownAdapter;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;

public class SensorGraphActivity extends AppCompatActivity {

    private static String SENSOR_DATA_MAP_KEY_INDEX = "data_key";
    private static String SENSOR_DATA_MAP_KEY_AVGVALUE = "key_avg_value";
    private static String SENSOR_DATA_MAP_KEY_MINVALUE = "key_min_value";
    private static String SENSOR_DATA_MAP_KEY_MAXVALUE = "key_max_value";

//    private static String TAG = "SensorGraphActivity";
//    private Integer currentlyActiveSensorNr = 0;
//    private String currentDateRange = GraphDataSeries.DaterangeType.DATERANGE_14_D;
//    private String currentSensorType = SensorDevice.SENSOR_TYPE_SOIL_HUMIDITY;
//    private GraphDataSeries currentSeries = null;
//
//    private XYPlot plot;
//    private SensorDataDBHelper mLSDHelper;
//    private SensorDeviceDBHelper mSDeHelper;
//    private ArrayList<SensorDevice> allSensors;
//    private ArrayList<String> Dateranges;
//    private Calendar calendar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sensor);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        mLSDHelper = new SensorDataDBHelper(getApplicationContext());
//        mLSDHelper.getReadableDatabase();
//        mSDeHelper = new SensorDeviceDBHelper(getApplicationContext());
//        mSDeHelper.getReadableDatabase();
//
//        plot = (XYPlot) findViewById(R.id.plot);
//        plot.setRenderMode(Plot.RenderMode.USE_MAIN_THREAD);
//        plot.redraw();
//
//        Dateranges = GraphDataSeries.DaterangeType.getDaterangePossibilities();
//
//        final int[] checkedItemTrangeDialog = {-1};
//        ImageButton bt_timerange = findViewById(R.id.bt_graph_trange);
//        bt_timerange.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SensorGraphActivity.this);
//                alertDialog.setIcon(R.drawable.ic_time);
//
//                alertDialog.setTitle("Choose an Item");
//
//                final String[] listItems = Dateranges.toArray(new String[Dateranges.size()]);
//
//                alertDialog.setSingleChoiceItems(listItems, checkedItemTrangeDialog[0], new DialogInterface.OnClickListener() {
//                    @SuppressLint("SetTextI18n")
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        checkedItemTrangeDialog[0] = which;
//                        String daterange = Dateranges.get(which);
//                        //if (!currentDateRange.equals(daterange)) { //TODO
//                           populateGraph(allSensors.get(currentlyActiveSensorNr).getId(), currentlyActiveSensorNr, currentSensorType, daterange);
//                        //}
//                        dialog.dismiss();
//                    }
//                });
//
//                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                AlertDialog customAlertDialog = alertDialog.create();
//                customAlertDialog.show();
//            }
//        });
//
//        Spinner sp_sensors = findViewById(R.id.sp_graph_choosesensor);
//        allSensors = new ArrayList<>();
//        allSensors = mSDeHelper.getAllsensor_device();
//        ArrayList<String> allSensorNames = new ArrayList<>();
//        for(int i = 0;i<allSensors.size();i++){
//            allSensorNames.add(allSensors.get(i).getSensor_device_name());
//        }
//
//        currentSeries = null;
//
//        SensorDropdownAdapter adap_sp_sensors = new SensorDropdownAdapter(getApplicationContext(),allSensorNames);
//        sp_sensors.setAdapter(adap_sp_sensors);
//        sp_sensors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String new_sensor = allSensors.get(position).getId();
//                String new_sensor_type = SensorDevice.SENSOR_TYPE_SOIL_HUMIDITY;
//                if(allSensors.get(position).getId().toLowerCase().contains("hum") || allSensors.get(position).getId().toLowerCase().contains("moist")){
//                    new_sensor_type = SensorDevice.SENSOR_TYPE_SOIL_HUMIDITY;
//                } else if (allSensors.get(position).getId().toLowerCase().contains("temp")){
//                    if(allSensors.get(position).getId().toLowerCase().contains("generic")) {
//                        new_sensor_type = SensorDevice.SENSOR_TYPE_TEMPERATURE_OUTISDE;
//                    } else {
//                        new_sensor_type = SensorDevice.SENSOR_TYPE_TEMPERATURE_INSIDE;
//                    }
//                }
//                populateGraph(new_sensor,position,new_sensor_type, currentDateRange);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//
//
//
//    }
//    private void populateGraph(String sensor, int positionInList, String sensor_type, String daterange) {
//        try {
//            calendar = Calendar.getInstance();
//            String now = Utils.dateFormatAsSaved.format(calendar.getTime());
//
//            LineAndPointFormatter standardSeriesFormat = new LineAndPointFormatter(getApplicationContext().getColor(R.color.primaryDarkColor), getApplicationContext().getColor(R.color.primaryDarkColor), getApplicationContext().getColor(R.color.ap_transparent), null);
//            standardSeriesFormat.setInterpolationParams(
//                    new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));
//            LineAndPointFormatter moistSeriesFormat = new LineAndPointFormatter(getApplicationContext().getColor(R.color.primaryDarkColor), getApplicationContext().getColor(R.color.primaryDarkColor), getApplicationContext().getColor(R.color.placedLightBlueColor), null);
//            moistSeriesFormat.setInterpolationParams(
//                    new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));
//
//            PointLabelFormatter plfShowNumbers = new PointLabelFormatter();
//            plfShowNumbers.getTextPaint().setTextSize(PixelUtils.spToPix(12));
//            plfShowNumbers.getTextPaint().setColor(Color.BLACK);
//            plfShowNumbers.vOffset = -30f;
//
//            PointLabelFormatter plfHideNumbers = new PointLabelFormatter();
//            plfHideNumbers.getTextPaint().setColor(Color.TRANSPARENT);
//
//            moistSeriesFormat.setPointLabelFormatter(plfShowNumbers);
//
//            moistSeriesFormat.setPointLabeler(new PointLabeler() {
//                @Override
//                public String getLabel(XYSeries series, int index) {
//                    int indexRangeOnScreen = plot.getBounds().getMaxX().intValue() - plot.getBounds().getMinX().intValue();
//                    if(indexRangeOnScreen<50){
//                        moistSeriesFormat.setPointLabelFormatter(plfShowNumbers);
//                    }else {
//                        moistSeriesFormat.setPointLabelFormatter(plfHideNumbers);
//                    }
//                    if (index > 10 && index < (series.size() - 2)) {
//                        if(series.getY(index-1).intValue() == series.getY(index).intValue()){
//                            return "";
//                        } else if(series.getY(index-1).intValue() == series.getY(index+1).intValue()){
//                            return "";
//                        }
//                    } else if(index < 10){
//                        return "";
//                    }
//
//                    Paint linePaint = new Paint();
//                    linePaint.setAntiAlias(false);
//                    linePaint.setAlpha(5);
//                    linePaint.setColor(Color.BLACK);
////                    XValueMarker PointLabelMarker = new XValueMarker(index,
////                            "",
////                            new VerticalPosition(series.getY(index).floatValue(),
////                                    VerticalPositioning.ABSOLUTE_FROM_BOTTOM),
////                            linePaint,
////                            linePaint);
////                    plot.addMarker(PointLabelMarker);
//                    return "" + series.getY(index).intValue();
//                }
//            });
//
//            if (!(currentSeries == null)) {
//                plot.removeSeries(currentSeries.getXySeries());
//                //    resetXYData();
//            }
//            ArrayList<SensorData> sourceDataList = new ArrayList<>();
//
//            if (daterange.equals(GraphDataSeries.DaterangeType.DATERANGE_1_H)) {
//                sourceDataList = mLSDHelper.getSensorDataLastDay(now, sensor);
//            } else if (daterange.equals(GraphDataSeries.DaterangeType.DATERANGE_14_D)) {
//                sourceDataList = mLSDHelper.getSensorDataForGraph(now, sensor);
//            } else if (daterange.equals(GraphDataSeries.DaterangeType.DATERANGE_1_EVERY)) {
//                sourceDataList = mLSDHelper.getSensorDataLastDay(now, sensor);
//            }
//
//            if(sourceDataList.size() == 0){
//                Toast.makeText(getApplicationContext(),"no Data available",Toast.LENGTH_SHORT).show();
//            }
//
//            GraphDataSeries gds = new GraphDataSeries(sourceDataList, daterange, GraphDataSeries.Type.YDATA_XDATA_XLABEL);
//
//
//            if (sensor_type.equals(SensorDevice.SENSOR_TYPE_SOIL_HUMIDITY)) {
//                plot.setRangeBoundaries(0, 100, BoundaryMode.FIXED);
//                plot.setRangeStep(StepMode.INCREMENT_BY_VAL, 10);
//                plot.addSeries(gds.getXySeries(), moistSeriesFormat);
//                plot.getOuterLimits().set(0, gds.getxDataList().get(gds.getxDataList().size()-1), 0,100);
//                plot.addMarker(new YValueMarker(35,""));
//            } else if (sensor_type.equals(SensorDevice.SENSOR_TYPE_TEMPERATURE_INSIDE)) {
//                plot.setRangeBoundaries(0, 30, BoundaryMode.FIXED);
//                plot.setRangeStep(StepMode.INCREMENT_BY_VAL, 5);
//                plot.addSeries(gds.getXySeries(), standardSeriesFormat);
//            } else if (sensor_type.equals(SensorDevice.SENSOR_TYPE_TEMPERATURE_OUTISDE)) {
//                plot.setRangeBoundaries(0, 40, BoundaryMode.FIXED);
//                plot.setRangeStep(StepMode.INCREMENT_BY_VAL, 5);
//                plot.addSeries(gds.getXySeries(), standardSeriesFormat);
//            }
//
//            if (daterange.equals(GraphDataSeries.DaterangeType.DATERANGE_1_H)) {
//                plot.setDomainStep(StepMode.SUBDIVIDE, 8);
//            } else if (daterange.equals(GraphDataSeries.DaterangeType.DATERANGE_14_D)) {
//                plot.setDomainStep(StepMode.SUBDIVIDE, 7);
//                plot.setDomainBoundaries(gds.getMaxIndex()-40,gds.getMaxIndex(),BoundaryMode.FIXED);
//            } else if (daterange.equals(GraphDataSeries.DaterangeType.DATERANGE_1_EVERY)) {
//                plot.setDomainStep(StepMode.SUBDIVIDE, 8);
//            }
//
//            PanZoom.attach(plot, PanZoom.Pan.HORIZONTAL, PanZoom.Zoom.STRETCH_HORIZONTAL);
//
//
//            plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
//                @Override
//                public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
//                    try {
//                        int i = Math.round(((Number) obj).floatValue());
//                        Integer labelindex = null;
//                        if (gds.getxDataList().contains(i)) {
//                            labelindex = gds.getxDataList().indexOf(i);
//                        }
//                        if (labelindex == null) {
//                            return toAppendTo.append("");
//                        } else {
//                            return toAppendTo.append(gds.getxLabelList().get(labelindex));
//                        }
//
//                        //Log.d("graphDataSeries", "obj.FloatValue: " + i);
//                        //return toAppendTo;
//                    } catch (Error e){
//                        Log.d("graphDataSeries", "plot.label.error: " + e.toString());
//                        return toAppendTo.append("");
//                    }
//                }
//
//                @Override
//                public Object parseObject(String source, ParsePosition pos) {
//                    return null;
//                }
//            });
//            plot.redraw();
//            currentlyActiveSensorNr = positionInList;
//            currentSensorType = sensor_type;
//            currentDateRange = daterange;
//            currentSeries = gds;
//
//            new dayMarkerCreator(plot,gds).execute();
//
//        } catch (Exception e){
//            e.printStackTrace();
//            Log.e("graphDataSeries", "population error: " + e.toString());
//        }
//    }
}