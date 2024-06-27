package com.example.diabfitapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FoodDatabaseAdapter extends RecyclerView.Adapter<FoodDatabaseAdapter.FoodViewHolder> {

    private List<FoodItem> foodItemList;

    public FoodDatabaseAdapter(List<FoodItem> foodItemList) {
        this.foodItemList = foodItemList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem foodItem = foodItemList.get(position);
        holder.foodNameTextView.setText(foodItem.getName());
        holder.caloriesTextView.setText(foodItem.getCalories() + " kcal");
        holder.foodImageView.setImageResource(foodItem.getImageResourceId());
    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView;
        TextView caloriesTextView;
        ImageView foodImageView;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.food_name);
            caloriesTextView = itemView.findViewById(R.id.calories);
            foodImageView = itemView.findViewById(R.id.food_image);
        }
    }
}
