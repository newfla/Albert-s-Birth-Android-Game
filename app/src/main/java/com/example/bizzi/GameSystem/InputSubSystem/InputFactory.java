package com.example.bizzi.GameSystem.InputSubSystem;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.WindowManager;

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
        WindowManager windowManager= (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Point point=new Point();
        windowManager.getDefaultDisplay().getSize(point);
        touchListener=new TouchListener(gameInput,point);
        ((MainActivity)context).setTouchListener(touchListener);
        ((MainActivity)context).setAccelerometerListener(accelerometerListener);
    }
}
