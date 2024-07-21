package com.example.diabfitapp.nutrition.food;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Calendar;

public class EatenDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "eatenItems.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "eatenItems";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CARBS = "carbs";
    public static final String COLUMN_SERVINGS = "servings";
    public static final String COLUMN_GLYCEMIC_INDEX = "glycemicIndex";
    public static final String COLUMN_SERVING_SIZE = "servingSize";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public EatenDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CARBS + " INTEGER, " +
                COLUMN_SERVINGS + " INTEGER, " +
                COLUMN_GLYCEMIC_INDEX + " INTEGER, " +
                COLUMN_SERVING_SIZE + " INTEGER, " +
                COLUMN_TIMESTAMP + " INTEGER" +
                ")";
        db.execSQL(createTable);

        // Create an index on the timestamp column to speed up queries
        db.execSQL("CREATE INDEX index_timestamp ON " + TABLE_NAME + " (" + COLUMN_TIMESTAMP + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addEatenItem(FoodItem item, int servingsEaten) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_CARBS, item.getCarbsPer100g());
        values.put(COLUMN_SERVINGS, servingsEaten);
        values.put(COLUMN_GLYCEMIC_INDEX, item.getGlycemicIndex());
        values.put(COLUMN_SERVING_SIZE, item.getSizeOfServing());
        values.put(COLUMN_TIMESTAMP, System.currentTimeMillis());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Collection<FoodEatenItem> getEatenItems(String timeFrame) {
        List<FoodEatenItem> foodEatenItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        long[] timeRange = getStartAndEndTimeForTimeFrame(timeFrame);
        long startTime = timeRange[0];
        long endTime = timeRange[1];

        String selection = COLUMN_TIMESTAMP + " >= ? AND " + COLUMN_TIMESTAMP + " <= ?";
        String[] selectionArgs = { String.valueOf(startTime), String.valueOf(endTime) };

        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, COLUMN_TIMESTAMP + " DESC");

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                int carbs = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARBS));
                int servings = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERVINGS));
                int glycemicIndex = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GLYCEMIC_INDEX));
                int servingSize = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERVING_SIZE));
                long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP));

                FoodEatenItem foodEatenItem = new FoodEatenItem(name, carbs, servings, glycemicIndex, servings * carbs, servingSize, timestamp);
                foodEatenItems.add(foodEatenItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return foodEatenItems;
    }

    private long[] getStartAndEndTimeForTimeFrame(String timeFrame) {
        Calendar calendar = Calendar.getInstance();

        long startTime;
        long endTime = System.currentTimeMillis();

        switch (timeFrame) {
            case "Today":
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                startTime = calendar.getTimeInMillis();
                break;
            case "Yesterday":
                calendar.add(Calendar.DATE, -1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                startTime = calendar.getTimeInMillis();
                calendar.add(Calendar.DATE, 1);
                endTime = calendar.getTimeInMillis() - 1;
                break;
            case "All":
                calendar.setTimeInMillis(0); // Epoch time
                startTime = calendar.getTimeInMillis();
                break;
            default:
                startTime = 0;
                break;
        }

        return new long[] { startTime, endTime };
    }


}

