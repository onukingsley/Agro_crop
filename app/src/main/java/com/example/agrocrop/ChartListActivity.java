package com.example.agrocrop;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.FirebaseFirestore;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_list);
        chartlistcontainer = findViewById(R.id.chartlistcontainer);
        db = FirebaseFirestore.getInstance();
        message = findViewById(R.id.message);
        getchart();

        ChartListAdapter adapter = new ChartListAdapter(this,models);
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

    public void getchart (){
        db.collection("user").document(mAuth.getUid()).collection("chart").orderBy("timestamp")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot docs : task.getResult()){
                        ChartListModel mod = new ChartListModel();
                        db.collection("user").document(mAuth.getUid()).collection("chart").document(docs.getId())
                                .collection("messages").orderBy("timestamp").limit(1)
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

                    }
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