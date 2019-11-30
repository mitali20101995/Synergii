package com.example.synergii.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.synergii.R;

public class ClientSearchPropertiesRecyclerAdapter extends RecyclerView.Adapter<ClientSearchPropertiesRecyclerAdapter.RecyclerViewHolder> {
    private String[] data;
    private ClientSearchPropertiesRecyclerAdapter.OnNoteListener onNoteListener;

    public ClientSearchPropertiesRecyclerAdapter(String[] data, OnNoteListener onNoteListener){
        this.data = data;
        this.onNoteListener = onNoteListener;
    }

    @Override
    public ClientSearchPropertiesRecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.client_search_properties_list_layout, parent, false);
        return new ClientSearchPropertiesRecyclerAdapter.RecyclerViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(ClientSearchPropertiesRecyclerAdapter.RecyclerViewHolder holder, int position) {
        String text = data[position];
        holder.propDetailsTextView.setText(text);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgIcon;
        Button addToWorkspaceBtn;
        TextView propDetailsTextView;
        ClientSearchPropertiesRecyclerAdapter.OnNoteListener onNoteListener;

        public RecyclerViewHolder(View itemView, ClientSearchPropertiesRecyclerAdapter.OnNoteListener onNoteListener) {
            super(itemView);
            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            addToWorkspaceBtn = (Button) itemView.findViewById(R.id.addToWorkspaceBtn);
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

