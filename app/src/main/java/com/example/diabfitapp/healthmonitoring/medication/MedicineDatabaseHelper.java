package com.example.diabfitapp.healthmonitoring.medication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MedicineDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "medicines.db";
    private static final int DATABASE_VERSION = 2; // Incremented the version number

    public static final String TABLE_MEDICINES = "medicines";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_HOUR = "hour";
    public static final String COLUMN_MINUTE = "minute";
    public static final String COLUMN_LOCKED = "locked";
    public static final String COLUMN_EATEN_DATE = "eaten_date";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_MEDICINES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_QUANTITY + " INTEGER, " +
                    COLUMN_HOUR + " INTEGER, " +
                    COLUMN_MINUTE + " INTEGER, " +
                    COLUMN_LOCKED + " INTEGER, " +
                    COLUMN_EATEN_DATE + " INTEGER" + // Adding new column here
                    ");";

    public MedicineDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_MEDICINES + " ADD COLUMN " + COLUMN_LOCKED + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_MEDICINES + " ADD COLUMN " + COLUMN_EATEN_DATE + " INTEGER DEFAULT 0");
        }
    }

    public void deleteMedicine(int medicineId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDICINES, COLUMN_ID + " = ?", new String[]{String.valueOf(medicineId)});
        db.close();
    }

    public void updateMedicine(Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_MEDICINES + " SET " +
                        COLUMN_NAME + " = ?, " +
                        COLUMN_QUANTITY + " = ?, " +
                        COLUMN_HOUR + " = ?, " +
                        COLUMN_MINUTE + " = ?, " +
                        COLUMN_LOCKED + " = ?, " +
                        COLUMN_EATEN_DATE + " = ? " +
                        "WHERE " + COLUMN_ID + " = ?",
                new Object[]{medicine.getName(), medicine.getQuantity(), medicine.getHour(),
                        medicine.getMinute(), medicine.isLocked() ? 1 : 0, medicine.getEatenDate(), medicine.getId()});
        db.close();
    }

    public Cursor getAllMedicines() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MEDICINES, null);
    }
}
