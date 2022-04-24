package com.example.reina;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseUser existUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (existUser != null){
            send_user_to_MainActivity();
        }
    }

    private void send_user_to_MainActivity() {
        Intent mainIntent = new Intent(LoginActivity.this, com.example.reina.MainActivity.class);
        startActivity(mainIntent);
    }
}