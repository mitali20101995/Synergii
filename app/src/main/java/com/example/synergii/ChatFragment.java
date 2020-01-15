package com.example.synergii;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.synergii.Adapters.ChatAdapterAgent;
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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ChatFragment extends Fragment implements ChatAdapterAgent.OnNoteListener{
    public static final String TAG = "ChatFragment";
    View v;
    private RecyclerView chatList;
    private TextView enterMsg;
    private ImageButton sendMsg;
    private SharedPreferences sharedPreferences;
    private TextView bName ;
    private TextView aInfo;
    private ImageView agentImage;
    private  ImageView bImage;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_chat, container, false);
        super.onCreate(savedInstanceState);

        //Agent Profile info
        bName = v.findViewById(R.id.brokerageTextView);
        aInfo = v.findViewById(R.id.agentInfoTextView);
        agentImage = v.findViewById(R.id.agentProfilePic);
        bImage = v.findViewById(R.id.brokerageLogo);
        String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query brokerage_query=reference.child(getString(R.string.dbnode_users))
                .orderByKey()
                .equalTo(currentUID);
        brokerage_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: query method found user: "
                            + singleSnapshot.getValue(User.class).toString());
                    User user = singleSnapshot.getValue(User.class);
                    bName.setText(user.getBrokerageName());
                    aInfo.setText(user.getFirstName() + " " + user.getLastName());
                    if(user.getProfileLogo() != null){
                        Picasso.with(v.getContext()).load(Uri.parse(user.getProfileLogo())).resize(bImage.getWidth(), bImage.getHeight()).centerCrop().into(bImage);
                    }
                    if(user.getProfilePhoto() != null){
                        Picasso.with(v.getContext()).load(Uri.parse(user.getProfilePhoto())).resize(agentImage.getWidth(), agentImage.getHeight()).centerCrop().into(agentImage);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }

        });

        this.sharedPreferences = v.getContext().getSharedPreferences("com.example.synergii", Context.MODE_PRIVATE);

        int selectedClientPosition = ((AgentHomeActivity) v.getContext()).getIntent().getIntExtra("selectedClientPosition",-1);
        Log.d(TAG, "onClick: " + selectedClientPosition);
        String serializedClients = sharedPreferences.getString("com.example.synergii.clients", null);
        Client selectedClient= serializedClients == null ? null : Arrays.asList(new Gson().fromJson(serializedClients, Client[].class)).get(selectedClientPosition);

        chatList =  v.findViewById(R.id.agentChatList);
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));

        enterMsg = v.findViewById(R.id.editTextEnterMsg);
        sendMsg = v.findViewById(R.id.msgSendBtn);

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

                reference.child(getString(R.string.dbnode_chat))
                        .child(key)
                        .child(getString(R.string.field_receivedBy))
                        .setValue(selectedClient.getId());

                Log.d(TAG, "SendMsgClicked");
                showChat(selectedClient);
                //clear the input
                enterMsg.setText("");
            }
        });

        showChat(selectedClient);
        return v;
    }

    public void showChat(Client selectedClient)
    {
        Query query = reference.child(getString(R.string.dbnode_chat));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                ArrayList<ChatMessage> chats = new ArrayList<>();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: query method found sent msg : "
                            + singleSnapshot.getValue(ChatMessage.class).toString());
                    ChatMessage msg= singleSnapshot.getValue(ChatMessage.class);
                    if(Objects.equals(msg.getReceivedBy(),selectedClient.getId()))
                    {
                        chats.add(msg);
                    }
                    else if(Objects.equals(msg.getSentBy(),selectedClient.getId()))
                    {
                        chats.add(msg);
                    }

                }
                chatList.setAdapter(new ChatAdapterAgent(chats,ChatFragment.this::onNoteClick));
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
