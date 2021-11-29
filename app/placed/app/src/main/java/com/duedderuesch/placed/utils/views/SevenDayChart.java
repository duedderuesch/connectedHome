package com.duedderuesch.placed.utils.views;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.duedderuesch.placed.R;
import com.duedderuesch.placed.data.handler.SevenDayDataHandler;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

public class SevenDayChart extends View {
    private final String TAG = "SevenDayChart";

    private Boolean dataInPercent;
    private Integer yMin;
    private Integer yMax;
    private Boolean showYLegend;
    private Boolean showXLegend;
    private Integer yLegendColor;
    private Boolean showDataMarker;
    private Boolean showDataLegend;

    private Paint yLegendPaint;
    private Paint daySeperatorPaint;
    private Paint daySeperatorPaintInactive;
    private Paint shadowLinePaint;
    private Paint graphBackgPaint;
    private Paint xLegendPaint;
    private Paint warningPaint;
    private Paint xLegendShadowPaint;
    private Paint dataLegendPaint;
    private Paint markerStrokePaint;
    private Paint markerFillPaint;

    private ArrayList<Float> dataList = null;
    private ArrayList<String> dateList = null;
    private Integer width;
    private Integer columnWidth;
    private Calendar currentWeekdayCalendar;
    private ArrayList<String> weekdayList;
    private int markerRadius = 16;
    private float xLegendTextSize = 24;
    private SevenDayDataHandler sddHandler;


    public SevenDayChart(Context ctx, AttributeSet chartattrs){
        super(ctx, chartattrs);
        TypedArray a = ctx.getTheme().obtainStyledAttributes(
                chartattrs,
                R.styleable.SevenDayChart,
                0, 0);

        try {
            dataInPercent = a.getBoolean(R.styleable.SevenDayChart_dataInPercent, true);
            if(dataInPercent){
                yMin = 0;
                yMax = 100;
            } else {
                yMin = a.getInteger(R.styleable.SevenDayChart_yMin, 0);
                yMax = a.getInteger(R.styleable.SevenDayChart_yMax, 40);
            }
            showYLegend = a.getBoolean(R.styleable.SevenDayChart_showYLegend, true);
            showXLegend = a.getBoolean(R.styleable.SevenDayChart_showXLegend, true);
            yLegendColor = a.getColor(R.styleable.SevenDayChart_yLegendColor,getResources().getColor(R.color.primaryTextColor));
            showDataMarker = a.getBoolean(R.styleable.SevenDayChart_showDataMarker, true);
            showDataLegend = a.getBoolean(R.styleable.SevenDayChart_showDataLegend, true);
        } finally {
            a.recycle();
        }

        weekdayList = new ArrayList<>(); //TODO: ordentlicher Array (+ Übersetzung)
        weekdayList.add("Mo");
        weekdayList.add("Di");
        weekdayList.add("Mi");
        weekdayList.add("Do");
        weekdayList.add("Fr");
        weekdayList.add("Sa");
        weekdayList.add("So");

        init();
    }

    public void setDataInPercent(boolean dataInPercent){
        this.dataInPercent = dataInPercent;

        if(dataInPercent){
            yMin = 0;
            yMax = 100;
        }
    }

    public void setYDimensions(int yMin, int yMax){
        this.dataInPercent = false;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public void setDataGraph(@NotNull ArrayList<Float> dataList){
        this.dataList = new ArrayList<>();
        this.dataList = dataList;
    }

    public void setDataHandler(@NotNull SevenDayDataHandler dataHandler){
        this.sddHandler = dataHandler;
        setDataGraph(sddHandler.getDataList());
    }

    private void init() {
        yLegendPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yLegendPaint.setColor(yLegendColor);
        yLegendPaint.setStyle(Paint.Style.STROKE);
        yLegendPaint.setStrokeWidth(4);
//        if (textHeight == 0) {
//            textHeight = textPaint.getTextSize();
//        } else {
            yLegendPaint.setTextSize(8);
//        }

        xLegendPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        xLegendPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        xLegendPaint.setStrokeWidth(0.5f);
        xLegendPaint.setTextSize(xLegendTextSize);
        xLegendPaint.setTextAlign(Paint.Align.CENTER);

        warningPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        warningPaint.setColor(getResources().getColor(R.color.secondaryColorDark));
        warningPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        warningPaint.setStrokeWidth(0.5f);
        warningPaint.setTextSize(xLegendTextSize);
        warningPaint.setTextAlign(Paint.Align.CENTER);

        xLegendShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xLegendShadowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        xLegendShadowPaint.setStrokeWidth(0.5f);
        xLegendShadowPaint.setTextAlign(Paint.Align.CENTER);
        xLegendShadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
        xLegendShadowPaint.setStrokeWidth(6);
        xLegendShadowPaint.setAlpha(180);

        daySeperatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        daySeperatorPaint.setColor(getResources().getColor(R.color.sdcDaySeperatorTint));
        daySeperatorPaint.setStyle(Paint.Style.STROKE);
        daySeperatorPaint.setStrokeWidth(3);

        daySeperatorPaintInactive = new Paint(Paint.ANTI_ALIAS_FLAG);
        daySeperatorPaintInactive.setColor(getResources().getColor(R.color.sdcDaySeperatorTint));
        daySeperatorPaintInactive.setStyle(Paint.Style.STROKE);
        daySeperatorPaintInactive.setStrokeWidth(1);
        daySeperatorPaintInactive.setAlpha(120);

        shadowLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowLinePaint.setColor(getResources().getColor(R.color.sdcDaySeperatorTint));
        shadowLinePaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
        shadowLinePaint.setStrokeWidth(6);
        shadowLinePaint.setAlpha(80);

        graphBackgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        graphBackgPaint.setColor(getResources().getColor(R.color.ap_gray));
        graphBackgPaint.setStrokeWidth(6);
        graphBackgPaint.setStyle(Paint.Style.STROKE);
        graphBackgPaint.setAlpha(220);

        markerStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerStrokePaint.setColor(getResources().getColor(R.color.black));
        markerStrokePaint.setStrokeWidth(2);
        markerStrokePaint.setStyle(Paint.Style.STROKE);

        markerFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerFillPaint.setColor(getResources().getColor(R.color.primaryColorDark));
        markerFillPaint.setStyle(Paint.Style.FILL);

        dataLegendPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dataLegendPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        dataLegendPaint.setStrokeWidth(0.5f);
        dataLegendPaint.setTextSize(16);
        dataLegendPaint.setTextAlign(Paint.Align.CENTER);

        int nightModeFlags =
                getContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                dataLegendPaint.setColor(ResourcesCompat.getColor(getResources(), R.color.primaryTextColorDarkMode, null));
                xLegendPaint.setColor(ResourcesCompat.getColor(getResources(), R.color.primaryTextColorDarkMode, null));
                xLegendShadowPaint.setColor(ResourcesCompat.getColor(getResources(), R.color.sdcShadow, null));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                dataLegendPaint.setColor(ResourcesCompat.getColor(getResources(), R.color.primaryTextColor, null));
                xLegendPaint.setColor(ResourcesCompat.getColor(getResources(), R.color.primaryTextColor, null));
                xLegendShadowPaint.setColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                //pass
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        width = w;
        columnWidth = width/7;
        int calculatedHeight = (getWidth()/16)*9;
        int minh = getPaddingBottom() + getPaddingTop() + getSuggestedMinimumHeight();
        int h = resolveSizeAndState(calculatedHeight, heightMeasureSpec, 0);

        xLegendTextSize = getHeight()/17f;
        xLegendPaint.setTextSize(xLegendTextSize);
        warningPaint.setTextSize(xLegendTextSize);
        xLegendShadowPaint.setTextSize(xLegendTextSize);
        dataLegendPaint.setTextSize(xLegendTextSize*(0.9f));
        setMeasuredDimension(w, h);
        invalidate();
        requestLayout();
    }


    /**
     * actual method, that draws (or calls the draw events)
     * @param canvas
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(dataList == null || dataList.size() == 0) {
            dataList = new ArrayList<>();
        }

        ArrayList<Float> yDataLegendList = new ArrayList<>();
        if(showDataLegend){
            yDataLegendList = getDataLegendHeight();
        }
        canvas.drawPath(RoundedRect(daySeperatorPaint.getStrokeWidth()/2,
                daySeperatorPaint.getStrokeWidth()/2,
                getWidth()-daySeperatorPaint.getStrokeWidth()/2,
                getHeight()-daySeperatorPaint.getStrokeWidth()/2,
                24,
                24,
                false),
                graphBackgPaint);

        for(int i=0; i<6;i++){
            if(dataList.size() > 0) {
                canvas.drawLine(width / (7) * (i + 1),
                        daySeperatorPaint.getStrokeWidth(),
                        width / (7) * (i + 1),
                        getHeight() - daySeperatorPaint.getStrokeWidth(),
                        daySeperatorPaint);
            } else {
                canvas.drawLine(width / (7) * (i + 1),
                        daySeperatorPaintInactive.getStrokeWidth(),
                        width / (7) * (i + 1),
                        getHeight() - daySeperatorPaintInactive.getStrokeWidth(),
                        daySeperatorPaintInactive);
            }
        }
        if (dataList != null) {
            canvas.drawPath(Graph(dataList),graphBackgPaint);
        }

        markerRadius = getHeight()/30;
        for (int i = 0; i < 7; i++) {
            if (dataList.size() > 0) {
                if (showDataMarker && dataList.get(i) != null) {
                    canvas.drawPath(DataMarker((i + 1) * (columnWidth) - (columnWidth / 2), dataList.get(i), markerRadius), markerFillPaint);
                    canvas.drawPath(DataMarker((i + 1) * (columnWidth) - (columnWidth / 2), dataList.get(i), markerRadius), markerStrokePaint);

                }
                if (showDataLegend && dataList.get(i) != null) {
                    float valueOfIndex = dataList.get(i);
                    if (yDataLegendList.get(i) != null)
                        canvas.drawText(String.valueOf(valueOfIndex), (i + 1) * (columnWidth) - (columnWidth / 2), yDataLegendList.get(i), dataLegendPaint);
                }
            }
            if (showXLegend) {
                canvas.drawText(getWeekday(i), (i + 1) * (columnWidth) - (columnWidth / 2), getHeight() - 20, xLegendShadowPaint);
                canvas.drawText(getWeekday(i), (i + 1) * (columnWidth) - (columnWidth / 2), getHeight() - 20, xLegendPaint);
            }
        }
        if(dataList == null || dataList.size() == 0) {
            canvas.drawText("keine aktuellen Daten vorhanden", getWidth()/2, getHeight()/2 , warningPaint);
        }
        invalidate();
        requestLayout();
    }

    private String getWeekday(int dayNr){
        if(currentWeekdayCalendar == null){
            currentWeekdayCalendar = Calendar.getInstance();
        }
        int lastDayWeekday = currentWeekdayCalendar.get(Calendar.DAY_OF_WEEK);
        int currentWeekday = lastDayWeekday - 8 + dayNr;
        if(currentWeekday < 0){
            currentWeekday = currentWeekday + 7;
        } else if(currentWeekday > 6){
            currentWeekday = currentWeekday - 7;
        }

        return weekdayList.get(currentWeekday);
    }

    private static Path RoundedRect(float left, float top, float right, float bottom, float rx, float ry, boolean conformToOriginalPost) {
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width/2) rx = width/2;
        if (ry > height/2) ry = height/2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        path.rLineTo(-widthMinusCorners, 0);
        path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        path.rLineTo(0, heightMinusCorners);

        if (conformToOriginalPost) {
            path.rLineTo(0, ry);
            path.rLineTo(width, 0);
            path.rLineTo(0, -ry);
        }
        else {
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
            path.rLineTo(widthMinusCorners, 0);
            path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }

    private Path Graph(ArrayList<Float> dataList){
        Path path = new Path();
        int height = getHeight();
        if(dataList.size() > 0) {
            Integer firstItemIndex = null;
            for(int i=0; i< dataList.size(); i++){
                if(dataList.get(i) != null){
                    firstItemIndex = i;
                    break;
                }
            }
            path.moveTo((columnWidth*(firstItemIndex+1)) - ((columnWidth) / 2),
                    height - ((height / 100f) * dataList.get(firstItemIndex)));

            for (int i = (firstItemIndex + 1); i < 7; i++) {
                float data;
                if (dataInPercent && dataList.size() > 0 && dataList.get(i) != null) {
                    data = height - ((height / 100f) * dataList.get(i));
                    path.lineTo((i + 1) * (columnWidth) - ((columnWidth) / 2),
                            data);
                } else if (dataInPercent && dataList.size() > 0 && dataList.get(i) == null) {
                    //pass
                } else {
                    //TODO
                    data = (float) (height - 10);
                    path.lineTo((i + 1) * (columnWidth) - ((columnWidth) / 2),
                            data);
                }
            }
        }
        return path;
    }

    private Path DataMarker(int x, float y, int radius){
        Path path = new Path();

        int height = getHeight();
        if(dataInPercent) {
            path.moveTo(x, height-((height / 100f) * y - radius));
            path.lineTo(x + radius, height-((height / 100f) * y));
            path.lineTo(x, height-((height / 100f) * y + radius));
            path.lineTo(x - radius, height-((height / 100f) * y));
            path.close();
        }

        return path;
    }

    private ArrayList<Float> getDataLegendHeight(){
        ArrayList<Float> dataLegendHeightList = new ArrayList<>();
        if(dataList.size() == 7) {
            for (int i = 0; i < 7; i++) {
                int viewHeight = getHeight();
                Float dataValue = dataList.get(i);
                float lowerBoundary = viewHeight - (viewHeight * 0.1f);
                float upperBoundary = viewHeight - (viewHeight * 0.9f);
                float lowerExceptionStandardHeight = viewHeight - (viewHeight * 0.18f);
                if (dataValue != null) {
                    if (dataInPercent) {
                        float supposedY = viewHeight - ((viewHeight / 100f) * dataValue + 2 * markerRadius);
                        if (supposedY < upperBoundary) {
                            dataLegendHeightList.add(viewHeight - ((viewHeight / 100f) * dataValue - 2 * markerRadius - (xLegendTextSize * 0.6f)));
                        } else if (supposedY > lowerBoundary) {
                            dataLegendHeightList.add(lowerBoundary);
                        } else {
                            dataLegendHeightList.add(supposedY);
                        }
                    }
                } else {
                    dataLegendHeightList.add(null);
                }
            }
        }
        return dataLegendHeightList;
    }
}
