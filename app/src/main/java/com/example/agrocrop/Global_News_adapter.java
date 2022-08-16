package com.example.agrocrop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Global_News_adapter extends ArrayAdapter {
    Context context;
    ArrayList<NewsModel> model = new ArrayList<>();


    public Global_News_adapter(@NonNull Context context, ArrayList<NewsModel> model) {
        super(context, 0, model);
        this.model = model;
        this.context = context;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        view = LayoutInflater.from(context).inflate(R.layout.single_global_news,parent,false);
        TextView post_title, post_content, post_author,post_description,publishedate;
        ImageView post_image;
        post_author = view.findViewById(R.id.post_author);
        post_content = view.findViewById(R.id.post_content);
        post_description = view.findViewById(R.id.post_description);
        post_title = view.findViewById(R.id.post_title);
        publishedate = view.findViewById(R.id.publishedate);
        post_image = view.findViewById(R.id.post_image);

        post_title.setText(model.get(position).getPost_title());
        post_content.setText(model.get(position).getPost_content());
        post_description.setText(model.get(position).getPost_description());
        post_author.setText(model.get(position).getPost_author());
        publishedate.setText(model.get(position).getPublisheddate());
        Picasso.with(context).load(model.get(position).getPost_image()).into(post_image);


        post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),Global_webview.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", model.get(position).getPost_url());
                i.putExtras(bundle);

                ((AppCompatActivity)getContext()).startActivity(i);
            }
        });


        return view;
    }
}
