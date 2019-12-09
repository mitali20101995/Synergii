package com.example.synergii.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.synergii.AgentHomeActivity;
import com.example.synergii.R;
import com.example.synergii.models.Client;
import com.example.synergii.models.Property;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AgentSearchPropertiesRecyclerAdapter extends RecyclerView.Adapter<AgentSearchPropertiesRecyclerAdapter.RecyclerViewHolder>
{
    public static final String TAG = "AgentSearchRecycle";
    private ArrayList<Property> data;
    private SharedPreferences sharedPreferences;
    private AgentSearchPropertiesRecyclerAdapter.OnNoteListener onNoteListener;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public AgentSearchPropertiesRecyclerAdapter(ArrayList<Property> data,SharedPreferences sharedPreferences, OnNoteListener onNoteListener){
        this.data = data;
        this.onNoteListener = onNoteListener;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public AgentSearchPropertiesRecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.agent_search_property_list_layout, parent, false);
        return new AgentSearchPropertiesRecyclerAdapter.RecyclerViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(AgentSearchPropertiesRecyclerAdapter.RecyclerViewHolder holder, int position) {
        Property property = data.get(position);
        holder.propDetailsTextView.setText(property.getAddress());
        holder.saleOrLease.setText(property.getTransaction_type());

       holder.addToWorkspaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = ((AgentHomeActivity) v.getContext());
                int selectedClientPosition = ((AgentHomeActivity) v.getContext()).getIntent().getIntExtra("selectedClientPosition",-1);
                Log.d(TAG, "onClick: " + selectedClientPosition);

                String serializedClients = sharedPreferences.getString("com.example.synergii.clients", null);
                Client selectedClient= serializedClients == null ? null : Arrays.asList(new Gson().fromJson(serializedClients, Client[].class)).get(selectedClientPosition);

                ArrayList<String> clientProperties = selectedClient.getProperties();
                if(clientProperties.contains(property.getId())){
                    return;
                }
                clientProperties.add(property.getId());
                selectedClient.setProperties(clientProperties);
                reference.child(context.getString(R.string.dbnode_clients))
                        .child(selectedClient.getId())
                        .child(context.getString(R.string.field_client_properties))
                        .setValue(selectedClient.getProperties()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        reference.child(context.getString(R.string.dbnode_clients))
                                .orderByKey()
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        ArrayList<Client> clients = new ArrayList<>();
                                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                            Log.d(TAG, "onDataChange: query method found user: "
                                                    + singleSnapshot.getValue(Client.class).toString());
                                            Client client= singleSnapshot.getValue(Client.class);
                                            client.setId(singleSnapshot.getKey());
                                            clients.add(client);
                                        }
                                        String serializedClient = clients == null ? null : new Gson().toJson(clients);
                                        sharedPreferences.edit().putString("com.example.synergii.clients", serializedClient).apply();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }
                });
            }
        });

    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgIcon;
        Button addToWorkspaceBtn;
        TextView propDetailsTextView;
        TextView saleOrLease;
        AgentSearchPropertiesRecyclerAdapter.OnNoteListener onNoteListener;

        public RecyclerViewHolder(View itemView, AgentSearchPropertiesRecyclerAdapter.OnNoteListener onNoteListener) {
            super(itemView);
            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            addToWorkspaceBtn = (Button) itemView.findViewById(R.id.addToWorkspaceBtn);
            propDetailsTextView = (TextView) itemView.findViewById(R.id.propDetailsTextView);
            saleOrLease = (TextView) itemView.findViewById(R.id.saleOrLeaseLabel);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());

        }
    }
    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
