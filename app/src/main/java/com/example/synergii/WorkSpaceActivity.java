package com.example.synergii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.synergii.models.Client;
import com.example.synergii.models.User;
import com.google.android.gms.common.api.Api;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.synergii.Adapters.RecyclerAdapter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WorkSpaceActivity extends AppCompatActivity implements RecyclerAdapter.OnNoteListener {
    public static final String TAG = "WorkSpaceActivity";
    private RecyclerView clientsList;
    private TextView bName ;
    private TextView aInfo;
    private ImageView agentImage;
    private  ImageView bImage;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_space);
        sharedPreferences = getSharedPreferences("com.example.synergii", Context.MODE_PRIVATE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        //Agent Profile info
        bName = findViewById(R.id.brokerageTextView);
        aInfo = findViewById(R.id.agentInfoTextView);
        agentImage = findViewById(R.id.agentProfilePic);
        bImage = findViewById(R.id.brokerageLogo);
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
                            Picasso.with(getApplicationContext()).load(Uri.parse(user.getProfileLogo())).resize(bImage.getWidth(), bImage.getHeight()).centerCrop().into(bImage);
                        }
                        if(user.getProfilePhoto() != null){
                            Picasso.with(getApplicationContext()).load(Uri.parse(user.getProfilePhoto())).resize(agentImage.getWidth(), agentImage.getHeight()).centerCrop().into(agentImage);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }

            });

        clientsList = findViewById(R.id.recyclerViewHome);
        clientsList.setLayoutManager(new LinearLayoutManager(this));

        Query query = reference.child(getString(R.string.dbnode_clients))
                .orderByChild("assignedAgent")
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Client> clients = new ArrayList<>();
                //this loop will return a single result
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: query method found user: "
                            + singleSnapshot.getValue(Client.class).toString());
                    Client client= singleSnapshot.getValue(Client.class);
                    client.setId(singleSnapshot.getKey());
                    clients.add(client);
                }

                String serializedClient = clients == null ? null : new Gson().toJson(clients);
                sharedPreferences.edit().putString("com.example.synergii.clients", serializedClient).apply();
                clientsList.setAdapter(new RecyclerAdapter(clients,WorkSpaceActivity.this::onNoteClick));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }

        });
    }

    @Override
    public void onNoteClick(int position) {

        Intent startIntent = new Intent(getApplicationContext(), AgentHomeActivity.class);
        startIntent.putExtra("selectedClientPosition",position);
        startActivity(startIntent);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.side_navigation, menu);
        return true;
    }

    public void onAddClientItemClick(MenuItem item) {
        Intent startIntent = new Intent(getApplicationContext(), AddClientActivity.class);
        startActivity(startIntent);
    }

    public void onProfileItemClick(MenuItem item) {
        Intent startIntent = new Intent(getApplicationContext(), AgentProfileActivity.class);
        startActivity(startIntent);
    }


    public void onTermsAndConditionsItemClick(MenuItem item) {
        Intent startIntent = new Intent(getApplicationContext(), TermsAndConditionsActivity.class);
        startActivity(startIntent);
    }

    public void onLogoutItemClick(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

}
