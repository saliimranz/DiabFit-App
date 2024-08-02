// VideoFragment.java
package com.example.diabfitapp.education.diabedu.video;

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

import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment implements VideoAdapter.OnItemClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new VideoAdapter(getDummyData(), this));
    }

    private List<VideoItem> getDummyData() {
        List<VideoItem> videos = new ArrayList<>();
        videos.add(new VideoItem("Insullin Titration", "Titration Procedure for the Insulin", R.drawable.insulin_1, "https://diabfit-data-uploads.s3.us-east-2.amazonaws.com/Insulin+titration.mp4"));
        videos.add(new VideoItem("Insulin Safety & Insulin prescribing standards", "Disscussion about Insulin Safety Procedures taken by doctors and their mistakes along the way", R.drawable.ic_2_1, "https://diabfit-data-uploads.s3.us-east-2.amazonaws.com/Insulin+safety_+Insulin+prescribing+standards.mp4"));
        videos.add(new VideoItem("Type 1 Diabetes Nutrition", "A comprehensive diet plan for type 1 diabates patients for healthy lifestyle", R.drawable.ic_3_1, "https://diabfit-data-uploads.s3.us-east-2.amazonaws.com/What+All+Parents+Need+to+Know+About+Type+1+Diabetes+Nutrition.mp4 "));
        videos.add(new VideoItem("Insullin Titration", "Titration Procedure for the Insulin", R.drawable.insulin_2, "https://diabfit-data-uploads.s3.us-east-2.amazonaws.com/Insulin+titration.mp4"));
        videos.add(new VideoItem("Insulin Safety & Insulin prescribing standards", "Disscussion about Insulin Safety Procedures taken by doctors and their mistakes along the way", R.drawable.ic_2_2, "https://diabfit-data-uploads.s3.us-east-2.amazonaws.com/Insulin+safety_+Insulin+prescribing+standards.mp4"));
        videos.add(new VideoItem("Type 1 Diabetes Nutrition", "A comprehensive diet plan for type 1 diabates patients for healthy lifestyle", R.drawable.ic_3_2, "https://diabfit-data-uploads.s3.us-east-2.amazonaws.com/What+All+Parents+Need+to+Know+About+Type+1+Diabetes+Nutrition.mp4 "));

        return videos;
    }

    @Override
    public void onItemClick(VideoItem videoItem) {
        VideoPlayerFragment videoPlayerFragment = VideoPlayerFragment.newInstance(videoItem.getVideoUrl());
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, videoPlayerFragment)
                .addToBackStack(null)
                .commit();
    }
}
