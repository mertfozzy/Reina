package com.example.reina;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity {

    private Button createAccountButton;
    private EditText userMail, userPassword;
    private TextView alreadyHaveAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //controls
        createAccountButton = findViewById(R.id.signup_button);

        userMail = findViewById(R.id.signup_mail);
        userPassword = findViewById(R.id.signup_password);

        alreadyHaveAccount = findViewById(R.id.login_instead);

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginActivityIntent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(loginActivityIntent);
            }
        });

    }
}