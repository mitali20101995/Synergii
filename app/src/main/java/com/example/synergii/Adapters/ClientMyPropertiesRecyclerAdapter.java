package com.example.synergii.Adapters;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.synergii.R;
import com.example.synergii.models.Property;

import java.util.ArrayList;

public class ClientMyPropertiesRecyclerAdapter extends RecyclerView.Adapter<ClientMyPropertiesRecyclerAdapter.RecyclerViewHolder> {
    private ArrayList<Property> data;
    private ClientMyPropertiesRecyclerAdapter.OnNoteListener onNoteListener;
   // private SharedPreferences sharedPreferences;
   public static final String TAG = "ClientMyProRecycle";

    public ClientMyPropertiesRecyclerAdapter(ArrayList<Property> data, ClientMyPropertiesRecyclerAdapter.OnNoteListener onNoteListener){
        this.data = data;
        this.onNoteListener = onNoteListener;
    }

    @Override
    public ClientMyPropertiesRecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.client_my_properties_list_layout, parent, false);
        return new ClientMyPropertiesRecyclerAdapter.RecyclerViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(ClientMyPropertiesRecyclerAdapter.RecyclerViewHolder holder, int position) {
        Property property = data.get(position);
        holder.propDetailsTextView.setText(property.getAddress());
        holder.saleOrLease.setText(property.getTransaction_type());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgIcon;
        ImageView favStarIcon;
        TextView propDetailsTextView;
        TextView saleOrLease;
        ClientMyPropertiesRecyclerAdapter.OnNoteListener onNoteListener;

        public RecyclerViewHolder(View itemView, ClientMyPropertiesRecyclerAdapter.OnNoteListener onNoteListener) {
            super(itemView);
            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            //favStarIcon = (ImageView) itemView.findViewById(R.id.favStarIcon);
            saleOrLease = (TextView) itemView.findViewById(R.id.saleOrLeaseClientMyPro);
            propDetailsTextView = (TextView) itemView.findViewById(R.id.propDetailsTextViewClientMyPro);
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
