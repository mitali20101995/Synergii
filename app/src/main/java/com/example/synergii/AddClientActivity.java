package com.example.synergii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.synergii.models.Client;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static android.text.TextUtils.isEmpty;
import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class AddClientActivity extends AppCompatActivity {
    private static final String TAG = "AddClientActivity";
    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseAuth mAuth2;
    // widgets
    private EditText mEmail, mFName, mLName, mListingId, mClientPassword;
    //private String mClientId;
    private ProgressBar mProgressBar;
    public static boolean isActivityRunning;
    private DatabaseReference mDatabase;
    private Button mBtnAddClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        mAuth = getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mEmail = findViewById(R.id.emailEditText);
        mFName = findViewById(R.id.FNameEditText);
        mLName = findViewById(R.id.LNameEditText);
        mListingId = findViewById(R.id.editTextListingId);
        mClientPassword = findViewById(R.id.editTextClientPassword);
        mBtnAddClient = findViewById(R.id.addClientBtn);


        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("https://synergii-5dfe2.firebaseio.com")
                .setApiKey("AIzaSyCAtv1jaDeqUZRjx6lllMgtm6z2HKDnToE")
                .setApplicationId("synergii-5dfe2").build();

        try { FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "com.example.synergii");
            mAuth2 = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e){
            mAuth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance("com.example.synergii"));
        }

        setOnCLickListener();

    }
    private void setOnCLickListener() {
        mBtnAddClient.setOnClickListener(view -> {

            if(!isEmpty(mEmail.getText().toString())
                    && !isEmpty(mFName.getText().toString()) && !isEmpty(mLName.getText().toString()) && !isEmpty(mListingId.getText().toString()) && !isEmpty(mClientPassword.getText().toString()))
            {
                Client post = new Client();
                post.setFirstName(mFName.getText().toString());
                post.setLastName(mLName.getText().toString());
                post.setEmail(mEmail.getText().toString());
                post.setListingId(mListingId.getText().toString());
                post.setAssignedAgent(getInstance().getCurrentUser().getUid());
                post.setClientPassword(mClientPassword.getText().toString());

                //Add client in auth
                mAuth2.createUserWithEmailAndPassword(mEmail.getText().toString(),mClientPassword.getText().toString())
                        .addOnCompleteListener(task1 -> {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task1.isSuccessful());
                            if (task1.isSuccessful()){
                                Log.d(TAG, "onComplete: AuthState: " + getInstance().getCurrentUser().getUid());
                                addPost(post, view, getInstance().getCurrentUser().getUid());
                                mAuth2.signOut();

                            }
                            else {
                                Log.d(TAG, "onComplete: with failure "+task1.getException().toString());

                                Toast.makeText(AddClientActivity.this, "Unable to Register",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+ e.toString());
                    }
                });

            }
            else{
                Toast.makeText(AddClientActivity.this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void addPost(Client post, View view, String uid) {

        //add client in client database
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/clients/" + uid, post.toMap());
        mDatabase.updateChildren(childUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(view.getContext(), "Client added and email sent.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddClientActivity.this,WorkSpaceActivity.class);


                        //Send client email
                        String subject = "Synergii temporary Login Credentials ";
                        String message = "You have been added as a client.\n" +
                                "These are your login details:\n" +
                                "Username: " + mEmail.getText().toString() +
                                "\n Password:" + mClientPassword.getText().toString() +
                                "\n Change password after first login.";
                        Intent intent2 = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", "mEmail.getText().toString()", null));
                        intent2.putExtra(Intent.EXTRA_SUBJECT, subject);
                        intent2.putExtra(Intent.EXTRA_TEXT, message);
                        intent2.putExtra(Intent.EXTRA_EMAIL, mEmail.getText().toString());
                        startActivity(intent2);
                        finish();

                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), "something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
