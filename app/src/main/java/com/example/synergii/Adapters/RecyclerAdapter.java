package com.example.synergii.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.synergii.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private String[] data;
    private OnNoteListener onNoteListener;

    public RecyclerAdapter(String[] data, OnNoteListener onNoteListener){
        this.data = data;
        this.onNoteListener = onNoteListener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.client_list_item_layout, parent, false);
        return new RecyclerViewHolder(view, onNoteListener);
    }
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        String text = data[position];
        holder.clientNameTextView.setText(text);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgIcon;
        TextView clientNameTextView;
        OnNoteListener onNoteListener;

        public RecyclerViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            clientNameTextView = (TextView) itemView.findViewById(R.id.clientNameTextView);
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

