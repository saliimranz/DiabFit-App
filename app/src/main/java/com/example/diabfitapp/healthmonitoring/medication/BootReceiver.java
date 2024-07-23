package com.example.diabfitapp.healthmonitoring.medication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Log the boot completion event
            Log.d("BootReceiver", "BOOT_COMPLETED received. Resetting medication alarms.");

            MedicineDatabaseHelper dbHelper = new MedicineDatabaseHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = null;
            try {
                cursor = db.query(MedicineDatabaseHelper.TABLE_MEDICINES, null, null, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(MedicineDatabaseHelper.COLUMN_ID);
                    int nameIndex = cursor.getColumnIndex(MedicineDatabaseHelper.COLUMN_NAME);
                    int quantityIndex = cursor.getColumnIndex(MedicineDatabaseHelper.COLUMN_QUANTITY);
                    int hourIndex = cursor.getColumnIndex(MedicineDatabaseHelper.COLUMN_HOUR);
                    int minuteIndex = cursor.getColumnIndex(MedicineDatabaseHelper.COLUMN_MINUTE);
                    int lockedIndex = cursor.getColumnIndex(MedicineDatabaseHelper.COLUMN_LOCKED);
                    int eatenDateIndex = cursor.getColumnIndex(MedicineDatabaseHelper.COLUMN_EATEN_DATE);

                    do {
                        int id = cursor.getInt(idIndex);
                        String name = cursor.getString(nameIndex);
                        int quantity = cursor.getInt(quantityIndex);
                        int hour = cursor.getInt(hourIndex);
                        int minute = cursor.getInt(minuteIndex);
                        int locked = cursor.getInt(lockedIndex);
                        long eatenDate = cursor.getLong(eatenDateIndex);

                        Medicine medicine = new Medicine(id, name, quantity, hour, minute, locked == 1, eatenDate);
                        setMedicineAlarm(context, medicine);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.e("BootReceiver", "Error while setting medicine alarms", e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                db.close();
            }
            resetSugarLogAlert(context);
        }
    }


    private void setMedicineAlarm(Context context, Medicine medicine) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("medicine_name", medicine.getName());
        intent.putExtra("medicine_id", medicine.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, medicine.getId(), intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, medicine.getHour());
        calendar.set(Calendar.MINUTE, medicine.getMinute());
        calendar.set(Calendar.SECOND, 0);

        if (alarmManager != null) {
            try {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } catch (SecurityException e) {
                Log.e("BootReceiver", "SecurityException while setting exact alarm", e);
                // Optionally notify the user or handle the exception accordingly
            }
        }
    }

    private void resetSugarLogAlert(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int hour = prefs.getInt("alert_hour", 8); // Default to 8 AM if no value is saved
        int minute = prefs.getInt("alert_minute", 0);

        Calendar notificationTime = Calendar.getInstance();
        notificationTime.set(Calendar.HOUR_OF_DAY, hour);
        notificationTime.set(Calendar.MINUTE, minute);
        notificationTime.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("notification_type", "sugar_log");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            try {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime.getTimeInMillis(), pendingIntent);
            } catch (SecurityException e) {
                Log.e("SugerLoggingFragment", "SecurityException while setting exact alarm", e);
                // Optionally notify the user or handle the exception accordingly
            }
        }
    }
}
