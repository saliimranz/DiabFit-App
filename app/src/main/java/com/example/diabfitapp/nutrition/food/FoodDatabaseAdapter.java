package com.example.diabfitapp.nutrition.food;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.ArrayList;
import android.util.Log;
import android.os.Handler;
import android.os.Looper;
import jp.wasabeef.blurry.Blurry;
import androidx.fragment.app.FragmentActivity;

import com.example.diabfitapp.main.MainActivity;



import com.example.diabfitapp.R;

public class FoodDatabaseAdapter extends RecyclerView.Adapter<FoodDatabaseAdapter.FoodViewHolder> {

    private List<FoodItem> foodItemList;
    private List<FoodItem> foodItemListFull;
    private Context context;

    public FoodDatabaseAdapter(List<FoodItem> foodItemList) {
        this.foodItemList = foodItemList;
        this.foodItemListFull = new ArrayList<>(foodItemList);
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        context = parent.getContext();
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem foodItem = foodItemList.get(position);
        holder.foodNameTextView.setText(foodItem.getName());
        holder.glycemicIndexTextView.setText("GI: " + foodItem.getGlycemicIndex());
        holder.carbsPer100gTextView.setText("Carbs/100g: " + foodItem.getCarbsPer100g());
        holder.sizeOfServingTextView.setText("Serving size: " + foodItem.getSizeOfServing() + "g");

        // Set click listener for the add button
        holder.addButton.setOnClickListener(v -> {
            // Open dialog with food item details
            showAddServingDialog(foodItem);
        });

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

    // Inside your FoodDatabaseAdapter
    private void showAddServingDialog(FoodItem item) {

        Blurry.with(context).radius(25).onto((ViewGroup) ((FragmentActivity) context).findViewById(R.id.fragment_container));

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_serving);

        TextView nameTextView = dialog.findViewById(R.id.nameTextView);
        TextView carbsPer100gTextView = dialog.findViewById(R.id.carbsPer100gTextView);
        TextView servingSizeTextView = dialog.findViewById(R.id.servingSizeTextView);
        TextView glycemicIndexTextView = dialog.findViewById(R.id.glycemicIndexTextView);
        Button addButton = dialog.findViewById(R.id.addButton);
        Button incrementButton = dialog.findViewById(R.id.incrementButton);
        Button decrementButton = dialog.findViewById(R.id.decrementButton);
        EditText servingsInput = dialog.findViewById(R.id.servingsInput);

        nameTextView.setText(item.getName());
        carbsPer100gTextView.setText(String.valueOf(item.getCarbsPer100g()));
        servingSizeTextView.setText(String.valueOf(item.getSizeOfServing()));
        glycemicIndexTextView.setText(String.valueOf(item.getGlycemicIndex()));

        final int[] servingsEaten = {0};

        incrementButton.setOnClickListener(v -> {
            servingsEaten[0]++;
            servingsInput.setText(String.valueOf(servingsEaten[0]));
        });

        decrementButton.setOnClickListener(v -> {
            if (servingsEaten[0] > 0) {
                servingsEaten[0]--;
                servingsInput.setText(String.valueOf(servingsEaten[0]));
            }
        });

        addButton.setOnClickListener(v -> {

            if (context instanceof MainActivity) {
                Log.d("FoodDatabaseAdapter", "servingsEaten[0]: " + servingsEaten[0]);
                ((MainActivity) context).addFoodToCGCountFragment(item, servingsEaten[0]);
            }
            // Remove blur effect and close dialog
            Blurry.delete((ViewGroup) ((FragmentActivity) context).findViewById(R.id.fragment_container));
            dialog.dismiss();

            // Navigate back to CarbsCountingFragment
            ((FragmentActivity) context).getSupportFragmentManager().popBackStack();
        });

        dialog.setOnDismissListener(dialogInterface -> {
            // Remove blur effect when dialog is dismissed
            Blurry.delete((ViewGroup) ((FragmentActivity) context).findViewById(R.id.fragment_container));
        });

        dialog.show();
    }


    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView;
        TextView glycemicIndexTextView;
        TextView carbsPer100gTextView;
        TextView sizeOfServingTextView;
        ImageView addButton;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.food_name);
            glycemicIndexTextView = itemView.findViewById(R.id.glycemic_index);
            carbsPer100gTextView = itemView.findViewById(R.id.carbs_per_100g);
            sizeOfServingTextView = itemView.findViewById(R.id.size_of_serving);
            addButton = itemView.findViewById(R.id.add_button);
        }
    }


}
