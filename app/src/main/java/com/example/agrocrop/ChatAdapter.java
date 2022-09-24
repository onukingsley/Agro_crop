package com.example.agrocrop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {
    ArrayList<ChatModel> model;
    Context context;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    final int Item_sent =1;
    final int Item_recieve =2;

    public ChatAdapter(ArrayList<ChatModel> model, Context context) {
        this.model = model;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (mAuth.getUid().equals(model.get(position).userid)){
            return Item_sent;
        }else {
            return Item_recieve;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Item_sent){
            View view =
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.single_orher_chat,
                            parent,false);
            SentViewHolder holder = new SentViewHolder(view);
            return holder;
        }else{
            View view =
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_chat,
                            parent,false);
            RecieveViewHolder holder = new RecieveViewHolder(view);
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getClass() == SentViewHolder.class){
            SentViewHolder viewHolder = (SentViewHolder) holder;
            viewHolder.message.setText(model.get(position).message);
            Picasso.with(context).load(model.get(position).userimage).into(viewHolder.userImage);
            viewHolder.timestamp.setText(model.get(position).timestamp);
        }
        else{
            RecieveViewHolder viewHolder = (RecieveViewHolder) holder;
            viewHolder.message.setText(model.get(position).message);
            Picasso.with(context).load(model.get(position).userimage).into(viewHolder.userImage);
            viewHolder.timestamp.setText(model.get(position).timestamp);
        }
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder{
        ImageView userImage;
        TextView timestamp,message;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            timestamp = itemView.findViewById(R.id.timestamp);
            message = itemView.findViewById(R.id.message);
        }
    }
    public class RecieveViewHolder extends RecyclerView.ViewHolder{
        ImageView userImage;
        TextView timestamp,message;
        public RecieveViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            timestamp = itemView.findViewById(R.id.timestamp);
            message = itemView.findViewById(R.id.message);
        }
    }
}
