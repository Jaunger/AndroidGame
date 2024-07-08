package com.example.danielraby_hm1.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.danielraby_hm1.R;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    private static final int SENSORS = 1;
    private static final int BUTTONS = 2;
    int Delay;
    int type;

    private MaterialButton menu_BTN_scoreboard; //TODO: scoreboard
    private MaterialButton menu_BTN_sensors;
    private MaterialButton menu_BTN_buttons;
    private MaterialButton menu_BTN_slow;
    private MaterialButton menu_BTN_fast;
    private MaterialButton menu_BTN_launch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        findViews();

        requestPermissions();
        initViews();
    }

    private void requestPermissions() {

            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION  };

            if (ActivityCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 999);
            }
            if (ActivityCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 998);
            }

    }

    private void initViews() {  //TODO: have to make sure something has been chosen
        menu_BTN_sensors.setOnClickListener(v -> type = SENSORS);
        menu_BTN_buttons.setOnClickListener(v -> type = BUTTONS);
        menu_BTN_slow.setOnClickListener(v -> Delay = 1000);
        menu_BTN_fast.setOnClickListener(v -> Delay = 500);

        menu_BTN_launch.setOnClickListener((view) -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("delay", Delay);
            startActivity(intent);
            finish();
        });

        menu_BTN_scoreboard.setOnClickListener((view)-> {
            Intent intent = new Intent(getApplicationContext(), ScoreboardActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void findViews() {
        menu_BTN_scoreboard = findViewById(R.id.menu_BTN_scoreboard);
        menu_BTN_sensors = findViewById(R.id.menu_BTN_sensors);
        menu_BTN_buttons = findViewById(R.id.menu_BTN_buttons);
        menu_BTN_slow = findViewById(R.id.menu_BTN_slow);
        menu_BTN_fast = findViewById(R.id.menu_BTN_fast);
        menu_BTN_launch = findViewById(R.id.menu_BTN_launch);


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 999) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 998) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}