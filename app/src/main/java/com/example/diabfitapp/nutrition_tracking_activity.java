package com.example.diabfitapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

import java.util.Objects;

public class nutrition_tracking_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_tracking);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Buttons
        Button foodDatabaseButton = findViewById(R.id.btn_food_database);
        Button mealPlanningButton = findViewById(R.id.btn_meal_planning);
        Button cgCountingButton = findViewById(R.id.btn_cg_counting);

        foodDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(nutrition_tracking_activity.this, FoodDatabase.class);
                startActivity(intent);
            }
        });

        mealPlanningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(nutrition_tracking_activity.this, MealPlanning.class);
                startActivity(intent);
            }
        });

        cgCountingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(nutrition_tracking_activity.this, CGCount.class);
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
