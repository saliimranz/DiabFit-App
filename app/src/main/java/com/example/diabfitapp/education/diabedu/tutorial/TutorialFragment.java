package com.example.diabfitapp.education.diabedu.tutorial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabfitapp.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class TutorialFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutorial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new TutorialAdapter(loadTutorials(), this::onTutorialClicked));
    }

    private List<Tutorial> loadTutorials() {
        List<Tutorial> tutorials = new ArrayList<>();
        try {
            InputStream inputStream = getContext().getAssets().open("tutorials.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONObject jsonObject = new JSONObject(jsonString.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray tutorialArray = jsonArray.getJSONArray(i);
                String title = tutorialArray.getString(0);
                String author = tutorialArray.getString(1);
                String date = tutorialArray.getString(2);
                String description = tutorialArray.getString(3);
                String imageResName = tutorialArray.getString(4);

                tutorials.add(new Tutorial(title, author, date, description, imageResName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tutorials;
    }

    private void onTutorialClicked(Tutorial tutorial) {
        FullTutorialFragment fullTutorialFragment = FullTutorialFragment.newInstance(tutorial);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fullTutorialFragment)
                .addToBackStack(null)
                .commit();
    }
}
