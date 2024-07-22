package com.example.diabfitapp.healthmonitoring;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.diabfitapp.R;
import com.example.diabfitapp.healthmonitoring.sugerlog.SugerLoggingFragment;
import com.example.diabfitapp.main.MainActivity;

public class HealthMonitoringFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health_monitoring, container, false);

        // Setup toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Health Monitoring");
        toolbar.setNavigationIcon(R.drawable.ic_back);  // Ensure this icon is properly sized and placed in drawable folders
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Buttons
        Button bloodSugarLoggingButton = view.findViewById(R.id.btn_blood_sugar_logging);
        Button healthInsightsButton = view.findViewById(R.id.btn_health_insights);
        Button medicationRemindersButton = view.findViewById(R.id.btn_medication_reminders);
        Button emergencyAlertsButton = view.findViewById(R.id.btn_emergency_alerts);

        bloodSugarLoggingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new SugerLoggingFragment());
            }
        });

        healthInsightsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new HealthInsightsFragment());
            }
        });

        medicationRemindersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new MedicationRemindersFragment());
            }
        });

        emergencyAlertsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new EmergencyAlertsFragment());
            }
        });

        return view;
    }
}
