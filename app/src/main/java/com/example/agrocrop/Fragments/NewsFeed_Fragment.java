package com.example.agrocrop.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toolbar;

import com.example.agrocrop.PostAdapter;
import com.example.agrocrop.PostModel;
import com.example.agrocrop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class NewsFeed_Fragment extends Fragment {
FrameLayout container;
androidx.appcompat.widget.Toolbar home_toolbar;
ListView listview_container;
FirebaseFirestore db = FirebaseFirestore.getInstance();
FirebaseAuth mAuth = FirebaseAuth.getInstance();
FirebaseUser currentuser = mAuth.getCurrentUser();
ArrayList<PostModel> model = new ArrayList<>();
PostAdapter adapter;
String username,fullname,role,avatar;





    public NewsFeed_Fragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getuserdetails();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getpost();
        getuserdetails();
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        home_toolbar = view.findViewById(R.id.home_toolbar);
        listview_container = view.findViewById(R.id.listview_container);
        adapter = new PostAdapter(view.getContext(),model,2,username,fullname,avatar,role);
        listview_container.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        /*((AppCompatActivity)getActivity()).setSupportActionBar(home_toolbar);*/




        return view;
    }

    public void getpost(){
        db.collection("post").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot :  task.getResult()){
                                PostModel postmodel = new PostModel(documentSnapshot.getId(),
                                        documentSnapshot.getString("post_title"),
                                        documentSnapshot.getString("post_content"),
                                        documentSnapshot.getString("post_image"),
                                        documentSnapshot.getString("fullname"),
                                        documentSnapshot.getString("username"),
                                        documentSnapshot.getString("userid"),
                                        documentSnapshot.getString("userimage"),
                                        documentSnapshot.getString("no_of_likes"),
                                        documentSnapshot.getString("no_of_dislikes"),
                                        documentSnapshot.getString("no_of_comment"),
                                        documentSnapshot.getString("published_at"),
                                        documentSnapshot.getString("user_qualification"));

                                model.add(postmodel);
                                adapter.notifyDataSetChanged();

                            }
                        }
                    }
                });
    }

    public void getuserdetails(){
        db.collection("user").document(currentuser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    username = documentSnapshot.getString("username");
                    fullname = documentSnapshot.getString("fullname");
                    avatar = documentSnapshot.getString("avatar");
                    role = documentSnapshot.getString("role");
                }
            }
        });
    }
}