package com.example.diabfitapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button nutritionTrackingButton = findViewById(R.id.btn_nutrition);
        Button exercisePlansButton = findViewById(R.id.btn_exercise);
        Button healthMonitoringButton = findViewById(R.id.btn_health_monitoring);
        Button educationSupportButton = findViewById(R.id.btn_education);

        nutritionTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Personalized Nutrition Tracking Activity
                // Intent intent = new Intent(MainActivity.this, NutritionTrackingActivity.class);
                // startActivity(intent);
            }
        });

        exercisePlansButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Exercise and Workout Plans Activity
                // Intent intent = new Intent(MainActivity.this, ExercisePlansActivity.class);
                // startActivity(intent);
            }
        });

        healthMonitoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Health Monitoring and Alerts Activity
                // Intent intent = new Intent(MainActivity.this, HealthMonitoringActivity.class);
                // startActivity(intent);
            }
        });

        educationSupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Educational Resources and Community Support Activity
                // Intent intent = new Intent(MainActivity.this, EducationSupportActivity.class);
                // startActivity(intent);
            }
        });
    }
}
