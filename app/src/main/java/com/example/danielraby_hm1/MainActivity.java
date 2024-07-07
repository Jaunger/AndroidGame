package com.example.danielraby_hm1;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

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


        initViews();
    }

    private void initViews() {
        menu_BTN_sensors.setOnClickListener(v -> type = SENSORS);
        menu_BTN_buttons.setOnClickListener(v -> type = BUTTONS);
        menu_BTN_slow.setOnClickListener(v -> Delay = 1000);
        menu_BTN_fast.setOnClickListener(v -> Delay = 500);

        menu_BTN_launch.setOnClickListener((view) -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("delay", Delay);
            startActivity(intent);
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
}