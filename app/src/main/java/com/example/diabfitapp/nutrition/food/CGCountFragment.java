package com.example.diabfitapp.nutrition.food;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabfitapp.R;
import com.example.diabfitapp.main.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CGCountFragment extends Fragment {

    private Spinner timeFrameSpinner;
    private EatenDatabaseHelper dbHelper;
    private List<FoodEatenItem> foodEatenItems;
    private FoodEatenAdapter foodEatenAdapter;

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

        timeFrameSpinner = view.findViewById(R.id.timeFrameSpinner);
        ArrayAdapter<CharSequence> timeFrameAdapter = ArrayAdapter.createFromResource(getContext(), R.array.time_frame_array, android.R.layout.simple_spinner_item);
        timeFrameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeFrameSpinner.setAdapter(timeFrameAdapter);

        foodEatenItems = new ArrayList<>();
        foodEatenAdapter = new FoodEatenAdapter(foodEatenItems);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Carbs and Glycemic Counter");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        circularProgressView = view.findViewById(R.id.circularProgressView);
        carbsTextView = view.findViewById(R.id.carbsTextView);
        overTargetMessage = view.findViewById(R.id.overTargetMessage);

        RecyclerView foodEatenList = view.findViewById(R.id.foodEatenList);
        foodEatenList.setLayoutManager(new LinearLayoutManager(requireContext()));
        foodEatenList.setAdapter(foodEatenAdapter);

        FloatingActionButton fabAddFood = view.findViewById(R.id.fabAddFood);
        fabAddFood.setOnClickListener(v -> ((MainActivity) getActivity()).replaceFragment(new FoodDatabaseFragment()));

        timeFrameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateFoodList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        Button changeCarbsLimitButton = view.findViewById(R.id.changeCarbsLimitButton);
        changeCarbsLimitButton.setOnClickListener(v -> showChangeCarbsLimitDialog());

        // Retrieve the targetCarbs from SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        targetCarbs = sharedPreferences.getFloat("targetCarbs", 150.0f); // Default is 150.0f if not set

        updateFoodList();
    }

    private void showChangeCarbsLimitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_change_carbs_limit, null);
        builder.setView(dialogView);

        final EditText editTextNewTarget = dialogView.findViewById(R.id.editTextNewTarget);
        Button buttonConfirm = dialogView.findViewById(R.id.buttonConfirm);

        final AlertDialog dialog = builder.create();

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTargetString = editTextNewTarget.getText().toString();
                if (!newTargetString.isEmpty()) {
                    float newTarget = Float.parseFloat(newTargetString);
                    updateTargetCarbs(newTarget);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void updateTargetCarbs(float newTarget) {
        targetCarbs = newTarget;
        updateProgress();

        // Save the new target to SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("targetCarbs", newTarget);
        editor.apply();
    }

    private int getItemSizeFromDB(String name) {
        // Fetch and return the item size from your existing database or data source
        return 0; // Placeholder, replace with actual implementation
    }

    private int calculateCarbsIntake(int carbsPer100g, int servings) {
        return (carbsPer100g * servings) / 100;
    }

    public void updateFoodList() {
        String selectedTimeFrame = timeFrameSpinner.getSelectedItem().toString();
        EatenDatabaseHelper dbHelper = new EatenDatabaseHelper(getContext());
        foodEatenItems.clear();
        foodEatenItems.addAll(dbHelper.getEatenItems(selectedTimeFrame));
        foodEatenAdapter.notifyDataSetChanged();
        updateProgress();
    }

    private void updateProgress() {
        float totalCarbs = 0;
        for (FoodEatenItem item : foodEatenItems) {
            totalCarbs += item.getCarbsIntake();
        }

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
