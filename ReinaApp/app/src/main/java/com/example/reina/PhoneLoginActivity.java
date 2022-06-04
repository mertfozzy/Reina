package com.example.reina;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PhoneLoginActivity extends AppCompatActivity {

    private Button sendAuthCodeButton, authenticateButton;
    private EditText phoneNumberInput, authCodeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        sendAuthCodeButton = findViewById(R.id.send_auth_code);
        authenticateButton = findViewById(R.id.authenticate_the_code);

        phoneNumberInput = findViewById(R.id.phone_number_input);
        authCodeInput = findViewById(R.id.authenticate_code_input);

        sendAuthCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAuthCodeButton.setVisibility(View.INVISIBLE);
                authenticateButton.setVisibility(View.VISIBLE);
                phoneNumberInput.setVisibility(View.INVISIBLE);
                authCodeInput.setVisibility(View.VISIBLE);
            }
        });

    }
}