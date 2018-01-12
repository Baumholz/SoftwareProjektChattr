package com.example.david.chattr.messaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.david.chattr.R;
import com.example.david.chattr.startup.HomeActivity;

/**
 * Created by david on 30.12.17.
 */

public class MessageNotifier {

    private static int NOTIFICATION_ID;

    NotificationCompat.Builder notificationBuilder;
    NotificationManager notificationManager;

    public MessageNotifier (Context context) {
        notificationBuilder = new NotificationCompat.Builder(context, "channelId")
                .setSmallIcon(R.drawable.chattr_small_icon)
                .setContentTitle("Chattr")
                .setContentText("New message arrived")
                .setColor(context.getResources().getColor(R.color.notificationColor))
                .setAutoCancel(false);

        Intent resultIntent = new Intent(context, HomeActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(HomeActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

//        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(resultPendingIntent);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        initChannels(context);
    }

    public void showOrUpdateNotification(String content) {
        notificationBuilder.setContentText(content);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    public void removeNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
    }

    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("channelId", "Channel name", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
    }
}
