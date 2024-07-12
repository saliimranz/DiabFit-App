package com.example.diabfitapp.education.communityforum;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;

import com.example.diabfitapp.R;


public class CreatePostFragment extends Fragment {

    public CreatePostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        EditText editText = view.findViewById(R.id.edit_text);
        Button shareButton = view.findViewById(R.id.share_button);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // For now, just go back to the post list fragment
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
}