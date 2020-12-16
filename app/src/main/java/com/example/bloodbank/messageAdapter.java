package com.example.bloodbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.MyViewHolder> {

    public static final int MSG_LEFT=0;
    public static final int MSG_RIGHT=1;

    List<Chat> arrayList;
    Context ct;
    String imageuri;
    FirebaseUser firebaseUser;

    public messageAdapter(Context ctx, List<Chat> array) {
        this.ct=ctx;
        arrayList=array;


    }


    @Override
    public messageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_RIGHT){
            return new messageAdapter.MyViewHolder(LayoutInflater.from(ct).inflate(R.layout.chat_item_right,parent,false));

        }else{
            return new messageAdapter.MyViewHolder(LayoutInflater.from(ct).inflate(R.layout.chat_item_left,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Chat chat=arrayList.get(position);

        holder.textView.setText(arrayList.get(position).getMessage());

        if(position==arrayList.size()-1){
            if(chat.isIsseen()){
               holder.textView2.setText("Seen");
            }else{
                holder.textView2.setText("Delivered");
            }
        }else{
            holder.textView2.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView,textView2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView=(TextView)itemView.findViewById(R.id.showmessage);
            textView2=(TextView)itemView.findViewById(R.id.isseen);


        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(arrayList.get(position).getSender().equals(firebaseUser.getEmail())){
            return MSG_RIGHT;
        }else{
            return MSG_LEFT;
        }
    }
}
