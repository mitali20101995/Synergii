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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.synergii.Adapters.AgentSearchPropertiesRecyclerAdapter;

public class SearchFragment extends Fragment implements AgentSearchPropertiesRecyclerAdapter.OnNoteListener, AdapterView.OnItemSelectedListener{
    View v;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            v = inflater.inflate(R.layout.fragment_search, container, false);
            super.onCreate(savedInstanceState);

            Spinner filterSpinner = v.findViewById((R.id.filterSpinner));
            ArrayAdapter<CharSequence> filterDataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Filter, android.R.layout.simple_spinner_item);
            filterDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            filterSpinner.setAdapter(filterDataAdapter);
            filterSpinner.setOnItemSelectedListener(this);

            Spinner areaSpinner = v.findViewById((R.id.areaSpinner));
            ArrayAdapter<CharSequence> areaDataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Filter, android.R.layout.simple_spinner_item);
            areaDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            areaSpinner.setAdapter(areaDataAdapter);
            areaSpinner.setOnItemSelectedListener(this);

            Spinner municipalitySpinner = v.findViewById((R.id.municipalitySpinner));
            ArrayAdapter<CharSequence> municipalityDataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Filter, android.R.layout.simple_spinner_item);
            municipalityDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            municipalitySpinner.setAdapter(municipalityDataAdapter);
            municipalitySpinner.setOnItemSelectedListener(this);

            Spinner lowerPriceSpinner = v.findViewById((R.id.lowerPriceSpinner));
            ArrayAdapter<CharSequence> lowerPriceDataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Filter, android.R.layout.simple_spinner_item);
            lowerPriceDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lowerPriceSpinner.setAdapter(lowerPriceDataAdapter);
            lowerPriceSpinner.setOnItemSelectedListener(this);

            Spinner upperPriceSpinner = v.findViewById((R.id.upperPriceSpinner));
            ArrayAdapter<CharSequence> upperPriceDataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Filter, android.R.layout.simple_spinner_item);
            upperPriceDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            upperPriceSpinner.setAdapter(upperPriceDataAdapter);
            upperPriceSpinner.setOnItemSelectedListener(this);

            RecyclerView agentSearchPropertiesList = (RecyclerView) v.findViewById(R.id.agentSearchPropertiesList);
            agentSearchPropertiesList.setLayoutManager(new LinearLayoutManager(getContext()));
            String[] data = {"property1", "property2", "property3", "property4", "property5", "property6", "property7", "property8"};
            agentSearchPropertiesList.setAdapter(new AgentSearchPropertiesRecyclerAdapter(data, this));

            return v;
        }

        @Override
        public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
            if (parent.getItemAtPosition(position).equals("Filter")) {
                //do nothing
            } else {
                //on selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
            }

        }

        @Override
        public void onNothingSelected (AdapterView < ? > parent){

        }

        public void onNoteClick ( int position){

            Intent startIntent = new Intent(getContext(), PropertyDetailActivity.class);
            startActivity(startIntent);
        }


    }

