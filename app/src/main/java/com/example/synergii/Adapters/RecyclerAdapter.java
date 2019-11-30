package com.example.synergii.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.synergii.R;
import com.example.synergii.models.Client;

import java.util.ArrayList;

import static com.example.synergii.WorkSpaceActivity.TAG;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private ArrayList<String> data;
    private OnNoteListener onNoteListener;

    public RecyclerAdapter(ArrayList<String> data, OnNoteListener onNoteListener){
        this.data = data;
        this.onNoteListener = onNoteListener;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.client_list_item_layout, parent, false);
        return new RecyclerViewHolder(view, onNoteListener);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String text = data.get(position);
        Log.d(TAG, "onBindViewHolder: "+ text);
        holder.clientNameTextView.setText(text);
    }

    @Override
    public int getItemCount() {
        return data.size();
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

