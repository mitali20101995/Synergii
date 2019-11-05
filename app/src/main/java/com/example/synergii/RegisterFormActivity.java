package com.example.synergii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.synergii.models.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterFormActivity extends AppCompatActivity {

    private static final String TAG = "RegisterFormActivity";
    private static final String DOMAIN_NAME = "gmail.com";

    private FirebaseAuth mAuth;
    private EditText edtTextEmail;
    private EditText edtTextPassword;
    private EditText edtTextConfirmPassword;
    private EditText edtTextFirstName;
    private EditText edtTextLastName;
    private ProgressBar mProgressBar;

    public static boolean isActivityRunning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);

        mAuth = FirebaseAuth.getInstance();

        mProgressBar = (ProgressBar) findViewById(R.id.progressBarSignUp);
        edtTextEmail = (EditText) findViewById(R.id.email_txt);
        edtTextPassword = (EditText)findViewById(R.id.password_txt);
        edtTextConfirmPassword = (EditText) findViewById(R.id.confirm_password_txt);
        edtTextFirstName = (EditText) findViewById(R.id.f_name_txt);
        edtTextLastName = (EditText) findViewById(R.id.l_name_txt);

        hideSoftKeyboard();
    }

    public void moveToLogInScreen(View view)
    {
        Intent intent = new Intent(RegisterFormActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean doStringsMatch(String s1, String s2){
        return s1.equals(s2);
    }


    public void signUpClick(View view)
    {
        Log.d(TAG, "onClick: attempting to register.");
        //check for null valued EditText fields
        if(!isEmpty(edtTextEmail.getText().toString())
                && !isEmpty(edtTextPassword.getText().toString())
                && !isEmpty(edtTextConfirmPassword.getText().toString())){
            if(isValidDomain(edtTextEmail.getText().toString())){
                if(doStringsMatch(edtTextPassword.getText().toString(),
                        edtTextConfirmPassword.getText().toString())){
                    registerNewEmail(edtTextEmail.getText().toString(),
                            edtTextPassword.getText().toString(),
                            edtTextFirstName.getText().toString(),
                            edtTextLastName.getText().toString()
                    );
                }else{
                    Toast.makeText(RegisterFormActivity.this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(RegisterFormActivity.this, "Please Register with gmail", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(RegisterFormActivity.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
        }

    }
    public void registerNewEmail(final String email, String password, String firstname, String lastname){

        showDialog();


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()){
                            Log.d(TAG, "onComplete: AuthState: " + FirebaseAuth.getInstance().getCurrentUser().getUid());

                            sendVerificationEmail();
                            User user = new User();
                            user.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            user.setEmail(email);
                            user.setFirstName(firstname);
                            user.setLastName(lastname);
                            FirebaseDatabase.getInstance().getReference()
                                    .child(getString(R.string.dbnode_users))
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            FirebaseAuth.getInstance().signOut();
                                            redirectLoginScreen();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterFormActivity.this, "something went wrong.", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    redirectLoginScreen();

                                }
                            });
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterFormActivity.this, "Unable to Register",
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideDialog();
                    }
                });
    }

    private boolean isValidDomain(String email){
        Log.d(TAG, "isValidDomain: verifying email has correct domain: " + email);
        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        Log.d(TAG, "isValidDomain: users domain: " + domain);
        return domain.equals(DOMAIN_NAME);
    }

    private boolean isEmpty(String string){
        return string.equals("");
    }
    private void redirectLoginScreen(){
        Log.d(TAG, "redirectLoginScreen: redirecting to login screen.");

        Intent intent = new Intent(RegisterFormActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterFormActivity.this, "Sent Verification Email", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(RegisterFormActivity.this, "Couldn't Send Verification Email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
        isActivityRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityRunning = false;
    }
}
