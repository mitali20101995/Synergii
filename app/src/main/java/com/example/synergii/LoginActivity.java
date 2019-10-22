package com.example.synergii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_login);
    }

    public void moveToRegisterForm(View view)
    {
        Intent intent = new Intent(LoginActivity.this, RegisterFormActivity.class);
        startActivity(intent);
    }

}
