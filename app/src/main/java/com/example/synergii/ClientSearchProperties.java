package com.example.synergii;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.synergii.Adapters.ClientSearchPropertiesRecyclerAdapter;

public class ClientSearchProperties extends Fragment implements com.example.synergii.Adapters.ClientSearchPropertiesRecyclerAdapter.OnNoteListener, AdapterView.OnItemSelectedListener {

    View v;
    LinearLayout collapseList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_client_search_properties,container,false);

        Spinner filterSpinner = v.findViewById((R.id.filterSpinner));
        ArrayAdapter<CharSequence> filterDataAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.Filter, android.R.layout.simple_spinner_item);
        filterDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterDataAdapter);
        filterSpinner.setOnItemSelectedListener(this);

        Spinner areaSpinner = v.findViewById((R.id.areaSpinner));
        ArrayAdapter<CharSequence> areaDataAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.Filter, android.R.layout.simple_spinner_item);
        areaDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(areaDataAdapter);
        areaSpinner.setOnItemSelectedListener(this);

        Spinner municipalitySpinner = v.findViewById((R.id.municipalitySpinner));
        ArrayAdapter<CharSequence> municipalityDataAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.Filter, android.R.layout.simple_spinner_item);
        municipalityDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        municipalitySpinner.setAdapter(municipalityDataAdapter);
        municipalitySpinner.setOnItemSelectedListener(this);

        Spinner lowerPriceSpinner = v.findViewById((R.id.lowerPriceSpinner));
        ArrayAdapter<CharSequence> lowerPriceDataAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.Filter, android.R.layout.simple_spinner_item);
        lowerPriceDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lowerPriceSpinner.setAdapter(lowerPriceDataAdapter);
        lowerPriceSpinner.setOnItemSelectedListener(this);

        Spinner upperPriceSpinner = v.findViewById((R.id.upperPriceSpinner));
        ArrayAdapter<CharSequence> upperPriceDataAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.Filter, android.R.layout.simple_spinner_item);
        upperPriceDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        upperPriceSpinner.setAdapter(upperPriceDataAdapter);
        upperPriceSpinner.setOnItemSelectedListener(this);

        RecyclerView clientSearchPropertiesList = (RecyclerView) v.findViewById(R.id.clientSearchPropertiesList);
        clientSearchPropertiesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        String[] data = {"property1", "property2", "property3", "property4", "property5", "property6", "property7", "property8"};
        clientSearchPropertiesList.setAdapter(new ClientSearchPropertiesRecyclerAdapter(data, this));

        collapseList = (LinearLayout) v.findViewById(R.id.collapseList);
        collapseList.setVisibility(View.GONE);

        Button resetFilterBtn = (Button) v.findViewById(R.id.resetFilterBtn);
        resetFilterBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                toggle_contents(v);
            }
        });

        Button filterResultBtn = (Button) v.findViewById(R.id.filterResultBtn);
        filterResultBtn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                collapseList.setVisibility(View.GONE);
            }
        });


        return v;
    }

    public void toggle_contents(View v){
        collapseList.setVisibility( collapseList.isShown()
                ? View.GONE
                : View.VISIBLE );
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position).equals("Filter"))
        {
            //do nothing
        }
        else
        {
            //on selecting a spinner item
            String item = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onNoteClick(int position) {

        Intent startIntent = new Intent(getContext(), PropertyDetails.class);
        startActivity(startIntent);
    }


    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}

