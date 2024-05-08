package com.example.siestasiestabitirme;

import android.view.View;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

//import com.google.firebase.firestore.core.View;


public class PaymentSuccessfulActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successful);

        // Feedback butonuna tıklama dinleyicisi ekleme
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFeedback();
            }
        });
    }

    // Feedback Activity'e geçiş yapan metod
    private void sendFeedback() {
        Intent intentToFeed = new Intent(PaymentSuccessfulActivity.this, FeedbackActivity.class);
        startActivity(intentToFeed);
    }

    public void mainPageClicked(View view) {

        Intent intentToMain = new Intent(PaymentSuccessfulActivity.this, MainActivity.class);
        startActivity(intentToMain);
    }
}
   /* public void mainPageClicked(View view){

        Intent intentToMain = new Intent(PaymentSuccessfulActivity.this,MainActivity.class);
        startActivity(intentToMain);

    }
    public void sendFeedbackClicked(View view){

        Intent intentToFeed = new Intent(PaymentSuccessfulActivity.this,FeedbackActivity.class);
        startActivity(intentToFeed);

    }
*/
