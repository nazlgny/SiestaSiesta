package com.example.siestasiestabitirme;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;


public class BasketActivity  extends AppCompatActivity {

    private Handler umbrellaHandler = new Handler();
    private Handler chairHandler = new Handler();
    private TextView timerTextView,timerTextView2;
    private long umbrellaTimeLeftInMillis = 0;
    private long chairTimeLeftInMillis = 0;
    private boolean umbrellaTimerRunning;
    private boolean chairTimerRunning;
    private long umbrellaStartTimeInMillis;
    private long chairStartTimeInMillis;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference chair1 = db.collection("chair").document("u6kqc3Aoz4wpSgVlxueV");
    private DocumentReference productRef2 = db.collection("umbrella").document("2tapFqBsHzLNFVsTJ63S");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);


        Button add_umbrella,add_chair,discard_chair,discard_umbrella,useQr;

        useQr = findViewById(R.id.useQr);
        add_umbrella = findViewById(R.id.add_umbrella);
        add_chair = findViewById(R.id.add_chair);
        discard_chair = findViewById(R.id.discard_chair);
        discard_umbrella = findViewById(R.id.discard_umbrella);
        timerTextView = findViewById(R.id.timer_text_view);
        timerTextView2 = findViewById(R.id.timer_text_view2);

        add_umbrella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUmbrella();
                startUmbrellaTimer();
            }



        });
        discard_umbrella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscardUmbrella();
                stopUmbrellaTimer();
            }



        });


        add_chair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddChair();
                startChairTimer();

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

    private void AddChair() {
        chair1.update("inUse", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BasketActivity.this, "Sandalye sepete eklendi!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BasketActivity.this, "Sandalye sepete eklenirken hata oluştu! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void DiscardChair() {
        chair1.update("inUse", false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BasketActivity.this, "Sandalye sepetten atıldı!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BasketActivity.this, "Sandalye sepetten atılırken hata oluştu! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void AddUmbrella() {
        productRef2.update("inUse", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BasketActivity.this, "Şemsiye sepete eklendi!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BasketActivity.this, "Şemsiye sepete eklenirken hata oluştu! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void DiscardUmbrella() {
        productRef2.update("inUse", false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BasketActivity.this, "Şemsiye sepetten atıldı!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BasketActivity.this, "Şemsiye sepetten atılırken hata oluştu! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startUmbrellaTimer() {
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

    private void startChairTimer() {
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
        }, 1000);
    }

    private void stopUmbrellaTimer() {
        umbrellaTimerRunning = false;
    }

    private void stopChairTimer() {
        chairTimerRunning = false;
    }

    private void updateUmbrellaTimerText() {
        long hours = umbrellaTimeLeftInMillis / (1000 * 60 * 60);
        long minutes = (umbrellaTimeLeftInMillis % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (umbrellaTimeLeftInMillis % (1000 * 60)) / 1000;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    private void updateChairTimerText() {
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