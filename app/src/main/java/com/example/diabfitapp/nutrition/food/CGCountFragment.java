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
import com.example.diabfitapp.main.MainActivity;
import com.example.diabfitapp.nutrition.food.CircularProgressView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.diabfitapp.nutrition.food.EatenDatabaseHelper;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.List;

public class CGCountFragment extends Fragment {

    private EatenDatabaseHelper dbHelper;
    private List<FoodEatenItem> foodEatenItems;
    private FoodEatenAdapter adapter;

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

        dbHelper = new EatenDatabaseHelper(getContext());
        foodEatenItems = new ArrayList<>();
        adapter = new FoodEatenAdapter(foodEatenItems);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Carbs and Glycemic Counter");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        circularProgressView = view.findViewById(R.id.circularProgressView);
        carbsTextView = view.findViewById(R.id.carbsTextView);
        overTargetMessage = view.findViewById(R.id.overTargetMessage);

        RecyclerView foodEatenList = view.findViewById(R.id.foodEatenList);
        foodEatenList.setLayoutManager(new LinearLayoutManager(requireContext()));
        //FoodEatenAdapter adapter = new FoodEatenAdapter(getSampleData());
        foodEatenList.setAdapter(adapter);

        loadItemsFromDatabase();

        FloatingActionButton fabAddFood = view.findViewById(R.id.fabAddFood);
        fabAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new FoodDatabaseFragment());
            }
        });

        updateProgress();
    }

    private void loadItemsFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Today's date range
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        long startOfDay = today.getTimeInMillis();
        long endOfDay = startOfDay + 24 * 60 * 60 * 1000;

        Cursor cursor = db.query(
                EatenDatabaseHelper.TABLE_NAME,
                null,
                EatenDatabaseHelper.COLUMN_DATE + " BETWEEN ? AND ?",
                new String[]{String.valueOf(startOfDay), String.valueOf(endOfDay)},
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(EatenDatabaseHelper.COLUMN_NAME));
            int carbs = cursor.getInt(cursor.getColumnIndexOrThrow(EatenDatabaseHelper.COLUMN_CARBS));
            int servings = cursor.getInt(cursor.getColumnIndexOrThrow(EatenDatabaseHelper.COLUMN_SERVINGS));
            int glycemicIndex = cursor.getInt(cursor.getColumnIndexOrThrow(EatenDatabaseHelper.COLUMN_GI));
            long date = cursor.getLong(cursor.getColumnIndexOrThrow(EatenDatabaseHelper.COLUMN_DATE));

            FoodEatenItem item = new FoodEatenItem(name, carbs, servings, glycemicIndex, calculateCarbsIntake(carbs, servings), getItemSizeFromDB(name));
            foodEatenItems.add(item);
        }

        cursor.close();
        db.close();
        adapter.notifyDataSetChanged();
    }

    private int getItemSizeFromDB(String name) {
        // Fetch and return the item size from your existing database or data source
        return 0; // Placeholder, replace with actual implementation
    }


    private int calculateCarbsIntake(int carbsPer100g, int servings) {
        return (carbsPer100g * servings) / 100;
    }

    private List<FoodEatenItem> getSampleData() {
        List<FoodEatenItem> items = new ArrayList<>();
        // Add sample data
        items.add(new FoodEatenItem("Apple", 14, 5, 40, 99, 20));
        items.add(new FoodEatenItem("Banana", 23, 15, 60, 22, 10));
        items.add(new FoodEatenItem("Bread", 50, 2, 70, 236,5));
        return items;
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
