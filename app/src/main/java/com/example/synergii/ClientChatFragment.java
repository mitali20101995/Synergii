package com.example.synergii;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.synergii.Adapters.ChatAdapterClient;
import com.example.synergii.models.ChatMessage;
import com.example.synergii.models.Client;
import com.example.synergii.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ClientChatFragment extends Fragment implements ChatAdapterClient.OnNoteListener
{
    public static final String TAG = "ClientChatFragment";
    View v;
    private RecyclerView chatList;
    private TextView enterMsg;
    private ImageButton sendMsg;
    Client loggedInUser;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_client_chat,container,false);
        super.onCreate(savedInstanceState);

        chatList =  v.findViewById(R.id.clientChatList);
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));

        enterMsg = v.findViewById(R.id.editTextEnterMsgClient);
        sendMsg = v.findViewById(R.id.msgSendBtnClient);

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Read the input field and push a new instance to database with key
                String key = reference.child(getString(R.string.dbnode_chat)).push().getKey();
                reference.child(getString(R.string.dbnode_chat))
                        .child(key)
                        .child(getString(R.string.field_msg))
                        .setValue(enterMsg.getText().toString());

                reference.child(getString(R.string.dbnode_chat))
                        .child(key)
                        .child(getString(R.string.field_sentBy))
                        .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                Query query1 = reference.child(getString(R.string.dbnode_clients))
                        .orderByKey()
                        .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //this loop will return a single result
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            Log.d(TAG, "onDataChange found user: "
                                    + singleSnapshot.getValue(Client.class).toString());
                            loggedInUser = singleSnapshot.getValue(Client.class);
                            reference.child(getString(R.string.dbnode_chat))
                                    .child(key)
                                    .child(getString(R.string.field_receivedBy))
                                    .setValue(loggedInUser.getAssignedAgent());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
                Log.d(TAG, "SendMsgClicked");
                showChat();
                //clear the input
                enterMsg.setText("");
            }

        });
        showChat();
        return v;
    }

    public void showChat()
    {
        Query query = reference.child(getString(R.string.dbnode_chat));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                ArrayList<ChatMessage> chats = new ArrayList<>();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: query method found chat: "
                            + singleSnapshot.getValue(ChatMessage.class).toString());
                    ChatMessage msg= singleSnapshot.getValue(ChatMessage.class);
                    if(Objects.equals(msg.getReceivedBy(),FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        chats.add(msg);
                    }
                    else if(Objects.equals(msg.getSentBy(),FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        chats.add(msg);
                    }

                }
                chatList.setAdapter(new ChatAdapterClient(chats,ClientChatFragment.this::onNoteClick));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onNoteClick(int position) {

    }

}
