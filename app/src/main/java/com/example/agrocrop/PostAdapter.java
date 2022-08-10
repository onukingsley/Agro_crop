package com.example.agrocrop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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


    public PostAdapter(@NonNull Context context, int resource, ArrayList<PostModel> models,int num) {
        super(context, resource,models);
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
            CheckBox dislike_btn,comment_btn;


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



        }
        return view;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public void like(String blogid){
        Map<String, Object> like = new HashMap<>();
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
        Map<String, Object> post = new HashMap<>();
        post.put("username", username);
        post.put("fullname", fullname);
        post.put("avatar", avatar);
        post.put("role", role);

        db.collection("post").document(blogid).collection("likes").document(currentuser.getUid())
                .set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                nooflikes += 1;
                String res = String.valueOf(nooflikes);
                Map<String, Object> no_of_likes = new HashMap<>();
                no_of_likes.put("no_of_likes", res);
                db.collection("post").document(blogid).update(no_of_likes).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, res.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
