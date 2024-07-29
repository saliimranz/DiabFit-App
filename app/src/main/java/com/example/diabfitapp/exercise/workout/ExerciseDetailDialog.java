package com.example.diabfitapp.exercise.workout;

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

import com.bumptech.glide.Glide;
import com.example.diabfitapp.R;
import com.example.diabfitapp.exercise.progress.ProgressDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExerciseDetailDialog extends Dialog {

    private Exercise exercise;
    private int sets = 1;

    public ExerciseDetailDialog(@NonNull Context context, Exercise exercise) {
        super(context);
        this.exercise = exercise;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_exercise_detail, null);
        setContentView(view);

        ImageView gifImageView = view.findViewById(R.id.gif_image_view);
        TextView nameTextView = view.findViewById(R.id.name_text_view);
        TextView bodyPartTextView = view.findViewById(R.id.body_part_text_view);
        TextView equipmentTextView = view.findViewById(R.id.equipment_text_view);
        TextView targetTextView = view.findViewById(R.id.target_text_view);
        TextView secondaryMusclesTextView = view.findViewById(R.id.secondary_muscles_text_view);
        TextView instructionsTextView = view.findViewById(R.id.instructions_text_view);
        Button minusButton = view.findViewById(R.id.minus_button);
        Button plusButton = view.findViewById(R.id.plus_button);
        Button addButton = view.findViewById(R.id.add_button);
        TextView setsTextView = view.findViewById(R.id.sets_text_view);

        Glide.with(getContext()).load(exercise.getGifUrl()).into(gifImageView);
        nameTextView.setText(exercise.getName());
        bodyPartTextView.setText("Body Part: " + exercise.getBodyPart());
        equipmentTextView.setText("Equipment: " + exercise.getEquipment());
        targetTextView.setText("Target: " + exercise.getTarget());
        String secondaryMuscles = exercise.getSecondaryMuscles();
        secondaryMusclesTextView.setText(formatList("Secondary Muscles", secondaryMuscles));
        String instructions = exercise.getInstructions();
        instructionsTextView.setText(formatList("Instructions", instructions));

        setsTextView.setText(String.valueOf(sets));

        minusButton.setOnClickListener(v -> {
            if (sets > 1) {
                sets--;
                setsTextView.setText(String.valueOf(sets));
            }
        });

        plusButton.setOnClickListener(v -> {
            sets++;
            setsTextView.setText(String.valueOf(sets));
        });

        addButton.setOnClickListener(v -> {
            // Save exercise to database
            saveExerciseToDatabase();
            Toast.makeText(getContext(), "Exercise added", Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }

    private void saveExerciseToDatabase() {
        ProgressDatabaseHelper dbHelper = new ProgressDatabaseHelper(getContext());
        exercise.setSets(sets);

        // Get current date
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        dbHelper.addExercise(exercise, currentDate);
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
