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

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityEtkinlikler extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private CategoryAdapter adapter;

    private ArrayList<Event> konserArrayList, tiyatroArrayList,söylesiArrayList,performansArrayList,sergiArrayList;
    private ArrayList<Event> sinemaArrayList;
    private ArrayList<Category> categoriesArrayList;


    private ActivityEtkinliklerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEtkinliklerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.recyclerViewCategory.setLayoutManager(manager);
        categoriesArrayList = new ArrayList<>();
        konserArrayList = new ArrayList<>();
        tiyatroArrayList = new ArrayList<>();
        söylesiArrayList=new ArrayList<>();
        sinemaArrayList= new ArrayList<>();
        sergiArrayList=new ArrayList<>();
        performansArrayList=new ArrayList<>();

        adapter = new CategoryAdapter(categoriesArrayList, this);
        binding.recyclerViewCategory.setAdapter(adapter);

        getKonserEvent(categoriesArrayList,konserArrayList);
        getTiyatroEvent(categoriesArrayList,tiyatroArrayList);
        getSöylesiEvent(categoriesArrayList,söylesiArrayList);
        getPerformansEvent(categoriesArrayList,performansArrayList);
        getSergiEvent(categoriesArrayList,sergiArrayList);
        getSinemaEvent(categoriesArrayList,sinemaArrayList);

        adapter.notifyDataSetChanged();

        auth = FirebaseAuth.getInstance();
         // firebaseFirestore = FirebaseFirestore.getInstance();


    }

    private void getKonserEvent(ArrayList<Category> categories, ArrayList<Event> konserArrayList) {

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Event").whereEqualTo("eventCategory", "Konser").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(ActivityEtkinlikler.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String eventImage = (String) data.get("eventImage");
                        String category = (String) data.get("eventCategory");

                        konserArrayList.add(new Event(category,eventImage));

                        // Event konser = new Event(category, eventImage);
                        // konserArrayList.add(konser);

                    }

                    categories.add(new Category("konser",konserArrayList));
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void getTiyatroEvent(ArrayList<Category> categories, ArrayList<Event> tiyatroArrayList) {
        firebaseFirestore.collection("Event").whereEqualTo("eventCategory","Tiyatro").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(ActivityEtkinlikler.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {

                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String eventImage = (String) data.get("eventImage");
                        String category = (String) data.get("eventCategory");

                        tiyatroArrayList.add(new Event(category, eventImage));

                    }
                    categories.add(new Category("tiyatro", tiyatroArrayList));
                    adapter.notifyDataSetChanged();

                }
            }
        });


    }

    private void getSöylesiEvent(ArrayList<Category> categories, ArrayList<Event> söylesiArrayList) {
        firebaseFirestore.collection("Event").whereEqualTo("eventCategory","Söyleşi").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(ActivityEtkinlikler.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {

                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String eventImage = (String) data.get("eventImage");
                        String category = (String) data.get("eventCategory");

                        söylesiArrayList.add(new Event(category, eventImage));

                    }
                    categories.add(new Category("Söyleşi", söylesiArrayList));
                    adapter.notifyDataSetChanged();

                }
            }
        });


    }

    private void getSinemaEvent(ArrayList<Category> categories, ArrayList<Event> söylesiArrayList) {
        firebaseFirestore.collection("Event").whereEqualTo("eventCategory","Sinema").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(ActivityEtkinlikler.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {

                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String eventImage = (String) data.get("eventImage");
                        String category = (String) data.get("eventCategory");

                        sinemaArrayList.add(new Event(category, eventImage));

                    }
                    categories.add(new Category("Sinema", sinemaArrayList));
                    adapter.notifyDataSetChanged();

                }
            }
        });


    }

    private void getPerformansEvent(ArrayList<Category> categories, ArrayList<Event> söylesiArrayList) {
        firebaseFirestore.collection("Event").whereEqualTo("eventCategory","Performans").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(ActivityEtkinlikler.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {

                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String eventImage = (String) data.get("eventImage");
                        String category = (String) data.get("eventCategory");

                        performansArrayList.add(new Event(category, eventImage));

                    }
                    categories.add(new Category("Performans", söylesiArrayList));
                    adapter.notifyDataSetChanged();

                }
            }
        });


    }

    private void getSergiEvent(ArrayList<Category> categories, ArrayList<Event> sergiArrayList) {
        firebaseFirestore.collection("Event").whereEqualTo("eventCategory","Sergi").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(ActivityEtkinlikler.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {

                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String eventImage = (String) data.get("eventImage");
                        String category = (String) data.get("eventCategory");

                        sergiArrayList.add(new Event(category, eventImage));

                    }
                    categories.add(new Category("Sergi ", sergiArrayList));
                    adapter.notifyDataSetChanged();

                }
            }
        });


    }



}


