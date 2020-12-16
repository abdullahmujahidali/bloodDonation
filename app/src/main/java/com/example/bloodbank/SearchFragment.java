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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    RecyclerView recyclerView;

    List<User> arrayList;
    EditText editText;
    Button button;
    UserListAdapter myadapater;
    String data;
    int check=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);


    }public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView=(RecyclerView)getView().findViewById(R.id.recycle);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        arrayList=new ArrayList<>();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Data").child("Users");

        editText=getView().findViewById(R.id.seach_input);

        button=getView().findViewById(R.id.seach_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data=editText.getText().toString();
                if(!data.equals("")){
                    if(data.equalsIgnoreCase("AB+") || data.equalsIgnoreCase("AB-") || data.equalsIgnoreCase("B+")
                    || data.equalsIgnoreCase("B-") || data.equalsIgnoreCase("A+") ||data.equalsIgnoreCase("A-") ||
                            data.equalsIgnoreCase("o+") || data.equalsIgnoreCase("o-")){

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                arrayList.clear();
                                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                                    User u = ds.getValue(User.class);
                                    if((FirebaseAuth.getInstance().getCurrentUser().getEmail()).compareTo(u.getEmail())!=0 && u.getBlood().equalsIgnoreCase(data)){
                                        check = 1;
                                        arrayList.add(u);

                                    }

                                }
                                myadapater=new UserListAdapter(getContext(),arrayList);
                                recyclerView.setAdapter(myadapater);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }else{
                        editText.setError("Enter Valid Blood group");
                    }
                }else{
                    editText.setError("Enter blood group");
                }

            }
        });

    }


}
