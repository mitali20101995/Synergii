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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.synergii.Adapters.ClientMyPropertiesRecyclerAdapter;
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

public class ClientProperties extends Fragment implements ClientMyPropertiesRecyclerAdapter.OnNoteListener {

    public static final String TAG = "ClientMyProperties";
    View v;
    private TextView bName ;
    private ImageView bImage;
    private TextView cInfo;
    private Client loggedInUser;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private ImageView clientImage;
    private SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_client_properties,container,false);
        bName = v.findViewById(R.id.brokerageTextView);
        bImage = v.findViewById(R.id.brokerageLogo);
        cInfo = v.findViewById(R.id.clientInfoTextView);
        clientImage = v.findViewById(R.id.clientProfilePic);
        this.sharedPreferences = v.getContext().getSharedPreferences("com.example.synergii", Context.MODE_PRIVATE);

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
                    Query brokerage_query = reference.child(getString(R.string.dbnode_users))
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

        RecyclerView clientMyPropertiesList = (RecyclerView) v.findViewById(R.id.clientMyPropertiesList);
        clientMyPropertiesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        reference.child(v.getContext().getString(R.string.dbnode_clients))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //for (DataSnapshot singleDatasnapshot : dataSnapshot.getChildren()) {
                            ArrayList<String> clientProperties = dataSnapshot.getValue(Client.class).getProperties();
                            reference.child(v.getContext().getString(R.string.dbnode_properties))
                                    .orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ArrayList<Property> properties = new ArrayList<>();

                                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                        Log.d(TAG, "onDataChange: query method found property: "
                                                + singleSnapshot.getValue(Property.class).toString());
                                        Property property = singleSnapshot.getValue(Property.class);
                                        property.setId(singleSnapshot.getKey());
                                        if (clientProperties.contains(property.getId())) {
                                            properties.add(property);
                                        }
                                    }
                                    clientMyPropertiesList.setAdapter(new ClientMyPropertiesRecyclerAdapter(properties, ClientProperties.this::onNoteClick));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    //}
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }


                });
        return v;
    }

    @Override
    public void onNoteClick(int position) {

        Intent startIntent = new Intent(getContext(), PropertyDetails.class);
        startActivity(startIntent);

    }
/*
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.nav_properties_client:

                    break;

                case R.id.nav_chat_client:
                    Intent intent2 = new Intent(ClientMyProperties.this, ClientChat.class);
                    startActivity(intent2);
                    break;

                case R.id.nav_search_client:
                    Intent intent3 = new Intent(ClientMyProperties.this, ClientSearchProperties.class);
                    startActivity(intent3);
                    break;
            }

            return true;
        }
    };

 */

}
