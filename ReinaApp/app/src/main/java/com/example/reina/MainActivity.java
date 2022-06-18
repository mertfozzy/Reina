package com.example.reina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabAccessAdapter myTabAccessAdapter;

    //Firebase Auth
    //private FirebaseUser existUser;
    private FirebaseAuth mAuthentication;
    private DatabaseReference usersReference;
    private String activeUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Reina Messenger");

        myViewPager = findViewById(R.id.main_tabs_pager);
        myTabAccessAdapter = new TabAccessAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabAccessAdapter);

        myTabLayout = findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);

        //Firebase
        mAuthentication = FirebaseAuth.getInstance();
        //existUser = mAuthentication.getCurrentUser();
        usersReference = FirebaseDatabase.getInstance().getReference();
        //activeUserID = mAuthentication.getCurrentUser().getUid();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser existUser = mAuthentication.getCurrentUser();

        if (existUser == null){
            send_user_to_LoginActivity();
        }

        else{
            userLastSeenUpdate("online");
            verifyIfUserExist();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser existUser = mAuthentication.getCurrentUser();
        if (existUser != null){
            userLastSeenUpdate("offline");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseUser existUser = mAuthentication.getCurrentUser();
        if (existUser != null){
            userLastSeenUpdate("offline");
        }
    }

    private void verifyIfUserExist() {

        String existUserID = mAuthentication.getCurrentUser().getUid();

        usersReference.child("Users").child(existUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if ((dataSnapshot.child("name").exists())){
                    //Toast.makeText(MainActivity.this, "Welcome..", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                    settings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(settings);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void send_user_to_LoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.main_find_friend){
            Intent findFriend = new Intent(MainActivity.this, FindFriend.class);
            startActivity(findFriend);
        }
        if (item.getItemId() == R.id.main_settings){
            Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settings);
        }
        if (item.getItemId() == R.id.main_logout){
            userLastSeenUpdate("offline");
            mAuthentication.signOut();
            Intent loginScreen = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginScreen);
        }

        if (item.getItemId() == R.id.main_create_group){
            newGroupRequest();
        }
        return true;
    }

    private void newGroupRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
        builder.setTitle("Enter a Group Name: ");
        final EditText groupNameField = new EditText(MainActivity.this);
        groupNameField.setTextColor(Color.BLACK);
        groupNameField.setHint("Example : Reina Developers");
        groupNameField.setHintTextColor(Color.GRAY);
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String groupName = groupNameField.getText().toString();

                if (TextUtils.isEmpty(groupName)){
                    Toast.makeText(MainActivity.this, "Group name cannot be empty.", Toast.LENGTH_LONG).show();
                }
                else{
                    CreateNewGroup(groupName);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void CreateNewGroup(String groupName) {
        usersReference.child("Groups").child(groupName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, groupName + " created successfully.", Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    private void userLastSeenUpdate(String lastSeen){

        String savedActiveTime, savedActiveDate;

        Calendar calendar = Calendar.getInstance();

        //date format
        SimpleDateFormat activeDate = new SimpleDateFormat("MMM dd, yyyy");
        savedActiveDate = activeDate.format(calendar.getTime());

        //time format
        SimpleDateFormat activeTime = new SimpleDateFormat("hh:mm a");
        savedActiveTime = activeTime.format(calendar.getTime());

        HashMap<String, Object> lastSeenStatusMap = new HashMap<>();
        lastSeenStatusMap.put("time", savedActiveTime);
        lastSeenStatusMap.put("date", savedActiveDate);
        lastSeenStatusMap.put("last_seen_status", lastSeen);

        activeUserID = mAuthentication.getCurrentUser().getUid();

        usersReference.child("Users").child(activeUserID).child("user_last_seen").updateChildren(lastSeenStatusMap);

    }
}