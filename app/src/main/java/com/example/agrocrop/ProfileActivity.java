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
FloatingActionButton newpost;
FirebaseFirestore db;
FirebaseAuth mAuth;
TextView fullname, username, no_of_posts, role;
ImageView profile_image, user_coverpage;
CardView edit_profile;
ProgressDialog progressDialog;
String txtavatar,datereg,email,txtfullname,txtnoofpost,txtrole,txtusername;



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
        db.collection("user").document(mAuth.getUid().toString()).get().addOnCompleteListener(
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