package com.example.diabfitapp.exercise.progress;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import com.example.diabfitapp.exercise.workout.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ProgressDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "progress.db";
    private static final int DATABASE_VERSION = 2; // Incremented the version
    private static final String TABLE_NAME = "exercises";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_BODY_PART = "body_part";
    private static final String COLUMN_EQUIPMENT = "equipment";
    private static final String COLUMN_GIF_URL = "gif_url";
    private static final String COLUMN_TARGET = "target";
    private static final String COLUMN_SECONDARY_MUSCLES = "secondary_muscles";
    private static final String COLUMN_INSTRUCTIONS = "instructions";
    private static final String COLUMN_SETS = "sets";
    private static final String COLUMN_COMPLETED_SETS = "completed_sets";
    private static final String COLUMN_DATE = "date";

    public ProgressDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " TEXT PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_BODY_PART + " TEXT,"
                + COLUMN_EQUIPMENT + " TEXT,"
                + COLUMN_GIF_URL + " TEXT,"
                + COLUMN_TARGET + " TEXT,"
                + COLUMN_SECONDARY_MUSCLES + " TEXT,"
                + COLUMN_INSTRUCTIONS + " TEXT,"
                + COLUMN_SETS + " INTEGER,"
                + COLUMN_COMPLETED_SETS + " INTEGER DEFAULT 0,"
                + COLUMN_DATE + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Adding the completed_sets column in version 2
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_COMPLETED_SETS + " INTEGER DEFAULT 0");
        }
    }

    public void addExercise(Exercise exercise, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, exercise.getId());
        values.put(COLUMN_NAME, exercise.getName());
        values.put(COLUMN_BODY_PART, exercise.getBodyPart());
        values.put(COLUMN_EQUIPMENT, exercise.getEquipment());
        values.put(COLUMN_GIF_URL, exercise.getGifUrl());
        values.put(COLUMN_TARGET, exercise.getTarget());
        values.put(COLUMN_SECONDARY_MUSCLES, exercise.getSecondaryMuscles());
        values.put(COLUMN_INSTRUCTIONS, exercise.getInstructions());
        values.put(COLUMN_SETS, exercise.getSets());
        values.put(COLUMN_COMPLETED_SETS, exercise.getCompletedSets());
        values.put(COLUMN_DATE, date);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPLETED_SETS, exercise.getCompletedSets());
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{exercise.getId()});
        db.close();
    }

    public List<Exercise> getExercisesByDate(String date) {
        List<Exercise> exerciseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_DATE + " = ?";
        String[] selectionArgs = {date};
        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Exercise exercise = new Exercise(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BODY_PART)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EQUIPMENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GIF_URL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TARGET)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SECONDARY_MUSCLES)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTRUCTIONS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SETS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED_SETS))
                );
                exerciseList.add(exercise);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return exerciseList;
    }
}
