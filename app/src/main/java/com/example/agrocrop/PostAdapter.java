package com.example.agrocrop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostAdapter extends ArrayAdapter {
    ArrayList<PostModel> models = new ArrayList<>();
    Context context;
    int num,nooflikes,noofdislikes,noofcomment;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentuser = mAuth.getCurrentUser();
    String username,fullname,avatar,role;
    ArrayList<String> likesmodel = new ArrayList<>();
    ArrayList<String> dislikesmodel = new ArrayList<>();
    ArrayList<String> likesmodel1 = new ArrayList<>();
    ArrayList<String> dislikesmodel1 = new ArrayList<>();
    ArrayList<String> checkdislike = new ArrayList<>();
    ArrayList<String> checklike = new ArrayList<>();


    public PostAdapter(@NonNull Context context, ArrayList<PostModel> models,int num,String username,String fullname, String avatar,String role) {
        super(context,0,models);
        this.models = models;
        this.context = context;
        this.num = num;
        this.username = username;
        this.fullname = fullname;
        this.avatar = avatar;
        this.role = role;

    }
    public PostAdapter(@NonNull Context context, ArrayList<PostModel> models,int num) {
        super(context,0,models);
        this.models = models;
        this.context = context;
        this.num = num;


    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (num == 2){

            view = LayoutInflater.from(context).inflate(R.layout.singlepost,parent,false);
            ImageView profile_image,post_image;
            TextView fullname,username,publishedate,post_title,post_content,likes,dislikes,comment;
            ToggleButton like_btn, dislike_btn;
            ImageButton comment_btn;


            profile_image = view.findViewById(R.id.profile_image);
            username = view.findViewById(R.id.username);
            fullname = view.findViewById(R.id.fullname);
            publishedate = view.findViewById(R.id.publishedate);
            post_title = view.findViewById(R.id.post_title);
            post_content = view.findViewById(R.id.post_content);
            likes = view.findViewById(R.id.likes);
            dislikes = view.findViewById(R.id.dislikes);
            comment = view.findViewById(R.id.comment);
            post_image= view.findViewById(R.id.post_image);
            like_btn = view.findViewById(R.id.like_btn);
            dislike_btn = view.findViewById(R.id.dislike_btn);
            comment_btn = view.findViewById(R.id.comment_btn);

            post_title.setText( models.get(position).getPost_title());
            post_content.setText(models.get(position).getPost_content());
            Picasso.with(context).load(models.get(position).getPost_image()).into(post_image);
            publishedate.setText(models.get(position).getPublished_at());
            username.setText(models.get(position).username);
            Picasso.with(context).load(models.get(position).getUserimage()).into(profile_image);
            fullname.setText(models.get(position).fullname);
            likes.setText(models.get(position).getNo_of_likes());
            dislikes.setText(models.get(position).getNo_of_dislikes());

            comment.setText(models.get(position).getNo_of_comment());

            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context,ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("fullname", models.get(position).fullname);
                    bundle.putString("username", models.get(position).username);
                    bundle.putString("userImage", models.get(position).userimage);
                    bundle.putString("userid", models.get(position).userid);

                    i.putExtras(bundle);
                    ((AppCompatActivity)getContext()).startActivity(i);
                }
            });


            /*fetching the no of likes directly from firebase from adapter and assigning it to the model*/
        /*    db.collection("post").document(models.get(position).post_id).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();

                                models.get(position).no_of_likes = (document.getString("no_of_likes"));
                                models.get(position).no_of_dislikes = (document.getString("no_of_dislikes"));
                                likes.setText(models.get(position).getNo_of_likes());
                                dislikes.setText(models.get(position).getNo_of_dislikes());
                            }

                        }
                    });*/


            db.collection("post").document(models.get(position).post_id).collection("dislikes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    checkdislike.clear();
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot snapshot : task.getResult() ){
                            checkdislike.add(snapshot.getId());

                                /* if (snapshot.getString("userid").equals(currentuser.getUid())){




                                }else{


                            }*/


                        }
                        if (checkdislike.contains(mAuth.getUid().toString())){
                            dislike_btn.setButtonDrawable(R.drawable.dislike_red);
                            dislike_btn.setChecked(true);
                        }else{
                            dislike_btn.setButtonDrawable(R.drawable.dislike);
                        }

                    }
                }
            });

            db.collection("post").document(models.get(position).post_id).collection("likes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    checklike.clear();
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot snapshot : task.getResult() ){
                            checklike.add(snapshot.getId());
                           /* if (snapshot.getString("userid").equals(currentuser.getUid())){

                                like_btn.setButtonDrawable(R.drawable.like_green);
                                like_btn.setChecked(true);



                            }else{

                                like_btn.setButtonDrawable(R.drawable.like);

                            }*/
                        }
                        if (checklike.contains(mAuth.getUid().toString())){
                            like_btn.setButtonDrawable(R.drawable.like_green);
                            like_btn.setChecked(true);
                        }else{
                            like_btn.setButtonDrawable(R.drawable.like);
                        }
                    }
                }
            });



            like_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                    if(ischecked){
                        likesmodel.clear();
                        dislikesmodel.clear();
                        db.collection("post").document(models.get(position).post_id).collection("likes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot snapshot : task.getResult() ){
                                        likesmodel.add(snapshot.getString("userid"));
                                    }
                                }
                            }
                        });
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (likesmodel.contains(mAuth.getUid())){

                                }else{
                                    db.collection("post").document(models.get(position).post_id).collection("dislikes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()){
                                                for (QueryDocumentSnapshot snapshot : task.getResult() ){
                                                    dislikesmodel.add(snapshot.getString("userid"));

                                                }
                                            }
                                        }
                                    });
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (dislikesmodel.contains(mAuth.getUid())){
                                                deletedislike(models.get(position).post_id,dislikes,position);
                                                dislike_btn.setChecked(false);
                                            }
                                        }
                                    },1000);


                                    like(models.get(position).post_id,likes,position);

                                    like_btn.setButtonDrawable(R.drawable.like_green);
                                    dislike_btn.setChecked(false);


                                }
                            }
                        },1000);


                    }
                    else {
                        deletelike(models.get(position).post_id,likes,position);

                        like_btn.setButtonDrawable(R.drawable.like);
                    }
                }
            });

            dislike_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                    if (ischecked){
                        if(ischecked){
                            likesmodel1.clear();
                            dislikesmodel1.clear();
                            db.collection("post").document(models.get(position).post_id).collection("dislikes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        for (QueryDocumentSnapshot snapshot : task.getResult() ){
                                            dislikesmodel1.add(snapshot.getString("userid"));
                                        }
                                    }
                                }
                            });
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (dislikesmodel1.contains(mAuth.getUid())){

                                    }else{
                                        db.collection("post").document(models.get(position).post_id).collection("likes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()){
                                                    for (QueryDocumentSnapshot snapshot : task.getResult() ){
                                                        likesmodel1.add(snapshot.getString("userid"));

                                                    }
                                                }
                                            }
                                        });
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (likesmodel1.contains(mAuth.getUid())){
                                                    deletelike(models.get(position).post_id,likes,position);

                                                    like_btn.setChecked(false);
                                                }
                                            }
                                        },1000);


                                        dislike(models.get(position).post_id,dislikes,position);

                                        dislike_btn.setButtonDrawable(R.drawable.dislike_red);
                                        like_btn.setChecked(false);


                                    }
                                }
                            },1000);


                        }






                       /* dislike(models.get(position).post_id);
                        dislike_btn.setButtonDrawable(R.drawable.dislike_red);
                        deletelike(models.get(position).post_id);
                        like_btn.setChecked(false);*/
                    }
                    else{
                        deletedislike(models.get(position).post_id,dislikes,position);

                        dislike_btn.setButtonDrawable(R.drawable.dislike);
                    }
                }
            });



            return view;
        }
        return view;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public void like(String blogid,TextView likes, int position){
        /*retrieving of user details form the user collection so as to add in the like collection*/
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*   retriving the current no of likes form thfe post collection*/
                db.collection("post").document(blogid).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                nooflikes=0;
                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();

                                    nooflikes = Integer.valueOf(document.getString("no_of_likes"));
                                }

                            }
                        });
                /*Map creating and adding of the likes collection to the post collection*/
                Map<String, Object> post = new HashMap<>();
                post.put("username", username);
                post.put("fullname", fullname);
                post.put("avatar", avatar);
                post.put("role", role);
                post.put("userid", currentuser.getUid());

                db.collection("post").document(blogid).collection("likes").document(currentuser.getUid())
                        .set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        nooflikes += 1;
                        String res = String.valueOf(nooflikes);
                        Map<String, Object> no_of_likes = new HashMap<>();
                        no_of_likes.put("no_of_likes", res);
                        likes.setText(res);

                        /*if the post adding was successful, the no of likes in the post collection should be updated*/
                        db.collection("post").document(blogid).update(no_of_likes).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                models.get(position).no_of_likes = res;
                            }
                        });
                    }
                });
            }
        },1000);


    }

    public void deletelike(String blogid,TextView like,int position){

        db.collection("post").document(blogid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    nooflikes = 0;
                    DocumentSnapshot document = task.getResult();
                    nooflikes =Integer.valueOf(document.getString("no_of_likes"));
                }
            }
        });

        db.collection("post").document(blogid).collection("likes").document(currentuser.getUid()).delete().addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        nooflikes -= 1;
                        String res =  String.valueOf(nooflikes);
                        Map<String, Object> no_of_likes = new HashMap<>();
                        no_of_likes.put("no_of_likes",res);
                        like.setText(res);
                        db.collection("post").document(blogid).update(no_of_likes).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                models.get(position).no_of_likes = res;

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,"an error occured while updating and decrementing the no of likes",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
        );
    }

    public void dislike (String blogid, TextView dislike , int position){
        /*retrieving of user details form the user collection so as to add in the like collection*/
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*retriving the current no of likes form thfe post collection*/
                db.collection("post").document(blogid).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                noofdislikes = 0;
                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    noofdislikes = Integer.valueOf(document.getString("no_of_dislikes"));
                                }

                            }
                        });

                /*Map creating and adding of the likes collection to the post collection*/
                Map<String, Object> post = new HashMap<>();
                post.put("username", username);
                post.put("fullname", fullname);
                post.put("avatar", avatar);
                post.put("role", role);
                post.put("userid", currentuser.getUid());

                db.collection("post").document(blogid).collection("dislikes").document(currentuser.getUid())
                        .set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        noofdislikes += 1;
                        String res = String.valueOf(noofdislikes);
                        Map<String, Object> no_of_dislikes = new HashMap<>();
                        no_of_dislikes.put("no_of_dislikes", res);
                        dislike.setText(res);
                        /*if the post adding was successful, the no of likes in the post collection should be updated*/
                        db.collection("post").document(blogid).update(no_of_dislikes).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                models.get(position).no_of_dislikes = res;
                            }
                        });
                    }
                });
            }
        },1000);


    }

    public void deletedislike(String blogid, TextView dislike, int position){


        db.collection("post").document(blogid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                noofdislikes = 0;
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    noofdislikes =Integer.valueOf(document.getString("no_of_dislikes"));
                }
            }
        });

        /*Delete the dislike and update the no of dislike with the decremented one*/
        db.collection("post").document(blogid).collection("dislikes").document(currentuser.getUid()).delete().addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        noofdislikes -= 1;
                        String res =  String.valueOf(noofdislikes);
                        Map<String, Object> no_of_dislikes = new HashMap<>();
                        no_of_dislikes.put("no_of_dislikes",res);
                        dislike.setText(res);
                        db.collection("post").document(blogid).update(no_of_dislikes).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                models.get(position).no_of_dislikes = res;
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,"an error occured while updating and decrementing the no of likes",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
        );
    }
}
