package com.example.synergii.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.synergii.R;

public class AgentMyPropertiesAdapter extends RecyclerView.Adapter<AgentMyPropertiesAdapter.RecyclerViewHolder>
{
    private String[] data;
    private AgentMyPropertiesAdapter.OnNoteListener onNoteListener;

    public AgentMyPropertiesAdapter(String[] data, AgentMyPropertiesAdapter.OnNoteListener onNoteListener){
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
        String text = data[position];
        holder.propDetailsTextView.setText(text);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgIcon;
        ImageView favStarIcon;
        TextView propDetailsTextView;
        AgentMyPropertiesAdapter.OnNoteListener onNoteListener;

        public RecyclerViewHolder(View itemView, AgentMyPropertiesAdapter.OnNoteListener onNoteListener) {
            super(itemView);
            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            favStarIcon = (ImageView) itemView.findViewById(R.id.favStarIcon);
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
