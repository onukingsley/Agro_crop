package com.example.agrocrop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NewpostActivity extends AppCompatActivity {
ImageButton imagepost;
EditText title,content;
TextView imagetext,home;
LinearLayout layoutcontainer;
Button postbtn;
FirebaseFirestore db;
FirebaseAuth mAuth;
FirebaseUser currentuser;
FirebaseStorage storage;
StorageReference reference;
Uri avatar;
String dbimage,userimage,fullname,username,userid,user_qualification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpost);

        imagepost = findViewById(R.id.imagebtn);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        imagetext = findViewById(R.id.text);
        layoutcontainer = findViewById(R.id.layoutcontainer);

        db =FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentuser  = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();

        getuserdetails();

        imagepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagepicker();
            }
        });

        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendtodb();
            }
        });






    }
    public void imagepicker (){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i,"selectpostimage"),200);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 200){
            avatar = data.getData();
            if (avatar != null){
                imagepost.setImageURI(avatar);
                imagetext.setVisibility(View.GONE);
            }
        }

    }

    public void uploadimagepost(){
        StorageReference ref = reference.child("image/post/*"+ UUID.randomUUID());

        ref.putFile(avatar).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        dbimage = task.getResult().toString();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar snackbar = Snackbar.make(layoutcontainer,"Failed to get image Uri",Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar snackbar = Snackbar.make(layoutcontainer,"image upload failed",Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    public void sendtodb(){

        uploadimagepost();
        SimpleDateFormat format = new SimpleDateFormat("dd/mm/yy");
        Date date = new Date();
        Map<String, Object> newpost = new HashMap<>();


        newpost.put("post_title", title.getText().toString());
        newpost.put("post_content", content.getText().toString());
        newpost.put("post_image",dbimage);
        newpost.put("post_validation","0");
        newpost.put("fullname",fullname);
        newpost.put("username",username);
        newpost.put("userid",userid);
        newpost.put("userimage",userimage);
        newpost.put("no_of_likes","0");
        newpost.put("no_of_dislikes","0");
        newpost.put("no_of_comment","0");
        newpost.put("published_at",format.format(date));
        newpost.put("user_qualification", user_qualification);


        db.collection("post").add(newpost)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Snackbar snackbar = Snackbar.make(layoutcontainer,"uploaded successfully",Snackbar.LENGTH_LONG);
                        snackbar.show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewpostActivity.this);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                title.setText("");
                                content.setText("");
                                imagetext.setVisibility(View.VISIBLE);
                                imagepost.setImageURI(null);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(NewpostActivity.this,ProfileActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder.setTitle("New post");
                        builder.setMessage("Do you want to create a new post?");
                        builder.setIcon(R.drawable.add);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
    }
    public void getuserdetails (){
        db.collection("user").document(currentuser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            userimage = snapshot.getString("avatar");
                            fullname = snapshot.getString("fullname");
                            username = snapshot.getString("username");
                            userid = snapshot.getId();
                            user_qualification = snapshot.getString("user_qualification");
                        }
                    }
                });
    }
}