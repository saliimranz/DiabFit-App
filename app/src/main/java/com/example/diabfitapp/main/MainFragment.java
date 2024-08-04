package com.example.diabfitapp.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diabfitapp.education.EducationSupportFragment;
import com.example.diabfitapp.exercise.ExercisePlansFragment;
import com.example.diabfitapp.healthmonitoring.HealthMonitoringFragment;
import com.example.diabfitapp.R;
import com.example.diabfitapp.nutrition.NutritionTrackingFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button nutritionTrackingButton = view.findViewById(R.id.btn_nutrition);
        Button exercisePlansButton = view.findViewById(R.id.btn_exercise);
        Button healthMonitoringButton = view.findViewById(R.id.btn_health_monitoring);
        Button educationSupportButton = view.findViewById(R.id.btn_education);
        Button topRightButton = view.findViewById(R.id.btn_top_right);


        topRightButton.setOnClickListener(v -> {
            // End user session and log out
            FirebaseAuth.getInstance().signOut();
            // Replace fragment with LoginFragment
            ((MainActivity) getActivity()).replaceFragment(new LoginFragment());
        });

        nutritionTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new NutritionTrackingFragment());
            }
        });

        exercisePlansButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new ExercisePlansFragment());
            }
        });

        healthMonitoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new HealthMonitoringFragment());
            }
        });

        educationSupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new EducationSupportFragment());
            }
        });

        return view;
    }
}
