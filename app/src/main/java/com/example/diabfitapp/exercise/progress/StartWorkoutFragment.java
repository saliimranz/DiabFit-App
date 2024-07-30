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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StartWorkoutFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressExerciseAdapter progressExerciseAdapter;
    public List<Exercise> progressExerciseList;
    private TextView timerTextView;
    private Button pausePlayButton;
    public long startTime = 0L;
    private Handler customHandler = new Handler();
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private boolean isPaused = false;

    private int previousTimeSpent = 0;

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

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        WorkoutSession incompleteSession = checkForIncompleteSession(currentDate);

        if (incompleteSession != null) {
            // Incomplete session found, show dialog
            previousTimeSpent = incompleteSession.getTimeSpent();
            showResumeSessionDialog();
        }

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressExerciseList = fetchExercisesFromDatabase(); // Fetch exercises from the database
        progressExerciseAdapter = new ProgressExerciseAdapter(progressExerciseList, true);
        recyclerView.setAdapter(progressExerciseAdapter);

        onResume();

        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);


    }

    private WorkoutSession checkForIncompleteSession(String date) {
        ProgressDatabaseHelper dbHelper = new ProgressDatabaseHelper(getContext());
        return dbHelper.getIncompleteSessionByDate(date);
    }

    private void showResumeSessionDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Continue Previous Session?")
                .setMessage("You have an incomplete session from today. Would you like to continue?")
                .setPositiveButton("Yes", (dialog, which) -> resumePreviousSession())
                .setNegativeButton("No", (dialog, which) -> ResetSession())
                .show();
    }

    private void resumePreviousSession() {
        startTime = SystemClock.uptimeMillis();
        timeSwapBuff = previousTimeSpent * 1000L; // Convert seconds to milliseconds
        customHandler.postDelayed(updateTimerThread, 0);
        pausePlayButton.setText("Pause");
        isPaused = false;
    }

    private void ResetSession() {
        // Reset completed sets to zero for all exercises in the list
        for (Exercise exercise : progressExerciseList) {
            exercise.setCompletedSets(0);
        }
        // Update the database to reset completed sets to zero
        ProgressDatabaseHelper dbHelper = new ProgressDatabaseHelper(getContext());
        for (Exercise exercise : progressExerciseList) {
            dbHelper.updateExercise(exercise);
        }

        // Update the RecyclerView adapter with the modified list
        progressExerciseAdapter.updateData(progressExerciseList);

        // Reset the timer
        startTime = SystemClock.uptimeMillis();
        timeSwapBuff = 0L;
        customHandler.postDelayed(updateTimerThread, 0);
        pausePlayButton.setText("Pause");
        isPaused = false;
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

    public void refreshRecyclerView() {
        // Notify the adapter that the data set has changed
        progressExerciseAdapter.notifyDataSetChanged();
    }

    private void showExitWarningDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Warning")
                .setMessage("You are in the middle of a workout session. Do you want to go back?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    updateWorkoutSession();
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void saveWorkoutSession() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        int timeSpent = (int) (updatedTime / 1000); // Time in seconds
        double carbsBurnt = calculateCarbsBurnt(timeSpent); // Implement this method based on your logic
        double workoutPercentage = calculateWorkoutPercentage(); // Implement this method based on your logic

        List<String> exerciseNames = new ArrayList<>();
        List<Integer> completedSets = new ArrayList<>();
        List<Integer> targetSets = new ArrayList<>();

        for (Exercise exercise : progressExerciseList) {
            exerciseNames.add(exercise.getName());
            completedSets.add(exercise.getCompletedSets());
            targetSets.add(exercise.getSets());
        }

        WorkoutSession session = new WorkoutSession(0, currentDate, timeSpent, carbsBurnt, workoutPercentage, exerciseNames, completedSets, targetSets);
        ProgressDatabaseHelper dbHelper = new ProgressDatabaseHelper(getContext());
        dbHelper.insertOrUpdateWorkoutSession(session);
    }

    // Update methods to save workout session
    private void updateWorkoutSession() {
        saveWorkoutSession();
    }

    private double calculateCarbsBurnt(int timeSpent) {
        // Your logic to calculate carbs burnt based on time spent
        return timeSpent * 0.1; // Example calculation
    }

    private double calculateWorkoutPercentage() {
        int totalSets = 0;
        int completedSets = 0;
        for (Exercise exercise : progressExerciseList) {
            totalSets += exercise.getSets();
            completedSets += exercise.getCompletedSets();
        }
        return totalSets == 0 ? 0 : (double) completedSets / totalSets * 100;
    }
}
