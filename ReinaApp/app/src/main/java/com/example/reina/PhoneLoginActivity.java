package com.example.reina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {

    private Button sendAuthCodeButton, authenticateButton;
    private EditText phoneNumberInput, authCodeInput;

    //phone login credentials
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    //firebase
    FirebaseAuth mAuth;

    //loading window
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        sendAuthCodeButton = findViewById(R.id.send_auth_code);
        authenticateButton = findViewById(R.id.authenticate_the_code);

        phoneNumberInput = findViewById(R.id.phone_number_input);
        authCodeInput = findViewById(R.id.authenticate_code_input);

        //progress dialog
        loadingBar = new ProgressDialog(this);

        //firebase
        mAuth = FirebaseAuth.getInstance();

        sendAuthCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phoneNumberInput.getText().toString();

                if (TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(PhoneLoginActivity.this, "Phone number cannot be empty.", Toast.LENGTH_LONG).show();
                }
                else{
                    //progress dialog
                    loadingBar.setTitle("Phone Authentication");
                    loadingBar.setMessage("Please wait..");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    /*
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,
                            60,
                            TimeUnit.SECONDS,
                            PhoneLoginActivity.this,
                            callbacks);*/

                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber(phoneNumber)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(PhoneLoginActivity.this)                 // Activity (for callback binding)
                                    .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }

            }
        });

        authenticateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //visibility
                sendAuthCodeButton.setVisibility(View.INVISIBLE);
                phoneNumberInput.setVisibility(View.INVISIBLE);

                String authenticateCode = authCodeInput.getText().toString();
                if (TextUtils.isEmpty(authenticateCode)){
                    Toast.makeText(PhoneLoginActivity.this, "Authentication Code cannot be empty.", Toast.LENGTH_LONG).show();
                }
                else {
                    //progress dialog
                    loadingBar.setTitle("Code Authentication");
                    loadingBar.setMessage("Please wait..");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, authenticateCode);
                    signInWithPhone(credential);
                }
            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhone(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                loadingBar.dismiss();

                Toast.makeText(PhoneLoginActivity.this, "Invalid phone number! Be careful about Country Codes.", Toast.LENGTH_LONG).show();

                //visibility
                sendAuthCodeButton.setVisibility(View.VISIBLE);
                authenticateButton.setVisibility(View.INVISIBLE);
                phoneNumberInput.setVisibility(View.VISIBLE);
                authCodeInput.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                mResendToken = token;

                loadingBar.dismiss();

                Toast.makeText(PhoneLoginActivity.this, "Code sent!", Toast.LENGTH_SHORT).show();

                //visibility
                sendAuthCodeButton.setVisibility(View.INVISIBLE);
                authenticateButton.setVisibility(View.VISIBLE);
                phoneNumberInput.setVisibility(View.INVISIBLE);
                authCodeInput.setVisibility(View.VISIBLE);
            }

        };

    }

    private void signInWithPhone(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(PhoneLoginActivity.this, "Authentication Confirmed.", Toast.LENGTH_LONG).show();
                            sendUserToMain();
                        }
                        else {
                            String errorMessage = task.getException().toString();
                            Toast.makeText(PhoneLoginActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendUserToMain() {
        Intent mainPage = new Intent(PhoneLoginActivity.this, MainActivity.class);
        mainPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainPage);
        finish();
    }
}