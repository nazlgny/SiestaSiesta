package com.example.siestasiestabitirme;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    ImageButton btn_scan;
    ImageButton basketButton;
    public FirebaseAuth auth;
    public GoogleMap mMap;
    public ActivityMapsBinding binding;
    public Marker istanbulMarker;
    public Marker istanbulWestMarker;
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v -> {
            scanCode();
        });

        basketButton = findViewById(R.id.basketButton); // Layout'unuzda bu ID'ye sahip bir ImageButton veya Button olduğundan emin olun
        basketButton.setOnClickListener(v -> {
            // BasketActivity'e yönlendir
            Intent intentToBasket = new Intent(MapsActivity.this, BasketActivity.class);
            startActivity(intentToBasket);
        });
    }

    public void navigateToBasketActivity(String scanResult) {
        Intent intent = new Intent(MapsActivity.this, BasketActivity.class);
        intent.putExtra("scanResult", scanResult);
        startActivity(intent);
    }

    public void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan QR to rent products / Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            // QR kodundan elde edilen ID'yi kullanarak Firestore'daki ilgili dokümanı güncelle
            if (result.getContents().equals("u6kqc3Aoz4wpSgVlxueV")) {
                navigateToBasketActivity(result.getContents());
            } else if (result.getContents().equals("2tapFqBsHzLNFVsTJ63S")) {
                navigateToBasketActivity(result.getContents());
            }
        } else {
            // QR kodu okunamadı veya içerik boş ise
            new AlertDialog.Builder(MapsActivity.this)
                    .setTitle("Hata!")
                    .setMessage("QR kodu okunamadı.")
                    .setPositiveButton("OK", null)
                    .show();
        }
    });

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Ankara'nın merkezi için koordinatlar
        LatLng ankara = new LatLng(39.894244, 32.862910);
        istanbulMarker = mMap.addMarker(new MarkerOptions()
                .position(ankara)
                .title("Seğmenler Park")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); // Yeşil işaretleyici

        // Ankara'nın batısı için biraz batıya kaydırılmış koordinatlar
        LatLng ankaraWest = new LatLng(41.170266, 29.599960);
        istanbulWestMarker = mMap.addMarker(new MarkerOptions()
                .position(ankaraWest)
                .title("Serintepe Kamp ALanı")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))); // Kırmızı işaretleyici

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ankara, 10)); // Haritayı Ankara merkezine odakla ve yakınlaştır

        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(istanbulMarker)) {
            showAvailableChairs(marker);
        } else if (marker.equals(istanbulWestMarker)) {
            showAvailableChairs(marker);
        }

        marker.showInfoWindow();
        return true;
    }

    private void showAvailableChairs(Marker marker) {
        // Firestore'dan kullanılabilir sandalyelerin sayısını almak
        db.collection("chair")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int availableChairs = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Long number = document.getLong("number");
                            if (number != null) {
                                availableChairs += number;
                            }
                        }
                        Toast.makeText(this, "Available Chair Number: " + availableChairs, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Couldn't find the state of chairs..", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile) {
            Intent intentToProfilePage = new Intent(MapsActivity.this, ProfilePage.class);
            startActivity(intentToProfilePage);
        } else if (item.getItemId() == R.id.signout) {
            //Sign out
            auth.signOut();
            Intent intentToMain = new Intent(MapsActivity.this, MainActivity.class);
            startActivity(intentToMain);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}