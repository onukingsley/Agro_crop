package com.example.agrocrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agrocrop.Fragments.Complains_Fragment;
import com.example.agrocrop.Fragments.Crop_Data_Fragment;
import com.example.agrocrop.Fragments.Global_Newsfeed_Fragment;
import com.example.agrocrop.Fragments.NewsFeed_Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class Home extends AppCompatActivity {
    /*BottomNavigationView buttomnav;*/
    SmoothBottomBar buttomnav;
    NavigationView navbar;
    FrameLayout container;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentuser;
    Complains_Fragment complains_fragment;
    Crop_Data_Fragment crop_data_fragment;
    Global_Newsfeed_Fragment global_newsfeed_fragment;
    NewsFeed_Fragment newsFeed_fragment;
    TextView no_of_posts, userstatus,fullname,username,email;
    ImageView profileimage;
    Button edit;
    String fullnametxt,usernametxt,posttxt,status,emailtxt,avatar;
    DrawerLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);




        buttomnav = findViewById(R.id.bottom_navbar);
        navbar = findViewById(R.id.home_navbar);
        container = findViewById(R.id.homecontainer);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser();
        complains_fragment = new Complains_Fragment();
        crop_data_fragment = new Crop_Data_Fragment();
        global_newsfeed_fragment = new Global_Newsfeed_Fragment();
        newsFeed_fragment = new NewsFeed_Fragment();
        layout = findViewById(R.id.container);
        get_user_details();

        getSupportFragmentManager().beginTransaction().replace(R.id.homecontainer,newsFeed_fragment).commit();

        buttomnav.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {

                switch (i){
                    case R.id.newsfeed:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homecontainer,newsFeed_fragment).commit();
                        break;
                    case R.id.cropData:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homecontainer,crop_data_fragment).commit();
                        break;
                    case R.id.complains:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homecontainer,complains_fragment).commit();
                        break;
                    case R.id.globalnews:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homecontainer,global_newsfeed_fragment).commit();
                        break;
                }


                return false;
            }
        });
        navbar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile:
                        Intent i = new Intent(Home.this,Normal_User_Profile.class);
                        startActivity(i);
                        break;

                }


                return false;
            }

        });

/*this is for the normal/default buttom navigation*/
  /*      buttomnav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.newsfeed:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homecontainer,newsFeed_fragment).commit();
                        break;
                    case R.id.cropData:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homecontainer,crop_data_fragment).commit();
                        break;
                    case R.id.complains:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homecontainer,complains_fragment).commit();
                        break;
                    case R.id.globalnews:
                        getSupportFragmentManager().beginTransaction().replace(R.id.homecontainer,global_newsfeed_fragment).commit();
                        break;
                }
                return false;
            }
        });
        BadgeDrawable drawable = buttomnav.getOrCreateBadge(R.id.complains);
        drawable.setNumber(22);*/


        View view = navbar.inflateHeaderView(R.layout.navbar_header);
        edit = view.findViewById(R.id.edituserProfile);
        profileimage = view.findViewById(R.id.profile_image);
        no_of_posts = view.findViewById(R.id.no_of_posts);
        userstatus = view.findViewById(R.id.userstatus);
        fullname= view.findViewById(R.id.fullname);
        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Picasso.with(Home.this).load(avatar).into(profileimage);
                no_of_posts.setText(posttxt);
                if(status.equals("0")){
                    userstatus.setText("Super Admin");
                }else if (status.equals("1")){
                    userstatus.setText("Admin");
                }else if (status.equals("2")){
                    userstatus.setText("Farmer");
                }
                fullname.setText(fullnametxt);
                username.setText(usernametxt);
                email.setText(emailtxt);

            }
        },3000);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(layout,"Go to edit user profile",Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });








    }

    public void get_user_details(){
        db.collection("user").document(currentuser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    fullnametxt = document.getString("fullname");
                    usernametxt = document.getString("username");
                    avatar = document.getString("avatar");
                    emailtxt = document.getString("email");
                    status = document.getString("role");
                    posttxt = document.getString("no_of_posts");



                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar snackbar = Snackbar.make(layout,"Error fetching user details, please refresh the app",Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
    }
}