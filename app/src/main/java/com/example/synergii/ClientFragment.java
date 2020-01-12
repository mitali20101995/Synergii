package com.example.synergii;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.synergii.models.Client;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class ClientFragment extends Fragment {
    private static final String TAG = "ClientProfileFragment";
    private static final int REQUEST_CODE = 1234;
    private static final String DOMAIN_NAME = "gmail.com";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmail, mCurrentPassword, mFName, mLName;
    private ImageView mProfileImage;
    private Button mSave, mEditImage;
    private TextView mPhone;
    private ProgressBar mProgressBar;
    private TextView mResetPasswordLink;
    private DatabaseReference mDatabase;
    private boolean mStoragePermissions;
    public static boolean isActivityRunning;
    private StorageTask uploadTask;
    Client loggedInUser;
    private StorageReference mStorageRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mEmail = view.findViewById(R.id.editTextEmail);
        mSave = view.findViewById(R.id.updateProfileBtn);
        mProgressBar = view.findViewById(R.id.progressBarClientProfile);
        mResetPasswordLink = view.findViewById(R.id.textViewChangePasswordLink);
        mFName = view.findViewById(R.id.editTextFName);
        mLName = view.findViewById(R.id.editTextLName);
        mEditImage = view.findViewById(R.id.editImageBtn);
        mPhone = view.findViewById(R.id.editTextPhNumber);
        mProfileImage = view.findViewById(R.id.clientProfilePicForm);
        mStorageRef = FirebaseStorage.getInstance().getReference("images");

        verifyStoragePermissions();
        setCurrentEmail();
        init();

        //update profile pic
        mEditImage.setOnClickListener(v -> {
            if (uploadTask != null && uploadTask.isInProgress())
            {
                Toast.makeText(getContext(),"Upload in progress.",Toast.LENGTH_LONG).show();
            }
            else {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }

        });
        return view;
    }

    private Task<Uri> uploadImageTask(Uri imguri){
        StorageReference imageRef = mStorageRef.child(System.currentTimeMillis()+"."+ getExtenstion(imguri));

        uploadTask = imageRef.putFile(imguri);

        return uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(),"Image uploaded successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    private String getExtenstion(Uri uri){
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == 1 && resultCode == RESULT_OK && intent != null && intent.getData() != null ){
            Uri imguri = intent.getData();

            uploadImageTask(imguri).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {


                    loggedInUser.setProfilePhoto(task.getResult().toString());
                    Picasso.with(getContext()).load(task.getResult()).resize(mProfileImage.getWidth(), mProfileImage.getHeight()).centerCrop().into(mProfileImage);

                }
            });
        }
    }
    private void init(){
        getUserAccountData();
        mSave.setOnClickListener(v -> {
            Log.d(TAG, "onClick: attempting to save settings.");
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            reference.child(getString(R.string.dbnode_clients))
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(getString(R.string.field_client_profile_photo))
                    .setValue(loggedInUser.getProfilePhoto());

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

        Query query1 = reference.child(getString(R.string.dbnode_clients))
                .orderByKey()
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //this loop will return a single result
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: (QUERY METHOD 1) found client: "
                            + singleSnapshot.getValue(Client.class).toString());
                    loggedInUser = singleSnapshot.getValue(Client.class);
                    mFName.setText(loggedInUser.getFirstName());
                    mLName.setText(loggedInUser.getLastName());
                    mPhone.setText(loggedInUser.getPhone());
                    if(loggedInUser.getProfilePhoto() != null){
                        Picasso.with(getContext()).load(Uri.parse(loggedInUser.getProfilePhoto())).resize(mProfileImage.getWidth(), mProfileImage.getHeight()).centerCrop().into(mProfileImage);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
