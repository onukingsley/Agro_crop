package com.example.agrocrop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChartListAdapter extends RecyclerView.Adapter<ChartListAdapter.ViewHolder> {
   Context context;
    ArrayList<ChartListModel> model;

    public ChartListAdapter(Context context, ArrayList<ChartListModel> model) {
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public ChartListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_chat_card,
                parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChartListAdapter.ViewHolder holder, int position) {
        holder.username.setText(model.get(position).getUsername());
        holder.timestamp.setText(model.get(position).getTimestamp());
        Picasso.with(context).load(model.get(position).getUserimage()).into(holder.user_profileimg);
        holder.lastmessage.setText(model.get(position).getLastMessages());
        if (model.get(position).isRead() == "true"){
            holder.notification.setVisibility(View.GONE);
        }else if (model.get(position).isRead() == "false"){
            holder.notification.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userid",model.get(position).getUserid());
                bundle.putString("username",model.get(position).getUsername());
                bundle.putString("userimage",model.get(position).getUserimage());

                i.putExtras(bundle);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return model.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView user_profileimg,notification;
        TextView username, lastmessage, timestamp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            lastmessage = itemView.findViewById(R.id.lastmessage);
            timestamp = itemView.findViewById(R.id.timestamp);
            user_profileimg  = itemView.findViewById(R.id.user_profileimg);
            notification = itemView.findViewById(R.id.notification);
        }
    }
}
