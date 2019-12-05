package com.example.synergii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.synergii.models.Client;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;
    // widgets
    private EditText mEmail, mPassword;
    private ProgressBar mProgressBar;
    private Button logIn;
    public static boolean isActivityRunning;
    private DatabaseReference mDatabase;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
        logIn = (Button)  findViewById(R.id.email_log_in_button);


        setupFirebaseAuth();

        if(servicesOK()){
            hideSoftKeyboard();
        }
    }

    public void moveToHome(View view)
    {

        //For Clients
        if(!isEmpty(mEmail.getText().toString())
                && !isEmpty(mPassword.getText().toString()))
        {
            showDialog();

            Query query = FirebaseDatabase.getInstance().getReference()
                    .child(getString(R.string.dbnode_clients))
                    .orderByChild("email")
                    .equalTo(mEmail.getText().toString());

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("LoginActivity","ondatachange");
                    Client client = dataSnapshot.getValue(Client.class);
                    email = client.getEmail();
                    password = client.getClientPassword();

                    if(mEmail.getText().toString().equals(email) && mPassword.getText().toString().equals(password))
                    {
                        Intent intent = new Intent(LoginActivity.this, ClientHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Client Authentication Failed", Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("LoginActivity","The read failed: " + databaseError);
                }
            });

        }
        else{
            Toast.makeText(LoginActivity.this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
        }

        //For Agent Only
        if(!isEmpty(mEmail.getText().toString())
                && !isEmpty(mPassword.getText().toString())){
            Log.d(TAG, "onClick: attempting to authenticate.");

            showDialog();

            FirebaseAuth.getInstance().signInWithEmailAndPassword(mEmail.getText().toString(),
                    mPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            hideDialog();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
            });
        }else{
            Toast.makeText(LoginActivity.this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
        }
    }

    public void moveToRegisterForm(View view)
    {
        Intent intent = new Intent(LoginActivity.this, RegisterFormActivity.class);
        startActivity(intent);
    }


    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    //check if email is verified
                    if(user.isEmailVerified()){
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        Toast.makeText(LoginActivity.this, "Authenticated with: " + user.getEmail(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, WorkSpaceActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }else{
                        Intent intent = new Intent(LoginActivity.this, ClientHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        Toast.makeText(LoginActivity.this, "Email is not Verified\nCheck your Inbox", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public boolean servicesOK(){
        Log.d(TAG, "servicesOK: Checking Google Services.");

        int isAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(LoginActivity.this);

        if(isAvailable == ConnectionResult.SUCCESS){
            Log.d(TAG, "servicesOK: Play Services is OK");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(isAvailable)){
            Log.d(TAG, "servicesOK: an error occurred, but it's resolvable.");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(LoginActivity.this, isAvailable, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this, "Can't connect to mapping services", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private boolean isEmpty(String string){
        return string.equals("");
    }

    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }
    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
        isActivityRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
        isActivityRunning = false;

    }

    public void moveToClient(View view)
    {
        Intent intent = new Intent(LoginActivity.this, ClientHomeActivity.class);
                        startActivity(intent);
                        finish();
    }

}
