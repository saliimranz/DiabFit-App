package com.example.diabfitapp.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.diabfitapp.R;
import com.example.diabfitapp.exercise.progress.ProgressMonitoringFragment;
import com.example.diabfitapp.exercise.workout.PersonaliseWorkoutFragment;
import com.example.diabfitapp.main.MainActivity;

public class ExercisePlansFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_plans, container, false);

        // Setup toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Exercise Plans");
        toolbar.setNavigationIcon(R.drawable.ic_back);  // Make sure this icon is properly sized and placed in drawable folders
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Buttons
        Button personalizeWorkoutButton = view.findViewById(R.id.btn_personalize_workout);
        Button progressMonitoringButton = view.findViewById(R.id.btn_progress_monitoring);

        personalizeWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new PersonaliseWorkoutFragment());
            }
        });

        progressMonitoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new ProgressMonitoringFragment());
            }
        });

        return view;
    }
}
