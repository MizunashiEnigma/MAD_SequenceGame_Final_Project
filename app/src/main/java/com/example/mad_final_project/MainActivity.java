package com.example.mad_final_project;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener
{

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private List<Button> playButtons;
    private int correctIndex;
    private boolean tiltDetected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButtons = new ArrayList<>();

        setupPlayButtons();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Toast.makeText(this, "No Accelerometer Found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupPlayButtons() {
        playButtons.add(findViewById(R.id.redButton));
        playButtons.add(findViewById(R.id.blueButton));
        playButtons.add(findViewById(R.id.greenButton));
        playButtons.add(findViewById(R.id.yellowButton));

        correctIndex = (int) (Math.random() * playButtons.size());
        highlightButton(playButtons.get(correctIndex));
    }

    private void highlightButton(Button button) {
        button.setBackgroundColor(Color.WHITE);
        button.postDelayed(() -> button.setBackgroundColor(getColorForButton(button)), 1000);
    }

    private int getColorForButton(Button button) {
        if (button == findViewById(R.id.redButton)) return Color.RED;
        if (button == findViewById(R.id.blueButton)) return Color.BLUE;
        if (button == findViewById(R.id.greenButton)) return Color.GREEN;
        if (button == findViewById(R.id.yellowButton)) return Color.YELLOW;
        return Color.GRAY;
    }

    private void checkUserInput(int inputIndex) {
        if (inputIndex == correctIndex) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            restartGame();
        } else {
            Toast.makeText(this, "Wrong Move!", Toast.LENGTH_SHORT).show();
            endGame();
        }
    }

    private void restartGame() {
        correctIndex = (int) (Math.random() * playButtons.size());
        tiltDetected = false;
        highlightButton(playButtons.get(correctIndex));
    }

    private void endGame() {
        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra("finalScore", correctIndex);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];

            if (!tiltDetected) {
                if (Math.abs(x) > Math.abs(y)) {
                    tiltDetected = true;
                    if (x < 0) {
                        checkUserInput(1); // Right Tilt (Blue)
                    } else {
                        checkUserInput(3); // Left Tilt (Yellow)
                    }
                } else {
                    tiltDetected = true;
                    if (y < 0) {
                        checkUserInput(2); // Up Tilt (Green)
                    } else {
                        checkUserInput(0); // Down Tilt (Red)
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}