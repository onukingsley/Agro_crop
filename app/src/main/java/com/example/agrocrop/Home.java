package com.example.agrocrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agrocrop.Fragments.Complains_Fragment;
import com.example.agrocrop.Fragments.Crop_Data_Fragment;
import com.example.agrocrop.Fragments.Global_Newsfeed_Fragment;
import com.example.agrocrop.Fragments.NewsFeed_Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Home extends AppCompatActivity {
    BottomNavigationView buttomnav;
    NavigationView navbar;
    FrameLayout container;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentuser;
    Complains_Fragment complains_fragment;
    Crop_Data_Fragment crop_data_fragment;
    Global_Newsfeed_Fragment global_newsfeed_fragment;
    NewsFeed_Fragment newsFeed_fragment;
    TextView no_of_posts, userstatus,fullname,username;
    ImageView profileimage;
    Button edit;
    String fullnametxt,usernametxt,posttxt,status;


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

        getSupportFragmentManager().beginTransaction().replace(R.id.homecontainer,newsFeed_fragment).commit();

        buttomnav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
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

        View view = navbar.inflateHeaderView(R.layout.navbar_header);
        edit = view.findViewById(R.id.edituserProfile);
        profileimage = view.findViewById(R.id.profile_image);
        no_of_posts = view.findViewById(R.id.no_of_posts);
        userstatus = view.findViewById(R.id.userstatus);
        fullname= view.findViewById(R.id.fullname);
        username = view.findViewById(R.id.username);







    }

    public void get_user_details(){
        db.collection("user").document(currentuser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                /*Snackbar*/
            }
        });
    }
}