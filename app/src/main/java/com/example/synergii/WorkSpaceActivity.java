package com.example.synergii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.synergii.Adapters.RecyclerAdapter;

public class WorkSpaceActivity extends AppCompatActivity implements RecyclerAdapter.OnNoteListener {
    public static final String TAG = "WorkSpaceActivity";

    private RecyclerView clientsList;
    private String[] data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_space);
         clientsList = findViewById(R.id.recyclerViewHome);
        clientsList.setLayoutManager(new LinearLayoutManager(this));
         data = new String[]{"client1", "client2", "client3", "client4", "client5", "client6", "client7", "client8", "client9", "client10", "client11", "client12"};
        clientsList.setAdapter(new RecyclerAdapter(data, this));


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
