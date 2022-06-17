package com.example.reina;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reina.model.contacts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class RequestsFragment extends Fragment {

    private View requestsFragmentView;
    private RecyclerView requestsList;

    private DatabaseReference chatRequestPath, usersPath, chatsPath;
    private FirebaseAuth mAuth;
    private String activeUserID;

    public RequestsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requestsFragmentView = inflater.inflate(R.layout.fragment_requests, container, false);

        mAuth = FirebaseAuth.getInstance();
        activeUserID = mAuth.getCurrentUser().getUid();
        chatRequestPath = FirebaseDatabase.getInstance().getReference().child("Chat Request");
        usersPath = FirebaseDatabase.getInstance().getReference().child("Users");
        chatsPath = FirebaseDatabase.getInstance().getReference().child("Chats");

        requestsList = requestsFragmentView.findViewById(R.id.chat_requests_list);
        requestsList.setLayoutManager(new LinearLayoutManager(getContext()));

        return  requestsFragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<contacts> options = new FirebaseRecyclerOptions.Builder<contacts>().setQuery(chatRequestPath.child(activeUserID), contacts.class).build();

        FirebaseRecyclerAdapter<contacts,RequestViewHolder> adapter = new FirebaseRecyclerAdapter<contacts, RequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RequestViewHolder holder, int position, @NonNull contacts model) {

                //buttons visible
                holder.itemView.findViewById(R.id.accept_request_button).setVisibility(View.VISIBLE);
                holder.itemView.findViewById(R.id.decline_request_button).setVisibility(View.VISIBLE);

                //all request show
                final String user_id_list = getRef(position).getKey();

                DatabaseReference fetchRequestTypePath = getRef(position).child("request_type").getRef();

                fetchRequestTypePath.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()){
                            String type = snapshot.getValue().toString();

                            if (type.equals("received")){
                                usersPath.child(user_id_list).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                                        final String requestUserName = snapshot.child("name").getValue().toString();
                                        final String requestUserAbout = snapshot.child("about").getValue().toString();
                                        holder.userName.setText(requestUserName);
                                        holder.userAbout.setText("The user wants to chat with you.");
                                        holder.userName.setTextColor(Color.WHITE);
                                        holder.userAbout.setTextColor(Color.WHITE);


                                        //every line clicked
                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                CharSequence options[] = new CharSequence[]{
                                                        "Accept",
                                                        "Decline"
                                                };

                                                //alertdialog
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setTitle( "Do you confirm " + requestUserName + "'s request?");

                                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        if (i == 0){

                                                            chatsPath.child(activeUserID).child(user_id_list).child("Chats")
                                                            .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    if (task.isSuccessful()){
                                                                        chatsPath.child(user_id_list).child(activeUserID)
                                                                                .child("Chats").setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                if (task.isSuccessful()){
                                                                                    
                                                                                    chatRequestPath.child(activeUserID).child(user_id_list)
                                                                                            .removeValue()
                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    
                                                                                                    if (task.isSuccessful()){
                                                                                                        
                                                                                                        chatRequestPath.child(user_id_list).child(activeUserID)
                                                                                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                                                Toast.makeText(getContext(), "Chat is saved.", Toast.LENGTH_SHORT).show();
                                                                                                                
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
                                                        if (i == 1){

                                                            chatRequestPath.child(activeUserID).child(user_id_list)
                                                                    .removeValue()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            if (task.isSuccessful()){

                                                                                chatRequestPath.child(user_id_list).child(activeUserID)
                                                                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                                        Toast.makeText(getContext(), "Chat is deleted.", Toast.LENGTH_SHORT).show();

                                                                                    }
                                                                                });

                                                                            }

                                                                        }
                                                                    });

                                                        }

                                                    }
                                                });

                                                builder.show();

                                            }


                                        });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            else if (type.equals("sent")){
                                Button requestSendButton = holder.itemView.findViewById(R.id.accept_request_button);
                                requestSendButton.setText("Pending..");
                                holder.itemView.findViewById(R.id.decline_request_button).setVisibility(View.INVISIBLE);

                                usersPath.child(user_id_list).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                                        final String requestUserName = snapshot.child("name").getValue().toString();
                                        final String requestUserAbout = snapshot.child("about").getValue().toString();
                                        holder.userName.setText(requestUserName);
                                        holder.userAbout.setText("Sent request to " + requestUserName);
                                        holder.userName.setTextColor(Color.WHITE);
                                        holder.userAbout.setTextColor(Color.WHITE);


                                        //every line clicked
                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                CharSequence options[] = new CharSequence[]{
                                                        "Cancel Chat Request"
                                                };

                                                //alertdialog
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setTitle( "There is existing chat request.");

                                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        if (i == 0){

                                                            chatRequestPath.child(activeUserID).child(user_id_list)
                                                                    .removeValue()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            if (task.isSuccessful()){

                                                                                chatRequestPath.child(user_id_list).child(activeUserID)
                                                                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                                        Toast.makeText(getContext(), "Chat reuest is canceled.", Toast.LENGTH_SHORT).show();

                                                                                    }
                                                                                });

                                                                            }

                                                                        }
                                                                    });

                                                        }

                                                    }
                                                });

                                                builder.show();

                                            }


                                        });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
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
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_show_layout, viewGroup, false);
                RequestViewHolder holder = new RequestViewHolder(view);
                return holder;

            }
        };

        requestsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.startListening();

    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userAbout;
        CircleImageView profilePicture;
        Button acceptButton, declineButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_profile_name);
            userAbout = itemView.findViewById(R.id.user_profile_status);
            profilePicture = itemView.findViewById(R.id.users_profile_pictures);
            acceptButton = itemView.findViewById(R.id.accept_request_button);
            declineButton = itemView.findViewById(R.id.decline_request_button);

        }
    }

}