package com.example.reina;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton sendMessageButton;
    private EditText userMessageInput;
    private ScrollView mScrollview;
    private TextView showTextMessages;

    //Firebase
    private FirebaseAuth mAuthentication;
    private DatabaseReference userPath, groupNamePath, groupMessageKeyPath;

    //Intent
    private String existGroupName, activeUserID, activeUserName, activeDate, activeTime ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        //fetch Intent
        existGroupName = getIntent().getExtras().get("GroupName").toString();
        Toast.makeText(this, existGroupName, Toast.LENGTH_LONG).show();

        //Firebase
        mAuthentication = FirebaseAuth.getInstance();
        activeUserID = mAuthentication.getCurrentUser().getUid();
        userPath = FirebaseDatabase.getInstance().getReference().child("Users");
        groupNamePath = FirebaseDatabase.getInstance().getReference().child("Groups").child(existGroupName);

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

                userMessageInput.setText("");

                mScrollview.fullScroll(ScrollView.FOCUS_DOWN);


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        groupNamePath.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.exists()){
                    showTextMessages(snapshot);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.exists()){
                    showTextMessages(snapshot);
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showTextMessages(DataSnapshot snapshot) {

        Iterator iterator = snapshot.getChildren().iterator();

        while (iterator.hasNext()){

            String chatDate = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();

            showTextMessages.append(chatName + " :\n" + chatMessage + "\n" + chatTime + "  " + chatDate + "\n\n\n");

            mScrollview.fullScroll(ScrollView.FOCUS_DOWN);

        }

    }

    private void saveMessageToDatabase() {

        String message = userMessageInput.getText().toString();
        String messageKey = groupNamePath.push().getKey();

        if (TextUtils.isEmpty(message)){
            Toast.makeText(this, "Message cannot be empty.", Toast.LENGTH_LONG).show();
        }

        else{
            Calendar dateForCalendar = Calendar.getInstance();
            SimpleDateFormat activeDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            activeDate = activeDateFormat.format(dateForCalendar.getTime());

            Calendar timeForCalendar = Calendar.getInstance();
            SimpleDateFormat activeTimeFormat = new SimpleDateFormat("hh:mm:ss a");
            activeTime = activeTimeFormat.format(timeForCalendar.getTime());

            HashMap<String, Object>groupMessageKey = new HashMap<>();
            groupNamePath.updateChildren(groupMessageKey);

            groupMessageKeyPath = groupNamePath.child(messageKey);

            HashMap<String, Object>messageInformationMap = new HashMap<>();

            messageInformationMap.put("name", activeUserName);
            messageInformationMap.put("message", message);
            messageInformationMap.put("date", activeDate);
            messageInformationMap.put("time", activeTime);

            groupMessageKeyPath.updateChildren(messageInformationMap);
        }

    }

    private void fetchUserInfo() {

        userPath.child(activeUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    activeUserName = snapshot.child("name").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}