package com.example.reina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class profileActivity extends AppCompatActivity {

    private String fetchedUserID, activeStatus, activeUserID;

    private CircleImageView userProfilePhoto;
    private TextView userProfileName, userProfileAbout;
    private Button sendMessageButton;

    //firebase
    private DatabaseReference userPath, chatRequestPath;
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

        activeStatus = "new";

        userPath = FirebaseDatabase.getInstance().getReference().child("Users");
        chatRequestPath = FirebaseDatabase.getInstance().getReference().child("Chat Request");
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
                }
            });
        }

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