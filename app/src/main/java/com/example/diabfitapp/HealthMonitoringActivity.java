package com.example.diabfitapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class HealthMonitoringActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_monitoring);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back button
       Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Button bloodsugerloggingButton = findViewById(R.id.btn_blood_suger_logging);
        Button healthinsightsButton = findViewById(R.id.btn_health_insights);
        Button medicationRemindersButton = findViewById(R.id.btn_medication_Reminders);
        Button emergencyalertsButton = findViewById(R.id.btn_emergency_alerts);

        bloodsugerloggingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthMonitoringActivity.this, SugerLogging.class);
                startActivity(intent);
            }
        });

        healthinsightsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthMonitoringActivity.this, HealthInsights.class);
                startActivity(intent);
            }
        });

        medicationRemindersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthMonitoringActivity.this, MedicationReminders.class);
                startActivity(intent);
            }
        });

        emergencyalertsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthMonitoringActivity.this, EmergencyAlerts.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
