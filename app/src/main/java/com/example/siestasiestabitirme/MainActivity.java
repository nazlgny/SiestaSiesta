package com.example.siestasiestabitirme;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.siestasiestabitirme.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth auth;
// github denemesi
    //github denemesi 2
    // github denemesi 3
    //git denemesi nehirş
    // git denemesi giray
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        binding.passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

    }

    public void signInClicked(View view){
        String email = binding.emailText.getText().toString();
        String password = binding.passwordText.getText().toString();

            if (email.equals("") || password.equals("")) {
        Toast.makeText(this, "Enter a valid email and password", Toast.LENGTH_SHORT).show();
    } else {
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent= new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }}

    public void signUpClicked(View view){

            String email = binding.emailText.getText().toString();
            String password = binding.passwordText.getText().toString();

            if (email.equals("") || password.equals("")) {
                Toast.makeText(this, "Enter a valid email and password", Toast.LENGTH_SHORT).show();
            } else {
                auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent intent= new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                });


        }

    }

    public void forgotPasswordClick(View view) {
        String email = binding.emailText.getText().toString();

        if (!email.isEmpty()) {
            auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Şifre sıfırlama e-postası gönderildi. Lütfen e-posta adresinizi kontrol edin.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Şifre sıfırlama e-postası gönderilemedi: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(MainActivity.this, "Lütfen e-posta adresinizi girin", Toast.LENGTH_SHORT).show();
        }
    }
}
