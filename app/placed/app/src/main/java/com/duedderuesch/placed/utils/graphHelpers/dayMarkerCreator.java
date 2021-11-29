package com.duedderuesch.placed.utils.graphHelpers;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;

import com.androidplot.xy.XValueMarker;
import com.androidplot.xy.XYPlot;
import com.duedderuesch.placed.data.entities.GraphDataSeries;

import java.util.ArrayList;

public class dayMarkerCreator extends AsyncTask<String, Integer, Void> {

    private XYPlot plot;
    private GraphDataSeries gds;
    private ArrayList<Integer> markerList;

    public dayMarkerCreator(XYPlot plot, GraphDataSeries gds) {
        super();
        this.plot = plot;
        this.gds = gds;
    }

    @Override
    protected Void doInBackground(String... strings) {
        markerList = new ArrayList<>();
        ArrayList<Integer> querryList = new ArrayList<>();
        querryList = gds.getxDataList();
        for(int i=0; i<querryList.size();i++){
            String date = gds.getxLabelList().get(i);
            if(date.substring(0,5).equals("00:00")){
//                if(markerList.size()>0){
//                    if((markerList.get(markerList.size()-1)-i)>10){
//                        markerList.add(i);
//                    }
//                } else {
                    markerList.add(i);
//                }

            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        Log.d("GRAPGH_ACTION","list.size: " + markerList.size());
        for(int i=0;i<markerList.size();i++){
            Log.d("GRAPGH_ACTION", markerList.get(i)+"");
            XValueMarker querryMarker = new XValueMarker(markerList.get(i),"");
            Paint linePaint = new Paint();
            linePaint.setAntiAlias(false);
            linePaint.setAlpha(5);
            linePaint.setColor(Color.BLACK);
            querryMarker.setLinePaint(linePaint);
            plot.addMarker(querryMarker);
        }

    }
}