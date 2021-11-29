package com.duedderuesch.placed.data.entities;

import android.graphics.Color;

public class ProgressStyle {
    private int progressColor;
    private int bgColor;

    public ProgressStyle(int progressColor, int bgColor){
        this.progressColor = progressColor;
        this.bgColor = bgColor;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public int getBgColor() {
        return bgColor;
    }

}
