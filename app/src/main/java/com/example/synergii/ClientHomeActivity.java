package com.example.synergii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


public class ClientHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_client);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ClientChatFragment()).commit();

        }

    }
    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ClientChatFragment()).commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.nav_agent:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AgentFragment()).commit();

                break;

            case R.id.nav_client_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ClientFragment()).commit();

                break;

            case R.id.nav_terms:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ClientsTermsFragment()).commit();

                break;

            case R.id.nav_logout:
                Toast.makeText(this, "You Logged Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ClientHomeActivity.this, LoginActivity.class);
                startActivity(intent);

        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.nav_chat_client:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ClientChatFragment()).commit();
                    break;

                case R.id.nav_properties_client:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ClientProperties()).commit();
                    break;

                case R.id.nav_search_client:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ClientSearchProperties()).commit();
                    break;

            }

            return true;
        }
    };

}

