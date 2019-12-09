package com.example.synergii.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.synergii.R;
import com.example.synergii.models.Property;

import java.util.ArrayList;

public class AgentMyPropertiesAdapter extends RecyclerView.Adapter<AgentMyPropertiesAdapter.RecyclerViewHolder>
{
    private ArrayList<Property> data;
    private AgentMyPropertiesAdapter.OnNoteListener onNoteListener;

    public AgentMyPropertiesAdapter(ArrayList<Property> data, AgentMyPropertiesAdapter.OnNoteListener onNoteListener){
        this.data = data;
        this.onNoteListener = onNoteListener;
    }

    @Override
    public AgentMyPropertiesAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.agent_my_properties_layout, parent, false);
        return new AgentMyPropertiesAdapter.RecyclerViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(AgentMyPropertiesAdapter.RecyclerViewHolder holder, int position) {
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
        AgentMyPropertiesAdapter.OnNoteListener onNoteListener;

        public RecyclerViewHolder(View itemView, AgentMyPropertiesAdapter.OnNoteListener onNoteListener) {
            super(itemView);
            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            saleOrLease = (TextView)  itemView.findViewById(R.id.saleOrLeaseMyProperties);
//            favStarIcon = (ImageView) itemView.findViewById(R.id.favStarIcon);
            propDetailsTextView = (TextView) itemView.findViewById(R.id.propDetailsTextView);
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
