package com.example.synergii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.synergii.Adapters.RecyclerAdapter;

import java.util.ArrayList;

public class WorkSpaceActivity extends AppCompatActivity implements RecyclerAdapter.OnNoteListener {
    public static final String TAG = "WorkSpaceActivity";

    private RecyclerView clientsList;
    private ArrayList<String> data;
    private TextView bName ;
    private TextView aInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_space);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        //Agent Profile info
        bName = findViewById(R.id.brokerageTextView);
        aInfo = findViewById(R.id.agentInfoTextView);
        String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query brokerage_query=reference.child(getString(R.string.dbnode_users))
                .orderByKey()
                .equalTo(currentUID);

        brokerage_query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                bName.setText(user.getBrokerageName());
                aInfo.setText(user.getFirstName() +" " + user.getLastName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    //Client list
        data = new ArrayList<>();
        clientsList = findViewById(R.id.recyclerViewHome);
        clientsList.setLayoutManager(new LinearLayoutManager(this));

        Query query = reference.child(getString(R.string.dbnode_clients))
                .orderByChild("assignedAgent")
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> clients = new ArrayList<>();
                //this loop will return a single result
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: query method found user: "
                            + singleSnapshot.getValue(Client.class).toString());
                    Client client= singleSnapshot.getValue(Client.class);
                   // String name = client.getFirstName() +" "+ client.getLastName();
                    clients.add(client.getFirstName() + " " + client.getLastName());
                }
                clientsList.setAdapter(new RecyclerAdapter(clients,WorkSpaceActivity.this::onNoteClick));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }

        });
    }

    @Override
    public void onNoteClick(int position) {
        Intent startIntent = new Intent(getApplicationContext(), AgentHomeActivity.class);
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
