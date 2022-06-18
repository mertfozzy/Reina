package com.example.reina.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reina.R;
import com.example.reina.model.messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessagesViewHolder> {

    private List<messages> userMessagesList;

    private FirebaseAuth mAuth;
    private DatabaseReference usersPath;

    public MessageAdapter (List<messages> userMessagesList){
        this.userMessagesList = userMessagesList;

    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder{

        public TextView senderMessageText, receiverMessageText;
        public CircleImageView receiverProfilePhoto;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessageText = itemView.findViewById(R.id.sender_message_field);
            receiverMessageText = itemView.findViewById(R.id.receiver_message_field);
            receiverProfilePhoto = itemView.findViewById(R.id.receiver_message_profile_photo);

        }
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.private_messages_layout, viewGroup, false);

        mAuth = FirebaseAuth.getInstance();


        return new MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {

        String messageSenderID = mAuth.getCurrentUser().getUid();
        messages messages = userMessagesList.get(position);

        String fromWhoUserID = messages.getWho();
        String fMessageType = messages.getType();

        usersPath = FirebaseDatabase.getInstance().getReference().child("Users").child(fromWhoUserID);
        usersPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /*
                if (snapshot.hasChild("uid")){

                    String nameFetcher = snapshot.child("name").getValue().toString();

                }
                */

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (fMessageType.equals("text")){
            holder.receiverMessageText.setVisibility(View.INVISIBLE);
            holder.receiverProfilePhoto.setVisibility(View.INVISIBLE);
            holder.senderMessageText.setVisibility(View.INVISIBLE);

            if (fromWhoUserID.equals(messageSenderID)){

                holder.senderMessageText.setVisibility(View.VISIBLE);
                holder.senderMessageText.setBackgroundResource(R.drawable.sender_message_layout);
                holder.senderMessageText.setTextColor(Color.WHITE);
                holder.senderMessageText.setText(messages.getMessage() + "\n" + messages.getTime());

            }

            else {


                holder.receiverMessageText.setVisibility(View.VISIBLE);
                holder.receiverProfilePhoto.setVisibility(View.VISIBLE);

                holder.receiverMessageText.setBackgroundResource(R.drawable.receiver_message_layout);
                holder.receiverMessageText.setTextColor(Color.WHITE);
                holder.receiverMessageText.setText(messages.getMessage() + "\n" + messages.getTime());
            }
        }

    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }

}
