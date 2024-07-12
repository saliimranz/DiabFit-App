package com.example.diabfitapp.education.communityforum;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabfitapp.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.profileImage.setImageResource(post.getProfileImage());
        holder.username.setText(post.getUsername());
        holder.title.setText(post.getTitle());
        holder.time.setText(post.getTime());
        holder.content.setText(post.getContent());
        holder.category.setText(post.getCategory());
        holder.views.setText(post.getViews() + " Views");
        holder.replies.setText(post.getReplies() + " Replies");
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView username;
        TextView title;
        TextView time;
        TextView content;
        TextView category;
        TextView views;
        TextView replies;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.post_username);
            title = itemView.findViewById(R.id.post_title);
            time = itemView.findViewById(R.id.post_time);
            content = itemView.findViewById(R.id.post_content);
            category = itemView.findViewById(R.id.post_category);
            views = itemView.findViewById(R.id.post_views);
            replies = itemView.findViewById(R.id.post_replies);
        }
    }
}
