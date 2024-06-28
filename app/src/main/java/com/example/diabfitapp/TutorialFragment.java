package com.example.diabfitapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

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
        recyclerView.setAdapter(new TutorialAdapter(getDummyData()));
    }

    private List<Tutorial> getDummyData() {
        List<Tutorial> tutorials = new ArrayList<>();
        tutorials.add(new Tutorial("Tutorial 1", "Description 1", R.drawable.ic_tutorial_placeholder));
        tutorials.add(new Tutorial("Tutorial 2", "Description 2", R.drawable.ic_tutorial_placeholder));
        tutorials.add(new Tutorial("Tutorial 3", "Description 1", R.drawable.ic_tutorial_placeholder));
        tutorials.add(new Tutorial("Tutorial 4", "Description 2", R.drawable.ic_tutorial_placeholder));
        tutorials.add(new Tutorial("Tutorial 5", "Description 1", R.drawable.ic_tutorial_placeholder));
        tutorials.add(new Tutorial("Tutorial 6", "Description 2", R.drawable.ic_tutorial_placeholder));
        tutorials.add(new Tutorial("Tutorial 7", "Description 1", R.drawable.ic_tutorial_placeholder));
        tutorials.add(new Tutorial("Tutorial 8", "Description 2", R.drawable.ic_tutorial_placeholder));
        // Add more dummy videos
        return tutorials;
    }
}
