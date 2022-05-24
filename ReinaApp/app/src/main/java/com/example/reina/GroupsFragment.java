package com.example.reina;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GroupsFragment extends Fragment {

    private View groupFragmentView;
    private ListView listView;
    private ArrayAdapter<String>arrayAdapter;
    private ArrayList<String>group_lists = new ArrayList<>();
    private DatabaseReference groupPath;


    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        groupFragmentView = inflater.inflate(R.layout.fragment_groups, container, false);

        //Firebase
        groupPath = FirebaseDatabase.getInstance().getReference().child("Groups");

        //Definitions
        listView = groupFragmentView.findViewById(R.id.list_view);
        //arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, group_lists);
        arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.row, group_lists);
        listView.setAdapter(arrayAdapter);

        //Fetch Groups
        fetchGroupsAndShow();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String existGroupName = parent.getItemAtPosition(position).toString();
                Intent groupChatActivity = new Intent(getContext(), GroupChatActivity.class);
                groupChatActivity.putExtra("GroupName", existGroupName);
                startActivity(groupChatActivity);

            }
        });

        return groupFragmentView;
    }

    private void fetchGroupsAndShow() {
        groupPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String>set = new HashSet<>();
                Iterator iterator = snapshot.getChildren().iterator();

                while (iterator.hasNext())
                {
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                group_lists.clear();
                group_lists.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}