package com.example.reina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileActivity extends AppCompatActivity {

    private String fetchedUserID, activeStatus, activeUserID;

    private CircleImageView userProfilePhoto;
    private TextView userProfileName, userProfileAbout;
    private Button sendMessageButton, evaluationButton;

    //firebase
    private DatabaseReference userPath, chatRequestPath, chatsPath, notificationPath;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fetchedUserID = getIntent().getExtras().get("showClickedUserID").toString();

        //Toast.makeText(this, "ID : " + fetchedUserID, Toast.LENGTH_LONG).show();

        userProfilePhoto = findViewById(R.id.profile_picture_visit);
        userProfileName = findViewById(R.id.username_visit);
        userProfileAbout = findViewById(R.id.about_visit);
        sendMessageButton = findViewById(R.id.message_sent_request);
        evaluationButton = findViewById(R.id.message_request_evaluation);

        activeStatus = "new";

        userPath = FirebaseDatabase.getInstance().getReference().child("Users");
        chatRequestPath = FirebaseDatabase.getInstance().getReference().child("Chat Request");
        chatsPath = FirebaseDatabase.getInstance().getReference().child("Chats");
        notificationPath = FirebaseDatabase.getInstance().getReference().child("Notifications");
        mAuth = FirebaseAuth.getInstance();
        activeUserID = mAuth.getCurrentUser().getUid();


        userInformationFetch();

    }

    private void userInformationFetch() {

        userPath.child(fetchedUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if ((snapshot.exists()) && (snapshot.hasChild("uid"))){

                    String userName = snapshot.child("name").getValue().toString();
                    String userAbout = snapshot.child("about").getValue().toString();

                    userProfileName.setText(userName);
                    userProfileAbout.setText(userAbout);

                    //chat request
                    manageChatRequest();

                }
                else{
                    Toast.makeText(profileActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void manageChatRequest() {

        //if there is a request, show cancel button
        chatRequestPath.child(activeUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild(fetchedUserID)){
                    String requestType = snapshot.child(fetchedUserID).child("request_type").getValue().toString();

                    if (requestType.equals("sent")){

                        activeStatus = "request_sent";
                        sendMessageButton.setText("Cancel Chat Request");

                    }

                    else{

                        activeStatus = "request_received";
                        sendMessageButton.setText("Accept the Chat Request");

                        evaluationButton.setVisibility(View.VISIBLE);
                        evaluationButton.setEnabled(true);
                        evaluationButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cancelChatRequest();
                            }
                        });

                    }
                }

                else {

                    chatsPath.child(activeUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChild(fetchedUserID)){

                                activeStatus = "contacts";
                                sendMessageButton.setText("Clear This Chat");

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (activeUserID.equals(fetchedUserID)){
            //hide button (user cannot send message to himself)
            sendMessageButton.setVisibility(View.INVISIBLE);
        }
        else {
            //messageRequest
            sendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    sendMessageButton.setEnabled(false);

                    if (activeStatus.equals("new")){
                        sendChatRequest();
                    }
                    if (activeStatus.equals("request_sent")){
                        cancelChatRequest();
                    }
                    if (activeStatus.equals("request_received")){
                        acceptChatRequest();
                    }
                    if (activeStatus.equals("contacts")){
                        clearPrivateChat();
                    }
                }
            });
        }

    }

    private void clearPrivateChat() {

        chatsPath.child(activeUserID).child(fetchedUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    chatsPath.child(fetchedUserID).child(activeUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                sendMessageButton.setEnabled(true);
                                activeStatus = "new";
                                sendMessageButton.setText("Send a Chat Request");

                                evaluationButton.setVisibility(View.INVISIBLE);
                                evaluationButton.setEnabled(false);

                            }

                        }
                    });
                }

            }
        });

    }

    private void acceptChatRequest() {

        chatsPath.child(activeUserID).child(fetchedUserID).child("Chats").setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    chatsPath.child(fetchedUserID).child(activeUserID).child("Chats").setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                chatRequestPath.child(activeUserID).child(fetchedUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){

                                            chatRequestPath.child(fetchedUserID).child(activeUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    sendMessageButton.setEnabled(true);
                                                    activeStatus = "contacts";
                                                    sendMessageButton.setText("Clear Chat");
                                                    evaluationButton.setVisibility(View.INVISIBLE);
                                                    evaluationButton.setEnabled(false);

                                                }
                                            });

                                        }

                                    }
                                });

                            }

                        }
                    });

                }

            }
        });

    }

    private void cancelChatRequest() {

        chatRequestPath.child(activeUserID).child(fetchedUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    chatRequestPath.child(fetchedUserID).child(activeUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                sendMessageButton.setEnabled(true);
                                activeStatus = "new";
                                sendMessageButton.setText("Send a Chat Request");

                                evaluationButton.setVisibility(View.INVISIBLE);
                                evaluationButton.setEnabled(false);

                            }

                        }
                    });
                }

            }
        });



    }

    private void sendChatRequest() {

        chatRequestPath.child(activeUserID).child(fetchedUserID).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    chatRequestPath.child(fetchedUserID).child(activeUserID).child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                //notification system
                                HashMap<String, String> chatNotificationMap = new HashMap<>();
                                chatNotificationMap.put("fromWho", activeUserID);
                                chatNotificationMap.put("type", "request");

                                notificationPath.child(fetchedUserID).push().setValue(chatNotificationMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            sendMessageButton.setEnabled(true);
                                            activeStatus = "request_sent";
                                            sendMessageButton.setText("Cancel Chat Request");
                                        }
                                    }
                                });



                            }

                        }
                    });
                }

            }
        });

    }
}