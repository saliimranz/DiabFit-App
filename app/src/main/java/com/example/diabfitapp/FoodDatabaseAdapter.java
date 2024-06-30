package com.example.diabfitapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.ArrayList;
import android.util.Log;

import android.os.Handler;
import android.os.Looper;


public class FoodDatabaseAdapter extends RecyclerView.Adapter<FoodDatabaseAdapter.FoodViewHolder> {

    private List<FoodItem> foodItemList;
    private List<FoodItem> foodItemListFull;

    public FoodDatabaseAdapter(List<FoodItem> foodItemList) {
        this.foodItemList = foodItemList;
        this.foodItemListFull = new ArrayList<>(foodItemList);
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
        holder.glycemicIndexTextView.setText("GI: " + foodItem.getGlycemicIndex());
        holder.carbsPer100gTextView.setText("Carbs/100g: " + foodItem.getCarbsPer100g());
        holder.sizeOfServingTextView.setText("Serving size: " + foodItem.getSizeOfServing() + "g");
        Log.d("FoodDatabaseAdapter", "Binding item: " + foodItem.getName());
    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }

    public void filter(String text) {
        foodItemList.clear();
        if (text.isEmpty()) {
            foodItemList.addAll(foodItemListFull);
        } else {
            String searchText = text.toLowerCase();
            for (FoodItem item : foodItemListFull) {
                if (item.getName().toLowerCase().contains(searchText)) {
                    foodItemList.add(item);
                }
            }
        }
        Log.d("FoodDatabaseAdapter", "Filtered list size: " + foodItemList.size());
        new Handler(Looper.getMainLooper()).post(this::notifyDataSetChanged);

    }

    public void addItem(FoodItem item) {
        foodItemList.add(item);
        foodItemListFull.add(item);
        notifyDataSetChanged();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView;
        TextView glycemicIndexTextView;
        TextView carbsPer100gTextView;
        TextView sizeOfServingTextView;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.food_name);
            glycemicIndexTextView = itemView.findViewById(R.id.glycemic_index);
            carbsPer100gTextView = itemView.findViewById(R.id.carbs_per_100g);
            sizeOfServingTextView = itemView.findViewById(R.id.size_of_serving);
        }
    }
}
