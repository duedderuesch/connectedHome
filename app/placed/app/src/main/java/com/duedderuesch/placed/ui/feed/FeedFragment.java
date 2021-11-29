package com.duedderuesch.placed.ui.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.duedderuesch.placed.R;

public class FeedFragment extends Fragment {

    private com.duedderuesch.placed.ui.feed.FeedViewModel FeedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FeedViewModel =
                new ViewModelProvider(this).get(com.duedderuesch.placed.ui.feed.FeedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_feed, container, false);
        final TextView textView = root.findViewById(R.id.tv_feed_title);
        FeedViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}