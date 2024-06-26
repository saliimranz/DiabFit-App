package com.example.diabfitapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

public class EducationSupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_support);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Buttons
        Button diabetesEducationButton = findViewById(R.id.btn_diabetes_education);
        Button communityForumButton = findViewById(R.id.btn_community_forum);

        diabetesEducationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Diabetes Education button click
                Intent intent = new Intent(EducationSupportActivity.this, DiabatesEducation.class);
                startActivity(intent);
            }
        });

        communityForumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Community Forum button click
                Intent intent = new Intent(EducationSupportActivity.this, CommunityForum.class);
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
