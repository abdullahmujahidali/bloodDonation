package com.example.bloodbank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Personal_chat extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Chat> list;
    messageAdapter mad;
    Chat chat;

    EditText editText;


    TextView textView;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String reciever;
    String current_mail;
    String email;
    String Sender;
    String msg;
    String r_image;
    ValueEventListener seenListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);

        //Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar2);
        //toolbar.inflateMenu(R.menu.chat_options);

        //getSupportActionBar().setTitle(" ");
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        chat=new Chat();

        editText=(EditText)findViewById(R.id.message);




        textView=(TextView)findViewById(R.id.username);



        Intent intent=getIntent();
        email=intent.getStringExtra("Reciever");
        textView.setText(email);


        textView.setVisibility(View.VISIBLE);
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        current_mail=firebaseUser.getEmail();
        check();

    }


    public void check(){
        databaseReference=firebaseDatabase.getReference().child("Data").child("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    User user=ds.getValue(User.class);
                    String u=user.getFirstname();
                    if(email.compareTo(u)== 0){
                        reciever=user.getEmail();

                        readmsg(current_mail,reciever);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        seenmsg(reciever);
    }public void  seenmsg(final String userid){
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Data").child("Chat");
        seenListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Chat chat=ds.getValue(Chat.class);

                    if(chat.getReceiver().equals(current_mail) && chat.getSender().equals(userid)){
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        ds.getRef().updateChildren(hashMap);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void send_message(View view) {
        Sender=firebaseUser.getEmail();
        msg=editText.getText().toString();
        databaseReference=firebaseDatabase.getReference().child("Data").child("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    User user=ds.getValue(User.class);
                    String u=user.getFirstname();
                    if(email.compareTo(u)== 0){
                        reciever=user.getEmail();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference Reference=firebaseDatabase.getReference().child("Data").child("Chat");
        if(msg.length()>=1){
            HashMap<String,Object> hashmap=new HashMap<>();
            hashmap.put("receiver",reciever);
            hashmap.put("to",reciever);
            hashmap.put("Message",msg);
            hashmap.put("sender",current_mail);
            hashmap.put("isseen",false);
            Reference.push().setValue(hashmap);

        }else{
            editText.setError("Type Msg");
            editText.requestFocus();

        }editText.setText("");

    }
    private void readmsg(final String myid, final String userid){
        list=new ArrayList<>();
        DatabaseReference Reference=firebaseDatabase.getReference().child("Data").child("Chat");
        Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Chat chat=ds.getValue(Chat.class);
                    if(chat.getSender().equals(myid) && chat.getReceiver().equals(userid) || chat.getSender().equals(userid) && chat.getReceiver().equals(myid)){
                        list.add(chat);
                    }
                    mad=new messageAdapter(Personal_chat.this,list);
                    recyclerView.setAdapter(mad);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(seenListener);
    }
}
