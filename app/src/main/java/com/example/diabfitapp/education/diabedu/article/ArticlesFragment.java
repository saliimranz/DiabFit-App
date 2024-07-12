package com.example.diabfitapp.education.diabedu.article;

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

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        recyclerView.setAdapter(new ArticleAdapter(loadArticles(), this::onArticleClicked));
    }

    private List<Article> loadArticles() {
        List<Article> articles = new ArrayList<>();
        try {
            InputStream inputStream = getContext().getAssets().open("articles.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONObject jsonObject = new JSONObject(jsonString.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray articleArray = jsonArray.getJSONArray(i);
                String title = articleArray.getString(0);
                String author = articleArray.getString(1);
                String date = articleArray.getString(2);
                String content = articleArray.getString(3);
                String imageResName = articleArray.getString(4);

                articles.add(new Article(title, author, date, content, imageResName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }

    private void onArticleClicked(Article article) {
        FullArticleFragment fullArticleFragment = FullArticleFragment.newInstance(article);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fullArticleFragment)
                .addToBackStack(null)
                .commit();
    }
}
