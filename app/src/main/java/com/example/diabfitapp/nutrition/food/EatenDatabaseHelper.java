package com.example.diabfitapp.nutrition.food;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EatenDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "eatenItems.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "eatenItems";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CARBS = "carbs";
    public static final String COLUMN_SERVINGS = "servings";
    public static final String COLUMN_GI = "glycemicIndex";
    public static final String COLUMN_DATE = "date";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_CARBS + " INTEGER, " +
                    COLUMN_SERVINGS + " INTEGER, " +
                    COLUMN_GI + " INTEGER, " +
                    COLUMN_DATE + " INTEGER);";

    public EatenDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
