package com.example.synergii;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.synergii.Adapters.RecyclerAdapter;
import com.example.synergii.models.User;
import com.google.firebase.database.annotations.Nullable;
import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class WorkSpaceFragment extends Fragment {
    public static final String TAG = "WorkSpaceFragment";

    private RecyclerView recyclerViewHome;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_space, container, false);
        Log.d(TAG, "onCreateView: started");

        recyclerViewHome = (RecyclerView) view.findViewById(R.id.recyclerViewHome);
        setupRecyclerView(view);
        return view;
    }

    private void setupRecyclerView(View view){

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewHome.setLayoutManager(layoutManager);
    }


}
