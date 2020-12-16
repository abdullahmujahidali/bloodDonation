package com.example.bloodbank;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;


public class RecentChatList extends Fragment {
    DatabaseReference databaseReference,databaseReference1;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    StorageReference storage;
    RecyclerView recyclerView;
    String reciever;


    List<String> userlist;
    List<User> arrayList;
    UserListAdapter myadapater;
    int check=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent_chat_list, container, false);
    }public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView=(RecyclerView)getView().findViewById(R.id.recycle);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userlist = new ArrayList<>();


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Data").child("Chat");
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Data").child("Users");
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Chat u = ds.getValue(Chat.class);
                    if(u.getSender().equals(firebaseUser.getEmail())){
                        for(int i=0;i<userlist.size();i++){
                            if(userlist.get(i).equals(u.getReceiver())){
                                check=1;
                            }
                        }if(check==0){
                            userlist.add(u.getReceiver());

                        }check=0;

                    }if(u.getReceiver().equals(firebaseUser.getEmail())){
                        for(int i=0;i<userlist.size();i++){
                            if(userlist.get(i).equals(u.getReceiver())){
                                check=1;
                            }
                        }if(check==0){
                            userlist.add(u.getSender());

                        }check=0;

                    }

                }getdetails();
            }




            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //
    }private void getdetails() {
        arrayList = new ArrayList<>();
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User us = ds.getValue(User.class);
                    for (String id : userlist) {
                        if (us.getEmail().equals(id)) {
                            arrayList.add(us);
                           /* if (arrayList.size() != 0) {
                                for (int i = 0; i<arrayList.size(); i++) {
                                    User use = arrayList.get(i);
                                    if (!(us.getEmail().equals(arrayList.get(i).getEmail()))) {

                                    }
                                }
                            }else {
                                arrayList.add(us);
                            }*/
                        }
                    }
                }
                myadapater = new UserListAdapter(getContext(), arrayList);
                recyclerView.setAdapter(myadapater);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
