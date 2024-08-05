package com.example.diabfitapp.healthmonitoring.medication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diabfitapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MedicationRemindersFragment extends Fragment {

    private MedicineDatabaseHelper dbHelper;
    private MedicineAdapter medicineAdapter;
    private List<Medicine> medicines;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medication_reminders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new MedicineDatabaseHelper(requireContext());
        medicines = new ArrayList<>();
        medicineAdapter = new MedicineAdapter(requireContext(), medicines);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Medication Reminders");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        Button addMedicineButton = view.findViewById(R.id.add_medicine_button);
        addMedicineButton.setOnClickListener(v -> showAddMedicineDialog());

        RecyclerView recyclerView = view.findViewById(R.id.medicine_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(medicineAdapter);

        resetLockedMedicines();
        loadMedicines();
    }

    private void showAddMedicineDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_medicine, null);
        builder.setView(dialogView);

        EditText medicineNameInput = dialogView.findViewById(R.id.medicine_name_input);
        TimePicker timePicker = dialogView.findViewById(R.id.time_picker);
        EditText quantityInput = dialogView.findViewById(R.id.quantity_input);
        Button scheduleAlertButton = dialogView.findViewById(R.id.schedule_alert_button);

        AlertDialog dialog = builder.create();

        scheduleAlertButton.setOnClickListener(v -> {
            String medicineName = medicineNameInput.getText().toString().trim();
            String quantityStr = quantityInput.getText().toString().trim();

            if (medicineName.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a medicine name.", Toast.LENGTH_SHORT).show();
            } else if (quantityStr.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a quantity.", Toast.LENGTH_SHORT).show();
            } else {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                int quantity = Integer.parseInt(quantityStr);

                saveMedicine(new Medicine(0, medicineName, quantity, hour, minute, false, 0));
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void saveMedicine(Medicine medicine) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO " + MedicineDatabaseHelper.TABLE_MEDICINES + " (" +
                        MedicineDatabaseHelper.COLUMN_NAME + ", " +
                        MedicineDatabaseHelper.COLUMN_QUANTITY + ", " +
                        MedicineDatabaseHelper.COLUMN_HOUR + ", " +
                        MedicineDatabaseHelper.COLUMN_MINUTE + ", " +
                        MedicineDatabaseHelper.COLUMN_LOCKED + ", " +
                        MedicineDatabaseHelper.COLUMN_EATEN_DATE + ") VALUES (?, ?, ?, ?, ?, ?)",
                new Object[]{medicine.getName(), medicine.getQuantity(), medicine.getHour(), medicine.getMinute(), medicine.isLocked() ? 1 : 0, medicine.getEatenDate()});
        loadMedicines();
    }

    private void loadMedicines() {
        medicines.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + MedicineDatabaseHelper.TABLE_MEDICINES, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MedicineDatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MedicineDatabaseHelper.COLUMN_NAME));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(MedicineDatabaseHelper.COLUMN_QUANTITY));
                int hour = cursor.getInt(cursor.getColumnIndexOrThrow(MedicineDatabaseHelper.COLUMN_HOUR));
                int minute = cursor.getInt(cursor.getColumnIndexOrThrow(MedicineDatabaseHelper.COLUMN_MINUTE));
                boolean locked = cursor.getInt(cursor.getColumnIndexOrThrow(MedicineDatabaseHelper.COLUMN_LOCKED)) == 1;
                long eatenDate = cursor.getLong(cursor.getColumnIndexOrThrow(MedicineDatabaseHelper.COLUMN_EATEN_DATE));

                medicines.add(new Medicine(id, name, quantity, hour, minute, locked, eatenDate));
            } while (cursor.moveToNext());
        }
        cursor.close();
        medicineAdapter.notifyDataSetChanged();
    }

    private void resetLockedMedicines() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long currentTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        int currentDay = calendar.get(Calendar.DAY_OF_YEAR);

        Cursor cursor = db.rawQuery("SELECT * FROM " + MedicineDatabaseHelper.TABLE_MEDICINES, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MedicineDatabaseHelper.COLUMN_ID));
                long eatenDate = cursor.getLong(cursor.getColumnIndexOrThrow(MedicineDatabaseHelper.COLUMN_EATEN_DATE));

                calendar.setTimeInMillis(eatenDate);
                int eatenDay = calendar.get(Calendar.DAY_OF_YEAR);

                if (currentDay != eatenDay) {
                    ContentValues values = new ContentValues();
                    values.put(MedicineDatabaseHelper.COLUMN_LOCKED, 0);
                    values.put(MedicineDatabaseHelper.COLUMN_EATEN_DATE, 0);
                    db.update(MedicineDatabaseHelper.TABLE_MEDICINES, values, MedicineDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
