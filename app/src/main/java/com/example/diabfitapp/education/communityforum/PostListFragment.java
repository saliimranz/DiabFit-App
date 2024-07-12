package com.example.diabfitapp.education.communityforum;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;

import com.example.diabfitapp.R;
import com.example.diabfitapp.main.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class PostListFragment extends Fragment {

    private FloatingActionButton fab;
    private final androidx.fragment.app.FragmentManager.OnBackStackChangedListener backStackListener = () -> {
        if (isAdded()) {
            Fragment currentFragment = requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (currentFragment instanceof PostListFragment) {
                fab.setVisibility(View.VISIBLE);
            } else {
                fab.setVisibility(View.GONE);
            }
        }
    };

    public PostListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        // Toolbar setup
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Community Forum");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        // FloatingActionButton setup
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            ((MainActivity) getActivity()).replaceFragment(new CreatePostFragment());
            fab.setVisibility(View.GONE);
        });

        // RecyclerView setup
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Post> posts = new ArrayList<>();
        posts.add(new Post(R.drawable.ic_profile1, "Many Variations of Passages of Lorem Ipsum Available", "John Doe", "4 Hours Ago", "Suffered alteration in some form...", "Business", 456, 352));
        posts.add(new Post(R.drawable.ic_profile2, "You are going to use a passage of Lorem Ipsum", "Regina Falangie", "9 Hours Ago", "You need to be sure there isn’t anything embarrassing hidden...", "Technology", 543, 243));
        posts.add(new Post(R.drawable.ic_profile3, "Many Variations of Passages of Lorem Ipsum Available", "John Doe", "4 Hours Ago", "Suffered alteration in some form...", "Business", 456, 352));
        posts.add(new Post(R.drawable.ic_profile4, "You are going to use a passage of Lorem Ipsum", "Regina Falangie", "9 Hours Ago", "You need to be sure there isn’t anything embarrassing hidden...", "Technology", 543, 243));
        posts.add(new Post(R.drawable.ic_profile5, "Many Variations of Passages of Lorem Ipsum Available", "John Doe", "4 Hours Ago", "Suffered alteration in some form...", "Business", 456, 352));
        posts.add(new Post(R.drawable.ic_profile6, "You are going to use a passage of Lorem Ipsum", "Regina Falangie", "9 Hours Ago", "You need to be sure there isn’t anything embarrassing hidden...", "Technology", 543, 243));
        posts.add(new Post(R.drawable.ic_profile7, "Many Variations of Passages of Lorem Ipsum Available", "John Doe", "4 Hours Ago", "Suffered alteration in some form...", "Business", 456, 352));
        posts.add(new Post(R.drawable.ic_profile8, "You are going to use a passage of Lorem Ipsum", "Regina Falangie", "9 Hours Ago", "You need to be sure there isn’t anything embarrassing hidden...", "Technology", 543, 243));
        PostAdapter adapter = new PostAdapter(posts);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getSupportFragmentManager().addOnBackStackChangedListener(backStackListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().getSupportFragmentManager().removeOnBackStackChangedListener(backStackListener);
    }
}
