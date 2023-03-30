package com.mtaner.android_ticmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityGirisYap extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginButton;
    private TextView textSifremiUnuttum;
    private FirebaseAuth mAuth;

    private TextView textViewKaydol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_yap);

        mAuth = FirebaseAuth.getInstance();

        mEmailField = findViewById(R.id.editTextEmail2);
        mPasswordField = findViewById(R.id.editTextTextPassword);
        mLoginButton = findViewById(R.id.btnGiris);
        textViewKaydol=findViewById(R.id.textViewKaydol);
        textSifremiUnuttum=findViewById(R.id.textViewSifremiUnuttum);

        textViewKaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ActivityGirisYap.this,ActivityKaydol.class);
                startActivity(intent);
            }
        });


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailField.getText().toString();
                String password = mPasswordField.getText().toString();
                signIn(email, password);

            }
        });

        textSifremiUnuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ActivityGirisYap.this,ActivitySifremiSifirla.class );
                startActivity(intent);

            }
        });

    }



    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent =new Intent(ActivityGirisYap.this,ActivityEtkinlikler.class);
                            startActivity(intent);
                            System.out.println("Başarılı");

                        } else {
                            Toast.makeText(ActivityGirisYap.this,"Giriş Başarısız!",Toast.LENGTH_LONG).show();
                            System.out.println("Başarısız");
                        }
                    }
                });
    }



}