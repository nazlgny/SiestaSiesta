package com.example.siestasiestabitirme;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.siestasiestabitirme.databinding.ActivityProfilePageBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilePage extends AppCompatActivity {

    private ActivityProfilePageBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfilePageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            binding.textViewEmail.setText(currentUser.getEmail());
        }


    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.profile){
            Intent intentToMapsActivity = new Intent(ProfilePage.this,MapsActivity.class);
            startActivity(intentToMapsActivity);
        }
        else if(item.getItemId()==R.id.signout){
            //Sign out
            auth.signOut();

            Intent intentToMain = new Intent(ProfilePage.this,MainActivity.class);
            startActivity(intentToMain);
            finish();

        }


        return super.onOptionsItemSelected(item);
    }


    public void changeEmailClick(View view) {
        String newEmail = binding.editTextNewEmail.getText().toString();

        if(currentUser!=null) {
            currentUser.verifyBeforeUpdateEmail(newEmail)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            binding.textViewEmail.setText(newEmail);
                            Toast.makeText(ProfilePage.this, "E-posta başarıyla güncellendi", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfilePage.this, "E-posta güncelleme başarısız oldu: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            Toast.makeText(ProfilePage.this, "E-posta adresi doğrulaması başarısız oldu", Toast.LENGTH_SHORT).show();
        }

    }

    public void changePasswordClick(View view) {
        // String currentPassword = binding.editTextCurrentPassword.getText().toString();
        String newPassword = binding.editTextNewPassword.getText().toString();

        if (!newPassword.isEmpty()) {
            currentUser.updatePassword(newPassword);
        } else {
            Toast.makeText(ProfilePage.this, "Lütfen mevcut ve yeni şifreleri girin", Toast.LENGTH_SHORT).show();
        }
    }







}