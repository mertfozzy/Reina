package com.example.reina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Button UpdateSettings;
    private EditText userName, userAbout;
    private CircleImageView profilePhoto;

    //Firebase
    private FirebaseAuth mAuthentication;
    private DatabaseReference dataPath;

    private String existUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Firebase
        mAuthentication = FirebaseAuth.getInstance();
        dataPath = FirebaseDatabase.getInstance().getReference();

        existUserID = mAuthentication.getCurrentUser().getUid();

        UpdateSettings = findViewById(R.id.update_setting_button);
        userName = findViewById(R.id.username_setting);
        userAbout = findViewById(R.id.profile_about);
        profilePhoto = findViewById(R.id.profile_picture_setting);

        UpdateSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNewSettings();
            }
        });

    }

    private void updateNewSettings() {

        String userNameSetting = userName.getText().toString();
        String userAboutSetting = userAbout.getText().toString();

        if (TextUtils.isEmpty(userNameSetting)){
            Toast.makeText(this, "Name cannot be empty.", Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(userAboutSetting)){
            Toast.makeText(this, "Please type something.", Toast.LENGTH_LONG).show();
        }
        else {
            HashMap <String, String> profileMap = new HashMap<>();
            profileMap.put("uid", existUserID);
            profileMap.put("name", userNameSetting);
            profileMap.put("about", userAboutSetting);

            dataPath.child("Users").child(existUserID).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){
                        Toast.makeText(SettingsActivity.this, "Profile is updated succesfully..", Toast.LENGTH_SHORT).show();
                        Intent mainScreen = new Intent(SettingsActivity.this, MainActivity.class);
                        mainScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainScreen);
                        finish();
                    }
                    else{
                        String message = task.getException().toString();
                        Toast.makeText(SettingsActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }
}