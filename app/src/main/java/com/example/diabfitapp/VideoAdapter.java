// VideoAdapter.java
package com.example.diabfitapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(VideoItem videoItem);
    }

    private List<VideoItem> videoList;
    private OnItemClickListener listener;

    public VideoAdapter(List<VideoItem> videoList, OnItemClickListener listener) {
        this.videoList = videoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videos, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoItem videoItem = videoList.get(position);
        holder.videoImageView.setImageResource(videoItem.getImageResId());
        holder.videoTitleTextView.setText(videoItem.getTitle());
        holder.videoDescriptionTextView.setText(videoItem.getDescription());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(videoItem));
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView videoImageView;
        TextView videoTitleTextView;
        TextView videoDescriptionTextView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoImageView = itemView.findViewById(R.id.imageView);
            videoTitleTextView = itemView.findViewById(R.id.video_title);
            videoDescriptionTextView = itemView.findViewById(R.id.video_description);
        }
    }
}
