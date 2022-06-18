package com.example.reina;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


public class ContactsFragment extends Fragment {

    private View contactsView;
    private RecyclerView contactsList;

    private DatabaseReference chatsPath, usersPath;
    private FirebaseAuth mAuth;

    private String activeUserID;

    public ContactsFragment() {
        // Required empty public constructor

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contactsView = inflater.inflate(R.layout.fragment_contacts, container, false);

        contactsList = contactsView.findViewById(R.id.contacts_list);
        contactsList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        activeUserID = mAuth.getCurrentUser().getUid();
        chatsPath = FirebaseDatabase.getInstance().getReference().child("Chats").child(activeUserID);
        usersPath = FirebaseDatabase.getInstance().getReference().child("Users");



        return contactsView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<contacts>().setQuery(chatsPath, contacts.class).build();

        //adapter
        FirebaseRecyclerAdapter<contacts, ContactsViewHolder> adapter = new FirebaseRecyclerAdapter<contacts, ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ContactsViewHolder holder, int position, @NonNull contacts model) {

                String clickedUserID = getRef(position).getKey();

                usersPath.child(clickedUserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()){
                            //user last seen from database
                            if (snapshot.child("user_last_seen").hasChild("last_seen_status")){
                                String status = snapshot.child("user_last_seen").child("last_seen_status").getValue().toString();
                                String date = snapshot.child("user_last_seen").child("date").getValue().toString();
                                String time = snapshot.child("user_last_seen").child("time").getValue().toString();

                                if (status.equals("online")){
                                    holder.onlineIcon.setVisibility(View.VISIBLE);
                                }
                                else if (status.equals("offline")){
                                    holder.onlineIcon.setVisibility(View.INVISIBLE);
                                }
                            }

                            else {
                                holder.onlineIcon.setVisibility(View.INVISIBLE);
                            }

                            if (snapshot.hasChild("uid")){
                                String username = snapshot.child("name").getValue().toString();
                                String userabout = snapshot.child("about").getValue().toString();

                                holder.userName.setText(username);
                                holder.userAbout.setText(userabout);

                                holder.userName.setTextColor(Color.WHITE);
                                holder.userAbout.setTextColor(Color.WHITE);
                            }
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_show_layout, viewGroup, false);

                ContactsViewHolder viewHolder = new ContactsViewHolder(view);

                return viewHolder;

            }
        };

        contactsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();

    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder{

        TextView userName, userAbout;
        CircleImageView profilePicture;
        ImageView onlineIcon;



        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_profile_name);
            userAbout = itemView.findViewById(R.id.user_profile_status);
            profilePicture = itemView.findViewById(R.id.users_profile_pictures);
            onlineIcon = itemView.findViewById(R.id.user_online_status);

        }
    }
}