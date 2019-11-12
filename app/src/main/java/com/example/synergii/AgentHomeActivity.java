package com.example.synergii;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AgentHomeActivity extends AppCompatActivity {

        private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_workspace:
                        initAgentHomeFragment();
                        return true;
                    case R.id.nav_chat:
                        initChatFragment();
                        return true;
                    case R.id.nav_properties:
                        initPropertiesFragment();
                        return true;
                    case R.id.nav_search:
                        initSearchFragment();
                        return true;
                }
                return false;
            }
        };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_agent_home);
            BottomNavigationView navView = findViewById(R.id.nav_view);
            navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            initAgentHomeFragment();
        }

        private void initAgentHomeFragment(){
            WorkSpaceFragment homeFragment = new WorkSpaceFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_content_frame, homeFragment, "WorkSpace");
            transaction.commit();
        }

        private void initPropertiesFragment(){
            PropertiesFragment fragment = new PropertiesFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_content_frame, fragment, "Properties");
            transaction.commit();
        }

        private void  initChatFragment(){
            ChatFragment fragment = new ChatFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_content_frame, fragment, "Chat");
            transaction.commit();
        }

        private void  initSearchFragment(){
            SearchFragment fragment = new SearchFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_content_frame, fragment, "Search");
            transaction.commit();
        }

    }
