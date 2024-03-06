package com.example.siestasiestabitirme;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class qrscreen extends AppCompatActivity {

    public BasketActivity basketActivity;

    public qrscreen(BasketActivity basketActivity) {
        this.basketActivity = basketActivity;
    }

    private Button btn_scan;
    //public  FirebaseFirestore db = FirebaseFirestore.getInstance();

   // public DocumentReference chair1 = db.collection("chair").document("u6kqc3Aoz4wpSgVlxueV");
    //public DocumentReference productRef2 = db.collection("umbrella").document("2tapFqBsHzLNFVsTJ63S");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscreen);

        // Firestore veritabanı örneğini başlat

        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v -> basketActivity.scanCode());


    }






}
