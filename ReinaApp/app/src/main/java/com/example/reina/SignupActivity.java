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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class SignupActivity extends AppCompatActivity {

    private Button createAccountButton;
    private EditText userMail, userPassword;
    private TextView alreadyHaveAccount;

    //Firebase :
    private DatabaseReference rootReference;
    private FirebaseAuth mAuthentication;

    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Firebase Authentication
        mAuthentication = FirebaseAuth.getInstance();
        rootReference = FirebaseDatabase.getInstance().getReference();

        //controls
        createAccountButton = findViewById(R.id.signup_button);

        userMail = findViewById(R.id.signup_mail);
        userPassword = findViewById(R.id.signup_password);

        alreadyHaveAccount = findViewById(R.id.login_instead);

        loadingDialog = new ProgressDialog(this);

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginActivityIntent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(loginActivityIntent);
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewAccount();
            }
        });

    }

    private void CreateNewAccount() {
        String mail = userMail.getText().toString();
        String password = userPassword.getText().toString();

        if(TextUtils.isEmpty(mail)){
            Toast.makeText(this, "E-mail cannot be empty.", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
        }
        else{

            loadingDialog.setTitle("New account is creating..");
            loadingDialog.setMessage("Please wait a second..");
            loadingDialog.setCanceledOnTouchOutside(true);
            loadingDialog.show();

            mAuthentication.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    
                    if (task.isSuccessful()){

                        String existUserID = mAuthentication.getCurrentUser().getUid();
                        rootReference.child("Users").child(existUserID).setValue("");

                        FirebaseMessaging.getInstance().getToken()
                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {

                                        String deviceToken = task.getResult();

                                        rootReference.child("Users").child(existUserID).child("device_token").setValue(deviceToken);

                                    }
                                });

                        Intent mainScreen = new Intent(SignupActivity.this, MainActivity.class);
                        mainScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainScreen);
                        finish();

                        Toast.makeText(SignupActivity.this, "Account is created succesfully.", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                    else{
                        String message = task.getException().toString();
                        Toast.makeText(SignupActivity.this, "Error: " + message +" Please check your account information.", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                }
            });
        }
    }
}