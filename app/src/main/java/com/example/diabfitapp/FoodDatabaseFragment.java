package com.example.diabfitapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class FoodDatabaseFragment extends Fragment {

    public static FoodDatabaseFragment newInstance() {
        return new FoodDatabaseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_database, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new FoodDatabaseAdapter(getDummyData()));
    }

    private List<FoodItem> getDummyData() {
        List<FoodItem> foodItems = new ArrayList<>();
        foodItems.add(new FoodItem("Egg, boiled", 78, R.drawable.ic_egg_boiled));
        foodItems.add(new FoodItem("Scrambled Eggs", 102, R.drawable.ic_scrambled_eggs));
        foodItems.add(new FoodItem("Apple", 96, R.drawable.ic_apple));
        foodItems.add(new FoodItem("Avocado", 456, R.drawable.ic_avocado));
        foodItems.add(new FoodItem("Egg, boiled", 78, R.drawable.ic_egg_boiled));
        foodItems.add(new FoodItem("Scrambled Eggs", 102, R.drawable.ic_scrambled_eggs));
        foodItems.add(new FoodItem("Apple", 96, R.drawable.ic_apple));
        foodItems.add(new FoodItem("Avocado", 456, R.drawable.ic_avocado));
        foodItems.add(new FoodItem("Egg, boiled", 78, R.drawable.ic_egg_boiled));
        foodItems.add(new FoodItem("Scrambled Eggs", 102, R.drawable.ic_scrambled_eggs));
        foodItems.add(new FoodItem("Apple", 96, R.drawable.ic_apple));
        foodItems.add(new FoodItem("Avocado", 456, R.drawable.ic_avocado));
        foodItems.add(new FoodItem("Egg, boiled", 78, R.drawable.ic_egg_boiled));
        foodItems.add(new FoodItem("Scrambled Eggs", 102, R.drawable.ic_scrambled_eggs));
        foodItems.add(new FoodItem("Apple", 96, R.drawable.ic_apple));
        foodItems.add(new FoodItem("Avocado", 456, R.drawable.ic_avocado));
        foodItems.add(new FoodItem("Egg, boiled", 78, R.drawable.ic_egg_boiled));
        foodItems.add(new FoodItem("Scrambled Eggs", 102, R.drawable.ic_scrambled_eggs));
        foodItems.add(new FoodItem("Apple", 96, R.drawable.ic_apple));
        foodItems.add(new FoodItem("Avocado", 456, R.drawable.ic_avocado));
        // Add more items as needed
        return foodItems;
    }
}
