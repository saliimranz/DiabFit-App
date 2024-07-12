package com.example.diabfitapp.education.diabedu.tutorial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabfitapp.R;

import java.util.List;

public class TutorialAdapter extends RecyclerView.Adapter<TutorialAdapter.TutorialViewHolder> {

    private List<Tutorial> tutorialList;
    private OnItemClickListener listener;

    public TutorialAdapter(List<Tutorial> tutorialList, OnItemClickListener listener) {
        this.tutorialList = tutorialList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TutorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutorial, parent, false);
        return new TutorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorialViewHolder holder, int position) {
        Tutorial tutorial = tutorialList.get(position);
        int imageResId = holder.itemView.getContext().getResources().getIdentifier(tutorial.getImageName(), "drawable", holder.itemView.getContext().getPackageName());
        holder.tutorialImageView.setImageResource(imageResId);
        holder.tutorialTitleTextView.setText(tutorial.getTitle());
        holder.tutorialDescriptionTextView.setText(tutorial.getDescription().substring(0, Math.min(100, tutorial.getDescription().length())) + "...");
        holder.itemView.setOnClickListener(v -> listener.onItemClick(tutorial));
    }

    @Override
    public int getItemCount() {
        return tutorialList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Tutorial tutorial);
    }

    static class TutorialViewHolder extends RecyclerView.ViewHolder {
        ImageView tutorialImageView;
        TextView tutorialTitleTextView;
        TextView tutorialDescriptionTextView;

        public TutorialViewHolder(@NonNull View itemView) {
            super(itemView);
            tutorialImageView = itemView.findViewById(R.id.tutorial_image);
            tutorialTitleTextView = itemView.findViewById(R.id.tutorial_title);
            tutorialDescriptionTextView = itemView.findViewById(R.id.tutorial_description);
        }
    }
}
