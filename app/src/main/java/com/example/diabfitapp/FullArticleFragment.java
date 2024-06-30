package com.example.diabfitapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FullArticleFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_AUTHOR = "author";
    private static final String ARG_DATE = "date";
    private static final String ARG_CONTENT = "content";
    private static final String ARG_IMAGE_RES_NAME = "imageResName";

    public static FullArticleFragment newInstance(Article article) {
        FullArticleFragment fragment = new FullArticleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, article.getTitle());
        args.putString(ARG_AUTHOR, article.getAuthor());
        args.putString(ARG_DATE, article.getDate());
        args.putString(ARG_CONTENT, article.getContent());
        args.putString(ARG_IMAGE_RES_NAME, article.getImageResName());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_full_article, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView titleTextView = view.findViewById(R.id.full_article_title);
        TextView authorTextView = view.findViewById(R.id.full_article_author);
        TextView dateTextView = view.findViewById(R.id.full_article_date);
        TextView contentTextView = view.findViewById(R.id.full_article_content);
        ImageView imageView = view.findViewById(R.id.full_article_image);

        if (getArguments() != null) {
            titleTextView.setText(getArguments().getString(ARG_TITLE));
            authorTextView.setText(getArguments().getString(ARG_AUTHOR));
            dateTextView.setText(getArguments().getString(ARG_DATE));
            contentTextView.setText(getArguments().getString(ARG_CONTENT));

            int imageResId = getContext().getResources().getIdentifier(getArguments().getString(ARG_IMAGE_RES_NAME), "drawable", getContext().getPackageName());
            imageView.setImageResource(imageResId);
        }
    }
}
