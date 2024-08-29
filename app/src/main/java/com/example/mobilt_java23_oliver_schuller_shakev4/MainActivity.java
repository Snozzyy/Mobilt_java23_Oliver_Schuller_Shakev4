package com.example.mobilt_java23_oliver_schuller_shakev4;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor, gyroscopeSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            Log.d("onCreate", "Initialize Sensor Services");
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometerSensor != null) {
                sensorManager.registerListener(MainActivity.this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                Log.d("onCreate", "Accelerometer initialized");
            } else
                Log.d("onCreate", "Accelerometer not available");

            return insets;
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        ImageView imageViewBox = findViewById(R.id.imgViewBox);
        ProgressBar progressBarLeft = findViewById(R.id.progressLeft);
        ProgressBar progressBarRight = findViewById(R.id.progressRight);


        // Log accelerometer values
        Log.d("AccelerometerValues", "Y: " + sensorEvent.values[0] + " Y: " + sensorEvent.values[1] + " Z: " + sensorEvent.values[2]);
        Log.d("BoxScale", "X: " + imageViewBox.getScaleX() + " Y: " + imageViewBox.getScaleY());

        // Scale the box based on the accelerometer y-axis value
        if (sensorEvent.values[1] > 7 && imageViewBox.getScaleX() < 3) {
            Log.d("yAxis", "Up");
            imageViewBox.setScaleX((float) (imageViewBox.getScaleX() + 0.1));
            imageViewBox.setScaleY((float) (imageViewBox.getScaleY() + 0.1));
        } else if (sensorEvent.values[1] < 3 && imageViewBox.getScaleX() > 0.1) {
            Log.d("yAxis", "Down");
            imageViewBox.setScaleX((float) (imageViewBox.getScaleX() - 0.1));
            imageViewBox.setScaleY((float) (imageViewBox.getScaleY() - 0.1));
        }

        // Move the progress bar left or right depending on the accelerometer x-axis value
        if (sensorEvent.values[0] > 5) {
            Log.d("xAxis", "Left");
            if (progressBarRight.getProgress() != 0)
                progressBarRight.setProgress(progressBarRight.getProgress() - 5);
            else
                progressBarLeft.setProgress(progressBarLeft.getProgress() + 5);
        } else if (sensorEvent.values[0] < -5){
            Log.d("xAxis", "Right");
            if (progressBarLeft.getProgress() != 0)
                progressBarLeft.setProgress(progressBarLeft.getProgress() - 5);
            else
                progressBarRight.setProgress(progressBarRight.getProgress() + 5);
        }
    }
}