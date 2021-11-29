package com.duedderuesch.placed.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.duedderuesch.placed.R;

import java.util.Random;

import static androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID;

public class AlarmReceiver extends BroadcastReceiver {

    public final String TAG = "AlarmReceiver";
    public final String ACTION_SNOOZE = "ac_snooze";
    public static final String RECURRING_POLLEN = "ac_rec_pollen";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        try {
            if (intent.getAction().equals(ACTION_SNOOZE)) {
                Log.d(TAG, "Snooze");
            } else if (intent.getAction().equals(RECURRING_POLLEN)) {
                Log.d(TAG, "Recurring alarm");
//                Intent startingPollen = new Intent(context, PollenActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startingPollen, 0);
//
//                Intent snoozeIntent = new Intent(context, AlarmReceiver.class);
//                snoozeIntent.setAction(ACTION_SNOOZE);
//                snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
//                PendingIntent snoozePendingIntent =
//                        PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);
//
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.noti_channel_id_pollen))
//                        .setSmallIcon(R.drawable.ic_pollen)
//                        .setContentTitle("Pollen")
//                        .setContentText("Logge heute deine Erfahrung")
//                        .setContentIntent(pendingIntent)
//                        .setAutoCancel(true)
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                        .addAction(R.drawable.ic_snooze, "Snooze",
//                                snoozePendingIntent);
//                final int random = new Random().nextInt(100000000) + 100000000;
//                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                assert mNotificationManager != null;
//                mNotificationManager.notify(random, builder.build());
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, e.toString());
        }
    }
}