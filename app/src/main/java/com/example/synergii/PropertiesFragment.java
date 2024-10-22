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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.synergii.Adapters.AgentMyPropertiesAdapter;
import com.example.synergii.models.Client;
import com.example.synergii.models.Property;
import com.example.synergii.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;


public class PropertiesFragment extends Fragment implements AgentMyPropertiesAdapter.OnNoteListener {
    public static final String TAG = "AgentPropertiesfragment";
    View v;
    private SharedPreferences sharedPreferences;
    private TextView bName ;
    private TextView aInfo;
    private ImageView agentImage;
    private  ImageView bImage;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        v = inflater.inflate(R.layout.fragment_properties, container, false);
        super.onCreate(savedInstanceState);
        this.sharedPreferences = v.getContext().getSharedPreferences("com.example.synergii", Context.MODE_PRIVATE);

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

        RecyclerView agentMyPropertiesList = (RecyclerView) v.findViewById(R.id.agentMyPropertiesList);
        agentMyPropertiesList.setLayoutManager(new LinearLayoutManager(getContext()));

        int selectedClientPosition = ((AgentHomeActivity) v.getContext()).getIntent().getIntExtra("selectedClientPosition",-1);
        Log.d(TAG, "onClick: " + selectedClientPosition);

        String serializedClients = sharedPreferences.getString("com.example.synergii.clients", null);
        Client selectedClient= serializedClients == null ? null : Arrays.asList(new Gson().fromJson(serializedClients, Client[].class)).get(selectedClientPosition);

        ArrayList<String> clientProperties = selectedClient.getProperties();

        reference.child(getString(R.string.dbnode_properties))
                .orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {
                        ArrayList<Property> properties = new ArrayList<>();

                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            Log.d(TAG, "onDataChange: query method found property: "
                                    + singleSnapshot.getValue(Property.class).toString());
                            Property property = singleSnapshot.getValue(Property.class);
                            property.setId(singleSnapshot.getKey());
                            if(clientProperties.contains(property.getId())){
                                properties.add(property);
                            }
                        }
                        agentMyPropertiesList.setAdapter(new AgentMyPropertiesAdapter(properties, PropertiesFragment.this::onNoteClick));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return v;
    }

    @Override
    public void onNoteClick(int position)
    {
        Intent startIntent = new Intent(getContext(), PropertyDetailActivity.class);
        startActivity(startIntent);
    }

}