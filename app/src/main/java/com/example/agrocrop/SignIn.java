package com.example.agrocrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agrocrop.admin.AdminHome;
import com.example.agrocrop.superadmin.SuperAdminHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignIn extends AppCompatActivity {
    EditText email,password;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Button signin;
    TextView signup;
    String role,username;
    ProgressDialog dialog;
    NavigationView navbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        dialog  = new ProgressDialog(SignIn.this);
        email = findViewById(R.id.signin_email);
        password = findViewById(R.id.passwd);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        navbar = findViewById(R.id.signin_navbar);

        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                signinuser(email.getText().toString(),password.getText().toString());

            }
        });

        navbar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()){

                    case R.id.login:
                        i = new Intent(SignIn.this, SignUp.class);
                        startActivity(i);
                        break;
                    case R.id.contact_us:
                        i = new Intent(SignIn.this, ContactUs.class);
                        break;
                }



                return false;
            }
        });






    }
    public void signinuser(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String userid = mAuth.getUid();
                Toast.makeText(SignIn.this, userid, Toast.LENGTH_SHORT).show();

                db.collection("user").document(userid)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            role = document.getString("role");

                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignIn.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        if (role.equals("2")){

                            Intent i = new Intent(SignIn.this,Home.class);
                            startActivity(i);
                            finish();
                        }
                        else if (role.equals("1")){

                            Intent i = new Intent(SignIn.this, AdminHome.class);
                            startActivity(i);
                            finish();
                        }
                        else if(role.equals("0")){

                            Intent i = new Intent(SignIn.this, SuperAdminHome.class);
                            startActivity(i);
                            finish();
                        }
                    }
                },3000);



            }
        });
    }
}