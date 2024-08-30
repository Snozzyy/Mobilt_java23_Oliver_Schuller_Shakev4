package com.example.mobilt_java23_oliver_schuller_shakev4;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor, gyroscopeSensor;
    private float xAxis, yAxis, zAxis;
    Switch bgSwitch;
    private boolean isSwitchOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            View decorView = getWindow().getDecorView();
            decorView.setBackgroundColor(Color.DKGRAY);

            Log.d("onCreate", "Initialize Sensor Services");
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometerSensor != null) {
                sensorManager.registerListener(MainActivity.this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                Log.d("onCreate", "Accelerometer initialized");
            } else
                Log.d("onCreate", "Accelerometer not available");

            bgSwitch = findViewById(R.id.bgSwitch);

            // Listen for changes in the switch and change the background color accordingly
            bgSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked) {
                        decorView.setBackgroundColor(Color.RED);
                        isSwitchOn = true;
                    }
                    else {
                        decorView.setBackgroundColor(Color.DKGRAY);
                        isSwitchOn = false;
                    }
                }
            });

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
        TextView xAxisText = findViewById(R.id.xAxisText);
        TextView yAxisText = findViewById(R.id.yAxisText);
        TextView zAxisText = findViewById(R.id.zAxisText);

        xAxis = sensorEvent.values[0];
        yAxis = sensorEvent.values[1];
        zAxis = sensorEvent.values[2];

        // Update the text views with the accelerometer values
        xAxisText.setText("X: " + xAxis);
        yAxisText.setText("Y: " + yAxis);
        zAxisText.setText("Z: " + zAxis);

        // Log accelerometer values
        Log.d("AccelerometerValues", "Y: " + xAxis + " Y: " + yAxis + " Z: " + zAxis);

        // Move the progress bar left or right depending on the accelerometer x-axis value
        if (xAxis > 5) {
            Log.d("xAxis", "Left " + xAxis);
            if (progressBarRight.getProgress() != 0)
                progressBarRight.setProgress(progressBarRight.getProgress() - 5);
            else
                progressBarLeft.setProgress(progressBarLeft.getProgress() + 5);

        } else if (xAxis < -5){
            Log.d("xAxis", "Right " + xAxis);
            if (progressBarLeft.getProgress() != 0)
                progressBarLeft.setProgress(progressBarLeft.getProgress() - 5);
            else
                progressBarRight.setProgress(progressBarRight.getProgress() + 5);
        }

        // Scale the box based on the accelerometer y-axis value
        if (yAxis > 7 && imageViewBox.getScaleX() < 2.5) {
            Log.d("yAxis", "Up " + yAxis);
            imageViewBox.setScaleX((float) (imageViewBox.getScaleX() + 0.1));
            imageViewBox.setScaleY((float) (imageViewBox.getScaleY() + 0.1));
        } else if (yAxis < 3 && imageViewBox.getScaleX() > 0.1) {
            Log.d("yAxis", "Down " + yAxis);
            imageViewBox.setScaleX((float) (imageViewBox.getScaleX() - 0.1));
            imageViewBox.setScaleY((float) (imageViewBox.getScaleY() - 0.1));
        }

        // Change the switch when meeting the z-value threshold
        if (zAxis > 38 || zAxis < -38) {
            Log.d("zAxis", "Shake " + zAxis);
            isSwitchOn = !isSwitchOn;
            bgSwitch.setChecked(isSwitchOn);
        }
    }
}