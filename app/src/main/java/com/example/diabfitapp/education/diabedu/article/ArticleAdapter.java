package com.example.diabfitapp.education.diabedu.article;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabfitapp.R;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articleList;
    private OnItemClickListener listener;

    public ArticleAdapter(List<Article> articleList, OnItemClickListener listener) {
        this.articleList = articleList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articleList.get(position);
        int imageResId = holder.itemView.getContext().getResources().getIdentifier(article.getImageResName(), "drawable", holder.itemView.getContext().getPackageName());
        holder.articleImageView.setImageResource(imageResId);
        holder.articleTitleTextView.setText(article.getTitle());
        holder.articleDescriptionTextView.setText(article.getContent().substring(0, Math.min(100, article.getContent().length())) + "...");
        holder.itemView.setOnClickListener(v -> listener.onItemClick(article));
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Article article);
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        ImageView articleImageView;
        TextView articleTitleTextView;
        TextView articleDescriptionTextView;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImageView = itemView.findViewById(R.id.article_image);
            articleTitleTextView = itemView.findViewById(R.id.article_title);
            articleDescriptionTextView = itemView.findViewById(R.id.article_description);
        }
    }
}
