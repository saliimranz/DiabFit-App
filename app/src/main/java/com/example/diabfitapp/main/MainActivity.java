package com.example.diabfitapp.main;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import com.example.diabfitapp.R;
import androidx.fragment.app.FragmentManager;
import com.example.diabfitapp.nutrition.food.CGCountFragment;
import com.example.diabfitapp.nutrition.food.FoodItem;

import com.example.diabfitapp.database.EatenDatabaseHelper;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends AppCompatActivity {

    private CGCountFragment cgCountFragment;
    private EatenDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new EatenDatabaseHelper(this);

        if (savedInstanceState == null) {
            MainFragment mainFragment = new MainFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, mainFragment);
            transaction.commit();
        }
    }

    public void replaceFragment(Fragment fragment) {
        if (fragment instanceof CGCountFragment) {
            cgCountFragment = (CGCountFragment) fragment;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addFoodToCGCountFragment(FoodItem item, int servingsEaten) {
        Log.d("MainActivity", "addFoodToCGCountFragment called");
        if (cgCountFragment != null) {
            saveFoodItemToDatabase(item, servingsEaten);
            Log.d("MainActivity", "Item added to cg count fragment: " + item.getName());
        } else {
            Log.d("MainActivity", "CGCountFragment not found");
        }
    }

    private void saveFoodItemToDatabase(FoodItem item, int servingsEaten) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EatenDatabaseHelper.COLUMN_NAME, item.getName());
        values.put(EatenDatabaseHelper.COLUMN_CARBS, item.getCarbsPer100g());
        values.put(EatenDatabaseHelper.COLUMN_SERVINGS, servingsEaten);
        values.put(EatenDatabaseHelper.COLUMN_GI, item.getGlycemicIndex());
        values.put(EatenDatabaseHelper.COLUMN_DATE, System.currentTimeMillis());

        db.insert(EatenDatabaseHelper.TABLE_NAME, null, values);
        db.close();
    }


    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }
}
