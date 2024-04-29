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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

// deneme nehir giti bozdu
public class BasketActivity  extends AppCompatActivity {



    public TextView timerTextView,timerTextView2;

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
                //AddChair();

            } else if (scanResult.equals("2tapFqBsHzLNFVsTJ63S")) {
                //AddUmbrella();

            }
        }
        Button discard_chair,discard_umbrella,useQr;

        useQr = findViewById(R.id.useQr);
        discard_chair = findViewById(R.id.discard_chair);
        discard_umbrella = findViewById(R.id.discard_umbrella);
        timerTextView = findViewById(R.id.timer_text_view);
        timerTextView2 = findViewById(R.id.timer_text_view2);
        timerTextView.setVisibility(View.GONE);
        timerTextView2.setVisibility(View.GONE);
//asdfghjklşlkjhgfdsaASDFGHJKJHGFDX

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
               BackgroundService.stopUmbrellaTimer();
            }
        });
        discard_chair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscardChair();
                BackgroundService.stopChairTimer();
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

                // Servisi başlatmak için Intent oluştur
                Intent serviceIntent = new Intent(this, BackgroundService.class);
                // Servisi başlat
                startService(serviceIntent);
            }
            else if(result.getContents().equals("2tapFqBsHzLNFVsTJ63S")){
                AddUmbrella();

                // Servisi başlatmak için Intent oluştur
                Intent serviceIntent = new Intent(this, BackgroundService.class);
                // Servisi başlat
                startService(serviceIntent);
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



    private boolean chairInUse = true;

    public void AddChair() {
        chair1.update("inUse", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BasketActivity.this, "Sandalye sepete eklendi!", Toast.LENGTH_SHORT).show();
                        timerTextView2.setVisibility(View.VISIBLE);

                        startTimerForTimeField(); // Firestore'daki time alanını güncellemek için zamanlayıcıyı başlat
                        chairInUse = true; // Sandalye kullanımda olduğu için bu değeri true yap
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BasketActivity.this, "Sandalye sepete eklenirken hata oluştu! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Zamanlayıcıyı durdurmak için kullanılacak Timer referansı
    private Timer timerForTimeField;

    // Firestore'daki time alanını güncellemek için zamanlayıcıyı başlatan metod
    private void startTimerForTimeField() {
        if (timerForTimeField != null) {
            timerForTimeField.cancel(); // Mevcut zamanlayıcı varsa öncelikle onu durdur
        }
        timerForTimeField = new Timer();
        timerForTimeField.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!chairInUse) { // Eğer sandalye kullanımda değilse zamanlayıcıyı durdur
                    this.cancel();
                    return;
                }
                // Firestore'daki time alanını 1 artır
                chair1.update("time", FieldValue.increment(1))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Başarılı olduğunda yapılacak işlemler buraya yazılabilir.
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Hata durumunda yapılacak işlemler buraya yazılabilir.
                            }
                        });
            }
        }, 0, 6000); // 60 saniyede bir çalışacak şekilde ayarlandı
    }

    // Zamanlayıcıyı durduracak metod
    private void stopTimerForTimeField() {
        if (timerForTimeField != null) {
            timerForTimeField.cancel();
            timerForTimeField = null; // Timer referansını temizle
        }
    }

    // Sandalyenin kullanım durumunu güncelleyen bir metod
    public void updateChairInUse(boolean isInUse) {
        chairInUse = isInUse; // Sandalyenin kullanım durumunu güncelle
        if (!chairInUse) {
            // Sandalye artık kullanımda değilse, ilgili işlemleri burada yapabilirsiniz
        }
    }

    public void DiscardChair() {
        chair1.update("inUse", false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BasketActivity.this, "Sandalye sepetten atıldı!", Toast.LENGTH_SHORT).show();
                        timerTextView2.setVisibility(View.GONE);

                        chairInUse = false; // Sandalye artık kullanımda değil
                        stopTimerForTimeField(); // Zamanlayıcıyı durdur
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
                        BackgroundService.stopUmbrellaTimer();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BasketActivity.this, "Şemsiye sepetten atılırken hata oluştu! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }








    public void backToMain(View View) {
        Intent intent = new Intent(BasketActivity.this, MapsActivity.class);
        startActivity(intent);
    }
}