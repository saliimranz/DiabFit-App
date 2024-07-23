package com.example.diabfitapp.healthmonitoring.medication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import com.example.diabfitapp.main.MainActivity;
import com.example.diabfitapp.R;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationType = intent.getStringExtra("notification_type");
        if (notificationType != null) {
            if (notificationType.equals("sugar_log")) {
                sendSugarLogNotification(context);
            } else if (notificationType.equals("medication")) {
                sendMedicationNotification(context, intent.getStringExtra("medicine_name"));
            }
        }
    }

    private void sendSugarLogNotification(Context context) {
        String channelId = "sugar_log_channel";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Sugar Log Alert", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_suger)
                .setContentTitle("Blood Sugar Logging")
                .setContentText("It's time to check your sugar and log.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(0, builder.build());
    }

    private void sendMedicationNotification(Context context, String medicineName) {
        String channelId = "medication_channel";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Medication Reminder", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_medicine)
                .setContentTitle("Medication Reminder")
                .setContentText("Time to take your medicine: " + medicineName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
        Log.d("NotificationReceiver", "Medication notification sent for: " + medicineName);
    }

}
