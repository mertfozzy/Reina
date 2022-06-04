package com.example.reina;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class profileActivity extends AppCompatActivity {

    private String fetchedUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fetchedUserID = getIntent().getExtras().get("showClickedUserID").toString();

        Toast.makeText(this, "ID : " + fetchedUserID, Toast.LENGTH_LONG).show();

    }
}