package com.example.bizzi.GameSystem.InputSubSystem;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.WindowManager;

import com.example.bizzi.AlbertBirthActivity.MainActivity;
import com.example.bizzi.GameSystem.Factory;

public final class InputFactory implements Factory{

    private AccelerometerListener accelerometerListener;

    private TouchListener touchListener;



    private final Context context;

    public final GameInput gameInput;

    public InputFactory(Context context){
        this.context=context;

        gameInput=new GameInput();
    }

    public void init(){
        //create and register Accelerometer Listener
        accelerometerListener=new AccelerometerListener(gameInput);
        ((MainActivity)context).setAccelerometerListener(accelerometerListener);


        //create and register Touch Listener
        WindowManager windowManager= (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Point point=new Point();
        windowManager.getDefaultDisplay().getSize(point);
        touchListener=new TouchListener(gameInput,point);
        ((MainActivity)context).setTouchListener(touchListener);
    }
}
