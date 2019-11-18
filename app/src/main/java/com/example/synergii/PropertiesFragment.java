package com.example.synergii;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.synergii.Adapters.AgentMyPropertiesAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class PropertiesFragment extends Fragment implements AgentMyPropertiesAdapter.OnNoteListener
{
View v;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_properties, container, false);
        super.onCreate(savedInstanceState);

        RecyclerView agentMyPropertiesList = (RecyclerView) v.findViewById(R.id.agentMyPropertiesList);
        agentMyPropertiesList.setLayoutManager(new LinearLayoutManager(getContext()));
        String[] data = {"property1", "property2", "property3", "property4", "property5", "property6", "property7", "property8"};
        agentMyPropertiesList.setAdapter(new AgentMyPropertiesAdapter(data, this));

        return v;
    }

    @Override
    public void onNoteClick(int position) {

        Intent startIntent = new Intent(getContext(), PropertyDetailActivity.class);
        startActivity(startIntent);
    }

}
