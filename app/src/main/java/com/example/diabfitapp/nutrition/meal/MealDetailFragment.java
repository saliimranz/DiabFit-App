package com.example.diabfitapp.nutrition.meal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.example.diabfitapp.R;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.Iterator;

import android.content.SharedPreferences;
import android.content.Context;

public class MealDetailFragment extends Fragment {

    private static final String ARG_MEAL = "meal";

    public static MealDetailFragment newInstance(MealItem mealItem) {
        MealDetailFragment fragment = new MealDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEAL, mealItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Meal Details");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        MealItem mealItem = (MealItem) getArguments().getSerializable(ARG_MEAL);

        ImageView mealImageView = view.findViewById(R.id.meal_image);
        TextView mealTypeTextView = view.findViewById(R.id.meal_type);
        TextView mealTitleTextView = view.findViewById(R.id.meal_title);
        TextView mealCarbsTextView = view.findViewById(R.id.meal_carbs);
        TextView mealCaloriesTextView = view.findViewById(R.id.meal_calories);
        TextView mealServingSizeTextView = view.findViewById(R.id.meal_serving_size);
        TextView mealIngredientsTextView = view.findViewById(R.id.meal_ingredients);
        TextView mealRecipeTitleTextView = view.findViewById(R.id.meal_recipe_title);
        TextView mealRecipeTextView = view.findViewById(R.id.meal_recipe);

        Button eatenButton = view.findViewById(R.id.btn_eaten);

        mealImageView.setImageResource(mealItem.getImageResId());
        mealTypeTextView.setText(mealItem.getMealType());
        mealTitleTextView.setText(mealItem.getTitle());
        mealCarbsTextView.setText(String.format("Carbs: %s", mealItem.getCarbs()));
        mealCaloriesTextView.setText(String.format("Calories: %d", mealItem.getCalories()));
        mealServingSizeTextView.setText(String.format("Serving Size: %s", mealItem.getServingSize()));

        mealRecipeTitleTextView.setVisibility(View.VISIBLE);
        mealRecipeTextView.setText(mealItem.getDetails());

        try {
            JSONObject ingredientsJson = new JSONObject(mealItem.getIngredients());
            StringBuilder ingredientsBuilder = new StringBuilder();
            ingredientsBuilder.append("Ingredients:\n");
            Iterator<String> keys = ingredientsJson.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = ingredientsJson.getString(key);
                ingredientsBuilder.append(key).append(": ").append(value).append("\n");
            }
            mealIngredientsTextView.setText(ingredientsBuilder.toString());
        } catch (JSONException e) {
            mealIngredientsTextView.setText("Ingredients: N/A");
        }

        eatenButton.setOnClickListener(v -> {
            markMealAsEaten(mealItem);
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }
    private void markMealAsEaten(MealItem mealItem) {
        // Save the eaten state of the meal item in SharedPreferences or database
        // Assuming you save it in SharedPreferences for simplicity
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("meal_eaten_" + mealItem.getTitle(), true);
        editor.apply();
    }
}
