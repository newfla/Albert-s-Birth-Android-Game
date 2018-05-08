package com.example.bizzi.GameSystem.InputSubSystem;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.example.bizzi.GameSystem.Factory;
import com.example.bizzi.albertBirthActivity.MainActivity;

public final class InputFactory implements Factory{

    private AccelerometerListener accelerometerListener;

    private TouchListener touchListener;

    private final SensorManager manager;

    private final Context context;

    public final GameInput gameInput;

    public InputFactory(Context context){
        this.context=context;
        manager= (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gameInput=new GameInput();
    }

    public void init(){
        //create and register Accelerometer Listener
        accelerometerListener=new AccelerometerListener(gameInput);
        Sensor sensor=manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.registerListener(accelerometerListener,sensor,SensorManager.SENSOR_DELAY_GAME);
        //create and register Touch Listener
        touchListener=new TouchListener(gameInput);
        ((MainActivity)context).setTouchListener(touchListener);
        ((MainActivity)context).setAccelerometerListener(accelerometerListener);
    }
}
