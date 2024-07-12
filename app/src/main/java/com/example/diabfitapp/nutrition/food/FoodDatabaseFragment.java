package com.example.diabfitapp.nutrition.food;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.widget.Toolbar;

import com.example.diabfitapp.R;

public class FoodDatabaseFragment extends Fragment {

    private FoodDatabaseAdapter adapter;
    private Button addNewItemButton;

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

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Food Database");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> {
            // Navigate back to NutritionTrackingFragment
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Load food data
        List<FoodItem> foodItems = loadFoodData();

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter and set to RecyclerView
        adapter = new FoodDatabaseAdapter(foodItems);
        recyclerView.setAdapter(adapter);

        // Setup search
        SearchView searchView = view.findViewById(R.id.search_text);
        setupSearchView(searchView);

        // Setup add new item button
        addNewItemButton = view.findViewById(R.id.add_new_item_button);
        addNewItemButton.setOnClickListener(v -> {
            // Open AddNewItemFragment with adapter
            AddNewItemFragment addNewItemFragment = AddNewItemFragment.newInstance(adapter);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, addNewItemFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void setupSearchView(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                if (adapter.getItemCount() == 0) {
                    addNewItemButton.setVisibility(View.VISIBLE);
                } else {
                    addNewItemButton.setVisibility(View.GONE);
                }
                return true;
            }
        });
    }

    private List<FoodItem> loadFoodData() {
        List<FoodItem> foodItems = new ArrayList<>();

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "food_items",
                new String[]{"name", "glycemic_index", "carbs_per_100g", "size_of_serving"},
                null, null, null, null, null
        );

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            int glycemicIndex = cursor.getInt(cursor.getColumnIndexOrThrow("glycemic_index"));
            int carbsPer100g = cursor.getInt(cursor.getColumnIndexOrThrow("carbs_per_100g"));
            int sizeOfServing = cursor.getInt(cursor.getColumnIndexOrThrow("size_of_serving"));

            foodItems.add(new FoodItem(name, glycemicIndex, carbsPer100g, sizeOfServing));
        }
        cursor.close();
        db.close();

        return foodItems;
    }
}
