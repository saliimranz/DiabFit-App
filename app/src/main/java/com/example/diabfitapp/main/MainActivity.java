package com.example.diabfitapp.main;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import com.example.diabfitapp.R;
import androidx.fragment.app.FragmentManager;
import com.example.diabfitapp.nutrition.food.CGCountFragment;
import com.example.diabfitapp.nutrition.food.FoodItem;

import com.example.diabfitapp.nutrition.food.EatenDatabaseHelper;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private CGCountFragment cgCountFragment;
    private EatenDatabaseHelper dbHelper;
    private static final int REQUEST_CODE_EXACT_ALARM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                requestExactAlarmPermission();
            }
        }

        dbHelper = new EatenDatabaseHelper(this);

        FirebaseApp.initializeApp(this);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (savedInstanceState == null) {
            if (currentUser != null) {
                // User is logged in, navigate to MainFragment
                replaceFragment(new MainFragment());
            } else {
                // No user is logged in, navigate to LoginFragment
                replaceFragment(new LoginFragment());
            }
        }
    }

    private void requestExactAlarmPermission() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            intent.setAction(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            startActivityForResult(intent, REQUEST_CODE_EXACT_ALARM);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EXACT_ALARM) {
            // Handle the result of the exact alarm permission request if needed
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager != null && alarmManager.canScheduleExactAlarms()) {
                    Toast.makeText(this, "Exact alarm permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Exact alarm permission denied", Toast.LENGTH_SHORT).show();
                }
            }
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
        Log.d("MainActivity", "addFoodToCGCountFragment called with servingsEaten: " + servingsEaten);

        // Save to SQLite database
        EatenDatabaseHelper dbHelper = new EatenDatabaseHelper(this);
        dbHelper.addEatenItem(item, servingsEaten);

        // Update CGCountFragment if it's currently displayed
        FragmentManager fragmentManager = getSupportFragmentManager();
        CGCountFragment fragment = (CGCountFragment) fragmentManager.findFragmentByTag("CGCountFragment");
        if (fragment != null) {
            fragment.updateFoodList();
            Log.d("MainActivity", "Item added to CGCountFragment: " + item.getName());
        } else {
            Log.d("MainActivity", "CGCountFragment not found");
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }
}
