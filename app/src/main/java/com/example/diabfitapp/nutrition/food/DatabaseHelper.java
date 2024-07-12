package com.example.diabfitapp.nutrition.food;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "food_database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_FOOD = "food_items";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_GI = "glycemic_index";
    private static final String COLUMN_CARBS = "carbs_per_100g";
    private static final String COLUMN_SERVING_SIZE = "size_of_serving";
    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FOOD_TABLE = "CREATE TABLE " + TABLE_FOOD + "("
                + COLUMN_NAME + " TEXT,"
                + COLUMN_GI + " INTEGER,"
                + COLUMN_CARBS + " INTEGER,"
                + COLUMN_SERVING_SIZE + " INTEGER" + ")";
        db.execSQL(CREATE_FOOD_TABLE);
        loadInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        onCreate(db);
    }

    private void loadInitialData(SQLiteDatabase db) {
        try {
            InputStream inputStream = context.getAssets().open("food_data.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            JSONObject jsonObject = new JSONObject(jsonString.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray foodArray = jsonArray.getJSONArray(i);
                String name = foodArray.getString(0);
                int glycemicIndex = foodArray.getInt(1);
                int carbsPer100g = foodArray.getInt(2);
                int sizeOfServing = foodArray.getInt(3);

                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME, name);
                values.put(COLUMN_GI, glycemicIndex);
                values.put(COLUMN_CARBS, carbsPer100g);
                values.put(COLUMN_SERVING_SIZE, sizeOfServing);

                db.insert(TABLE_FOOD, null, values);
            }

        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error reading JSON", e);
        }
    }

    public void insertFoodItem(FoodItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_GI, item.getGlycemicIndex());
        values.put(COLUMN_CARBS, item.getCarbsPer100g());
        values.put(COLUMN_SERVING_SIZE, item.getSizeOfServing());

        db.insert(TABLE_FOOD, null, values);
        db.close();
    }
}
