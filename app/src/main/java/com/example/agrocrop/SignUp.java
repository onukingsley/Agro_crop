package com.example.agrocrop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignUp extends AppCompatActivity {
    EditText username,fullname,email,password;
    Button signup;
    TextView signin;
    ImageView image;
    Uri imageuri;
    FirebaseStorage storage;
    StorageReference ref;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentuser;
    String avatar;
    NavigationView navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.username);
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        signin = findViewById(R.id.signin);
        image = findViewById(R.id.images);
        navbar = findViewById(R.id.signup_navbar);

        storage = FirebaseStorage.getInstance();
        ref = storage.getReference();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createuser(email.getText().toString(),password.getText().toString());
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this,SignIn.class);
                startActivity(intent);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagepicker();
            }
        });

        navbar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()){

                    case R.id.login:
                        i = new Intent(SignUp.this, SignUp.class);
                        startActivity(i);
                        break;
                    case R.id.contact_us:
                        i = new Intent(SignUp.this, ContactUs.class);
                        break;
                }



                return false;
            }
        });




    }

    public void imagepicker(){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i,"imagepicker"),200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 200){
            imageuri = data.getData();
            if (imageuri != null){
                image.setImageURI(imageuri);
            }

        }
    }
    public void uploadimage(){
        StorageReference sref = ref.child("image/profile/*"+ UUID.randomUUID().toString());
        sref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                sref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        avatar = task.getResult().toString();
                        postUser();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUp.this, "unable to download image uri", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this, "something went wrong please try again later", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void createuser (String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                uploadimage();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this, "Failed to create a user ", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void postUser(){
        SimpleDateFormat format = new SimpleDateFormat("dd/mm/yy");
        Date date = new Date();

        Map<String, Object> user = new HashMap<>();

        user.put("username", username.getText().toString());
        user.put("fullname", fullname.getText().toString());
        user.put("email", email.getText().toString());
        user.put("avatar",avatar);
        user.put("date registered", format.format(date));
        user.put("role", "2");
        user.put("verification", "0");

        db.collection("user").document(currentuser.getUid()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SignUp.this, "user has been created successfully", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}