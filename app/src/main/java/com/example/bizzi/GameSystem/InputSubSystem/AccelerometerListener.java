package com.example.bizzi.GameSystem.InputSubSystem;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v4.util.Pools;
import android.util.SparseArray;

public final class AccelerometerListener implements SensorEventListener {

    private static final int MAXPOOLSIZE = 100;
    static final Pools.SynchronizedPool<InputObject.AccelerometerObject> POOL=new Pools.SynchronizedPool<>(MAXPOOLSIZE);
    private final SparseArray<InputObject.AccelerometerObject> list;

     AccelerometerListener(GameInput gameInput){

        list=gameInput.accelerometerBuffer;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        InputObject.AccelerometerObject accelerometerObject=POOL.acquire();
        if (accelerometerObject==null)
            accelerometerObject=new InputObject.AccelerometerObject();

        //TODO manipulate

        accelerometerObject.x=event.values[0];
        accelerometerObject.y=event.values[1];
        list.append(list.size(),accelerometerObject);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
