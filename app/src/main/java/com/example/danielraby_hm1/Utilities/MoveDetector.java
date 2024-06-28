package com.example.danielraby_hm1.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.danielraby_hm1.Interfaces.MoveCallback;

public class MoveDetector {

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;

    private int moveCountX = 0;
    private int moveCountY = 0;
    private long timestamp = 0l;

    private MoveCallback moveCallback;

    public MoveDetector(Context context, MoveCallback moveCallback) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.moveCallback = moveCallback;
        initEventListener();
    }

    public int getMoveCountX() {
        return moveCountX;
    }


    private void initEventListener() {
        this.sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                calculateMove(x);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // pass
            }
        };
    }

    private void calculateMove(float x) {
        if (System.currentTimeMillis() - timestamp > 500){
            timestamp = System.currentTimeMillis();
            if (x < 6.0 && x > 0){
                moveCountX++;
                if (moveCallback != null){
                    moveCallback.moveLeft();
                }
            }
            if (  x < 0 && x > -6.0){
                if (moveCallback != null){
                  moveCallback.moveRight();
                }
            }
        }
    }


    public void start(){
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop(){
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}
