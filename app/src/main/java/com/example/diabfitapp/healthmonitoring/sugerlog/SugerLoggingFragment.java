package com.example.diabfitapp.healthmonitoring.sugerlog;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabfitapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SugerLoggingFragment extends Fragment {

    private SugarLogAdapter sugarLogAdapter;
    private List<SugarLog> sugarLogList = new ArrayList<>();
    private SugarLogDatabaseHelper sugarLogDatabaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_suger_logging, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Blood Sugar Logging");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> {
            // Navigate back to the previous fragment
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        sugarLogDatabaseHelper = new SugarLogDatabaseHelper(requireContext());

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        sugarLogAdapter = new SugarLogAdapter(sugarLogList);
        recyclerView.setAdapter(sugarLogAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> showAddSugerLogDialog());

        loadSugarLogs();
    }

    private void showAddSugerLogDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_suger_log, null);
        builder.setView(dialogView);

        EditText sugarInput = dialogView.findViewById(R.id.sugar_input);
        Spinner typeSpinner = dialogView.findViewById(R.id.type_spinner);
        Button addButton = dialogView.findViewById(R.id.add_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.sugar_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        AlertDialog dialog = builder.create();

        addButton.setOnClickListener(v -> {
            int sugarLevel = Integer.parseInt(sugarInput.getText().toString());
            String type = typeSpinner.getSelectedItem().toString();
            String time = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

            SugarLog sugarLog = new SugarLog(sugarLevel, type, time);
            saveSugarLog(sugarLog);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void saveSugarLog(SugarLog sugarLog) {
        AsyncTask.execute(() -> {
            sugarLogDatabaseHelper.addSugarLog(sugarLog);
            requireActivity().runOnUiThread(() -> {
                sugarLogList.add(0, sugarLog);
                sugarLogAdapter.notifyItemInserted(0);
            });
        });
    }

    private void loadSugarLogs() {
        AsyncTask.execute(() -> {
            List<SugarLog> logs = sugarLogDatabaseHelper.getAllSugarLogs();
            requireActivity().runOnUiThread(() -> {
                sugarLogList.clear();
                sugarLogList.addAll(logs);
                sugarLogAdapter.notifyDataSetChanged();
            });
        });
    }
}
