package com.example.diabfitapp.healthmonitoring.sugerlog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SugarLogDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sugar_log_database";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_SUGAR_LOGS = "sugar_logs";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SUGAR_LEVEL = "sugar_level";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_TIME = "time";

    public SugarLogDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SUGAR_LOGS_TABLE = "CREATE TABLE " + TABLE_SUGAR_LOGS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SUGAR_LEVEL + " INTEGER,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_TIME + " TEXT" + ")";
        db.execSQL(CREATE_SUGAR_LOGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUGAR_LOGS);
        onCreate(db);
    }

    public void addSugarLog(SugarLog sugarLog) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SUGAR_LEVEL, sugarLog.getSugarLevel());
        values.put(COLUMN_TYPE, sugarLog.getType());
        values.put(COLUMN_TIME, sugarLog.getTime());

        db.insert(TABLE_SUGAR_LOGS, null, values);
        db.close();
    }

    public List<SugarLog> getAllSugarLogs() {
        List<SugarLog> sugarLogList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_SUGAR_LOGS + " ORDER BY " + COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                int sugarLevel = cursor.getInt(cursor.getColumnIndex(COLUMN_SUGAR_LEVEL));
                String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));

                SugarLog sugarLog = new SugarLog(sugarLevel, type, time);
                sugarLog.setId(id);
                sugarLogList.add(sugarLog);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return sugarLogList;
    }
}
