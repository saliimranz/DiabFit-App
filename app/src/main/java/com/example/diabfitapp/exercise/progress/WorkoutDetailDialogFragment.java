package com.example.diabfitapp.exercise.progress;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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

public class WorkoutDetailDialogFragment extends DialogFragment {

    private static final String ARG_EXERCISE = "exercise";
    private static final String ARG_POSITION = "position";

    private Exercise exercise;
    private ProgressDatabaseHelper databaseHelper;
    private int completedSets;
    private ProgressExerciseAdapter adapter;
    private int position;

    public static WorkoutDetailDialogFragment newInstance(Exercise exercise, ProgressExerciseAdapter adapter, int position) {
        WorkoutDetailDialogFragment fragment = new WorkoutDetailDialogFragment();
        fragment.adapter = adapter;
        fragment.position = position;
        Bundle args = new Bundle();
        args.putSerializable(ARG_EXERCISE, exercise);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        databaseHelper = new ProgressDatabaseHelper(context);
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
                if (completedSets == exercise.getSets()) {
                    Toast.makeText(requireContext(), "Reached today's goal!", Toast.LENGTH_SHORT).show();
                    // Notify the adapter about the change
                    adapter.updateItem(position, exercise);
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
            }
        });

        builder.setView(view)
                .setPositiveButton("Close", (dialog, id) -> dialog.dismiss());

        return builder.create();
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
