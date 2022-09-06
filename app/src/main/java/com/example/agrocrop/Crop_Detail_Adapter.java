package com.example.agrocrop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Crop_Detail_Adapter extends RecyclerView.Adapter<Crop_Detail_Adapter.ViewHolder> {
    Context context;
    ArrayList<Crop_Detail_Model>models;
    onCardClick onCardClick;

    public Crop_Detail_Adapter(Context context, ArrayList<Crop_Detail_Model> models,
                               onCardClick onCardClick) {
        this.context = context;
        this.models = models;
        this.onCardClick = onCardClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_data_card,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.crop_title.setText(models.get(position).getProp_id());
        Picasso.with(context).load(models.get(position).getImage()).into(holder.container);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCardClick.onclickcard(holder.crop_title.getText().toString());
            }
        });
    }




    @Override
    public int getItemCount() {
        return models.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView crop_title;
        ImageView container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            crop_title = itemView.findViewById(R.id.crop_title);
            container = itemView.findViewById(R.id.container);
        }
    }

}

