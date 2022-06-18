package com.example.reina;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reina.Adapter.MessageAdapter;
import com.example.reina.model.messages;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {



    private String IDValueFetcher, NameValueFetcher, IDMessageSender;

    private TextView userName, userLastSeen;
    private CircleImageView profilePhoto;
    private ImageView backToMain;
    private ImageButton messageSend, fileSendButton;
    private EditText messageInput;
    private FirebaseAuth mAuth;
    private DatabaseReference messagePath, userPath;

    private final List<messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView profileMessagesList;
    private String savedActiveTime, savedActiveDate;
    private String controller = "", myUrl = "";
    private StorageTask uploadTask;
    private Uri fileUri;
    private ProgressDialog loadingBar;


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
        messageSend = findViewById(R.id.private_message_send_button);
        fileSendButton = findViewById(R.id.send_file_button);
        messageInput = findViewById(R.id.private_message_input);

        messageAdapter = new MessageAdapter(messagesList);
        profileMessagesList = findViewById(R.id.user_private_chat_messages);
        linearLayoutManager = new LinearLayoutManager(this);
        profileMessagesList.setLayoutManager(linearLayoutManager);
        profileMessagesList.setAdapter(messageAdapter);

        loadingBar = new ProgressDialog(this);

        //calendar
        Calendar calendar = Calendar.getInstance();

        //date format
        SimpleDateFormat activeDate = new SimpleDateFormat("MMM dd, yyyy");
        savedActiveDate = activeDate.format(calendar.getTime());

        //time format
        SimpleDateFormat activeTime = new SimpleDateFormat("hh:mm a");
        savedActiveTime = activeTime.format(calendar.getTime());

        mAuth = FirebaseAuth.getInstance();
        messagePath = FirebaseDatabase.getInstance().getReference();
        userPath = FirebaseDatabase.getInstance().getReference();
        IDMessageSender = mAuth.getCurrentUser().getUid();






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

        messageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SendMessage();

            }
        });

        //when clicked file button
        fileSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options [] = new CharSequence[]{"Image", "Pdf", "Word Doc"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("Choose a File");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        //image
                        if (which == 0){

                            controller = "image";


                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Select a Picture"), 438);


                        }
                        //pdf
                        if (which == 1){

                            controller = "pdf";

                        }
                        //word
                        if (which == 2){

                            controller = "docx";

                        }

                    }
                });

                builder.show();

            }
        });

        showLastSeen();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 438 && resultCode == RESULT_OK && data.getData() != null){

            loadingBar.setTitle("File Sending");
            loadingBar.setMessage("Wait a second..");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            fileUri = data.getData();
            if (!controller.equals("image")){
                loadingBar.dismiss();
                Toast.makeText(this, "Error.", Toast.LENGTH_SHORT).show();
            }
            else if (controller.equals("image")){
                StorageReference storagePath = FirebaseStorage.getInstance().getReference().child("Image Files");
                String messageSenderPath = "Messages/" + IDMessageSender + "/" + IDValueFetcher;
                String messageReceiverPath = "Messages/" + IDValueFetcher + "/" + IDMessageSender;

                DatabaseReference userMessageKeyPath = messagePath.child("Messages").child(IDMessageSender).child(IDValueFetcher).push();
                final String messageAddingID = userMessageKeyPath.getKey();

                StorageReference filePath = storagePath.child(messageAddingID + "." + "jpeg");

                uploadTask = filePath.putFile(fileUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {

                        if (!task.isSuccessful()){
                            throw task.getException();
                        }

                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();

                            Map messageTextBody = new HashMap();
                            messageTextBody.put("message", myUrl);
                            messageTextBody.put("name", fileUri.getLastPathSegment());
                            messageTextBody.put("type", controller);
                            messageTextBody.put("who", IDMessageSender);
                            messageTextBody.put("toWho", IDValueFetcher);
                            messageTextBody.put("time", savedActiveTime);
                            messageTextBody.put("date", savedActiveDate);
                            messageTextBody.put("messageID", messageAddingID);

                            Map messageBodyDetails = new HashMap();
                            messageBodyDetails.put(messageSenderPath + "/" + messageAddingID, messageTextBody);
                            messageBodyDetails.put(messageReceiverPath + "/" + messageAddingID, messageTextBody);

                            messagePath.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {

                                    if (task.isSuccessful()){

                                        loadingBar.dismiss();

                                        //Toast.makeText(ChatActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();

                                    }

                                    else {

                                        loadingBar.dismiss();
                                       // Toast.makeText(ChatActivity.this, "Message error.", Toast.LENGTH_SHORT).show();

                                    }

                                    messageInput.setText("");

                                }
                            });
                        }
                    }
                });

            }
            else {
                loadingBar.dismiss();
               // Toast.makeText(this, "Error. Item cannot choose.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showLastSeen(){
        userPath.child("Users").child(IDValueFetcher).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //user last seen from database
                if (snapshot.child("user_last_seen").hasChild("last_seen_status")){
                    String status = snapshot.child("user_last_seen").child("last_seen_status").getValue().toString();
                    String date = snapshot.child("user_last_seen").child("date").getValue().toString();
                    String time = snapshot.child("user_last_seen").child("time").getValue().toString();

                    if (status.equals("online")){
                        userLastSeen.setText("online");
                        userLastSeen.setTextColor(Color.WHITE);
                    }
                    else if (status.equals("offline")){
                        userLastSeen.setText("Last Seen : " + date +  " " + time);
                        userLastSeen.setTextColor(Color.WHITE);
                    }
                }

                else {
                    userLastSeen.setText("offline");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        messagePath.child("Messages").child(IDMessageSender).child(IDValueFetcher)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        messages mesajlar = snapshot.getValue(messages.class);
                        messagesList.add(mesajlar);
                        messageAdapter.notifyDataSetChanged();

                        profileMessagesList.smoothScrollToPosition(profileMessagesList.getAdapter().getItemCount());

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

    private void SendMessage() {

        String messageText = messageInput.getText().toString();

        if (TextUtils.isEmpty(messageText)){

            Toast.makeText(this, "Message field cannot be empty.", Toast.LENGTH_SHORT).show();

        }

        else {

            String messageSenderPath = "Messages/" + IDMessageSender + "/" + IDValueFetcher;
            String messageReceiverPath = "Messages/" + IDValueFetcher + "/" + IDMessageSender;

            DatabaseReference userMessageKeyPath = messagePath.child("Messages").child(IDMessageSender).child(IDValueFetcher).push();
            String messageAddingID = userMessageKeyPath.getKey();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("type", "text");
            messageTextBody.put("who", IDMessageSender);
            messageTextBody.put("toWho", IDValueFetcher);
            messageTextBody.put("time", savedActiveTime);
            messageTextBody.put("date", savedActiveDate);
            messageTextBody.put("messageID", messageAddingID);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderPath + "/" + messageAddingID, messageTextBody);
            messageBodyDetails.put(messageReceiverPath + "/" + messageAddingID, messageTextBody);

            messagePath.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if (task.isSuccessful()){

                       // Toast.makeText(ChatActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();

                    }

                    else {

                      //  Toast.makeText(ChatActivity.this, "Message error.", Toast.LENGTH_SHORT).show();

                    }

                    messageInput.setText("");

                }
            });
        }

    }
}