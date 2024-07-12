package com.example.diabfitapp.nutrition.food;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diabfitapp.R;

public class AddNewItemFragment extends Fragment {

    private EditText foodNameEditText;
    private EditText glycemicIndexEditText;
    private EditText carbsPer100gEditText;
    private EditText sizeOfServingEditText;
    private Button addButton;

    private FoodDatabaseAdapter adapter;

    public static AddNewItemFragment newInstance(FoodDatabaseAdapter adapter) {
        AddNewItemFragment fragment = new AddNewItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setAdapter(adapter);
        return fragment;
    }

    public void setAdapter(FoodDatabaseAdapter adapter) {
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_new_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        foodNameEditText = view.findViewById(R.id.food_name);
        glycemicIndexEditText = view.findViewById(R.id.glycemic_index);
        carbsPer100gEditText = view.findViewById(R.id.carbs_per_100g);
        sizeOfServingEditText = view.findViewById(R.id.size_of_serving);
        addButton = view.findViewById(R.id.add_button);

        addButton.setOnClickListener(v -> {
            String foodName = foodNameEditText.getText().toString();
            String glycemicIndexStr = glycemicIndexEditText.getText().toString();
            String carbsPer100gStr = carbsPer100gEditText.getText().toString();
            String sizeOfServingStr = sizeOfServingEditText.getText().toString();

            if (TextUtils.isEmpty(foodName) || TextUtils.isEmpty(glycemicIndexStr)
                    || TextUtils.isEmpty(carbsPer100gStr) || TextUtils.isEmpty(sizeOfServingStr)) {
                if (TextUtils.isEmpty(foodName)) {
                    foodNameEditText.setError("Required");
                }
                if (TextUtils.isEmpty(glycemicIndexStr)) {
                    glycemicIndexEditText.setError("Required");
                }
                if (TextUtils.isEmpty(carbsPer100gStr)) {
                    carbsPer100gEditText.setError("Required");
                }
                if (TextUtils.isEmpty(sizeOfServingStr)) {
                    sizeOfServingEditText.setError("Required");
                }
                return;
            }

            try {
                int glycemicIndex = Integer.parseInt(glycemicIndexStr);
                int carbsPer100g = Integer.parseInt(carbsPer100gStr);
                int sizeOfServing = Integer.parseInt(sizeOfServingStr);

                FoodItem newItem = new FoodItem(foodName, glycemicIndex, carbsPer100g, sizeOfServing);

                // Update database and adapter
                // Assuming adapter and database are passed/accessible
                DatabaseHelper db = new DatabaseHelper(getContext());
                db.insertFoodItem(newItem);
                adapter.addItem(newItem);

                // Navigate back
                getActivity().getSupportFragmentManager().popBackStack();
            } catch (NumberFormatException e) {
                if (!TextUtils.isDigitsOnly(glycemicIndexStr)) {
                    glycemicIndexEditText.setError("Must be a number");
                }
                if (!TextUtils.isDigitsOnly(carbsPer100gStr)) {
                    carbsPer100gEditText.setError("Must be a number");
                }
                if (!TextUtils.isDigitsOnly(sizeOfServingStr)) {
                    sizeOfServingEditText.setError("Must be a number");
                }
            }
        });
    }
}
