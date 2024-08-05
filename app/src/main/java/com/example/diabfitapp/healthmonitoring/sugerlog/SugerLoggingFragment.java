package com.example.diabfitapp.healthmonitoring.sugerlog;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Locale;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabfitapp.R;
import com.example.diabfitapp.healthmonitoring.medication.NotificationReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SugerLoggingFragment extends Fragment {

    private SugarLogAdapter sugarLogAdapter;
    private List<SugarLog> sugarLogList = new ArrayList<>();
    private SugarLogDatabaseHelper sugarLogDatabaseHelper;
    private Calendar notificationTime = Calendar.getInstance();
    private static final String PREF_ALERT_HOUR = "alert_hour";
    private static final String PREF_ALERT_MINUTE = "alert_minute";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_suger_logging, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Blood Sugar Logging");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> {
            // Navigate back to the previous fragment
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        sugarLogDatabaseHelper = new SugarLogDatabaseHelper(requireContext());

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        sugarLogAdapter = new SugarLogAdapter(sugarLogList);
        recyclerView.setAdapter(sugarLogAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> showAddSugerLogDialog());

        Button setAlertButton = view.findViewById(R.id.set_alert_button);
        setAlertButton.setOnClickListener(v -> showSetAlertDialog());

        loadSugarLogs();
        loadAlertTime();
    }

    private void showAddSugerLogDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_suger_log, null);
        builder.setView(dialogView);

        EditText sugarInput = dialogView.findViewById(R.id.sugar_input);
        Spinner typeSpinner = dialogView.findViewById(R.id.type_spinner);
        Button addButton = dialogView.findViewById(R.id.add_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.sugar_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        AlertDialog dialog = builder.create();

        addButton.setOnClickListener(v -> {
            String sugarLevelStr = sugarInput.getText().toString().trim();

            if (sugarLevelStr.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a sugar level.", Toast.LENGTH_SHORT).show();
            } else {
                int sugarLevel = Integer.parseInt(sugarLevelStr);
                String type = typeSpinner.getSelectedItem().toString();
                String time = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

                SugarLog sugarLog = new SugarLog(sugarLevel, type, time);
                saveSugarLog(sugarLog);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showSetAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_set_suger_alert, null);
        builder.setView(dialogView);

        final TimePicker timePicker = dialogView.findViewById(R.id.time_picker); // Ensure this ID matches the XML
        Button saveButton = dialogView.findViewById(R.id.save_button); // Ensure this ID matches the XML
        Button cancelButton = dialogView.findViewById(R.id.cancel_button); // Ensure this ID matches the XML

        // Set the current time in the TimePicker
        timePicker.setHour(notificationTime.get(Calendar.HOUR_OF_DAY));
        timePicker.setMinute(notificationTime.get(Calendar.MINUTE));

        AlertDialog dialog = builder.create();

        saveButton.setOnClickListener(v -> {
            notificationTime.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
            notificationTime.set(Calendar.MINUTE, timePicker.getMinute());
            notificationTime.set(Calendar.SECOND, 0);

            saveAlertTime(notificationTime);

            // Update the button text with the selected time
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            Button setAlertButton = requireView().findViewById(R.id.set_alert_button);
            setAlertButton.setText("Log Alert at " + timeFormat.format(notificationTime.getTime()));

            setSugarLogAlert();

            // Handle saving the notification time for future use here
            // e.g., save to SharedPreferences or a database

            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void saveAlertTime(Calendar time) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_ALERT_HOUR, time.get(Calendar.HOUR_OF_DAY));
        editor.putInt(PREF_ALERT_MINUTE, time.get(Calendar.MINUTE));
        editor.apply();
    }

    private void loadAlertTime() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        int hour = prefs.getInt(PREF_ALERT_HOUR, notificationTime.get(Calendar.HOUR_OF_DAY));
        int minute = prefs.getInt(PREF_ALERT_MINUTE, notificationTime.get(Calendar.MINUTE));
        notificationTime.set(Calendar.HOUR_OF_DAY, hour);
        notificationTime.set(Calendar.MINUTE, minute);
        notificationTime.set(Calendar.SECOND, 0);

        // Update the button text
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Button setAlertButton = requireView().findViewById(R.id.set_alert_button);
        setAlertButton.setText("Log Alert at " + timeFormat.format(notificationTime.getTime()));
    }

    private void setSugarLogAlert() {
        // Schedule the notification using the stored time
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireContext(), NotificationReceiver.class);
        intent.putExtra("notification_type", "sugar_log");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            try {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime.getTimeInMillis(), pendingIntent);
            } catch (SecurityException e) {
                Log.e("SugerLoggingFragment", "SecurityException while setting exact alarm", e);
                // Optionally notify the user or handle the exception accordingly
            }
        }
    }


    private void saveSugarLog(SugarLog sugarLog) {
        AsyncTask.execute(() -> {
            sugarLogDatabaseHelper.addSugarLog(sugarLog);
            requireActivity().runOnUiThread(() -> {
                sugarLogList.add(0, sugarLog);
                sugarLogAdapter.notifyItemInserted(0);
            });
        });
    }

    private void loadSugarLogs() {
        AsyncTask.execute(() -> {
            List<SugarLog> logs = sugarLogDatabaseHelper.getAllSugarLogs();
            requireActivity().runOnUiThread(() -> {
                sugarLogList.clear();
                sugarLogList.addAll(logs);
                sugarLogAdapter.notifyDataSetChanged();
            });
        });
    }
}
