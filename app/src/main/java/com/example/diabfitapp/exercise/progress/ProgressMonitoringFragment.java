package com.example.diabfitapp.exercise.progress;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.example.diabfitapp.R;
import com.example.diabfitapp.exercise.workout.PersonaliseWorkoutFragment;
import com.example.diabfitapp.exercise.workout.Exercise;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProgressMonitoringFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressExerciseAdapter progressExerciseAdapter;
    private ProgressDatabaseHelper databaseHelper;

    private static final String PREFS_NAME = "ProgressMonitoringPrefs";
    private static final String LAST_RUN_DATE_KEY = "lastRunDate";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress_monitoring, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Progress Monitoring");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        Button startWorkoutButton = view.findViewById(R.id.start_workout_button);
        startWorkoutButton.setOnClickListener(v -> {
            StartWorkoutFragment startWorkoutFragment = new StartWorkoutFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, startWorkoutFragment)
                    .addToBackStack(null)
                    .commit();
        });

        Button HistoryButton = view.findViewById(R.id.history_button);
        HistoryButton.setOnClickListener(v -> {
            HistoryFragment HistoryFragment = new HistoryFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, HistoryFragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new ProgressDatabaseHelper(getContext());

        // Get current date
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        List<Exercise> exerciseList = databaseHelper.getExercisesByDate(currentDate);

        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String lastRunDate = prefs.getString(LAST_RUN_DATE_KEY, "");

        if (!currentDate.equals(lastRunDate)) {
            databaseHelper.deleteAllExercises();
            prefs.edit().putString(LAST_RUN_DATE_KEY, currentDate).apply();
        }

        progressExerciseAdapter = new ProgressExerciseAdapter(exerciseList, false);
        recyclerView.setAdapter(progressExerciseAdapter);

        Button addExerciseButton = view.findViewById(R.id.add_exercise_button);
        addExerciseButton.setOnClickListener(v -> {
            PersonaliseWorkoutFragment personaliseWorkoutFragment = new PersonaliseWorkoutFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, personaliseWorkoutFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}
