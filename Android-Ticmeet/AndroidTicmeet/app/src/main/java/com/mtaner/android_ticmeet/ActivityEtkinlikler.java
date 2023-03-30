package com.mtaner.android_ticmeet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mtaner.android_ticmeet.databinding.ActivityEtkinliklerBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityEtkinlikler extends AppCompatActivity {


    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private imageAdapter imageAdapter;
    ArrayList<Event> eventArrayList;
    private ActivityEtkinliklerBinding binding;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding=ActivityEtkinliklerBinding.inflate(getLayoutInflater());
            View view=binding.getRoot();
            setContentView(view);

            eventArrayList =new ArrayList<>();

            auth = FirebaseAuth.getInstance();
            firebaseFirestore=FirebaseFirestore.getInstance();

            getData();
            binding.recyclerviewevent.setLayoutManager(new LinearLayoutManager(this));
            imageAdapter=new imageAdapter(eventArrayList);
            binding.recyclerviewevent.setAdapter(imageAdapter);
}

    private void getData(){
            firebaseFirestore.collection("Event").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error != null){
                        Toast.makeText(ActivityEtkinlikler.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                    if (value!=null){
                        for(DocumentSnapshot snapshot: value.getDocuments()){
                            Map<String,Object> data= snapshot.getData();
                            String eventImage=(String)data.get("eventImage");
                            String category=(String) data.get("eventCategory");


                            Event event= new Event(category,eventImage);
                            eventArrayList.add(event);

                        }

                       imageAdapter.notifyDataSetChanged();
                    }

                }
            });
    }

    }



