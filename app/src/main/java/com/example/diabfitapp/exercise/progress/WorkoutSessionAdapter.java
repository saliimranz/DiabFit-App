package com.example.diabfitapp.exercise.progress;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabfitapp.R;

import java.util.List;

public class WorkoutSessionAdapter extends RecyclerView.Adapter<WorkoutSessionAdapter.WorkoutSessionViewHolder> {

    private List<WorkoutSession> workoutSessions;

    public WorkoutSessionAdapter(List<WorkoutSession> workoutSessions) {
        this.workoutSessions = workoutSessions;
    }

    @NonNull
    @Override
    public WorkoutSessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout_session, parent, false);
        return new WorkoutSessionViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull WorkoutSessionViewHolder holder, int position) {
        WorkoutSession session = workoutSessions.get(position);
        holder.dateTextView.setText(session.getDay());
        holder.timeSpentTextView.setText("Time: " + formatTime(session.getTimeSpent()));
        String carbs = String.format("%.1f", session.getCarbsBurnt());
        holder.carbsBurntTextView.setText("Carbs Burnt: " + carbs);

        String formattedPercentage = String.format("%.1f", session.getWorkoutPercentage());
        holder.workoutPercentageTextView.setText("Completion: " + formattedPercentage + "%");

        // Set text color based on percentage
        float percentage = (float) session.getWorkoutPercentage();
        int color;
        if (percentage > 85) {
            color = holder.itemView.getContext().getResources().getColor(R.color.green); // Green
        } else if (percentage >= 40) {
            color = holder.itemView.getContext().getResources().getColor(R.color.yellow); // Yellow
        } else {
            color = holder.itemView.getContext().getResources().getColor(R.color.red); // Red
        }

        holder.dateTextView.setTextColor(color);
        holder.timeSpentTextView.setTextColor(color);
        holder.carbsBurntTextView.setTextColor(color);
        holder.workoutPercentageTextView.setTextColor(color);

        // Clear previous views
        holder.exercisesContainer.removeAllViews();

        // Populate exercises and sets
        List<String> exerciseNames = session.getExerciseNames();
        List<Integer> completedSets = session.getCompletedSets();
        List<Integer> targetSets = session.getTargetSets();

        for (int i = 0; i < exerciseNames.size(); i++) {
            String exerciseName = exerciseNames.get(i);
            int completed = completedSets.get(i);
            int target = targetSets.get(i);

            TextView exerciseTextView = new TextView(holder.itemView.getContext());
            exerciseTextView.setText((i + 1) + ". " + exerciseName + ": " + completed + "/" + target);
            exerciseTextView.setTextColor(color); // Set color for exercise details
            holder.exercisesContainer.addView(exerciseTextView);
        }
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    @Override
    public int getItemCount() {
        return workoutSessions.size();
    }

    static class WorkoutSessionViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView timeSpentTextView;
        TextView carbsBurntTextView;
        TextView workoutPercentageTextView;
        LinearLayout exercisesContainer;

        public WorkoutSessionViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            timeSpentTextView = itemView.findViewById(R.id.time_spent_text_view);
            carbsBurntTextView = itemView.findViewById(R.id.carbs_burnt_text_view);
            workoutPercentageTextView = itemView.findViewById(R.id.workout_percentage_text_view);
            exercisesContainer = itemView.findViewById(R.id.exercises_container);
        }
    }
}
