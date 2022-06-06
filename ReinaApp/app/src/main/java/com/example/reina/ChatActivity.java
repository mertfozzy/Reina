package com.example.reina;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class ChatActivity extends AppCompatActivity {

    private String IDValueFetcher, NameValueFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //take intent
        IDValueFetcher = getIntent().getExtras().get("user_id_visit").toString();
        NameValueFetcher = getIntent().getExtras().get("user_name_visit").toString();

        //Toast.makeText(this, IDValueFetcher + " " + NameValueFetcher, Toast.LENGTH_SHORT).show();

    }
}