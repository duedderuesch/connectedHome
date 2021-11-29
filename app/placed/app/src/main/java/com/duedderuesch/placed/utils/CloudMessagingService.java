package com.duedderuesch.placed.utils;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.duedderuesch.placed.R;
import com.duedderuesch.placed.data.entities.NotificationItem;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CloudMessagingService extends FirebaseMessagingService {

    private final String TAG = "CloudMessagingService";

    public CloudMessagingService() {
    }

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            handleNow(remoteMessage);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void handleNow(RemoteMessage remoteMessage){
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getApplicationContext().getString(R.string.noti_channel_id_plant))
                    .setSmallIcon(R.drawable.icon_plant_1)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            if(remoteMessage.getData().containsKey("progress")){
                builder.setProgress(100,Integer.valueOf(remoteMessage.getData().get("progress")),false);
                builder.setColor(getColor(R.color.placedIntenseRedColor));
            }

            final int random = new Random().nextInt(100000000) + 100000000;
            NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            assert mNotificationManager != null;
            mNotificationManager.notify(random, builder.build());
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }

}