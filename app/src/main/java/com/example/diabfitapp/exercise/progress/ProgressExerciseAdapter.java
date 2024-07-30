package com.example.diabfitapp.exercise.progress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diabfitapp.R;
import com.example.diabfitapp.exercise.workout.Exercise;

import java.util.List;

public class ProgressExerciseAdapter extends RecyclerView.Adapter<ProgressExerciseAdapter.ViewHolder> {

    private List<Exercise> exerciseList;
    private Context context;
    private boolean showCompletedSets;

    public ProgressExerciseAdapter(List<Exercise> exerciseList, boolean showCompletedSets) {
        this.exerciseList = exerciseList;
        this.showCompletedSets = showCompletedSets;
    }

    public void updateData(List<Exercise> newExerciseList) {
        this.exerciseList.clear();
        this.exerciseList.addAll(newExerciseList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_progress_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);

        holder.nameTextView.setText(exercise.getName());
        holder.setsTextView.setText("Sets: " + exercise.getCompletedSets() + "/" + exercise.getSets());

        Glide.with(context).load(exercise.getGifUrl()).into(holder.gifImageView);

        // Update item view based on completed sets
        if (exercise.getCompletedSets() >= exercise.getSets()) {
            holder.itemView.setEnabled(false);
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.greyish));
            holder.tickImageView.setVisibility(View.VISIBLE);
        } else {
            holder.itemView.setEnabled(true);
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.tickImageView.setVisibility(View.GONE);
        }

        // Make items clickable only in StartWorkoutFragment
        if (showCompletedSets) {
            if (exercise.getCompletedSets() < exercise.getSets()) {
                holder.itemView.setOnClickListener(v -> {
                    WorkoutDetailDialogFragment dialogFragment = WorkoutDetailDialogFragment.newInstance(exercise, this, position);
                    if (context instanceof FragmentActivity) {
                        dialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "ExerciseDetailsDialogFragment");
                    } else {
                        Toast.makeText(context, "Context is not a FragmentActivity", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                holder.itemView.setOnClickListener(null);
            }
        } else {
            holder.itemView.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public void updateItem(int position, Exercise updatedExercise) {
        exerciseList.set(position, updatedExercise);
        notifyItemChanged(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView gifImageView;
        TextView nameTextView;
        TextView setsTextView;
        ImageView tickImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gifImageView = itemView.findViewById(R.id.gif_image_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            setsTextView = itemView.findViewById(R.id.sets_text_view);
            tickImageView = itemView.findViewById(R.id.tick_image_view);
        }
    }
}
