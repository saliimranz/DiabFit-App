package com.example.diabfitapp.exercise.workout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WorkoutDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "workoutLog.db";
    private static final int DATABASE_VERSION = 1;

    public WorkoutDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE workout_log (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "exercise_id TEXT, " +
                "name TEXT, " +
                "bodyPart TEXT, " +
                "equipment TEXT, " +
                "gifUrl TEXT, " +
                "target TEXT, " +
                "secondaryMuscles TEXT, " +
                "instructions TEXT, " +
                "sets INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS workout_log");
        onCreate(db);
    }
}
