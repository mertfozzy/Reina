package com.example.reina;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GroupChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton sendMessageButton;
    private EditText userMessageInput;
    private ScrollView mScrollview;
    private TextView showTextMessages;

    //Firebase
    private FirebaseAuth mAuthentication;
    private DatabaseReference userPath;

    //Intent
    private String existGroupName, activeUserID, getActiveUserName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        //Firebase
        mAuthentication = FirebaseAuth.getInstance();
        activeUserID = mAuthentication.getCurrentUser().getUid();
        userPath = FirebaseDatabase.getInstance().getReference().child("Users");

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

        fetchUserInfo();

        //saving messages to the database
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveMessageToDatabase();

            }
        });

    }

    private void saveMessageToDatabase() {

        

    }

    private void fetchUserInfo() {

        userPath.child(activeUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    getActiveUserName = snapshot.child("name").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}