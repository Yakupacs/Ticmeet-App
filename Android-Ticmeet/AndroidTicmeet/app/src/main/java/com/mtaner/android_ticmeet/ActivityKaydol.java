package com.mtaner.android_ticmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityKaydol extends AppCompatActivity {

    // Email ve şifre için EditText alanları
    private EditText mEmailField;
    private EditText mPasswordField;

    // Kaydet butonu
    private Button mKaydetButton;

    // Firebase Authentication referansı
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);

        // EditText alanlarına erişim sağlanıyor
        mEmailField = findViewById(R.id.editTextEmail2);
        mPasswordField = findViewById(R.id.editTextTextPassword);

        // Kaydet butonuna tıklama olayı atanıyor
        mKaydetButton = findViewById(R.id.btnGiris);
        mKaydetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText alanlarından email ve şifre değerleri alınıyor
                String email = mEmailField.getText().toString();
                String password = mPasswordField.getText().toString();

                // Email ve şifre değerlerinin boş olup olmadığı kontrol ediliyor
                if (email.isEmpty() || password.isEmpty()) {
                    System.out.println("kaydoldu");
                    Toast.makeText(ActivityKaydol.this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                } else {
                    // Firebase Authentication referansı alınıyor
                    mAuth = FirebaseAuth.getInstance();

                    // Email ve şifre ile yeni kullanıcı hesabı oluşturuluyor
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(ActivityKaydol.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Kullanıcı ekleme işlemi başarılı olduğunda kullanıcı girişi yapılıyor
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(ActivityKaydol.this, "Başarıyla kaydedildi.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ActivityKaydol.this, ActivityKaydiTamamla.class));
                                        finish();
                                    } else {
                                        // Kullanıcı ekleme işlemi başarısız olduğunda hata mesajı gösteriliyor
                                        Toast.makeText(ActivityKaydol.this, "Kaydetme işlemi başarısız. Lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    }
