package com.example.bizzi.GameSystem.InputSubSystem;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v4.util.Pools;
import android.util.SparseArray;

public final class AccelerometerListener implements SensorEventListener {

    static Pools.SynchronizedPool<InputObject.AccelerometerObject> pool;
    private final SparseArray<InputObject.AccelerometerObject> list;
    private static final int MAXPOOLSIZE = 100;

     AccelerometerListener(GameInput gameInput){
        pool=new Pools.SynchronizedPool<>(MAXPOOLSIZE);
        list=gameInput.accelerometerBuffer;
    }

    @Override
    public synchronized void onSensorChanged(SensorEvent event) {
        InputObject.AccelerometerObject accelerometerObject=pool.acquire();

        //TODO manipulate

        accelerometerObject.x=event.values[0];
        accelerometerObject.y=event.values[1];
        accelerometerObject.manipulate();
        list.append(list.size(),accelerometerObject);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
