package com.example.agrocrop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PostAdapter extends ArrayAdapter {
    ArrayList<PostModel> models = new ArrayList<>();
    Context context;


    public PostAdapter(@NonNull Context context, int resource, ArrayList<PostModel> models) {
        super(context, resource,models);
        this.models = models;
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}
