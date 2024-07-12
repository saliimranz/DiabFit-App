package com.example.diabfitapp.education;

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
import com.example.diabfitapp.education.communityforum.PostListFragment;
import com.example.diabfitapp.education.diabedu.DiabatesEducationFragment;
import com.example.diabfitapp.main.MainActivity;

public class EducationSupportFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_education_support, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
        toolbar.setTitle("Education Support");

        // Buttons
        Button diabetesEducationButton = view.findViewById(R.id.btn_diabetes_education);
        Button communityForumButton = view.findViewById(R.id.btn_community_forum);

        diabetesEducationButton.setOnClickListener(v -> {
            // Handle Diabetes Education button click
            ((MainActivity) getActivity()).replaceFragment(new DiabatesEducationFragment());
        });

        communityForumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Community Forum button click
                ((MainActivity) getActivity()).replaceFragment(new PostListFragment());
            }
        });

        return view;
    }
}
