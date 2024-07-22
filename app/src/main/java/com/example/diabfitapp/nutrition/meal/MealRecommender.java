package com.example.diabfitapp.nutrition.meal;

import com.example.diabfitapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MealRecommender {
    public static List<MealItem> recommendMeals(JSONArray meals, float targetCarbs) throws JSONException {
        List<MealItem> breakfastList = new ArrayList<>();
        List<MealItem> lunchList = new ArrayList<>();
        List<MealItem> dinnerList = new ArrayList<>();

        for (int i = 0; i < meals.length(); i++) {
            JSONObject meal = meals.getJSONObject(i);
            String mealType = meal.getString("Meal_Type");
            float carbs = Float.parseFloat(meal.getString("Carbs").replace("g", ""));

            MealItem mealItem = new MealItem(
                    meal.getString("Name"),
                    meal.getString("Recipe"),
                    mealType,
                    carbs,
                    meal.getInt("Calories"),
                    meal.getJSONObject("Ingredients").toString(),
                    meal.getString("Serving Size"),
                    getImageResIdForMealType(mealType) // Replace with actual image logic
            );

            switch (mealType) {
                case "Breakfast":
                    breakfastList.add(mealItem);
                    break;
                case "Lunch":
                    lunchList.add(mealItem);
                    break;
                case "Dinner":
                    dinnerList.add(mealItem);
                    break;
            }
        }

        List<MealItem> recommendedMeals = new ArrayList<>();
        Random random = new Random();

        if (targetCarbs == 0 || targetCarbs < 15) {
            return Collections.emptyList(); // Ask user to increase target carbs
        } else {
            MealItem breakfast = breakfastList.get(random.nextInt(breakfastList.size()));
            recommendedMeals.add(breakfast);

            if (targetCarbs <= breakfast.getCarbs() + 15) {
                MealItem dinner = dinnerList.get(random.nextInt(dinnerList.size()));
                recommendedMeals.add(dinner);
            } else {
                MealItem lunch = lunchList.get(random.nextInt(lunchList.size()));
                recommendedMeals.add(lunch);

                if (targetCarbs > breakfast.getCarbs() + lunch.getCarbs()) {
                    MealItem dinner = dinnerList.get(random.nextInt(dinnerList.size()));
                    recommendedMeals.add(dinner);
                }
            }
        }

        return recommendedMeals;
    }

    private static int getImageResIdForMealType(String mealType) {
        switch (mealType) {
            case "Breakfast":
                return R.drawable.breakfast_image;
            case "Lunch":
                return R.drawable.lunch_image;
            case "Dinner":
                return R.drawable.dinner_image;
            default:
                return R.drawable.ic_apple; // Use a default placeholder image if needed
        }
    }
}
