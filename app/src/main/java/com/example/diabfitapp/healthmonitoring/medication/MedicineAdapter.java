package com.example.diabfitapp.healthmonitoring.medication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diabfitapp.R;

import java.util.List;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private List<Medicine> medicines;
    private Context context;
    private MedicineDatabaseHelper DatabaseHelper;

    public MedicineAdapter(Context context, List<Medicine> medicines) {
        this.medicines = medicines;
        this.context = context;
        this.DatabaseHelper = new MedicineDatabaseHelper(context);
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

        deleteButton.setOnClickListener(v -> {
            deleteMedicine(medicine);
            dialog.dismiss();
        });

        // Implement other button functionalities later
        eatenButton.setOnClickListener(v -> {
            // TODO: Implement Eaten functionality
        });

        editButton.setOnClickListener(v -> {
            // TODO: Implement Edit functionality
        });

        dialog.show();
    }

    private void deleteMedicine(Medicine medicine) {
        // Delete from SQLite database
        DatabaseHelper.deleteMedicine(medicine.getId());

        // Update RecyclerView
        medicines.remove(medicine);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
        notifyDataSetChanged();
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView quantityTextView;
        TextView timeTextView;

        MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.medicine_name);
            quantityTextView = itemView.findViewById(R.id.medicine_quantity);
            timeTextView = itemView.findViewById(R.id.medicine_time);
        }
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
}
