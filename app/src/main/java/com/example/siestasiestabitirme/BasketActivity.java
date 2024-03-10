package com.example.siestasiestabitirme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Locale;

// deneme nehir giti bozdu
public class BasketActivity  extends AppCompatActivity {


    public Handler umbrellaHandler = new Handler();
    public Handler chairHandler = new Handler();
    public TextView timerTextView,timerTextView2;
    public long umbrellaTimeLeftInMillis = 0;
    public long chairTimeLeftInMillis = 0;
    public boolean umbrellaTimerRunning;
    public boolean chairTimerRunning;
    public long umbrellaStartTimeInMillis;
    public long chairStartTimeInMillis;
    public  FirebaseFirestore db = FirebaseFirestore.getInstance();
    public DocumentReference chair1 = db.collection("chair").document("u6kqc3Aoz4wpSgVlxueV");
    public DocumentReference productRef2 = db.collection("umbrella").document("2tapFqBsHzLNFVsTJ63S");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        Intent intent = getIntent();
        // MapsActivity classında qr kod çalıştığı zaman sepete ürün atan kod
        if (intent != null && intent.hasExtra("scanResult")) {
            String scanResult = intent.getStringExtra("scanResult");
            if (scanResult.equals("u6kqc3Aoz4wpSgVlxueV")) {
                AddChair();
                startChairTimer();
            } else if (scanResult.equals("2tapFqBsHzLNFVsTJ63S")) {
                AddUmbrella();
                startUmbrellaTimer();
            }
        }
        Button add_umbrella,add_chair,discard_chair,discard_umbrella,useQr;

        useQr = findViewById(R.id.useQr);
        discard_chair = findViewById(R.id.discard_chair);
        discard_umbrella = findViewById(R.id.discard_umbrella);
        timerTextView = findViewById(R.id.timer_text_view);
        timerTextView2 = findViewById(R.id.timer_text_view2);
        timerTextView.setVisibility(View.GONE);
        timerTextView2.setVisibility(View.GONE);


        useQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }

        });

        discard_umbrella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscardUmbrella();
                stopUmbrellaTimer();
            }
        });
        discard_chair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscardChair();
                stopChairTimer();
            }
        });



    }

    public void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan QR to rent products / Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            // QR kodundan elde edilen ID'yi kullanarak Firestore'daki ilgili dokümanı güncelle
            if (result.getContents().equals("u6kqc3Aoz4wpSgVlxueV")) {
                AddChair();
                startChairTimer();
            }
            else if(result.getContents().equals("2tapFqBsHzLNFVsTJ63S")){
                AddUmbrella();
                startUmbrellaTimer();
            }
        } else {
            // QR kodu okunamadı veya içerik boş ise
            new AlertDialog.Builder(BasketActivity.this)
                    .setTitle("Hata!")
                    .setMessage("QR kodu okunamadı.")
                    .setPositiveButton("OK", null)
                    .show();
        }
    });



    public void AddChair() {
        chair1.update("inUse", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BasketActivity.this, "Sandalye sepete eklendi!", Toast.LENGTH_SHORT).show();
                        timerTextView2.setVisibility(View.VISIBLE);
                        startChairTimer();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BasketActivity.this, "Sandalye sepete eklenirken hata oluştu! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void DiscardChair() {
        chair1.update("inUse", false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BasketActivity.this, "Sandalye sepetten atıldı!", Toast.LENGTH_SHORT).show();
                        timerTextView2.setVisibility(View.GONE);
                        stopChairTimer();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BasketActivity.this, "Sandalye sepetten atılırken hata oluştu! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void AddUmbrella() {
        productRef2.update("inUse", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BasketActivity.this, "Şemsiye sepete eklendi!", Toast.LENGTH_SHORT).show();
                        timerTextView.setVisibility(View.VISIBLE);
                        startUmbrellaTimer();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BasketActivity.this, "Şemsiye sepete eklenirken hata oluştu! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void DiscardUmbrella() {
        productRef2.update("inUse", false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BasketActivity.this, "Şemsiye sepetten atıldı!", Toast.LENGTH_SHORT).show();
                        timerTextView.setVisibility(View.GONE);
                        stopUmbrellaTimer();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BasketActivity.this, "Şemsiye sepetten atılırken hata oluştu! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void startUmbrellaTimer() {
        umbrellaStartTimeInMillis = System.currentTimeMillis();

        umbrellaTimerRunning = true;
        umbrellaHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (umbrellaTimerRunning) {
                    long currentTimeInMillis = System.currentTimeMillis();
                    long umbrellaTimeElapsedInMillis = currentTimeInMillis - umbrellaStartTimeInMillis;
                    umbrellaTimeLeftInMillis = umbrellaTimeElapsedInMillis;

                    updateUmbrellaTimerText();

                    umbrellaHandler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    public void startChairTimer() {
        chairStartTimeInMillis = System.currentTimeMillis();

        chairTimerRunning = true;
        chairHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (chairTimerRunning) {
                    long currentTimeInMillis = System.currentTimeMillis();
                    long chairTimeElapsedInMillis = currentTimeInMillis - chairStartTimeInMillis;
                    chairTimeLeftInMillis = chairTimeElapsedInMillis;

                    updateChairTimerText();

                    chairHandler.postDelayed(this, 1000);
                }
            }
        }, 1000);//dasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasd
    }
    //ljljlkjl

    public void stopUmbrellaTimer() {
        umbrellaTimerRunning = false;
    }

    public void stopChairTimer() {
        chairTimerRunning = false;
    }

    public void updateUmbrellaTimerText() {
        long hours = umbrellaTimeLeftInMillis / (1000 * 60 * 60);
        long minutes = (umbrellaTimeLeftInMillis % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (umbrellaTimeLeftInMillis % (1000 * 60)) / 1000;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    public void updateChairTimerText() {
        long hours = chairTimeLeftInMillis / (1000 * 60 * 60);
        long minutes = (chairTimeLeftInMillis % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (chairTimeLeftInMillis % (1000 * 60)) / 1000;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        timerTextView2.setText(timeLeftFormatted);
    }




    public void backToMain(View View) {
        Intent intent = new Intent(BasketActivity.this, MapsActivity.class);
        startActivity(intent);
    }
}