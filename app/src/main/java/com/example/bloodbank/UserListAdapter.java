package com.example.bloodbank;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder>{

    List<User> arrayList;
    ArrayList<User> arrayList2;

    Context ct;
    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
    DatabaseReference databaseReference;

    public UserListAdapter(Context ctx,List<User> array) {
        this.ct=ctx;
        arrayList=array;
        arrayList2 = new ArrayList<>(arrayList);

    }



    @NonNull
    @Override
    public UserListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new MyViewHolder(LayoutInflater.from(ct).inflate(R.layout.cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final UserListAdapter.MyViewHolder holder, int position) {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Data").child("Chat");
        holder.textView.setText(arrayList.get(position).getFirstname());
        holder.textView2.setText(arrayList.get(position).getBlood());
        switch(holder.textView2.getText().toString()){
            case "A+":
                holder.imageView.setImageResource(R.drawable.ap2);
                break;
            case "A-":
                holder.imageView.setImageResource(R.drawable.an2);
                break;
            case "B+":
                holder.imageView.setImageResource(R.drawable.bp2);
                break;
            case "B-":
                holder.imageView.setImageResource(R.drawable.bn2);
                break;
            case "O+":
                holder.imageView.setImageResource(R.drawable.op2);
                break;
            case "0-":
                holder.imageView.setImageResource(R.drawable.on2);
                break;
            case "AB+":
                holder.imageView.setImageResource(R.drawable.abp2);
                break;
            case "AB-":
                holder.imageView.setImageResource(R.drawable.abn2);
                break;
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Chat u=ds.getValue(Chat.class);
                    if(!(u.isIsseen())){
                        if(u.getSender().compareTo(holder.textView.getText().toString())==0 && u.getSender().compareTo(firebaseUser.getEmail())!=0 && u.getReceiver().compareTo(firebaseUser.getEmail())==0) {

                           /* NotificationCompat.Builder builder=new NotificationCompat.Builder(ct);
                            builder
                                    .setContentTitle(u.getSender())
                                    .setContentText("You Got Message")
                                    .setSmallIcon(R.mipmap.ic_launcher_round)
                                    .setDefaults(NotificationCompat.DEFAULT_ALL);

                            //builder.setContentIntent(o
                            NotificationManager notificationManager= (NotificationManager)ct.getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(1,builder.build());*/



                            holder.textView3.setVisibility(View.VISIBLE);
                        }
                    }else{
                        holder.textView3.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ob=new Intent(ct,Personal_chat.class);
                String n=holder.textView.getText().toString();


                    ob.putExtra("Reciever",n);
                    ct.startActivity(ob);


            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView,textView2,textView3;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.pf_imgage);
            textView=(TextView)itemView.findViewById(R.id.profile_name);
            textView2=(TextView)itemView.findViewById(R.id.profile_number);
            textView3=(TextView)itemView.findViewById(R.id.notification);
        }
    }
}
