package com.example.diabfitapp.education.diabedu.tutorial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diabfitapp.R;

public class FullTutorialFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_AUTHOR = "author";
    private static final String ARG_DATE = "date";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_IMAGE_NAME = "imageName";

    public static FullTutorialFragment newInstance(Tutorial tutorial) {
        FullTutorialFragment fragment = new FullTutorialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, tutorial.getTitle());
        args.putString(ARG_AUTHOR, tutorial.getAuthor());
        args.putString(ARG_DATE, tutorial.getDate());
        args.putString(ARG_DESCRIPTION, tutorial.getDescription());
        args.putString(ARG_IMAGE_NAME, tutorial.getImageName());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_full_tutorial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imageView = view.findViewById(R.id.full_tutorial_image);
        TextView titleTextView = view.findViewById(R.id.full_tutorial_title);
        TextView authorTextView = view.findViewById(R.id.full_tutorial_author);
        TextView dateTextView = view.findViewById(R.id.full_tutorial_date);
        TextView descriptionTextView = view.findViewById(R.id.full_tutorial_description);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String imageName = bundle.getString(ARG_IMAGE_NAME);
            imageView.setImageResource(getResources().getIdentifier(imageName, "drawable", getContext().getPackageName()));
            titleTextView.setText(bundle.getString(ARG_TITLE));
            authorTextView.setText(bundle.getString(ARG_AUTHOR));
            dateTextView.setText(bundle.getString(ARG_DATE));
            descriptionTextView.setText(bundle.getString(ARG_DESCRIPTION));
        }
    }
}
