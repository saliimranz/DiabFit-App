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
        videos.add(new VideoItem("Video 1", "Description 1", R.drawable.ic_video_placeholder, "https://dummyurl.com/video1.mp4"));
        videos.add(new VideoItem("Video 2", "Description 2", R.drawable.ic_video_placeholder, "https://dummyurl.com/video2.mp4"));
        videos.add(new VideoItem("Video 3", "Description 3", R.drawable.ic_video_placeholder, "https://dummyurl.com/video3.mp4"));
        videos.add(new VideoItem("Video 1", "Description 1", R.drawable.ic_video_placeholder, "https://dummyurl.com/video1.mp4"));
        videos.add(new VideoItem("Video 2", "Description 2", R.drawable.ic_video_placeholder, "https://dummyurl.com/video2.mp4"));
        videos.add(new VideoItem("Video 3", "Description 3", R.drawable.ic_video_placeholder, "https://dummyurl.com/video3.mp4"));

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
