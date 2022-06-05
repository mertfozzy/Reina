package com.example.reina;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class RequestsFragment extends Fragment {

    private View requestsFragmentView;
    private RecyclerView requestsList;

    public RequestsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requestsFragmentView = inflater.inflate(R.layout.fragment_requests, container, false);

        requestsList = requestsFragmentView.findViewById(R.id.chat_requests_list);
        requestsList.setLayoutManager(new LinearLayoutManager(getContext()));

        return  requestsFragmentView;
    }
}