package com.example.danielraby_hm1.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.danielraby_hm1.Model.Score;
import com.example.danielraby_hm1.R;
import com.example.danielraby_hm1.ScoreFragment;
import com.example.danielraby_hm1.databinding.ActivityScoreboardBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ScoreboardActivity extends FragmentActivity implements OnMapReadyCallback {
    //TODO: add location to game manager

    private ScoreFragment scoreFragment;
    private ActivityScoreboardBinding binding;
    private GoogleMap playerLocation;
    private boolean isMapReady = false;

    private MaterialButton score_BTN_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FragmentManager manager = getSupportFragmentManager();
        scoreFragment = new ScoreFragment();
        manager.beginTransaction().replace(R.id.scorelist, scoreFragment).commit();
        scoreFragment.setScoreCallBack(score -> {
            if(isMapReady) {
                LatLng location = new LatLng(score.getLat(), score.getLng());
                playerLocation.clear();
                playerLocation.addMarker(new MarkerOptions().position(location).title("You played here!"));
                playerLocation.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));

            }
        });


        SupportMapFragment mapFragment = new SupportMapFragment();
        manager.beginTransaction().replace(R.id.map, mapFragment).commit();


        score_BTN_return = findViewById(R.id.score_BTN_return);
        score_BTN_return.setOnClickListener( v -> returnToMenu());

    }

    private void returnToMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        playerLocation = googleMap;
        isMapReady = true;
        Log.d("ScoreboardActivity", "onMapReady: ");
        ArrayList<Score> scores = scoreFragment.getScores();
        double lat = 0;
        double lng = 0;
        for (Score score : scores) {
            lat += score.getLat();
            lng += score.getLng();
            LatLng location = new LatLng(score.getLat(), score.getLng());
            playerLocation.addMarker(new MarkerOptions().position(location).title("You played here!"));
        }
        LatLng avgLocation = new LatLng(lat / scores.size(), lng / scores.size());
        playerLocation.moveCamera(CameraUpdateFactory.newLatLngZoom(avgLocation, 6f));

    }
}