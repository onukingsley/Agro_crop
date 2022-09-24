package com.example.agrocrop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ChatActivity extends AppCompatActivity {
ImageView userimage;
ImageButton btn_back;
TextView username;
RecyclerView chatcontainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        userimage = findViewById(R.id.userImage);
        btn_back = findViewById(R.id.btn_back);
        username = findViewById(R.id.username);
        chatcontainer = findViewById(R.id.chatcontainer);

        Bundle bundle = getIntent().getExtras();
        username.setText(bundle.getString("username"));
        Picasso.with(ChatActivity.this).load(bundle.getString("userimage")).into(userimage);

    }
}