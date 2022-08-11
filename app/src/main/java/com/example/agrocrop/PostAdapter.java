package com.example.agrocrop;

import android.content.Context;
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
            ToggleButton like_btn;
            CheckBox dislike_btn;
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


            db.collection("post").document(models.get(position).post_id).collection("dislikes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot snapshot : task.getResult() ){
                            if (snapshot.getString("userid").toString().equals(mAuth.getCurrentUser().getUid())){

                                dislike_btn.setButtonDrawable(R.drawable.dislike_red);
                                dislike_btn.setChecked(true);





                            }else{

                                dislike_btn.setButtonDrawable(R.drawable.dislike);

                            }
                        }
                    }
                }
            });

            db.collection("post").document(models.get(position).post_id).collection("likes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot snapshot : task.getResult() ){
                            if (snapshot.getString("userid").equals(currentuser.getUid())){

                                like_btn.setButtonDrawable(R.drawable.like_green);
                                like_btn.setChecked(true);





                            }else{

                                like_btn.setButtonDrawable(R.drawable.like);

                            }
                        }
                    }
                }
            });



            like_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                    if(ischecked){
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
                        if (likesmodel.contains(mAuth.getUid())){

                        }else{
                            like(models.get(position).post_id);
                            like_btn.setButtonDrawable(R.drawable.dislike_red);
                            deletedislike(models.get(position).post_id);
                            dislike_btn.setChecked(false);
                        }

                    }
                    else {
                        deletelike(models.get(position).post_id);
                        like_btn.setButtonDrawable(R.drawable.like);
                    }
                }
            });

            dislike_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                    if (ischecked){
                        dislike(models.get(position).post_id);
                        dislike_btn.setButtonDrawable(R.drawable.dislike_red);
                        deletelike(models.get(position).post_id);
                        like_btn.setChecked(false);
                    }
                    else{
                        deletedislike(models.get(position).post_id);
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

    public void like(String blogid){
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
                        /*if the post adding was successful, the no of likes in the post collection should be updated*/
                        db.collection("post").document(blogid).update(no_of_likes).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, res.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        },2000);


    }

    public void deletelike(String blogid){
        db.collection("post").document(blogid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
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
                        db.collection("post").document(blogid).update(no_of_likes).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
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

    public void dislike (String blogid){
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

        /*retriving the current no of likes form thfe post collection*/
        db.collection("post").document(blogid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
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

        db.collection("post").document(blogid).collection("dislikes").document(currentuser.getUid())
                .set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                noofdislikes += 1;
                String res = String.valueOf(noofdislikes);
                Map<String, Object> no_of_dislikes = new HashMap<>();
                no_of_dislikes.put("no_of_dislikes", res);
                /*if the post adding was successful, the no of likes in the post collection should be updated*/
                db.collection("post").document(blogid).update(no_of_dislikes).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, res.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void deletedislike(String blogid){
        db.collection("post").document(blogid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    noofdislikes =Integer.valueOf(document.getString("no_of_dislikes"));
                }
            }
        });

        db.collection("post").document(blogid).collection("dislikes").document(currentuser.getUid()).delete().addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        noofdislikes -= 1;
                        String res =  String.valueOf(noofdislikes);
                        Map<String, Object> no_of_dislikes = new HashMap<>();
                        no_of_dislikes.put("no_of_dislikes",res);
                        db.collection("post").document(blogid).update(no_of_dislikes).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
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
