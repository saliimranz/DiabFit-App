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

public class ArticlesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_articles, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ArticleAdapter(getDummyData()));
    }

    private List<Article> getDummyData() {
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("Title 1", "Description 1", R.drawable.ic_article_placeholder));
        articles.add(new Article("Title 2", "Description 2", R.drawable.ic_article_placeholder));
        articles.add(new Article("Title 3", "Description 1", R.drawable.ic_article_placeholder));
        articles.add(new Article("Title 4", "Description 2", R.drawable.ic_article_placeholder));
        articles.add(new Article("Title 5", "Description 1", R.drawable.ic_article_placeholder));
        articles.add(new Article("Title 6", "Description 2", R.drawable.ic_article_placeholder));
        articles.add(new Article("Title 7", "Description 1", R.drawable.ic_article_placeholder));
        articles.add(new Article("Title 8", "Description 2", R.drawable.ic_article_placeholder));
        // Add more dummy articles
        return articles;
    }
}
