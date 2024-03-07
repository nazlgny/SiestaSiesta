package com.example.siestasiestabitirme;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.siestasiestabitirme.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    ImageButton btn_scan;
    ImageButton basketButton;
    private FirebaseAuth auth;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Marker istanbulMarker, istanbulWestMarker;
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); // Add this line

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v -> scanCode());
        basketButton = findViewById(R.id.basketButton);
        basketButton.setOnClickListener(v -> {
            Intent intentToBasket = new Intent(MapsActivity.this, BasketActivity.class);
            startActivity(intentToBasket);
        });
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Ürünleri kiralamak için QR kodunu tarayın. Flaşı açmak için ses yükseltme tuşuna basın.");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true); // Kameranın yönünü kilitleyin
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            handleQRResult(result.getContents());
        } else {
            new AlertDialog.Builder(MapsActivity.this)
                    .setTitle("Hata!")
                    .setMessage("QR kodu okunamadı.")
                    .setPositiveButton("OK", null)
                    .show();
        }
    });

    private void handleQRResult(String qrContent) {
        DocumentReference docRef;
        if (qrContent.equals("u6kqc3Aoz4wpSgVlxueV")) { // Assuming this is the ID for a chair
            docRef = db.collection("chair").document(qrContent);
        } else if (qrContent.equals("2tapFqBsHzLNFVsTJ63S")) { // Assuming this is the ID for an umbrella
            docRef = db.collection("umbrella").document(qrContent);
        } else {
            Toast.makeText(this, "Geçersiz QR Kodu", Toast.LENGTH_LONG).show();
            return;
        }

        docRef.update("inUse", true)
                .addOnSuccessListener(aVoid -> Toast.makeText(MapsActivity.this, "Ürün kiralama başarılı!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(MapsActivity.this, "Ürün kiralama başarısız: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Ankara'nın merkezi için koordinatlar
        LatLng ankara = new LatLng(39.9334, 32.8597);
        istanbulMarker = mMap.addMarker(new MarkerOptions()
                .position(ankara)
                .title("Marker in Ankara")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); // Yeşil işaretleyici

        // Ankara'nın batısı için biraz batıya kaydırılmış koordinatlar
        LatLng ankaraWest = new LatLng(39.9334, 32.8497);
        istanbulWestMarker = mMap.addMarker(new MarkerOptions()
                .position(ankaraWest)
                .title("Marker west of Ankara")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))); // Kırmızı işaretleyici

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ankara, 10)); // Haritayı Ankara merkezine odakla ve yakınlaştır

        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(istanbulMarker)) {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else if (marker.equals(istanbulWestMarker)) {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

        marker.showInfoWindow();

        return true;
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.profile){
            Intent intentToProfilePage = new Intent(MapsActivity.this,ProfilePage.class);
            startActivity(intentToProfilePage);


        }
        else if(item.getItemId()==R.id.signout){
            //Sign out
            auth.signOut();

            Intent intentToMain = new Intent(MapsActivity.this,MainActivity.class);
            startActivity(intentToMain);
            finish();

        }



        return super.onOptionsItemSelected(item);
    }




}