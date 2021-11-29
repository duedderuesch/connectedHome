package com.duedderuesch.placed.utils.views;

import android.content.Context;
import android.content.res.Configuration;
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

public class PlantIconBorder extends View {
    private final String TAG = "PlantIconBorder";

    private Paint circlePaint;
    private Paint shadowPaint;
    private Paint whiteFillPaint;
    private Integer radius;
    private Integer width;


    public PlantIconBorder(Context ctx, AttributeSet chartattrs){
        super(ctx, chartattrs);
        TypedArray a = ctx.getTheme().obtainStyledAttributes(
                chartattrs,
                R.styleable.PlantIconBorder,
                0, 0);

        try {
            radius = a.getInteger(R.styleable.PlantIconBorder_radius, 56);
        } finally {
            a.recycle();
        }

        init();
    }

    public Boolean setPlantActiveRecent(Boolean recentActive){
        if(recentActive) circlePaint.setColor(ResourcesCompat.getColor(getResources(), R.color.primaryColor, null));
        else circlePaint.setColor(ResourcesCompat.getColor(getResources(), R.color.secondaryColor, null));

        invalidate();
        requestLayout();

        return recentActive;
    }

    private void init() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(12);

        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint.setStyle(Paint.Style.STROKE);
        shadowPaint.setStrokeWidth(8);
        shadowPaint.setAlpha(120);
        shadowPaint.setMaskFilter(new BlurMaskFilter(16, BlurMaskFilter.Blur.NORMAL));

        whiteFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whiteFillPaint.setStyle(Paint.Style.FILL);

        int nightModeFlags =
                getContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                circlePaint.setColor(ResourcesCompat.getColor(getResources(), R.color.sdcDaySeperatorTint, null));
                shadowPaint.setColor(ResourcesCompat.getColor(getResources(), R.color.sdcShadow, null));
                whiteFillPaint.setColor(ResourcesCompat.getColor(getResources(), R.color.backgroundDarkMode, null));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                circlePaint.setColor(ResourcesCompat.getColor(getResources(), R.color.sdcDaySeperatorTint, null));
                shadowPaint.setColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
                whiteFillPaint.setColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
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

        int minh = getPaddingBottom() + getPaddingTop() + getSuggestedMinimumHeight();
        int h = resolveSizeAndState(minh, heightMeasureSpec, 0);

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



        canvas.drawCircle((width/2), (width/2), (width/2) - 8, shadowPaint);
        canvas.drawCircle((width/2), (width/2), (width/2) - 8, circlePaint);

        final Path path = new Path();

        path.moveTo(0, 0);
        path.lineTo(width, 0);
        path.lineTo(width, width);
        path.lineTo(0, width);
        path.close();

        path.addCircle((width/2), (width/2), (width/2) - 8,Path.Direction.CW);

        path.setFillType(Path.FillType.EVEN_ODD);
        canvas.drawPath(path, whiteFillPaint);

        invalidate();
        requestLayout();
    }
}
