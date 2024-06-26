package com.example.diabfitapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder> {
    private List<FoodItem> foodItems;

    public FoodItemAdapter(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);
        holder.foodNameTextView.setText(foodItem.getName());

        // Add click listener if needed
        holder.itemView.setOnClickListener(v -> {
            // Handle item click
            // For example, you can open a detail view or perform an action
        });
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
        }
    }
}
