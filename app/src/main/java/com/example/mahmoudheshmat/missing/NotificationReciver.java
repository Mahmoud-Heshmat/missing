package com.example.mahmoudheshmat.missing;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.mahmoudheshmat.missing.Activites.Home;
import com.example.mahmoudheshmat.missing.Models.DatabaseURL;

public class NotificationReciver extends BroadcastReceiver {


    static int id =1;
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (DatabaseURL.accept.equals(action)) {
            Intent parent = new Intent(context, Home.class);
            parent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(parent);

            int n_id = intent.getIntExtra("notification_id", 0);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(n_id);
        }

        if (DatabaseURL.cancel.equals(action)) {
            int n_id = intent.getIntExtra("notification_id", 0);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(n_id);
        }
    }
}
