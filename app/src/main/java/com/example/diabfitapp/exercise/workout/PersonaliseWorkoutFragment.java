package com.example.diabfitapp.exercise.workout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.EditText;
import com.example.diabfitapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PersonaliseWorkoutFragment extends Fragment {
    private RecyclerView recyclerView;
    private ExerciseAdapter exerciseAdapter;
    private List<Exercise> exerciseList = new ArrayList<>();
    private List<Exercise> filteredExerciseList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personalise_workout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Personalise Workout");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        exerciseAdapter = new ExerciseAdapter(exerciseList, getContext());
        recyclerView.setAdapter(exerciseAdapter);

        EditText searchBar = view.findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterExercises(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed
            }
        });

        // Start fetching the exercises
        new FetchExercisesTask().execute();
    }

    private void filterExercises(String query) {
        filteredExerciseList.clear();
        for (Exercise exercise : exerciseList) {
            if (exercise.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredExerciseList.add(exercise);
            }
        }
        exerciseAdapter.notifyDataSetChanged();
    }

    private class FetchExercisesTask extends AsyncTask<Void, Void, List<Exercise>> {
        @Override
        protected List<Exercise> doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://exercisedb.p.rapidapi.com/exercises?")
                    .get()
                    .addHeader("x-rapidapi-key", "eb54444ba3mshca0a0e83cf41280p15a72bjsn9b6d1db414b8")
                    .addHeader("x-rapidapi-host", "exercisedb.p.rapidapi.com")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseData);
                    List<Exercise> exercises = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String bodyPart = jsonObject.getString("bodyPart");
                        String equipment = jsonObject.getString("equipment");
                        String gifUrl = jsonObject.getString("gifUrl");
                        String target = jsonObject.getString("target");
                        String secondaryMuscles = jsonObject.getString("secondaryMuscles");
                        String instructions = jsonObject.getString("instructions");
                        exercises.add(new Exercise(id, name, bodyPart, equipment, gifUrl, target, secondaryMuscles, instructions,0,0));
                    }
                    return exercises;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Exercise> exercises) {
            if (exercises != null) {
                exerciseList.clear();
                exerciseList.addAll(exercises);
                filteredExerciseList.clear();
                filteredExerciseList.addAll(exercises);
                exerciseAdapter.notifyDataSetChanged();
            }
        }
    }
}
