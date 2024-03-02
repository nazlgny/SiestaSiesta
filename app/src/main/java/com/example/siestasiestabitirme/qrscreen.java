package com.example.siestasiestabitirme;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class qrscreen extends AppCompatActivity {

    private Button btn_scan;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscreen);

        // Firestore veritabanı örneğini başlat
        db = FirebaseFirestore.getInstance();

        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v -> scanCode());
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan QR to rent products / Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            // QR kodundan elde edilen ID'yi kullanarak Firestore'daki ilgili dokümanı güncelle
            updateChairStatus(result.getContents(), true);
        } else {
            // QR kodu okunamadı veya içerik boş ise
            new AlertDialog.Builder(qrscreen.this)
                    .setTitle("Hata!")
                    .setMessage("QR kodu okunamadı.")
                    .setPositiveButton("OK", null)
                    .show();
        }
    });

    private void updateChairStatus(String chairId, boolean inUse) {
        // Firestore veritabanında 'chair' koleksiyonundaki ilgili dokümanı güncelle
        db.collection("chair").document(chairId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        int currentQuantity = documentSnapshot.getLong("quantity").intValue();
                        if (currentQuantity > 0) {
                            // Ürün bulundu ve stokta var, sayısını azalt
                            db.collection("chair").document(chairId)
                                    .update("quantity", currentQuantity - 1)
                                    .addOnSuccessListener(aVoid -> {
                                        // Güncelleme başarılı olduğunda kullanıcıya bilgi ver
                                        Toast.makeText(qrscreen.this, "Ürün sepete eklendi.", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Güncelleme sırasında bir hata oluştuğunda kullanıcıya bilgi ver
                                        Toast.makeText(qrscreen.this, "Bir hata oluştu: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            // Stokta ürün kalmadığını bildir
                            Toast.makeText(qrscreen.this, "Üzgünüz, ürün stokta bulunmamaktadır.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Ürün bulunamadı
                        Toast.makeText(qrscreen.this, "Ürün bulunamadı.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Sorgulama sırasında bir hata oluştuğunda kullanıcıya bilgi ver
                    Toast.makeText(qrscreen.this, "Bir hata oluştu: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}