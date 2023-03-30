package com.mtaner.android_ticmeet;

import static com.google.android.material.snackbar.Snackbar.*;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActivityKaydiTamamla extends AppCompatActivity {
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    Uri imageData;

    private EditText edtAdSoyad, edtBiyografi, edtYasadigiIl, edtYas, edtCinsiyet;
    private Button btnIlerle;
    ImageView imageProfil;

    private Bitmap selectedImage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private FirebaseStorage firebaseStorage;


/*
    public void profilResmiSec(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Galeri için izine ihtiyacımız var", Snackbar.LENGTH_INDEFINITE).setAction("İzin ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //izin isteme
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

                    }
                }).show();
            }  else {
                //izin isteme
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);


            }


        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);
        }
    }

 */
    public void registerLauncher(){
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK){
                    Intent intentFromResult = result.getData();
                    if (intentFromResult != null){
                        imageData= intentFromResult.getData();
                        imageProfil.setImageURI(imageData);
                        /*
                        //bitmape çevirme
                        try {

                            if(Build.VERSION.SDK_INT>=28){
                                ImageDecoder.Source source=ImageDecoder.createSource(ActivityKaydiTamamla.this.getContentResolver(),imageData);
                                selectedImage=ImageDecoder.decodeBitmap(source);
                                imageProfil.setImageBitmap(selectedImage);
                            } else{
                                selectedImage=MediaStore.Images.Media.getBitmap(ActivityKaydiTamamla.this.getContentResolver(),imageData);
                                imageProfil.setImageBitmap(selectedImage);

                            }

                        } catch (Exception e){
                            e.printStackTrace();
                        } */

                    }
                }

            }
        });

        permissionLauncher =registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result){
                    Intent intentToGallery= new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);

                } else {
                    Toast.makeText(ActivityKaydiTamamla.this,"İzine ihtiyaç var",Toast.LENGTH_LONG).show();
                }

            }
        });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaydi_tamamla);

        firebaseStorage = FirebaseStorage.getInstance();
        auth= FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference=firebaseStorage.getReference();

        edtAdSoyad = findViewById(R.id.editTextAdVeSoyad);
        edtBiyografi = findViewById(R.id.editTextBiyografi);
        edtYasadigiIl = findViewById(R.id.editTextYasadiginizIl);
        edtYas = findViewById(R.id.editTextYas);
        edtCinsiyet = findViewById(R.id.editTextCinsiyet);
        imageProfil= findViewById(R.id.imageViewP);

        registerLauncher();

        imageProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ActivityKaydiTamamla.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(ActivityKaydiTamamla.this,Manifest.permission.READ_MEDIA_IMAGES)){
                        Snackbar.make(v,"Galeri için izine ihtiyacımız var", Snackbar.LENGTH_LONG).setAction("İzin ver", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                //izin isteme
                                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);

                            }
                        }).show();
                    }  else {
                        //izin isteme
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);

                    }

                } else {
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                }

            }
        });

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

                    if(imageData != null){
                        //uuid, uydurma isim ver.
                        UUID uuid= UUID.randomUUID();
                        String imageName="images/"+ uuid+ ".jpg";


                        storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //download url
                                StorageReference newReference=firebaseStorage.getReference(imageName);
                                newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        FirebaseUser users= auth.getCurrentUser();
                                        String email=users.getEmail();
                                        String userImage= uri.toString();

                                        Map<String, Object> kayit = new HashMap<>();
                                        kayit.put("userName", adSoyad);
                                        kayit.put("userBio", biyografi);
                                        kayit.put("userLocation", yasadigiIl);
                                        kayit.put("userAge", yas);
                                        kayit.put("userGender", cinsiyet);
                                        kayit.put("userEmail",email);
                                        kayit.put("userimage", userImage);
                                        kayit.put("userRegisterDate", FieldValue.serverTimestamp());

                                        firebaseFirestore.collection("Users").add(kayit).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Intent intent=new Intent(ActivityKaydiTamamla.this,ActivityEtkinlikler.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ActivityKaydiTamamla.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }
                                });


                            }
                        });



                    }

                /*  db.collection("User").document(adSoyad)
                            .set(kayit)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ActivityKaydiTamamla.this, "Kayıt başarılı", Toast.LENGTH_SHORT).show();
                                        Intent intent= new Intent(ActivityKaydiTamamla.this,ActivityEtkinlikler.class);
                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(ActivityKaydiTamamla.this, "Kayıt başarısız", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }); */
            }
        });


    }

}