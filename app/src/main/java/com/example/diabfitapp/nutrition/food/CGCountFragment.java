package com.example.diabfitapp.nutrition.food;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabfitapp.R;
import com.example.diabfitapp.nutrition.food.CircularProgressView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CGCountFragment extends Fragment {

    private CircularProgressView circularProgressView;
    private TextView carbsTextView;
    private TextView overTargetMessage;
    private float totalCarbs = 230;
    private float targetCarbs = 150; // Example target

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_c_g_count, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Carbs and Glycemic Counter");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        circularProgressView = view.findViewById(R.id.circularProgressView);
        carbsTextView = view.findViewById(R.id.carbsTextView);
        overTargetMessage = view.findViewById(R.id.overTargetMessage);

        RecyclerView foodEatenList = view.findViewById(R.id.foodEatenList);
        foodEatenList.setLayoutManager(new LinearLayoutManager(requireContext()));
        FoodEatenAdapter adapter = new FoodEatenAdapter(getSampleData());
        foodEatenList.setAdapter(adapter);

        FloatingActionButton fabAddFood = view.findViewById(R.id.fabAddFood);
        fabAddFood.setOnClickListener(v -> {
            // Handle adding new food item
            addFoodItem();
        });

        updateProgress();
    }

    private List<FoodEatenItem> getSampleData() {
        List<FoodEatenItem> items = new ArrayList<>();
        // Add sample data
        items.add(new FoodEatenItem("Apple", 14, 5, 40, 99, 20));
        items.add(new FoodEatenItem("Banana", 23, 15, 60, 22, 10));
        items.add(new FoodEatenItem("Bread", 50, 2, 70, 236,5));
        return items;
    }

    private void addFoodItem() {
        // Example function to add a food item and update the progress
        totalCarbs += 30; // Example added carbs
        updateProgress();
    }

    private void updateProgress() {
        circularProgressView.setProgress(totalCarbs, targetCarbs);
        carbsTextView.setText(String.format("Today: %.1f / %.1f", totalCarbs, targetCarbs));
        if (totalCarbs > targetCarbs) {
            circularProgressView.setOverTarget(true);
            overTargetMessage.setVisibility(View.VISIBLE);
        } else {
            circularProgressView.setOverTarget(false);
            overTargetMessage.setVisibility(View.GONE);
        }
    }
}
