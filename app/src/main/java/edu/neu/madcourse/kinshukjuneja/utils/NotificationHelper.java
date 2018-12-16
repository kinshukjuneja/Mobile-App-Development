package edu.neu.madcourse.kinshukjuneja.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import edu.neu.madcourse.kinshukjuneja.R;
import edu.neu.madcourse.kinshukjuneja.activity.ScroggleLeaderboardActivity;

public class NotificationHelper {

    private Context context;

    private static final String TITLE = "New Leaderboard Entry";
    private static final String TEXT_TEMPLATE = "%s just made it to leaderboard with %s score of %d";
    private static final String CHANNEL_NAME = "Notification Channel";
    private static final String CHANNEL_DESCRIPTION = "Displays leaderboard updates";
    private static final String CHANNEL_ID = "leaderboard_notification";
    private static final int NOTIFICATION_ID = 001;

    public NotificationHelper(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void generateDrawerNotification(String username, int score, boolean isTotalScore) {
        Intent notifyIntent = new Intent(context, ScroggleLeaderboardActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(TITLE)
                .setContentText(String.format(TEXT_TEMPLATE, username, isTotalScore ? "total" : "highest", score))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }


}
