package com.example.synergii;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.synergii.Adapters.AgentSearchPropertiesRecyclerAdapter;
import com.example.synergii.Adapters.RecyclerAdapter;
import com.example.synergii.models.Client;
import com.example.synergii.models.Property;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements AgentSearchPropertiesRecyclerAdapter.OnNoteListener, AdapterView.OnItemSelectedListener
{
    View v;
    public static final String TAG = "AgentSearchfragment";
    private LinearLayout collapseList;
    private Button resetFilterBtn;
    private Button filterResultBtn;
    private RecyclerView agentSearchPropertiesList;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            v = inflater.inflate(R.layout.fragment_search, container, false);
            super.onCreate(savedInstanceState);
            sharedPreferences = v.getContext().getSharedPreferences("com.example.synergii", Context.MODE_PRIVATE);

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

    //Agent Search List
            agentSearchPropertiesList =  v.findViewById(R.id.agentSearchPropertiesList);
            agentSearchPropertiesList.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = reference.child(getString(R.string.dbnode_properties));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                ArrayList<Property> properties = new ArrayList<>();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: query method found property: "
                            + singleSnapshot.getValue(Property.class).toString());
                    Property property = singleSnapshot.getValue(Property.class);
                    property.setId(singleSnapshot.getKey());
                    properties.add(property);
                }
                agentSearchPropertiesList.setAdapter(new AgentSearchPropertiesRecyclerAdapter(properties,sharedPreferences,SearchFragment.this::onNoteClick));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });



        //Collapse list
        collapseList =  v.findViewById(R.id.collapseList);
        collapseList.setVisibility(View.GONE);

        resetFilterBtn =  v.findViewById(R.id.resetFilterBtn);
        resetFilterBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                toggle_contents(v);
            }
        });

        filterResultBtn =  v.findViewById(R.id.filterResultBtn);
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

