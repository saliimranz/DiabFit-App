package com.example.diabfitapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TutorialAdapter extends RecyclerView.Adapter<TutorialAdapter.TutorialViewHolder> {

    private List<Tutorial> tutorialList;

    public TutorialAdapter(List<Tutorial> tutorialList) {
        this.tutorialList = tutorialList;
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
        holder.tutorialImageView.setImageResource(tutorial.getImageResId());
        holder.tutorialTitleTextView.setText(tutorial.getTitle());
        holder.tutorialDescriptionTextView.setText(tutorial.getDescription());
    }

    @Override
    public int getItemCount() {
        return tutorialList.size();
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
