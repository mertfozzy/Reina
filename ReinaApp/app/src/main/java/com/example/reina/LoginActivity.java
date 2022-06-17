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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton, phoneLoginButton;
    private EditText userMail, userPassword;
    private TextView signupNew, forgetPassword;

    //Firebase
    //private FirebaseUser existUser;
    private FirebaseAuth mAuthentication;
    private DatabaseReference userPath;

    //Progress
    private ProgressDialog loginDialog;

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

        loginDialog = new ProgressDialog(this);

        //Firebase
        mAuthentication = FirebaseAuth.getInstance();
        userPath = FirebaseDatabase.getInstance().getReference().child("Users");
        //existUser = mAuthentication.getCurrentUser();

        signupNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupActivityIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signupActivityIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userLoginAuthentication();

            }
        });

        phoneLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phoneLogin = new Intent(LoginActivity.this, PhoneLoginActivity.class);
                startActivity(phoneLogin);
            }
        });


    }

    private void userLoginAuthentication() {

        String mail = userMail.getText().toString();
        String password = userPassword.getText().toString();

        if (TextUtils.isEmpty(mail)){
            Toast.makeText(this, "Email cannot be empty.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
        }
        else{
            loginDialog.setTitle("Login..");
            loginDialog.setMessage("Please wait a second..");
            loginDialog.setCanceledOnTouchOutside(true);
            loginDialog.show();

            //Firebase login:
            mAuthentication.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                        String activeUserID = mAuthentication.getCurrentUser().getUid();
                        //String deviceToken = String.valueOf(FirebaseMessaging.getInstance().getToken());

                        FirebaseMessaging.getInstance().getToken()
                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {

                                        String deviceToken = task.getResult();

                                        userPath.child(activeUserID).child("device_token").setValue(deviceToken);

                                        if (task.isSuccessful()){
                                            Intent mainScreen = new Intent(LoginActivity.this, MainActivity.class);
                                            mainScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(mainScreen);
                                            finish();
                                            Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                                            loginDialog.dismiss();
                                        }

                                    }
                                });



                    }
                    else {
                        String message = task.getException().toString();
                        Toast.makeText(LoginActivity.this, "Error: " + message + "Please check login information.", Toast.LENGTH_SHORT).show();
                        loginDialog.dismiss();
                    }

                }
            });

        }
    }
/*
    @Override
    protected void onStart() {
        super.onStart();

        if (existUser != null){
            send_user_to_MainActivity();
        }
    }
*/
    private void send_user_to_MainActivity() {
        Intent mainIntent = new Intent(LoginActivity.this, com.example.reina.MainActivity.class);
        startActivity(mainIntent);
    }
}