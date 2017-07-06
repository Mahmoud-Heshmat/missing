package com.example.mahmoudheshmat.missing;


import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.mahmoudheshmat.missing.Models.DatabaseURL;
import com.example.mahmoudheshmat.missing.Activites.MainActivity;
import com.example.mahmoudheshmat.missing.R;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{

    NotificationManager notificationManager;
    android.support.v7.app.NotificationCompat.Builder mBuilder;
    static int id =1;

    //sharedPreferences
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    Context context;
    String user_id;

    String message = null;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        context = getApplicationContext();

        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("User_id", null);

        if (remoteMessage.getData().size() > 0) {
            // get values from data that sent from php by fcm

            message = remoteMessage.getData().get("message");

            if(message != null){
                showNotification(getApplicationContext(), "Face Recognition Found", message);

            }else{
                String messageContent = remoteMessage.getData().get("content");
                String roomId = remoteMessage.getData().get("room_id");
                String userId = remoteMessage.getData().get("user_id");
                String username = remoteMessage.getData().get("user_name");
                String messageType = remoteMessage.getData().get("type");
                String timestamp = remoteMessage.getData().get("timeStamp");


                if ((Integer.valueOf(userId) != Integer.valueOf(user_id))) {

                    if (isAppIsInBackground(this)) {
                        sendNotification("New Message Sent", messageContent);
                    } else {
                        Intent intent = new Intent("UpdateChateActivity");
                        intent.putExtra("messageContent", messageContent);
                        intent.putExtra("roomId", roomId);
                        intent.putExtra("userId", userId);
                        intent.putExtra("username", username);
                        intent.putExtra("messageType", messageType);
                        intent.putExtra("timestamp", timestamp);
                        sendBroadcast(intent);
                    }
                }
            }
        }
    }

    private void sendNotification(String title , String message) {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.missing_logo);
        PendingIntent pendIntentTwo = PendingIntent.getActivity(context,0,
                new Intent(context,MainActivity.class),0);

        int numMessages = 0;

        mBuilder = (android.support.v7.app.NotificationCompat.Builder) new android.support.v7.app.NotificationCompat.Builder(context)
                .setVisibility(android.support.v7.app.NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.notifyicon)
                .setAutoCancel(true)
                .setLargeIcon(icon)
                .setContentTitle(title)
                .setContentText(message);


        mBuilder.setNumber(++numMessages);
        mBuilder.setContentIntent(pendIntentTwo);
        mBuilder.setDefaults(android.support.v7.app.NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        notificationManager =(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        Bundle extras = new Bundle();
        extras.putInt("notification_id", id);

        Intent yesReceive = new Intent();
        yesReceive.setAction(DatabaseURL.accept);
        yesReceive.putExtras(extras);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(context, 0, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.addAction(R.drawable.accept, "Accept", pendingIntentYes);

        Intent cancelReceive = new Intent();
        cancelReceive.setAction(DatabaseURL.cancel);
        cancelReceive.putExtras(extras);
        PendingIntent pendingIntentMaybe = PendingIntent.getBroadcast(context, 0, cancelReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.addAction(R.drawable.cancel, "Cancel", pendingIntentMaybe);


        notificationManager.notify(1,mBuilder.build());
    }

    // Notification For face recognition
    public void showNotification (Context context , String title , String message){

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.missing_logo);
        PendingIntent pendIntentTwo = PendingIntent.getActivity(context,0,
                new Intent(context,MainActivity.class),0);

        int numMessages = 0;

        mBuilder = (android.support.v7.app.NotificationCompat.Builder) new android.support.v7.app.NotificationCompat.Builder(context)
                .setVisibility(android.support.v7.app.NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.notifyicon)
                .setAutoCancel(true)
                .setLargeIcon(icon)
                .setContentTitle(title)
                .setContentText(message);


        mBuilder.setNumber(++numMessages);
        mBuilder.setContentIntent(pendIntentTwo);
        mBuilder.setDefaults(android.support.v7.app.NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        notificationManager =(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        Bundle extras = new Bundle();
        extras.putInt("notification_id", 0);

        Intent yesReceive = new Intent();
        yesReceive.setAction(DatabaseURL.accept);
        yesReceive.putExtras(extras);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(context, 0, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.addAction(R.drawable.accept, "Accept", pendingIntentYes);

        Intent cancelReceive = new Intent();
        cancelReceive.setAction(DatabaseURL.cancel);
        cancelReceive.putExtras(extras);
        PendingIntent pendingIntentMaybe = PendingIntent.getBroadcast(context, 0, cancelReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.addAction(R.drawable.cancel, "Cancel", pendingIntentMaybe);


        notificationManager.notify(0,mBuilder.build());
    }


    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}
