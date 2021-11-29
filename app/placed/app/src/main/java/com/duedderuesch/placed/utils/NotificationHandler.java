package com.duedderuesch.placed.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.duedderuesch.placed.MainActivity;
import com.duedderuesch.placed.R;
import com.duedderuesch.placed.data.entities.NotificationItem;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;

public class NotificationHandler {

    public static NotificationCompat.Builder getNotification(Context ctx, NotificationItem noti){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, noti.getChannel())
                .setSmallIcon(noti.getIcon())
                .setContentTitle(noti.getTitle())
                .setContentText(noti.getText())
                .setPriority(noti.getPriority());
        return builder;
    }

    public static void showNotification(NotificationCompat.Builder notif_build, Integer notif_id, Context ctx){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
        notificationManager.notify(notif_id, notif_build.build());
    }

    public static void setRecurringAlarm(Context context) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 1);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.setAction(AlarmReceiver.RECURRING_POLLEN);
        PendingIntent sender = PendingIntent.getBroadcast(context, 10, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
    }
}
