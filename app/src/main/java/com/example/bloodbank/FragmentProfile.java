package com.example.bloodbank;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentProfile extends Fragment {
    TextView textView1,textView2,textView3,textView4;
    DatabaseReference databaseReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_viewprofile,null);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView1=getView().findViewById(R.id.profile_text1_name);
        textView2=getView().findViewById(R.id.profile_text2_name);
        textView3=getView().findViewById(R.id.profile_text3_name);
        textView4=getView().findViewById(R.id.profile_text4_name);


        textView3.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Data").child("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    User u = ds.getValue(User.class);
                    String email=u.getEmail();
                    if(email.equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        textView1.setText(u.getFirstname());
                        textView2.setText(u.getLastname());
                        textView4.setText(u.getBlood());
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
