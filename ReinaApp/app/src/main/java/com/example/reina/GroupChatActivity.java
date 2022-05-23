package com.example.reina;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GroupChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton sendMessageButton;
    private EditText userMessageInput;
    private ScrollView mScrollview;
    private TextView showTextMessages;

    //Intent
    private String existGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        //fetch Intent
        existGroupName = getIntent().getExtras().get("GroupName").toString();
        Toast.makeText(this, existGroupName, Toast.LENGTH_LONG).show();

        //definitons:
        mToolbar = findViewById(R.id.group_chat_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(existGroupName);

        sendMessageButton = findViewById(R.id.message_send_button);
        userMessageInput = findViewById(R.id.group_message_input);
        showTextMessages = findViewById(R.id.group_chat_text_show);
        mScrollview = findViewById(R.id.my_scroll_view);

    }
}