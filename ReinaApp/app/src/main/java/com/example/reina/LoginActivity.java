package com.example.reina;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton, phoneLoginButton;
    private EditText userMail, userPassword;
    private TextView signupNew, forgetPassword;

    private FirebaseUser existUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //controls
        loginButton = findViewById(R.id.login_button);
        phoneLoginButton = findViewById(R.id.another_login_button);

        userMail = findViewById(R.id.login_mail);
        userPassword = findViewById(R.id.login_password);

        signupNew = findViewById(R.id.signup_new);
        forgetPassword = findViewById(R.id.forget_password);

        signupNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupActivityIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signupActivityIntent);
            }
        });


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