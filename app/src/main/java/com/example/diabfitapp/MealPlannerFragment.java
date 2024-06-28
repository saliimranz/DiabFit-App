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

public class MealPlannerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_planner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MealPlannerAdapter(getDummyData()));
    }

    private List<MealItem> getDummyData() {
        List<MealItem> meals = new ArrayList<>();
        meals.add(new MealItem("Breakfast", "Egg-in-the-hole", R.drawable.breakfast_image));
        meals.add(new MealItem("Lunch", "Korean Eggplant", R.drawable.lunch_image));
        meals.add(new MealItem("Dinner", "Spicy Chicken Stew", R.drawable.dinner_image));
        return meals;
    }
}
