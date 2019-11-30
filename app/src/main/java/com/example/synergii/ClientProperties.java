package com.example.synergii;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.synergii.Adapters.ClientMyPropertiesRecyclerAdapter;

public class ClientProperties extends Fragment implements ClientMyPropertiesRecyclerAdapter.OnNoteListener {

    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_client_properties,container,false);

        RecyclerView clientMyPropertiesList = (RecyclerView) v.findViewById(R.id.clientMyPropertiesList);
        clientMyPropertiesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        String[] data = {"Property 1 \n 17 Jasmine Rd Toronto, Ontario M9M2P8", "Property 2 \n 17 Jasmine Rd Toronto, Ontario M9M2P8", "Property 3 \n 17 Jasmine Rd Toronto, Ontario M9M2P8", " Property 4 \n 17 Jasmine Rd Toronto, Ontario M9M2P8 ", "Property 5 \n 17 Jasmine Rd Toronto, Ontario M9M2P8", "Property 6 \n 17 Jasmine Rd Toronto, Ontario M9M2P8", "Property 7 \n 17 Jasmine Rd Toronto, Ontario M9M2P8", "Property 8 \n 17 Jasmine Rd Toronto, Ontario M9M2P8"};
        clientMyPropertiesList.setAdapter(new ClientMyPropertiesRecyclerAdapter(data, this));

        /*BottomNavigationView bottomNav = v.findViewById(R.id.bottom_navigation_client);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

         */

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
