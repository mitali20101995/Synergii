package com.example.synergii;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.synergii.models.Client;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ClientFragment extends Fragment {
    private static final String TAG = "ClientProfileFragment";
    private static final int REQUEST_CODE = 1234;
    private static final String DOMAIN_NAME = "gmail.com";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmail, mCurrentPassword, mFName, mLName;
    private Button mSave;
    private TextView mPhone;
    private ProgressBar mProgressBar;
    private TextView mResetPasswordLink;
    private DatabaseReference mDatabase;
    private boolean mStoragePermissions;
    //private Uri mSelectedImageUri;
    //private Bitmap mSelectedImageBitmap;
    private byte[] mBytes;
    private double progress;
    public static boolean isActivityRunning;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mEmail = view.findViewById(R.id.editTextEmail);
        mSave = view.findViewById(R.id.updateProfileBtn);
        //mProgressBar = view.findViewById(R.id.progressBarClientProfile);
        mResetPasswordLink = view.findViewById(R.id.textViewChangePassHead);
        mFName = view.findViewById(R.id.editTextFName);
        mLName = view.findViewById(R.id.editTextLName);
        mPhone = view.findViewById(R.id.editTextPhNumber);
        verifyStoragePermissions();
        setCurrentEmail();
        init();
        return view;
    }
    private void init(){

        getUserAccountData();

        mSave.setOnClickListener(v -> {
            Log.d(TAG, "onClick: attempting to save settings.");


            //see if they changed the email
//            if (!mEmail.getText( ).toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
//                //make sure email an current password fields are filled
//                if (!isEmpty(mEmail.getText().toString())
//                        && !isEmpty(mCurrentPassword.getText().toString())) {
//
//                }
//                //verify that user is changing to a company email address
//                if (isValidDomain(mEmail.getText().toString())) {
//                    editUserEmail(v);
//                } else {
//                    Toast.makeText(v.getContext(), "Invalid Domain", Toast.LENGTH_SHORT).show();
//                }
//
//            } else {
//                Toast.makeText(v.getContext(), "Email and Current Password Fields Must be Filled to Save", Toast.LENGTH_SHORT).show();
//            }

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            //Change first name
            if(!mFName.getText().toString().equals("")){
                reference.child(getString(R.string.dbnode_clients))
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(getString(R.string.field_fname))
                        .setValue(mFName.getText().toString());
            }
            //Change last name
            if(!mLName.getText().toString().equals("")){
                reference.child(getString(R.string.dbnode_clients))
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(getString(R.string.field_lname))
                        .setValue(mLName.getText().toString());
            }
            //Change phone number
            if(!mPhone.getText().toString().equals("")){
                reference.child(getString(R.string.dbnode_clients))
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(getString(R.string.field_phone_number))
                        .setValue(mPhone.getText().toString());
            }

            mResetPasswordLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: sending password reset link");

                    //Reset Password link
                    sendResetPasswordLink();
                }
            });
            Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(),ClientHomeActivity.class);
            startActivity(intent);
        });


    }
    private void setCurrentEmail(){
        Log.d(TAG, "setCurrentEmail: setting current email to EditText field");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            Log.d(TAG, "setCurrentEmail: user is NOT null.");

            String email = user.getEmail();

            Log.d(TAG, "setCurrentEmail: got the email: " + email);

            mEmail.setText(email);
        }
    }
    private boolean isValidDomain(String email) {
        Log.d(TAG, "isValidDomain: verifying email has correct domain: " + email);
        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        Log.d(TAG, "isValidDomain: users domain: " + domain);
        return domain.equals(DOMAIN_NAME);
    }
    private void getUserAccountData(){
        Log.d(TAG, "getUserAccountData: getting the user's account information");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        /*
            ---------- QUERY Method 1 ----------
         */
        Query query1 = reference.child(getString(R.string.dbnode_clients))
                .orderByKey()
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //this loop will return a single result
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: (QUERY METHOD 1) found user: "
                            + singleSnapshot.getValue(Client.class).toString());
                    Client client = singleSnapshot.getValue(Client.class);

                    mFName.setText(client.getFirstName());
                    mLName.setText(client.getLastName());
                    mPhone.setText(client.getPhone());

                    //ImageLoader.getInstance().displayImage(client.getProfile_image(), mProfileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /*
            ---------- QUERY Method 2 ----------
         */
        Query query2 = reference.child(getString(R.string.dbnode_clients))
                .orderByChild(getString(R.string.field_client_id))
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //this loop will return a single result
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: (QUERY METHOD 2) found user: "
                            + singleSnapshot.getValue(Client.class).toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

    }
    public void verifyStoragePermissions(){
        Log.d(TAG, "verifyPermissions: asking user for permissions.");
        String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(this.getContext(),
                permissions[0] ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getContext(),
                permissions[1] ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getContext(),
                permissions[2] ) == PackageManager.PERMISSION_GRANTED) {
            mStoragePermissions = true;
        } else {
            requestPermissions(
                    permissions,
                    REQUEST_CODE
            );

        }

    }
    private void sendResetPasswordLink() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Password Reset Email sent.");
                            Toast.makeText(getContext(), "Sent Password Reset Link to Email",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "onComplete: No user associated with that email.");

                            Toast.makeText(getContext(), "No User Associated with that Email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void editUserEmail(View v){

        showDialog();

        AuthCredential credential = EmailAuthProvider
                .getCredential(FirebaseAuth.getInstance().getCurrentUser().getEmail(), mCurrentPassword.getText().toString());
        Log.d(TAG, "editUserEmail: reauthenticating with:  \n email " + FirebaseAuth.getInstance().getCurrentUser().getEmail()
                + " \n passowrd: " + mCurrentPassword.getText().toString());


        FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete: reauthenticate success.");

                        //make sure the domain is valid
                        if(isValidDomain(mEmail.getText().toString())){

                            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(mEmail.getText().toString()).addOnCompleteListener(
                                    task1 -> {

                                        if(task1.isSuccessful()){

                                            Log.d(TAG, "onComplete: RESULT: " + task1.getResult().getSignInMethods().size());
                                            if(task1.getResult().getSignInMethods().size() == 1){
                                                Log.d(TAG, "onComplete: That email is already in use.");
                                                hideDialog();
                                                Toast.makeText(v.getContext(), "That email is already in use", Toast.LENGTH_SHORT).show();

                                            }else{
                                                Log.d(TAG, "onComplete: That email is available.");

                                                //add new email
                                                FirebaseAuth.getInstance().getCurrentUser().updateEmail(mEmail.getText().toString())
                                                        .addOnCompleteListener(task11 -> {
                                                            if (task11.isSuccessful()) {
                                                                Log.d(TAG, "onComplete: User email address updated.");
                                                                Toast.makeText(v.getContext(), "Updated email", Toast.LENGTH_SHORT).show();
                                                                sendVerificationEmail(v);
                                                                FirebaseAuth.getInstance().signOut();
                                                            } else {
                                                                Log.d(TAG, "onComplete: Could not update email.");
                                                                Toast.makeText(v.getContext(), "unable to update email", Toast.LENGTH_SHORT).show();
                                                            }
                                                            hideDialog();
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            hideDialog();
                                                            Toast.makeText(v.getContext(), "unable to update email", Toast.LENGTH_SHORT).show();
                                                        });
                                            }

                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        hideDialog();
                                        Toast.makeText(v.getContext(), "unable to update email", Toast.LENGTH_SHORT).show();
                                    });
                        }else{
                            Toast.makeText(v.getContext(), "you must use a company email", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Log.d(TAG, "onComplete: Incorrect Password");
                        Toast.makeText(v.getContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }

                })
                .addOnFailureListener(e -> {
                    hideDialog();
                    Toast.makeText(v.getContext(), "“unable to update email”", Toast.LENGTH_SHORT).show();
                });
    }
    public void sendVerificationEmail(View v) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(v.getContext(), "Sent Verification Email", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(v.getContext(), "Couldn't Verification Send Email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

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
    public void onStart() {
        super.onStart();
//        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
        isActivityRunning = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
//            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
        isActivityRunning = false;
    }


}
