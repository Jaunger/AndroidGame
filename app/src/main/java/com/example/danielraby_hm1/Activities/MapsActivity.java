package com.example.danielraby_hm1.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.danielraby_hm1.Model.Score;
import com.example.danielraby_hm1.R;
import com.example.danielraby_hm1.ScoreFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.danielraby_hm1.databinding.ActivityMapsBinding;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private ScoreFragment scoreFragment;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private AppCompatImageButton score_BTN_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FragmentManager manager = getSupportFragmentManager();
        scoreFragment = new ScoreFragment();
        manager.beginTransaction().replace(R.id.scorelist, scoreFragment).commit();
        scoreFragment.setScoreCallBack(score -> {

            LatLng location = new LatLng(score.getLat(), score.getLng());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(location).title("You played here!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));


        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = new SupportMapFragment();
        manager.beginTransaction().replace(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);

        score_BTN_return = findViewById(R.id.score_BTN_return);
        score_BTN_return.setOnClickListener( v -> returnToMenu());
    }
    private void returnToMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<Score> scores = scoreFragment.getScores();
        double lat = 0;
        double lng = 0;
        for (Score score : scores) {
            lat += score.getLat();
            lng += score.getLng();
            LatLng location = new LatLng(score.getLat(), score.getLng());
            mMap.addMarker(new MarkerOptions().position(location).title("You played here!"));
        }
        LatLng avgLocation = new LatLng(lat / scores.size(), lng / scores.size());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(avgLocation, 6f));

    }
}