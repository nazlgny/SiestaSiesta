package com.example.siestasiestabitirme;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
    }

    public void mapButtonPressed(View view){
        Intent intent = new Intent(FeedActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
    }

    }
