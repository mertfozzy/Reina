package com.example.reina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabAccessAdapter myTabAccessAdapter;

    //Firebase Auth
    private FirebaseUser existUser;
    private FirebaseAuth mAuthentication;

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
        existUser = mAuthentication.getCurrentUser();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (existUser == null){
            send_user_to_LoginActivity();
        }
    }

    private void send_user_to_LoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
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

        }
        if (item.getItemId() == R.id.main_settings){
            Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settings);
        }
        if (item.getItemId() == R.id.main_logout){
            mAuthentication.signOut();
            Intent loginScreen = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginScreen);
        }
        return true;
    }
}