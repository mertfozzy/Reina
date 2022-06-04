package com.example.reina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

public class FindFriend extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView findFriendRecyclerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        //recycler
        findFriendRecyclerList = findViewById(R.id.find_friend_recycler);
        findFriendRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        //toolbar
        mToolbar = findViewById(R.id.find_friend_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find a Friend");

    }

    @Override
    protected void onStart() {
        super.onStart();

        //fecth data when start



    }

    public static class FindFriendViewHolder extends RecyclerView.ViewHolder{

        public FindFriendViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}