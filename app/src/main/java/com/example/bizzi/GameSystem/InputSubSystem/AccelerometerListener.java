package com.example.bizzi.GameSystem.InputSubSystem;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v4.util.Pools;

public final class AccelerometerListener implements SensorEventListener {

    private static final int MAXPOOLSIZE = 100;
    static final Pools.SynchronizedPool<InputObject.AccelerometerObject> POOL=new Pools.SynchronizedPool<>(MAXPOOLSIZE);
    private final GameInput gameInput;

     AccelerometerListener(GameInput gameInput){
         this.gameInput=gameInput;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        InputObject.AccelerometerObject accelerometerObject=POOL.acquire();
        if (accelerometerObject==null)
            accelerometerObject=new InputObject.AccelerometerObject();

        //TODO manipulate

        accelerometerObject.x=event.values[0];
        accelerometerObject.y=event.values[1];
        synchronized (gameInput) {
            gameInput.accelerometerBuffer.append(gameInput.accelerometerBuffer.size(), accelerometerObject);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
