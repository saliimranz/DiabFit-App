package com.example.diabfitapp.exercise.progress;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.diabfitapp.R;
import com.example.diabfitapp.exercise.workout.Exercise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WorkoutDetailDialogFragment extends DialogFragment {

    private static final String ARG_EXERCISE = "exercise";
    private static final String ARG_POSITION = "position";

    private Exercise exercise;
    private ProgressDatabaseHelper databaseHelper;
    private int completedSets;
    private ProgressExerciseAdapter adapter;
    private int position;
    private StartWorkoutFragment startWorkoutFragment; // Reference to the StartWorkoutFragment

    public static WorkoutDetailDialogFragment newInstance(Exercise exercise, ProgressExerciseAdapter adapter, int position) {
        WorkoutDetailDialogFragment fragment = new WorkoutDetailDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EXERCISE, exercise);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        fragment.adapter = adapter;
        fragment.position = position;
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        databaseHelper = new ProgressDatabaseHelper(context);

        // Attempt to find the StartWorkoutFragment
        if (getParentFragment() instanceof StartWorkoutFragment) {
            startWorkoutFragment = (StartWorkoutFragment) getParentFragment();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            exercise = (Exercise) getArguments().getSerializable(ARG_EXERCISE);
            position = getArguments().getInt(ARG_POSITION);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_workout_details, null);

        TextView nameTextView = view.findViewById(R.id.name_text_view);
        TextView bodyPartTextView = view.findViewById(R.id.body_part_text_view);
        TextView equipmentTextView = view.findViewById(R.id.equipment_text_view);
        ImageView gifImageView = view.findViewById(R.id.gif_image_view);
        TextView targetTextView = view.findViewById(R.id.target_text_view);
        TextView secondaryMusclesTextView = view.findViewById(R.id.secondary_muscles_text_view);
        TextView instructionsTextView = view.findViewById(R.id.instructions_text_view);
        TextView setsTextView = view.findViewById(R.id.sets_text_view);
        TextView completedSetsTextView = view.findViewById(R.id.completed_sets_text_view);
        Button increaseButton = view.findViewById(R.id.increase_button);
        Button decreaseButton = view.findViewById(R.id.decrease_button);

        nameTextView.setText(exercise.getName());
        bodyPartTextView.setText("Body Part: " + exercise.getBodyPart());
        equipmentTextView.setText("Equipment: " + exercise.getEquipment());
        Glide.with(requireContext()).load(exercise.getGifUrl()).into(gifImageView);
        targetTextView.setText("Target: " + exercise.getTarget());
        setsTextView.setText("Sets: " + exercise.getSets());
        completedSets = exercise.getCompletedSets();
        completedSetsTextView.setText(String.valueOf(completedSets));

        String secondaryMuscles = exercise.getSecondaryMuscles();
        secondaryMusclesTextView.setText(formatList("Secondary Muscles", secondaryMuscles));
        String instructions = exercise.getInstructions();
        instructionsTextView.setText(formatList("Instructions", instructions));

        increaseButton.setOnClickListener(v -> {
            if (completedSets < exercise.getSets()) {
                completedSets++;
                completedSetsTextView.setText(String.valueOf(completedSets));
                exercise.setCompletedSets(completedSets);
                databaseHelper.updateExercise(exercise);
                Log.d("WorkoutDetailDialog", "Incremented: " + completedSets);
                if (completedSets == exercise.getSets()) {
                    Toast.makeText(requireContext(), "Reached today's goal!", Toast.LENGTH_SHORT).show();
                    adapter.updateItem(position, exercise);
                }
                updateWorkoutSession();
                if (startWorkoutFragment != null) {
                    startWorkoutFragment.refreshRecyclerView();
                    Log.d("WorkoutDetailDialog", "RecyclerView refreshed after increment.");
                }
            }
        });

        decreaseButton.setOnClickListener(v -> {
            if (completedSets > 0) {
                completedSets--;
                completedSetsTextView.setText(String.valueOf(completedSets));
                exercise.setCompletedSets(completedSets);
                databaseHelper.updateExercise(exercise);
                // Notify the adapter about the change
                adapter.updateItem(position, exercise);
                updateWorkoutSession();
                // Refresh the StartWorkoutFragment's RecyclerView
                if (startWorkoutFragment != null) {
                    startWorkoutFragment.refreshRecyclerView();
                }
            }
        });

        builder.setView(view)
                .setPositiveButton("Close", (dialog, id) -> dialog.dismiss());

        return builder.create();
    }

    private void updateWorkoutSession() {
        if (startWorkoutFragment == null) return;

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        long timeInMilliseconds = SystemClock.uptimeMillis() - startWorkoutFragment.startTime;
        int timeSpent = (int) (timeInMilliseconds / 1000); // Time in seconds
        double carbsBurnt = calculateCarbsBurnt(timeSpent);
        double workoutPercentage = calculateWorkoutPercentage(startWorkoutFragment.progressExerciseList);

        List<String> exerciseNames = new ArrayList<>();
        List<Integer> completedSetsList = new ArrayList<>();
        List<Integer> targetSets = new ArrayList<>();

        for (Exercise exercise : startWorkoutFragment.progressExerciseList) {
            exerciseNames.add(exercise.getName());
            completedSetsList.add(exercise.getCompletedSets());
            targetSets.add(exercise.getSets());
        }

        WorkoutSession session = new WorkoutSession(0, currentDate, timeSpent, carbsBurnt, workoutPercentage, exerciseNames, completedSetsList, targetSets);
        databaseHelper.insertOrUpdateWorkoutSession(session);
    }

    private double calculateCarbsBurnt(int timeSpent) {
        // Example logic to calculate carbs burnt based on time spent
        return timeSpent * 0.1;
    }

    private double calculateWorkoutPercentage(List<Exercise> exercises) {
        int totalSets = 0;
        int completedSets = 0;

        // Iterate through each exercise to sum up total and completed sets
        for (Exercise exercise : exercises) {
            totalSets += exercise.getSets();
            completedSets += exercise.getCompletedSets();
        }

        // Calculate and return the percentage of completed sets
        return totalSets == 0 ? 0 : (double) completedSets / totalSets * 100;
    }

    private String formatList(String title, String listString) {
        StringBuilder formatted = new StringBuilder(title + ":\n");
        String[] items = listString.replace("[", "").replace("]", "").replace("\"", "").split(",");
        for (String item : items) {
            formatted.append(item.trim()).append("\n");
        }
        return formatted.toString();
    }
}
