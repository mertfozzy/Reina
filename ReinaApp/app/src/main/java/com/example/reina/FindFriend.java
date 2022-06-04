package com.example.reina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.example.reina.model.contacts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class FindFriend extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView findFriendRecyclerList;
    private DatabaseReference userPath;

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

        //firebase
        userPath = FirebaseDatabase.getInstance().getReference().child("Users");

    }

    @Override
    protected void onStart() {
        super.onStart();

        //fetch data when start

        FirebaseRecyclerOptions<contacts> options = new FirebaseRecyclerOptions.Builder<contacts>()
                .setQuery(userPath, contacts.class).build();

        FirebaseRecyclerAdapter<contacts, FindFriendViewHolder> adapter =
                new FirebaseRecyclerAdapter<contacts, FindFriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, final int position, @NonNull contacts model) {
                holder.userAbout.setText(model.getAbout());
                holder.userName.setText(model.getName());

                //when clicked
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String showClickedUserID = getRef(position).getKey();
                        Intent goProfileActivity = new Intent(FindFriend.this, profileActivity.class);
                        goProfileActivity.putExtra("showClickedUserID", showClickedUserID);
                        startActivity(goProfileActivity);
                    }
                });

            }

            @NonNull
            @Override
            public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_show_layout, parent, false);
                FindFriendViewHolder viewHolder = new FindFriendViewHolder(view);
                return viewHolder;
            }
        };

        findFriendRecyclerList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();

    }

    public static class FindFriendViewHolder extends RecyclerView.ViewHolder{

        TextView userName, userAbout;
        //CircleImageView profilePicture;

        public FindFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            userAbout = itemView.findViewById(R.id.user_profile_status);
            userName = itemView.findViewById(R.id.user_profile_name);

            //profilePicture = itemView.findViewById(R.id.users_profile_pictures);

        }
    }
}