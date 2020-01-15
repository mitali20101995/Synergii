package com.example.synergii;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.synergii.Adapters.ClientSearchPropertiesRecyclerAdapter;
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

public class ClientSearchProperties extends Fragment implements ClientSearchPropertiesRecyclerAdapter.OnNoteListener, AdapterView.OnItemSelectedListener, SearchView.OnQueryTextListener {

    View v;
    LinearLayout collapseList;
    public static final String TAG = "ClientSearchProperty";
    private TextView bName ;
    private ImageView bImage;
    private TextView cInfo;
    private Client loggedInUser;
    private ImageView clientImage;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private SharedPreferences sharedPreferences;
    private  RecyclerView clientSearchPropertiesList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_client_search_properties,container,false);
        sharedPreferences = v.getContext().getSharedPreferences("com.example.synergii", Context.MODE_PRIVATE);
        bName = v.findViewById(R.id.brokerageTextView);
        bImage = v.findViewById(R.id.brokerageLogo);
        cInfo = v.findViewById(R.id.clientInfoTextView);
        clientImage = v.findViewById(R.id.clientProfilePic);

        Query query1=reference.child(getString(R.string.dbnode_clients))
                .orderByKey()
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //this loop will return a single result
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    loggedInUser = singleSnapshot.getValue(Client.class);
                    if(loggedInUser.getFirstName() != null && loggedInUser.getLastName() != null)
                    {
                        cInfo.setText(loggedInUser.getFirstName() + " "  + loggedInUser.getLastName());
                    }
                    if(loggedInUser.getProfilePhotoClient() != null){
                        Picasso.with(v.getContext()).load(Uri.parse(loggedInUser.getProfilePhotoClient())).resize(clientImage.getWidth(), clientImage.getHeight()).centerCrop().into(clientImage);
                    }
                    Query brokerage_query=reference.child(getString(R.string.dbnode_users))
                            .orderByKey()
                            .equalTo(loggedInUser.getAssignedAgent());
                    brokerage_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                User user = singleSnapshot.getValue(User.class);
                                bName.setText(user.getBrokerageName());
                                if(user.getProfileLogo() != null){
                                    Picasso.with(v.getContext()).load(Uri.parse(user.getProfileLogo())).resize(bImage.getWidth(), bImage.getHeight()).centerCrop().into(bImage);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }

                    });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Spinner filterSpinner = v.findViewById((R.id.filterSpinner));
        ArrayAdapter<CharSequence> filterDataAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.Filter, android.R.layout.simple_spinner_item);
        filterDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterDataAdapter);
        filterSpinner.setOnItemSelectedListener(this);

        SearchView searchView = (SearchView) v.findViewById(R.id.searchByMLSClient);
        searchView.setOnQueryTextListener(this);

        clientSearchPropertiesList = (RecyclerView) v.findViewById(R.id.clientSearchPropertiesList);
        clientSearchPropertiesList.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                clientSearchPropertiesList.setAdapter(new ClientSearchPropertiesRecyclerAdapter(properties,sharedPreferences,ClientSearchProperties.this::onNoteClick));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
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
                    clientSearchPropertiesList.setAdapter(new ClientSearchPropertiesRecyclerAdapter(properties,sharedPreferences,ClientSearchProperties.this::onNoteClick));
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
                    clientSearchPropertiesList.setAdapter(new ClientSearchPropertiesRecyclerAdapter(properties,sharedPreferences,ClientSearchProperties.this::onNoteClick));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
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
                clientSearchPropertiesList.setAdapter(new ClientSearchPropertiesRecyclerAdapter(properties,sharedPreferences,ClientSearchProperties.this::onNoteClick));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}

