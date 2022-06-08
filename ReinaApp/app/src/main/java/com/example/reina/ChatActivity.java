package com.example.reina;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String IDValueFetcher, NameValueFetcher;

    private TextView userName, userLastSeen;
    private CircleImageView profilePhoto;
    private ImageView backToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        //take intent
        IDValueFetcher = getIntent().getExtras().get("user_id_visit").toString();
        NameValueFetcher = getIntent().getExtras().get("user_name_visit").toString();

        userName = findViewById(R.id.username_show_activity);
        userLastSeen = findViewById(R.id.userlastseen_show_activity);
        profilePhoto = findViewById(R.id.userprofilepicture_show_activity);
        backToMain = findViewById(R.id.back_to_main_picture);

        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backtochat = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(backtochat);
            }
        });

        /*
        ChatToolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(ChatToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.private_chat_bar, null);
        actionBar.setCustomView(actionBarView);

        */



        userName.setText(NameValueFetcher);

        //Toast.makeText(this, IDValueFetcher + " " + NameValueFetcher, Toast.LENGTH_SHORT).show();

    }
}