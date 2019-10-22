package com.example.synergii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    //Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            //this method will be executed once timer is over
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);

            //close this activity
            finish();
        }, SPLASH_TIME_OUT);
    }


}
