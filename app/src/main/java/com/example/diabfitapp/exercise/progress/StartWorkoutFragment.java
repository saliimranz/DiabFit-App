package com.example.diabfitapp.exercise.progress;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diabfitapp.R;
import com.example.diabfitapp.exercise.workout.Exercise;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StartWorkoutFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressExerciseAdapter progressExerciseAdapter;
    private List<Exercise> progressExerciseList;
    private TextView timerTextView;
    private Button pausePlayButton;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private boolean isPaused = false;

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            if (!isPaused) {
                timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
                updatedTime = timeSwapBuff + timeInMilliseconds;

                int secs = (int) (updatedTime / 1000);
                int mins = secs / 60;
                int hrs = mins / 60;
                secs = secs % 60;
                mins = mins % 60;
                timerTextView.setText(String.format("%02d:%02d:%02d", hrs, mins, secs));
                customHandler.postDelayed(this, 0);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_workout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Start Workout");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> showExitWarningDialog());

        timerTextView = view.findViewById(R.id.timer_text_view);
        pausePlayButton = view.findViewById(R.id.pause_play_button);
        pausePlayButton.setOnClickListener(v -> {
            if (isPaused) {
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);
                pausePlayButton.setText("Pause");
                isPaused = false;
            } else {
                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);
                pausePlayButton.setText("Play");
                isPaused = true;
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressExerciseList = fetchExercisesFromDatabase(); // Fetch exercises from the database
        progressExerciseAdapter = new ProgressExerciseAdapter(progressExerciseList, true);
        recyclerView.setAdapter(progressExerciseAdapter);

        onResume();

        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    private List<Exercise> fetchExercisesFromDatabase() {
        ProgressDatabaseHelper dbHelper = new ProgressDatabaseHelper(getContext());
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return dbHelper.getExercisesByDate(currentDate);
    }

    public void onResume() {
        super.onResume();
        progressExerciseList = fetchExercisesFromDatabase(); // Fetch the latest exercises
        progressExerciseAdapter.updateData(progressExerciseList); // Update the adapter
    }

    private void showExitWarningDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Warning")
                .setMessage("Your progress will be lost if you continue. Do you want to go back?")
                .setPositiveButton("Yes", (dialog, which) -> requireActivity().getSupportFragmentManager().popBackStack())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
