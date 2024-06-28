package com.example.diabfitapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;


public class PostListFragment extends Fragment {

    public PostListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Post> posts = new ArrayList<>();
        posts.add(new Post(R.drawable.ic_profile1, "Many Variations of Passages of Lorem Ipsum Available","John Doe", "4 Hours Ago", "Suffered alteration in some form...", "Business", 456, 352));
        posts.add(new Post(R.drawable.ic_profile2, "You are going to use a passage of Lorem Ipsum","Regina Falangie", "9 Hours Ago", "You need to be sure there isn’t anything embarrassing hidden...", "Technology", 543, 243));
        posts.add(new Post(R.drawable.ic_profile3, "Many Variations of Passages of Lorem Ipsum Available","John Doe", "4 Hours Ago", "Suffered alteration in some form...", "Business", 456, 352));
        posts.add(new Post(R.drawable.ic_profile4, "You are going to use a passage of Lorem Ipsum","Regina Falangie", "9 Hours Ago", "You need to be sure there isn’t anything embarrassing hidden...", "Technology", 543, 243));
        posts.add(new Post(R.drawable.ic_profile5, "Many Variations of Passages of Lorem Ipsum Available","John Doe", "4 Hours Ago", "Suffered alteration in some form...", "Business", 456, 352));
        posts.add(new Post(R.drawable.ic_profile6, "You are going to use a passage of Lorem Ipsum","Regina Falangie", "9 Hours Ago", "You need to be sure there isn’t anything embarrassing hidden...", "Technology", 543, 243));
        posts.add(new Post(R.drawable.ic_profile7, "Many Variations of Passages of Lorem Ipsum Available","John Doe", "4 Hours Ago", "Suffered alteration in some form...", "Business", 456, 352));
        posts.add(new Post(R.drawable.ic_profile8, "You are going to use a passage of Lorem Ipsum","Regina Falangie", "9 Hours Ago", "You need to be sure there isn’t anything embarrassing hidden...", "Technology", 543, 243));
        PostAdapter adapter = new PostAdapter(posts);
        recyclerView.setAdapter(adapter);

        return view;
    }
}

