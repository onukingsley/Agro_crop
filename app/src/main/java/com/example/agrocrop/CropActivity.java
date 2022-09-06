package com.example.agrocrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.GridView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CropActivity extends AppCompatActivity {
    GridView gridView;
    ArrayList<CropModel> model;
    FirebaseFirestore db;
    String crop_title,image;
    Crop_Data_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        db = FirebaseFirestore.getInstance();
        model = new ArrayList<>();
        gridView = findViewById(R.id.gridContainer);
        getCropinfo();
        adapter = new Crop_Data_Adapter(CropActivity.this,model,0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        },2000);



    }
    public void getCropinfo (){
        db.collection("crop").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot document : task.getResult()){
                        CropModel cropModel = new CropModel(document.getString("crop_title"),
                                document.getString("crop_image"),document.getId());

                        model.add(cropModel);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}