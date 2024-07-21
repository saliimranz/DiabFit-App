package com.example.diabfitapp.nutrition.food;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diabfitapp.R;
import java.util.List;

public class FoodEatenAdapter extends RecyclerView.Adapter<FoodEatenAdapter.ViewHolder> {

    private List<FoodEatenItem> items;

    public FoodEatenAdapter(List<FoodEatenItem> items) {
        this.items = items;
    }

    public List<FoodEatenItem> getItems() {
        return items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodeaten_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodEatenItem item = items.get(position);
        holder.name.setText(item.getName());
        holder.carbsPer100g.setText("Carbs per 100g: " + String.valueOf(item.getCarbsPer100g()));
        holder.servings.setText("Amount Eaten: " + item.getServings() + "g");
        holder.glycemicIndex.setText("Glycemic Index: " + String.valueOf(item.getGlycemicIndex()));
        holder.carbsIntake.setText("Carbs Intake: " + String.valueOf(item.getCarbsIntake()));
        holder.servingSize.setText("Serving Size: " + item.getServingsSize() + "g");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, carbsPer100g, servings, glycemicIndex, carbsIntake, servingSize;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.food_name);
            carbsPer100g = itemView.findViewById(R.id.carbs_per_100g);
            servings = itemView.findViewById(R.id.servings_eaten);
            glycemicIndex = itemView.findViewById(R.id.glycemic_index);
            carbsIntake = itemView.findViewById(R.id.carbs_intake);
            servingSize = itemView.findViewById(R.id.serving_size);
        }
    }
}
