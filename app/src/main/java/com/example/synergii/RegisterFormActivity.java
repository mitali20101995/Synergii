package com.example.synergii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_register_form);
    }

    public void moveToLogInScreen(View view)
    {
        Intent intent = new Intent(RegisterFormActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void signUpClick(View view)
    {
        Intent intent = new Intent(RegisterFormActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
