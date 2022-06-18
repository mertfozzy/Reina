package com.example.reina;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reina.model.contacts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatFragment extends Fragment {

    private View privateChatsView;
    private RecyclerView chatsList;

    private DatabaseReference chatsPath, userPath;
    private FirebaseAuth mAuth;
    private String activeUserID;


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        privateChatsView = inflater.inflate(R.layout.fragment_chat, container, false);

        mAuth = FirebaseAuth.getInstance();
        activeUserID = mAuth.getCurrentUser().getUid();
        chatsPath = FirebaseDatabase.getInstance().getReference().child("Chats").child(activeUserID);
        userPath = FirebaseDatabase.getInstance().getReference().child("Users");

        chatsList = privateChatsView.findViewById(R.id.chats_list);
        chatsList.setLayoutManager(new LinearLayoutManager(getContext()));

        return privateChatsView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<contacts> options = new FirebaseRecyclerOptions.Builder<contacts>()
                .setQuery(chatsPath, contacts.class).build();

        FirebaseRecyclerAdapter<contacts, ChatsViewHolder> adapter = new FirebaseRecyclerAdapter<contacts, ChatsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatsViewHolder holder, int position, @NonNull contacts model) {

                final String userIDs = getRef(position).getKey();

                userPath.child(userIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()){

                            final String username = snapshot.child("name").getValue().toString();
                            final String userabout = snapshot.child("about").getValue().toString();

                            holder.userName.setText(username);
                            //holder.userAbout.setText("Last Seen : " + "\n" + "Date" + " Time");
                            holder.userName.setTextColor(Color.WHITE);
                            holder.userAbout.setTextColor(Color.WHITE);

                            //user last seen from database
                            if (snapshot.child("user_last_seen").hasChild("last_seen_status")){
                                String status = snapshot.child("user_last_seen").child("last_seen_status").getValue().toString();
                                String date = snapshot.child("user_last_seen").child("date").getValue().toString();
                                String time = snapshot.child("user_last_seen").child("time").getValue().toString();

                                if (status.equals("online")){
                                    holder.userAbout.setText("online");
                                    holder.userAbout.setTextColor(Color.WHITE);
                                }
                                else if (status.equals("offline")){
                                    holder.userAbout.setText("Last Seen : " + date +  " " + time);
                                    holder.userAbout.setTextColor(Color.WHITE);
                                }
                            }

                            else {
                                holder.userAbout.setText("offline");
                            }


                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    //send chat activity
                                    Intent chatActivity = new Intent(getContext(), ChatActivity.class);
                                    chatActivity.putExtra("user_id_visit", userIDs);
                                    chatActivity.putExtra("user_name_visit", username);
                                    startActivity(chatActivity);


                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_show_layout, viewGroup, false);

                return new ChatsViewHolder(view);

            }
        };

        chatsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();

    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profilePicture;
        TextView userName, userAbout;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePicture = itemView.findViewById(R.id.users_profile_pictures);
            userName = itemView.findViewById(R.id.user_profile_name);
            userAbout = itemView.findViewById(R.id.user_profile_status);


        }
    }
}