package com.example.bizzi.GameSystem.InputSubSystem;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

final class AccelerometerListener implements SensorEventListener {

    private static final InputObject.AccelerometerObject ACCELEROMETER=new InputObject.AccelerometerObject();
    private final GameInput gameInput;

     AccelerometerListener(GameInput gameInput){ this.gameInput=gameInput;
     gameInput.accelerometerObject=ACCELEROMETER;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //TODO manipulate

        ACCELEROMETER.x=event.values[0];
        ACCELEROMETER.y=event.values[1];
        synchronized (gameInput) {
            gameInput.accelerometerObject=ACCELEROMETER;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
