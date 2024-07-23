package com.example.diabfitapp.healthmonitoring.medication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diabfitapp.R;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.List;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private List<Medicine> medicines;
    private Context context;
    private MedicineDatabaseHelper dbHelper;

    public MedicineAdapter(Context context, List<Medicine> medicines) {
        this.context = context;
        this.medicines = medicines;
        this.dbHelper = new MedicineDatabaseHelper(context);
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicines.get(position);
        holder.nameTextView.setText(medicine.getName());
        holder.quantityTextView.setText("Quantity: " + medicine.getQuantity());
        holder.timeTextView.setText("Time: " + formatTime(medicine.getHour(), medicine.getMinute()));

        if (medicine.isLocked()) {
            holder.itemView.setBackgroundColor(Color.GRAY);
            Drawable tickDrawable = context.getResources().getDrawable(R.drawable.ic_tick);
            tickDrawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
            holder.tickView.setImageDrawable(tickDrawable);
            holder.tickView.setVisibility(View.VISIBLE);
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTimeInMillis(currentTimeMillis);

            Calendar medicineCalendar = Calendar.getInstance();
            medicineCalendar.set(Calendar.HOUR_OF_DAY, medicine.getHour());
            medicineCalendar.set(Calendar.MINUTE, medicine.getMinute());
            medicineCalendar.set(Calendar.SECOND, 0);
            setMedicineAlarm(medicine);

            if (currentCalendar.after(medicineCalendar)) {
                holder.itemView.setBackgroundColor(Color.RED); // Highlight overdue medication in red
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
            holder.tickView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> showMedicineDetailsDialog(medicine));
    }

    private void showMedicineDetailsDialog(Medicine medicine) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_medicine_details, null);
        builder.setView(dialogView);

        TextView nameTextView = dialogView.findViewById(R.id.dialog_medicine_name);
        TextView quantityTextView = dialogView.findViewById(R.id.dialog_medicine_quantity);
        TextView timeTextView = dialogView.findViewById(R.id.dialog_medicine_time);
        Button eatenButton = dialogView.findViewById(R.id.eaten_button);
        Button editButton = dialogView.findViewById(R.id.edit_button);
        Button deleteButton = dialogView.findViewById(R.id.delete_button);

        nameTextView.setText(medicine.getName());
        quantityTextView.setText("Quantity: " + medicine.getQuantity());
        timeTextView.setText("Time: " + formatTime(medicine.getHour(), medicine.getMinute()));

        AlertDialog dialog = builder.create();

        if (medicine.isLocked()) {
            eatenButton.setVisibility(View.GONE);
        } else {
            eatenButton.setVisibility(View.VISIBLE);
            eatenButton.setOnClickListener(v -> {
                medicine.setLocked(true);
                medicine.setEatenDate(System.currentTimeMillis());
                dbHelper.updateMedicine(medicine);
                notifyDataSetChanged();
                dialog.dismiss();
            });
        }

        deleteButton.setOnClickListener(v -> {
            deleteMedicine(medicine);
            dialog.dismiss();
        });

        editButton.setOnClickListener(v -> {
            dialog.dismiss();
            showEditMedicineDialog(medicine);
        });

        dialog.show();
    }

    private void showEditMedicineDialog(Medicine medicine) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_medicine, null);
        builder.setView(dialogView);

        EditText medicineNameInput = dialogView.findViewById(R.id.medicine_name_input);
        TimePicker timePicker = dialogView.findViewById(R.id.time_picker);
        EditText quantityInput = dialogView.findViewById(R.id.quantity_input);
        Button saveButton = dialogView.findViewById(R.id.schedule_alert_button);

        medicineNameInput.setText(medicine.getName());
        timePicker.setHour(medicine.getHour());
        timePicker.setMinute(medicine.getMinute());
        quantityInput.setText(String.valueOf(medicine.getQuantity()));

        AlertDialog dialog = builder.create();

        saveButton.setOnClickListener(v -> {
            medicine.setName(medicineNameInput.getText().toString());
            medicine.setHour(timePicker.getHour());
            medicine.setMinute(timePicker.getMinute());
            medicine.setQuantity(Integer.parseInt(quantityInput.getText().toString()));
            dbHelper.updateMedicine(medicine);
            setMedicineAlarm(medicine);
            notifyDataSetChanged();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void deleteMedicine(Medicine medicine) {
        dbHelper.deleteMedicine(medicine.getId());
        medicines.remove(medicine);
        notifyDataSetChanged();
    }

    private String formatTime(int hour, int minute) {
        boolean isPM = hour >= 12;
        int hour12 = hour % 12;
        if (hour12 == 0) {
            hour12 = 12;
        }
        String amPm = isPM ? "PM" : "AM";
        return String.format("%02d:%02d %s", hour12, minute, amPm);
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView quantityTextView;
        TextView timeTextView;
        ImageView tickView;

        MedicineViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.medicine_name);
            quantityTextView = itemView.findViewById(R.id.medicine_quantity);
            timeTextView = itemView.findViewById(R.id.medicine_time);
            tickView = itemView.findViewById(R.id.tick_view);
        }
    }

    public void setMedicineAlarm(Medicine medicine) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("notification_type", "medication");
        intent.putExtra("medicine_name", medicine.getName());
        intent.putExtra("medicine_id", medicine.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, medicine.getId(), intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, medicine.getHour());
        calendar.set(Calendar.MINUTE, medicine.getMinute());
        calendar.set(Calendar.SECOND, 0);

        if (alarmManager != null) {
            try {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Log.d("MedicineAdapter", "Alarm set for medicine: " + medicine.getName() + " at " + formatTime(medicine.getHour(), medicine.getMinute()));
            } catch (SecurityException e) {
                Log.e("MedicineAdapter", "SecurityException while setting exact alarm", e);
                // Optionally notify the user or handle the exception accordingly
            }
        }
    }

}
