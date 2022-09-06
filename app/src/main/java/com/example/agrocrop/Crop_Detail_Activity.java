package com.example.agrocrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Crop_Detail_Activity extends AppCompatActivity implements onCardClick {

    TextView crop_title,prop_title,prop_detail;
    RecyclerView recycler_container;
    FirebaseFirestore db;
    ArrayList<Crop_Detail_Model> models = new ArrayList<>();
    String id;
    Crop_Detail_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_detail);
        crop_title = findViewById(R.id.crop_title);
        prop_detail = findViewById(R.id.prop_detail);
        prop_title = findViewById(R.id.prop_title);
        db = FirebaseFirestore.getInstance();

        recycler_container = findViewById(R.id.recycler_container);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(Crop_Detail_Activity.this,
                RecyclerView.HORIZONTAL,false);

        Bundle bundle = getIntent().getExtras();
        crop_title.setText(bundle.getString("crop_title"));
        getproperties(bundle.getString("crop_id"));
        adapter = new Crop_Detail_Adapter(Crop_Detail_Activity.this,models,
                Crop_Detail_Activity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                recycler_container.setLayoutManager(layout);
                recycler_container.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        },2000);




    }

    public void getproperties(String id){
        db.collection("crop").document(id).collection("properties").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Crop_Detail_Model mod = new Crop_Detail_Model(document.getId(),
                                        document.getString("image")
                                       );
                                models.add(mod);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Crop_Detail_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onclickcard(String crop_id) {
        prop_title.setText(crop_id);
        Bundle bundle = getIntent().getExtras();
        db.collection("crop").document(bundle.getString("crop_id")).collection("properties").document(crop_id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            prop_detail.setText(document.getString("details"));
                        }
                    }
                });
    }
}