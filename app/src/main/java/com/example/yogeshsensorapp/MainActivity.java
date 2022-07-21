package com.example.yogeshsensorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;

    private static final String TAG = "sensorActivity";

    private long lastProcessedTime = 0;
    private static final long processingInterval = 5000;

    private boolean darkMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // process sensorEvent.

        Sensor sensor = sensorEvent.sensor;

        if (sensor.getType() == Sensor.TYPE_LIGHT) {
            float illuminance = sensorEvent.values[0];

            // control the frequency of changes - based on last processed time

            long currentTime = System.currentTimeMillis();

            if (currentTime - lastProcessedTime > processingInterval) {
                lastProcessedTime = currentTime;
                Log.i(TAG, "event received, illuminance: " + illuminance);

                // if illuminance is below 5000, swap the background and foreground

                if (illuminance < 3000 && !darkMode) {
                    // make the rootView dark, and text light
                    // DARK MODE
                    findViewById(R.id.rootView).setBackgroundColor(Color.BLACK);
                    ((TextView) findViewById(R.id.textView)).setTextColor(Color.WHITE);
                    darkMode = true;
                } else if (illuminance >= 3000 && darkMode) {
                    // LIGHT MODE
                    findViewById(R.id.rootView).setBackgroundColor(Color.WHITE);
                    ((TextView) findViewById(R.id.textView)).setTextColor(Color.BLACK);
                    darkMode = false;
                }
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // homework - to understand this.
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
