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


public class VideoFragment extends Fragment {
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
        recyclerView.setAdapter(new VideoAdapter(getDummyData()));
    }

    private List<Video> getDummyData() {
        List<Video> videos = new ArrayList<>();
        videos.add(new Video("Video 1", "Description 1", R.drawable.ic_video_placeholder));
        videos.add(new Video("Video 2", "Description 2", R.drawable.ic_video_placeholder));
        videos.add(new Video("Video 3", "Description 1", R.drawable.ic_video_placeholder));
        videos.add(new Video("Video 4", "Description 2", R.drawable.ic_video_placeholder));
        videos.add(new Video("Video 5", "Description 1", R.drawable.ic_video_placeholder));
        videos.add(new Video("Video 6", "Description 2", R.drawable.ic_video_placeholder));
        videos.add(new Video("Video 7", "Description 1", R.drawable.ic_video_placeholder));
        videos.add(new Video("Video 8", "Description 2", R.drawable.ic_video_placeholder));
        // Add more dummy videos
        return videos;
    }
}








