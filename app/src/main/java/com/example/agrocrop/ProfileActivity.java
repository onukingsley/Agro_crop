package com.example.agrocrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agrocrop.admin.EditAdminProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
FloatingActionButton newpost,chat;
FirebaseFirestore db;
FirebaseAuth mAuth;
TextView fullname, username, no_of_posts, role;
ImageView profile_image, user_coverpage;
CardView edit_profile;
ProgressDialog progressDialog;
String txtavatar,datereg,email,txtfullname,txtnoofpost,txtrole,txtusername;
String userid,userimagebundle,usernamebundle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        newpost = findViewById(R.id.new_post);
        fullname = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        no_of_posts = findViewById(R.id.no_of_posts);
        role = findViewById(R.id.role);
        profile_image = findViewById(R.id.profile_image);
        user_coverpage = findViewById(R.id.user_coverpage);
        edit_profile = findViewById(R.id.edit_profile);
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setTitle("loading profile");
        progressDialog.show();
        chat = findViewById(R.id.chat);
        Bundle bundle = getIntent().getExtras();
        userid = bundle.getString("userid");
        usernamebundle = bundle.getString("username");
        userimagebundle = bundle.getString("userImage");





        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        getuserdetails();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fullname.setText(txtfullname);
                username.setText(txtfullname);
                no_of_posts.setText(txtfullname);
                if (txtrole == "2"){
                    role.setText("Farmer");
                }else if (txtrole == "1"){
                    role.setText("Admin");
                }else if (txtrole == "0"){
                    role.setText("Super Admin");
                }
                Picasso.with(ProfileActivity.this).load(txtavatar).into(profile_image);
                Picasso.with(ProfileActivity.this).load(txtavatar).into(user_coverpage);

                progressDialog.dismiss();
            }
        },3000);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("userid",userid);
                bundle.putString("username",usernamebundle);
                bundle.putString("userimage",userimagebundle);
                Intent i = new Intent(ProfileActivity.this,ChatActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        newpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this,NewpostActivity.class);
                startActivity(i);
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, EditAdminProfile.class);
                startActivity(i);
            }
        });



    }

    public void getuserdetails(){
        //Bundle bundle = getIntent().getExtras();


        db.collection("user").document(userid).get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            txtavatar = document.getString("avatar");
                            txtfullname = document.getString("fullname");
                            email = document.getString("email");
                            txtnoofpost = document.getString("no_of_posts");
                            txtusername = document.getString("username");
                            txtrole = document.getString("role");

                        }
                    }
                }
        );

    }
}