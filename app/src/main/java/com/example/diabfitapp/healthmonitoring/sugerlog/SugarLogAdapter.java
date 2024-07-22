package com.example.diabfitapp.healthmonitoring.sugerlog;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabfitapp.R;

import java.util.List;

public class SugarLogAdapter extends RecyclerView.Adapter<SugarLogAdapter.SugarLogViewHolder> {

    private List<SugarLog> sugarLogList;

    public SugarLogAdapter(List<SugarLog> sugarLogList) {
        this.sugarLogList = sugarLogList;
    }

    @NonNull
    @Override
    public SugarLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sugar_log, parent, false);
        return new SugarLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SugarLogViewHolder holder, int position) {
        SugarLog sugarLog = sugarLogList.get(position);
        holder.sugarLevelTextView.setText(String.valueOf(sugarLog.getSugarLevel()));
        holder.typeTextView.setText(sugarLog.getType());
        holder.timeTextView.setText(sugarLog.getTime());

        int sugarLevel = sugarLog.getSugarLevel();
        String type = sugarLog.getType();

        if (type.equals("Fasting Blood Sugar (FBS)")) {
            if (sugarLevel < 70) {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFA500")); // Orange
            } else if (sugarLevel <= 100) {
                holder.itemView.setBackgroundColor(Color.parseColor("#008000")); // Green
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FF0000")); // Red
            }
        } else if (type.equals("Postprandial Blood Sugar (PPBS)")) {
            if (sugarLevel < 70) {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFA500")); // Orange
            } else if (sugarLevel < 140) {
                holder.itemView.setBackgroundColor(Color.parseColor("#008000")); // Green
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FF0000")); // Red
            }
        }
    }

    @Override
    public int getItemCount() {
        return sugarLogList.size();
    }

    static class SugarLogViewHolder extends RecyclerView.ViewHolder {

        TextView sugarLevelTextView;
        TextView typeTextView;
        TextView timeTextView;

        public SugarLogViewHolder(@NonNull View itemView) {
            super(itemView);
            sugarLevelTextView = itemView.findViewById(R.id.sugar_level_text_view);
            typeTextView = itemView.findViewById(R.id.type_text_view);
            timeTextView = itemView.findViewById(R.id.time_text_view);
        }
    }
}
