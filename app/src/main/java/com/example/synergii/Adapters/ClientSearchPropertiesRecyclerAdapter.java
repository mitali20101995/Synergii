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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.synergii.R;
import com.example.synergii.models.Client;
import com.example.synergii.models.Property;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class ClientSearchPropertiesRecyclerAdapter extends RecyclerView.Adapter<ClientSearchPropertiesRecyclerAdapter.RecyclerViewHolder> {
    public static final String TAG = "ClientSearchRecycle";
    private ArrayList<Property> data;
    private SharedPreferences sharedPreferences;
    private ClientSearchPropertiesRecyclerAdapter.OnNoteListener onNoteListener;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public ClientSearchPropertiesRecyclerAdapter(ArrayList<Property> data, SharedPreferences sharedPreferences, OnNoteListener onNoteListener){
        this.data = data;
        this.onNoteListener = onNoteListener;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public ClientSearchPropertiesRecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.client_search_properties_list_layout, parent, false);
        return new ClientSearchPropertiesRecyclerAdapter.RecyclerViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(ClientSearchPropertiesRecyclerAdapter.RecyclerViewHolder holder, int position) {
        Property property = data.get(position);
        holder.propDetailsTextView.setText(property.getAddress());
        holder.saleOrLease.setText(property.getTransaction_type());

        holder.addToWorkspaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                reference.child(context.getString(R.string.dbnode_clients))
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                ArrayList<String> clientProperties = dataSnapshot.getValue(Client.class).getProperties();
                                if (clientProperties.contains(property.getId())) {
                                    return;
                                }
                                clientProperties.add(property.getId());
                                reference.child(context.getString(R.string.dbnode_clients))
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(context.getString(R.string.field_client_properties))
                                        .setValue(clientProperties);
                                Toast.makeText(v.getContext(), "Property Added.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        ClientSearchPropertiesRecyclerAdapter.OnNoteListener onNoteListener;

        public RecyclerViewHolder(View itemView, ClientSearchPropertiesRecyclerAdapter.OnNoteListener onNoteListener) {
            super(itemView);
            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            addToWorkspaceBtn = (Button) itemView.findViewById(R.id.addToWorkspaceBtnClient);
            propDetailsTextView = (TextView) itemView.findViewById(R.id.propDetailsTextView);
            saleOrLease = (TextView) itemView.findViewById(R.id.saleOrLeaseLabelClient);
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

