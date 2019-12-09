package com.example.synergii;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.synergii.models.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AgentFragment extends Fragment {

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    public static final String TAG = "AgentFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agent, container, false);


//        Query query = reference.child(getString(R.string.dbnode_users))
//                .orderByChild("userId")
//                .equalTo(reference.child(getString(R.string.dbnode_clients)).orderByChild("assignedAgent").equalTo(FirebaseAuth.getInstance().getUid()).toString());
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                ArrayList<String> clients = new ArrayList<>();
//                //this loop will return a single result
//                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
//                    Log.d(TAG, "onDataChange: query method found user: "
//                            + singleSnapshot.getValue(Client.class).toString());
//                    Client client= singleSnapshot.getValue(Client.class);
//                    clients.add(client.getFirstName() + " " + client.getLastName());
//                }
//    }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) { }
//   });
    }
}