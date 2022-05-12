package com.example.reina;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Button UpdateSettings;
    private EditText userName, userAbout;
    private CircleImageView profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        UpdateSettings = findViewById(R.id.update_setting_button);
        userName = findViewById(R.id.username_setting);
        userAbout = findViewById(R.id.profile_about);
        profilePhoto = findViewById(R.id.profile_picture_setting);


    }
}