package com.example.socialmedia;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialmedia.adapter.NotificationAdapter;
import com.example.socialmedia.databinding.FragmentNotiBinding;
import com.example.socialmedia.model.NotificationModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class NotiFragment extends Fragment {
FragmentNotiBinding binding;
ArrayList<NotificationModel> list=new ArrayList<>();
    public NotiFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentNotiBinding.inflate(inflater, container, false);
        NotificationAdapter adapter=new NotificationAdapter(list,getContext());
        binding.notiRV.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.notiRV.setNestedScrollingEnabled(false);
        binding.notiRV.setAdapter(adapter);


        FirebaseDatabase.getInstance().getReference().child("notification").child(FirebaseAuth.getInstance().getUid())
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        NotificationModel notificationModel=dataSnapshot.getValue(NotificationModel.class);
                        notificationModel.setNotificationId(dataSnapshot.getKey());
                            list.add(notificationModel)  ;
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        return binding.getRoot();
    }
}