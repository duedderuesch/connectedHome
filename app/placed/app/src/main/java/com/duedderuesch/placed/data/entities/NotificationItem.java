package com.duedderuesch.placed.data.entities;

import android.content.Context;

import com.duedderuesch.placed.R;

public class NotificationItem {
    private Integer id;
    private String channel;
    private String title;
    private String text;
    private Integer icon;
    private Integer priority;
    private Boolean hasProgressBar;
    private Integer progress;

    public static class ProgressStyle {
        public com.duedderuesch.placed.data.entities.ProgressStyle placed_plant(Context ctx){
            return new com.duedderuesch.placed.data.entities.ProgressStyle(R.color.primaryColorDark,
                    R.color.progressBarBackgroundTint);
        }
    }

    public NotificationItem(Integer id,String channel, String title, String text, Integer icon, Integer priority, Boolean hasProgressBar, Integer progress){
        this.id = id;
        this.channel = channel;
        this.title = title;
        this.text = text;
        this.icon = icon;
        this.priority = priority;
        this.hasProgressBar = hasProgressBar;
        this.progress = progress;
    }

    public NotificationItem(String channel, String title, String text, Integer icon, Integer priority){
        this.channel = channel;
        this.title = title;
        this.text = text;
        this.icon = icon;
        this.priority = priority;

    }

    public NotificationItem(String title, Integer icon, Integer priority, Boolean hasProgressBar, Integer progress){
        this.title = title;
        this.icon = icon;
        this.priority = priority;
        this.hasProgressBar = hasProgressBar;
        this.progress = progress;

    }

    public Integer getId() {
        return id;
    }

    public String getChannel() {
        return channel;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Integer getIcon() {
        return icon;
    }

    public Integer getPriority() {
        return priority;
    }

    public Boolean getHasProgressBar() {
        return hasProgressBar;
    }

    public Integer getProgress() {
        return progress;
    }
}
