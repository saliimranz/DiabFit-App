package com.example.diabfitapp.nutrition.meal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
import android.content.Context;

import com.example.diabfitapp.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.ArrayList;

public class MealPlannerFragment extends Fragment {

    private RecyclerView recyclerView;
    private MealPlannerAdapter adapter;
    private List<MealItem> recommendedMeals;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_planner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Meal Planning");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        long lastUpdate = sharedPreferences.getLong("lastMealRecommendation", 0);
        long currentTime = System.currentTimeMillis();
        long oneDayMillis = 24 * 60 * 60 * 1000;

        if (currentTime - lastUpdate > oneDayMillis) {
            // More than a day has passed since the last recommendation
            try {
                JSONArray meals = MealData.loadJSONFromAsset(getContext());
                float targetCarbs = sharedPreferences.getFloat("targetCarbs", 150.0f); // Default is 150.0f if not set
                recommendedMeals = MealRecommender.recommendMeals(meals, targetCarbs);

                // Save the new recommendations and timestamp
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("recommendedMeals", MealItem.toJsonArray(recommendedMeals).toString());
                editor.putLong("lastMealRecommendation", currentTime);
                editor.apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // Less than a day has passed, load the saved recommendations
            String savedMeals = sharedPreferences.getString("recommendedMeals", "[]");
            try {
                JSONArray savedMealsArray = new JSONArray(savedMeals);
                recommendedMeals = MealItem.fromJsonArray(savedMealsArray);
            } catch (JSONException e) {
                e.printStackTrace();
                recommendedMeals = new ArrayList<>();
            }
        }

        adapter = new MealPlannerAdapter(recommendedMeals);
        recyclerView.setAdapter(adapter);
    }
}
