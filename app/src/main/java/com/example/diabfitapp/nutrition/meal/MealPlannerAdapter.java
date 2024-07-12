package com.example.diabfitapp.nutrition.meal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
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
        holder.mealImageView.setImageResource(meal.getImageResId());
        holder.mealTitleTextView.setText(meal.getTitle());
        holder.mealDetailsTextView.setText(meal.getDetails());
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView mealImageView;
        TextView mealTitleTextView;
        TextView mealDetailsTextView;

        MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImageView = itemView.findViewById(R.id.meal_image);
            mealTitleTextView = itemView.findViewById(R.id.meal_title);
            mealDetailsTextView = itemView.findViewById(R.id.meal_details);
        }
    }
}
