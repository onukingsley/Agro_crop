package com.example.agrocrop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
ImageView userimage;
ImageButton btn_back,sendbtn;
EditText message;
TextView username;
RecyclerView chatcontainer;
ArrayList<ChatModel> model = new ArrayList<>();
FirebaseAuth mAuth = FirebaseAuth.getInstance();
FirebaseFirestore db = FirebaseFirestore.getInstance();
ArrayList<String> exist = new ArrayList<>();
String usernametxt,userimagetxt,useridtxt;
String c_userimage,c_fullname,c_username,c_userqualification;
ChatAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        userimage = findViewById(R.id.userImage);
        btn_back = findViewById(R.id.btn_back);
        username = findViewById(R.id.username);
        chatcontainer = findViewById(R.id.chatcontainer);
        sendbtn = findViewById(R.id.send);
        message = findViewById(R.id.message);
        adapter = new ChatAdapter(model,this);
        Bundle bundle = getIntent().getExtras();
        usernametxt = bundle.getString("username");
        userimagetxt = bundle.getString("userimage");
        useridtxt = bundle.getString("userid");
        username.setText(bundle.getString("username"));
        Picasso.with(ChatActivity.this).load(bundle.getString("userimage")).into(userimage);
        getuserdetails();
        getmessageupdate();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this,RecyclerView.VERTICAL,
                true);
        chatcontainer.setLayoutManager(manager);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                chatcontainer.setAdapter(adapter);
            }
        },2000);

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendtodb(useridtxt);
            }
        });


    }
    public void sendtodb(String userid){
        db.collection("user").document(mAuth.getUid()).collection("chat")
                .whereEqualTo("userid", userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot docs : task.getResult()){
                        exist.add(docs.getString("userid"));
                    }
                }
            }
        });
        if (!exist.isEmpty()){
            Date date  = new Date();
            SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");

            Map<String, Object> reciever = new HashMap<>();
            reciever.put("userid",userid);
            reciever.put("username",username);
            reciever.put("userimage",userimage);
            reciever.put("timestamp",format.format(date));
            db.collection("user").document(mAuth.getUid()).collection("chat").document(userid)
                    .set(reciever).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(ChatActivity.this, "added to chartlist successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChatActivity.this,
                            "error occured in chartlist upload"+e.toString(),
                            Toast.LENGTH_SHORT).show();
                }
            });
            /*writing current user to reciever chat collection*/

            Map<String, Object> giver = new HashMap<>();
            reciever.put("userid",mAuth.getUid());
            reciever.put("username",c_username);
            reciever.put("userimage",c_userimage);
            reciever.put("timestamp",format.format(date));
            db.collection("user").document(userid).collection("chat").document(mAuth.getUid())
                    .set(giver).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(ChatActivity.this, "added to reciever chatlist successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChatActivity.this, "failed to write current user to reciever chat collection", Toast.LENGTH_SHORT).show();
                }
            });
        }

        /*add the message to the sender chat collection*/
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("E, dd MMM HH:mm:ss");
        Map<String, Object> sender = new HashMap<>();
        sender.put("date",format.format(date));
        sender.put("timestamp",format1.format(date));
        sender.put("userid",mAuth.getUid());
        sender.put("userimage",c_userimage);
        sender.put("fullname", c_fullname);
        sender.put("username", c_username);
        sender.put("user_qualification", c_userqualification);
        sender.put("message", message.getText().toString());
        db.collection("user").document(mAuth.getUid()).collection("chat").document(userid).collection("message")
                .add(sender).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(ChatActivity.this, "added message successfully", Toast.LENGTH_SHORT).show();


                db.collection("user").document(userid).collection("chat").document(mAuth.getUid())
                        .collection("message").add(sender).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(ChatActivity.this, "added reverse message successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatActivity.this, "reverse message failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this, "message failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*fetching messages from database*/
    public void getmessages(){
        db.collection("user").document(mAuth.getUid()).collection("chat").document(useridtxt)
                .collection("message").orderBy("timestamp").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot docs : task.getResult()){
                        ChatModel mod = new ChatModel(docs.getString("timestamp"),
                                docs.getString("message"),
                                docs.getString("userimage"),
                                docs.getString("username"),
                                docs.getString("userid"));
                        model.add(mod);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this, "failed to get message form db", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getmessageupdate(){
        db.collection("user").document(mAuth.getUid()).collection("chat").document(useridtxt)
                .collection("message").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                model.clear();
                for (QueryDocumentSnapshot docs : value){
                    ChatModel mod = new ChatModel(docs.getString("timestamp"),
                            docs.getString("message"),
                            docs.getString("userimage"),
                            docs.getString("username"),
                            docs.getString("userid"));
                    model.add(mod);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }



    public void getuserdetails (){
        db.collection("user").document(mAuth.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            c_userimage = snapshot.getString("avatar");
                            c_fullname = snapshot.getString("fullname");
                            c_username = snapshot.getString("username");
                            c_userqualification = snapshot.getString("user_qualification");
                        }
                    }
                });
    }
}
