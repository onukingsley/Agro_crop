package com.example.agrocrop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ChartListActivity extends AppCompatActivity {
    RecyclerView chartlistcontainer;
    FirebaseFirestore db;
    TextView message;
    ArrayList<ChartListModel> models = new ArrayList<>();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String lastmessage,read;
    ChartListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_list);
        chartlistcontainer = findViewById(R.id.chartlistcontainer);
        db = FirebaseFirestore.getInstance();
        message = findViewById(R.id.message);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },2000);
        adapter = new ChartListAdapter(this,models);
        getchatupdate();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false);
        chartlistcontainer.setLayoutManager(layoutManager);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                chartlistcontainer.setAdapter(adapter);
            }
        },2000);




    }
    public void getchatupdate(){
        db.collection("user").document(mAuth.getUid()).collection("chat").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                models.clear();
                for (QueryDocumentSnapshot docs : value){
                    ChartListModel mod = new ChartListModel();
                    db.collection("user").document(mAuth.getUid()).collection("chart").document(docs.getId())
                            .collection("message").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            for (QueryDocumentSnapshot document : value){
                                mod.setRead(document.getString("read"));
                                mod.setLastMessages(document.getString("message"));
                                mod.setTimestamp(document.getString("timestamp"));
                            }
                        }
                    });

                    mod.setUsername(docs.getString("username"));
                    mod.setUserid(docs.getId());
                    mod.setUserimage(docs.getString("userimage"));

                    models.add(mod);
                    Toast.makeText(ChartListActivity.this, docs.getId()+docs.getString(
                            "timestamp"),
                            Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
            }
        });

    }

    public void getchart (){
        db.collection("user").document(mAuth.getUid()).collection("chat").orderBy("timestamp")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ChartListActivity.this, "yoooo", Toast.LENGTH_SHORT).show();
                    for (QueryDocumentSnapshot docs : task.getResult()){
                        ChartListModel mod = new ChartListModel();
                        db.collection("user").document(mAuth.getUid()).collection("chart").document(docs.getId())
                                .collection("message").orderBy("timestamp")
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    mod.setRead(document.getString("read"));
                                    mod.setLastMessages(document.getString("message"));
                                    mod.setTimestamp(document.getString("timestamp"));
                                }
                            }
                        });
                        mod.setUsername(docs.getString("username"));
                        mod.setUserid(docs.getId());
                        mod.setUserimage(docs.getString("userimage"));

                        models.add(mod);
                        Toast.makeText(ChartListActivity.this, docs.getId()+docs.getString(
                                "timestamp"),
                                Toast.LENGTH_SHORT).show();
                    }

                    adapter.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChartListActivity.this, "error while fetching chats"+ e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}