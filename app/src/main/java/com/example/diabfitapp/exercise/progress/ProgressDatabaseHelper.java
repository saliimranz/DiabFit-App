package com.example.diabfitapp.exercise.progress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.diabfitapp.exercise.workout.Exercise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgressDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "progress.db";
    private static final int DATABASE_VERSION = 3; // Incremented the version

    private static final String TABLE_EXERCISES = "exercises";
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

    private static final String TABLE_HISTORY = "history";
    private static final String COLUMN_HISTORY_ID = "id";
    private static final String COLUMN_DAY = "day";
    private static final String COLUMN_TIME_SPENT = "time_spent";
    private static final String COLUMN_CARBS_BURNT = "carbs_burnt";
    private static final String COLUMN_WORKOUT_PERCENTAGE = "workout_percentage";
    private static final String COLUMN_EXERCISE_NAMES = "exercise_names";
    private static final String COLUMN_COMPLETED_SETS_LIST = "completed_sets";
    private static final String COLUMN_TARGET_SETS = "target_sets";

    public ProgressDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EXERCISES_TABLE = "CREATE TABLE " + TABLE_EXERCISES + "("
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
        db.execSQL(CREATE_EXERCISES_TABLE);

        String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
                + COLUMN_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DAY + " TEXT,"
                + COLUMN_TIME_SPENT + " INTEGER,"
                + COLUMN_CARBS_BURNT + " REAL,"
                + COLUMN_WORKOUT_PERCENTAGE + " REAL,"
                + COLUMN_EXERCISE_NAMES + " TEXT,"
                + COLUMN_COMPLETED_SETS_LIST + " TEXT,"
                + COLUMN_TARGET_SETS + " TEXT"
                + ")";
        db.execSQL(CREATE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_EXERCISES + " ADD COLUMN " + COLUMN_COMPLETED_SETS + " INTEGER DEFAULT 0");
        }
        if (oldVersion < 3) {
            db.execSQL("CREATE TABLE " + TABLE_HISTORY + "("
                    + COLUMN_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DAY + " TEXT,"
                    + COLUMN_TIME_SPENT + " INTEGER,"
                    + COLUMN_CARBS_BURNT + " REAL,"
                    + COLUMN_WORKOUT_PERCENTAGE + " REAL,"
                    + COLUMN_EXERCISE_NAMES + " TEXT,"
                    + COLUMN_COMPLETED_SETS_LIST + " TEXT,"
                    + COLUMN_TARGET_SETS + " TEXT"
                    + ")");
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
        db.insert(TABLE_EXERCISES, null, values);
        db.close();
    }

    // Method to insert or update workout session
    public void insertOrUpdateWorkoutSession(WorkoutSession session) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("day", session.getDay());
        values.put("time_spent", session.getTimeSpent());
        values.put("carbs_burnt", session.getCarbsBurnt());
        values.put("workout_percentage", session.getWorkoutPercentage());
        values.put("exercise_names", convertListToString(session.getExerciseNames()));
        values.put("completed_sets", convertIntegerListToString(session.getCompletedSets()));
        values.put("target_sets", convertIntegerListToString(session.getTargetSets()));

        // Check if an entry already exists for today
        String selection = "day = ?";
        String[] selectionArgs = { session.getDay() };
        int rowsUpdated = db.update("history", values, selection, selectionArgs);

        // If no rows were updated, insert a new row
        if (rowsUpdated == 0) {
            db.insert("history", null, values);
        }
    }

    public void updateExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPLETED_SETS, exercise.getCompletedSets());
        db.update(TABLE_EXERCISES, values, COLUMN_ID + " = ?", new String[]{exercise.getId()});
        db.close();
    }

    public List<Exercise> getExercisesByDate(String date) {
        List<Exercise> exerciseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_DATE + " = ?";
        String[] selectionArgs = {date};
        Cursor cursor = db.query(TABLE_EXERCISES, null, selection, selectionArgs, null, null, null);

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

    public void deleteAllExercises() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EXERCISES);
        db.close();
    }

    public static String convertIntegerListToString(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i));
            if (i < list.size() - 1) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }

    public void insertWorkoutSession(String day, int timeSpent, double carbsBurnt, double workoutPercentage, List<String> exerciseNames, List<Integer> completedSets, List<Integer> targetSets) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DAY, day);
        values.put(COLUMN_TIME_SPENT, timeSpent);
        values.put(COLUMN_CARBS_BURNT, carbsBurnt);
        values.put(COLUMN_WORKOUT_PERCENTAGE, workoutPercentage);
        values.put(COLUMN_EXERCISE_NAMES, convertListToString(exerciseNames));
        values.put(COLUMN_COMPLETED_SETS_LIST, convertListToString(completedSets));
        values.put(COLUMN_TARGET_SETS, convertListToString(targetSets));

        db.insert(TABLE_HISTORY, null, values);
        db.close();
    }

    public List<WorkoutSession> getWorkoutHistory() {
        List<WorkoutSession> historyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HISTORY, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HISTORY_ID));
                String day = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY));
                int timeSpent = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME_SPENT));
                double carbsBurnt = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CARBS_BURNT));
                double workoutPercentage = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_WORKOUT_PERCENTAGE));
                List<String> exerciseNames = convertStringToList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE_NAMES)));
                List<Integer> completedSets = convertStringToIntegerList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED_SETS_LIST)));
                List<Integer> targetSets = convertStringToIntegerList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TARGET_SETS)));

                WorkoutSession session = new WorkoutSession(id, day, timeSpent, carbsBurnt, workoutPercentage, exerciseNames, completedSets, targetSets);
                historyList.add(session);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return historyList;
    }

    public List<WorkoutSession> getAllWorkoutSessions() {
        List<WorkoutSession> sessions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the column to sort by and the sort order
        String sortOrder = "day DESC"; // Assuming 'date' is the column name for date

        // Fetch all workout sessions ordered by date in descending order
        Cursor cursor = db.query(
                "history",    // Table name
                null,                  // Columns (null means all columns)
                null,                  // Selection (null means no selection)
                null,                  // Selection args
                null,                  // Group by
                null,                  // Having
                sortOrder              // Order by
        );

        if (cursor.moveToFirst()) {
            do {
                // Extract data from cursor and create WorkoutSession objects
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("day"));
                int timeSpent = cursor.getInt(cursor.getColumnIndexOrThrow("time_spent"));
                double carbsBurnt = cursor.getDouble(cursor.getColumnIndexOrThrow("carbs_burnt"));
                double workoutPercentage = cursor.getDouble(cursor.getColumnIndexOrThrow("workout_percentage"));
                String exerciseNamesStr = cursor.getString(cursor.getColumnIndexOrThrow("exercise_names"));
                String completedSetsStr = cursor.getString(cursor.getColumnIndexOrThrow("completed_sets"));
                String targetSetsStr = cursor.getString(cursor.getColumnIndexOrThrow("target_sets"));

                // Convert strings to lists
                List<String> exerciseNames = convertStringToStringList(exerciseNamesStr);
                List<Integer> completedSets = convertStringToIntegerList(completedSetsStr);
                List<Integer> targetSets = convertStringToIntegerList(targetSetsStr);

                // Create WorkoutSession object and add to the list
                WorkoutSession session = new WorkoutSession(id, date, timeSpent, carbsBurnt, workoutPercentage,
                        exerciseNames, completedSets, targetSets);
                sessions.add(session);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sessions;
    }

    public WorkoutSession getIncompleteSessionByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        WorkoutSession session = null;

        String selection = "day = ? AND workout_percentage < ?";
        String[] selectionArgs = { date, "100" };
        Cursor cursor = db.query("history", null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            int timeSpent = cursor.getInt(cursor.getColumnIndexOrThrow("time_spent"));
            double carbsBurnt = cursor.getDouble(cursor.getColumnIndexOrThrow("carbs_burnt"));
            double workoutPercentage = cursor.getDouble(cursor.getColumnIndexOrThrow("workout_percentage"));
            List<String> exerciseNames = convertStringToStringList(cursor.getString(cursor.getColumnIndexOrThrow("exercise_names")));
            List<Integer> completedSets = convertStringToIntegerList(cursor.getString(cursor.getColumnIndexOrThrow("completed_sets")));
            List<Integer> targetSets = convertStringToIntegerList(cursor.getString(cursor.getColumnIndexOrThrow("target_sets")));

            session = new WorkoutSession(id, date, timeSpent, carbsBurnt, workoutPercentage, exerciseNames, completedSets, targetSets);
        }

        cursor.close();
        db.close();
        return session;
    }

    private List<String> convertStringToStringList(String string) {
        List<String> list = new ArrayList<>();
        if (string == null || string.isEmpty()) {
            return list;
        }
        String[] items = string.split(",");
        for (String item : items) {
            list.add(item.trim());
        }
        return list;
    }


    private <T> String convertListToString(List<T> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (T item : list) {
            sb.append(item.toString()).append(",");
        }
        // Remove the trailing comma
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }

    private List<String> convertStringToList(String str) {
        return new ArrayList<>(Arrays.asList(str.split(",")));
    }

    private List<Integer> convertStringToIntegerList(String str) {
        List<String> stringList = convertStringToList(str);
        List<Integer> integerList = new ArrayList<>();
        for (String s : stringList) {
            integerList.add(Integer.parseInt(s));
        }
        return integerList;
    }


}
