package com.example.synergii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.synergii.models.Client;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddClientActivity extends AppCompatActivity {
    private static final String TAG = "AddClientActivity";
    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;
    // widgets
    private EditText mEmail, mFName, mLName, mListingId;
    //private String mClientId;
    private ProgressBar mProgressBar;
    public static boolean isActivityRunning;
    private DatabaseReference mDatabase;
    private Button mBtnAddClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEmail = findViewById(R.id.emailEditText);
        mFName = findViewById(R.id.FNameEditText);
        mLName = findViewById(R.id.LNameEditText);
        mListingId = findViewById(R.id.editTextListingId);
        mBtnAddClient = findViewById(R.id.addClientBtn);

        setOnCLickListener();

    }
    private void setOnCLickListener() {
        mBtnAddClient.setOnClickListener(view -> {
            Client post = new Client();
            post.setFirstName(mFName.getText().toString());
            post.setLastName(mLName.getText().toString());
            post.setEmail(mEmail.getText().toString());
            post.setListingId(mListingId.getText().toString());
            post.setAssignedAgent(FirebaseAuth.getInstance().getCurrentUser().getUid());
            //post.setClientId(mDatabase.child("clients").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).toString());
            addPost(post, view);
        });
    }

    private void addPost(Client post, View view) {
        String key = mDatabase.child(getString(R.string.dbnode_clients)).push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/clients/" + key, post.toMap());
        mDatabase.updateChildren(childUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(view.getContext(), "Client added", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), "something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
