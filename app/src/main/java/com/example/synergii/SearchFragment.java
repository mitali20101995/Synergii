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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.synergii.Adapters.AgentSearchPropertiesRecyclerAdapter;
import com.example.synergii.Adapters.RecyclerAdapter;
import com.example.synergii.models.Client;
import com.example.synergii.models.Property;
import com.example.synergii.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements AgentSearchPropertiesRecyclerAdapter.OnNoteListener, AdapterView.OnItemSelectedListener, SearchView.OnQueryTextListener {
    View v;
    public static final String TAG = "AgentSearchfragment";
    private LinearLayout collapseList;
    private Button resetFilterBtn;
    private Button filterResultBtn;
    private RecyclerView agentSearchPropertiesList;
    private  Spinner filterSpinner;
    private SharedPreferences sharedPreferences;
    private TextView bName ;
    private TextView aInfo;
    private ImageView agentImage;
    private  ImageView bImage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            v = inflater.inflate(R.layout.fragment_search, container, false);
            super.onCreate(savedInstanceState);
            sharedPreferences = v.getContext().getSharedPreferences("com.example.synergii", Context.MODE_PRIVATE);

        //Agent Profile info
        bName = v.findViewById(R.id.brokerageTextView);
        aInfo = v.findViewById(R.id.agentInfoTextView);
        agentImage = v.findViewById(R.id.agentProfilePic);
        bImage = v.findViewById(R.id.brokerageLogo);
        String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query brokerage_query=reference.child(getString(R.string.dbnode_users))
                .orderByKey()
                .equalTo(currentUID);
        brokerage_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: query method found user: "
                            + singleSnapshot.getValue(User.class).toString());
                    User user = singleSnapshot.getValue(User.class);
                    bName.setText(user.getBrokerageName());
                    aInfo.setText(user.getFirstName() + " " + user.getLastName());
                    if(user.getProfileLogo() != null){
                        Picasso.with(v.getContext()).load(Uri.parse(user.getProfileLogo())).resize(bImage.getWidth(), bImage.getHeight()).centerCrop().into(bImage);
                    }
                    if(user.getProfilePhoto() != null){
                        Picasso.with(v.getContext()).load(Uri.parse(user.getProfilePhoto())).resize(agentImage.getWidth(), agentImage.getHeight()).centerCrop().into(agentImage);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }

        });

        filterSpinner = v.findViewById((R.id.filterSpinner));
        ArrayAdapter<CharSequence> filterDataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Filter, android.R.layout.simple_spinner_item);
        filterDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterDataAdapter);
        filterSpinner.setOnItemSelectedListener(this);

        SearchView searchView = (SearchView) v.findViewById(R.id.searchByMLSAgent);
        searchView.setOnQueryTextListener(this);

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
    //For Sale or lease filter
    @Override
    public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        if (parent.getItemAtPosition(position).equals("Filter")) {
            //do nothing
            Log.d(TAG, "Filter is selected. ");
        }
        else if (parent.getItemAtPosition(position).equals("Sale")){
            //on selecting a spinner item Sale
            Log.d(TAG, "Filter Sale is selected. ");
            Query querySale = reference.child(getString(R.string.dbnode_properties))
                    .orderByChild("transaction_type")
                    .equalTo("sale");
            querySale.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange( DataSnapshot dataSnapshot) {
                    ArrayList<Property> properties = new ArrayList<>();
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Log.d(TAG, "onDataChange: query method found property sale: "
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

        }
        else {
            //on selecting a spinner item Lease
            Log.d(TAG, "Filter Lease is selected. ");
            Query queryLease = reference.child(getString(R.string.dbnode_properties))
                    .orderByChild("transaction_type")
                    .equalTo("lease");
            queryLease.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange( DataSnapshot dataSnapshot) {
                    ArrayList<Property> properties = new ArrayList<>();
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Log.d(TAG, "onDataChange: query method found property lease: "
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
        }

    }

    @Override
    public void onNothingSelected (AdapterView < ? > parent){

    }

    public void toggle_contents(View v){
        collapseList.setVisibility( collapseList.isShown()
                ? View.GONE
                : View.VISIBLE );
    }

    public void onNoteClick ( int position){

            Intent startIntent = new Intent(getContext(), PropertyDetailActivity.class);
            startActivity(startIntent);
        }


    @Override
    public boolean onQueryTextSubmit(String query) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query querySale = reference.child(getString(R.string.dbnode_properties))
                .orderByChild("mls_number").equalTo(query);

        querySale.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                ArrayList<Property> properties = new ArrayList<>();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: query method found property sale: "
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
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query querySale = reference.child(getString(R.string.dbnode_properties))
                .orderByChild("mls_number").equalTo(newText);

        querySale.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                ArrayList<Property> properties = new ArrayList<>();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: query method found property sale: "
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
        return false;
    }
}

