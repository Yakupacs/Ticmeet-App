package com.mtaner.android_ticmeet;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ActivityKaydiTamamla extends AppCompatActivity {

    private EditText edtAdSoyad, edtBiyografi, edtYasadigiIl, edtYas, edtCinsiyet;
    private Button btnIlerle;

    private DatabaseReference mDatabase;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaydi_tamamla);

        db = FirebaseFirestore.getInstance();

        edtAdSoyad = findViewById(R.id.editTextAdVeSoyad);
        edtBiyografi = findViewById(R.id.editTextBiyografi);
        edtYasadigiIl = findViewById(R.id.editTextYasadiginizIl);
        edtYas = findViewById(R.id.editTextYas);
        edtCinsiyet = findViewById(R.id.editTextCinsiyet);

        btnIlerle = findViewById(R.id.btnIlerle);

        btnIlerle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String adSoyad = edtAdSoyad.getText().toString();
                    String biyografi = edtBiyografi.getText().toString();
                    String yasadigiIl = edtYasadigiIl.getText().toString();
                    String yas = edtYas.getText().toString();
                    String cinsiyet = edtCinsiyet.getText().toString();

                    Map<String, Object> kayit = new HashMap<>();
                    kayit.put("adSoyad", adSoyad);
                    kayit.put("biyografi", biyografi);
                    kayit.put("yasadigiIl", yasadigiIl);
                    kayit.put("yas", yas);
                    kayit.put("cinsiyet", cinsiyet);

                    db.collection("User").document(adSoyad)
                            .set(kayit)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ActivityKaydiTamamla.this, "Kayıt başarılı", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ActivityKaydiTamamla.this, "Kayıt başarısız", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
            }
        });

    }

}