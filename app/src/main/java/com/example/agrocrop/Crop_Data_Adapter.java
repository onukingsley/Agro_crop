package com.example.agrocrop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;


public class Crop_Data_Adapter extends ArrayAdapter {
    Context context;
    ArrayList<CropModel>model;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int num;


    public Crop_Data_Adapter(@NonNull Context context, ArrayList<CropModel> model,int num) {
        super(context, 0, model);
        this.context = context;
        this.model = model;
        this.num = num;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (num == 0){
            view = LayoutInflater.from(context).inflate(R.layout.single_data_card,parent,false);
            TextView crop_title = view.findViewById(R.id.crop_title);
            ImageView background = view.findViewById(R.id.container);

            crop_title.setText(model.get(position).getCropTitle());
            Picasso.with(context).load(model.get(position).getImage()).into(background);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(),Crop_Detail_Activity.class);
                    Bundle cropinfo = new Bundle();
                    cropinfo.putString("crop_title", model.get(position).getCropTitle());
                    cropinfo.putString("crop_image", model.get(position).getImage());
                    i.putExtras(cropinfo);
                    ((AppCompatActivity)getContext()).startActivity(i);
                }
            });
            return view;
        }




        return view;
    }
}
