package com.example.diabfitapp.nutrition.meal;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diabfitapp.R;

import java.util.List;

public class MealPlannerAdapter extends RecyclerView.Adapter<MealPlannerAdapter.MealViewHolder> {

    private List<MealItem> mealList;

    public MealPlannerAdapter(List<MealItem> mealList) {
        this.mealList = mealList;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        MealItem meal = mealList.get(position);
        holder.mealTypeTextView.setText(meal.getMealType());
        holder.mealTitleTextView.setText(meal.getTitle());
        holder.mealCarbsTextView.setText("Carbs: " + meal.getCarbs() + "g");
        holder.mealCaloriesTextView.setText("Calories: " + meal.getCalories());
        holder.mealServingSizeTextView.setText("Serving Size: " + meal.getServingSize());

        switch (meal.getMealType().toLowerCase()) {
            case "breakfast":
                holder.mealImageView.setImageResource(R.drawable.breakfast_image);
                break;
            case "lunch":
                holder.mealImageView.setImageResource(R.drawable.lunch_image);
                break;
            case "dinner":
                holder.mealImageView.setImageResource(R.drawable.dinner_image);
                break;
        }

        // Check if the meal has been marked as eaten
        SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        boolean isEaten = sharedPreferences.getBoolean("meal_eaten_" + meal.getTitle(), false);

        if (isEaten) {
            holder.itemView.setAlpha(0.5f); // Apply grey tint
            holder.eatenTickImageView.setVisibility(View.VISIBLE); // Show green tick
        } else {
            holder.itemView.setAlpha(1.0f); // Remove grey tint
            holder.eatenTickImageView.setVisibility(View.GONE); // Hide green tick
        }

        holder.itemView.setOnClickListener(v -> {
            FragmentActivity activity = (FragmentActivity) v.getContext();
            MealDetailFragment fragment = MealDetailFragment.newInstance(meal);
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView mealImageView;
        ImageView eatenTickImageView;
        TextView mealTitleTextView;
        TextView mealCarbsTextView;
        TextView mealCaloriesTextView;
        TextView mealServingSizeTextView;
        TextView mealTypeTextView;

        MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImageView = itemView.findViewById(R.id.meal_image);
            eatenTickImageView = itemView.findViewById(R.id.eaten_tick);
            mealTitleTextView = itemView.findViewById(R.id.meal_title);
            mealCarbsTextView = itemView.findViewById(R.id.meal_carbs);
            mealCaloriesTextView = itemView.findViewById(R.id.meal_calories);
            mealServingSizeTextView = itemView.findViewById(R.id.meal_serving_size);
            mealTypeTextView = itemView.findViewById(R.id.meal_type);
        }
    }
}
