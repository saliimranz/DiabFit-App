package com.example.diabfitapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button nutritionTrackingButton = view.findViewById(R.id.btn_nutrition);
        Button exercisePlansButton = view.findViewById(R.id.btn_exercise);
        Button healthMonitoringButton = view.findViewById(R.id.btn_health_monitoring);
        Button educationSupportButton = view.findViewById(R.id.btn_education);

        nutritionTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openNutritionTrackingFragment();
            }
        });

        exercisePlansButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), exercise_plans_activity.class);
                startActivity(intent);
            }
        });

        healthMonitoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HealthMonitoringActivity.class);
                startActivity(intent);
            }
        });

        educationSupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EducationSupportActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
