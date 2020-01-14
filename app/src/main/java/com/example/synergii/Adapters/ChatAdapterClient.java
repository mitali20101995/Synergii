package com.example.synergii.Adapters;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.synergii.R;
import com.example.synergii.models.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class ChatAdapterClient extends RecyclerView.Adapter<ChatAdapterClient.RecyclerViewHolder>{

    private static final String TAG = "ChatAdapterClient";
    private ArrayList<ChatMessage> data;
    private RecyclerAdapter.OnNoteListener onNoteListener;

    public ChatAdapterClient(ArrayList<ChatMessage> data, RecyclerAdapter.OnNoteListener onNoteListener){
        this.data = data;
        this.onNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ChatAdapterClient.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.client_chat_layout, parent, false);
        return new RecyclerViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapterClient.RecyclerViewHolder holder, int position) {
        ChatMessage msg = data.get(position);
        holder.messageTextView.setText(msg.getMessage());
        Log.d(TAG, "onBindViewHolder: "+ msg.getSentBy() + FirebaseAuth.getInstance().getCurrentUser().getUid());
        if(Objects.equals(msg.getSentBy(), FirebaseAuth.getInstance().getCurrentUser().getUid())){
            Drawable sentMsgBackground = holder.messageTextView.getContext().getDrawable(R.drawable.my_message);
            Log.d(TAG, "Blue: "+ msg.getSentBy() + FirebaseAuth.getInstance().getCurrentUser().getUid());
            holder.messageTextView.setBackground(sentMsgBackground);
            RelativeLayout.LayoutParams layoutParams =(RelativeLayout.LayoutParams)holder.messageTextView.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            holder.messageTextView.setLayoutParams(layoutParams);
            holder.messageTextView.setTextColor(Color.parseColor("#FFFFFF"));
        }

    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView messageTextView;
        RecyclerAdapter.OnNoteListener onNoteListener;

        public RecyclerViewHolder(View itemView, RecyclerAdapter.OnNoteListener onNoteListener) {
            super(itemView);
            messageTextView = (TextView) itemView.findViewById(R.id.chat_White);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());

        }
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
