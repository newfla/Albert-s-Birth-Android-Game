package com.example.bizzi.albertBirthActivity;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.example.bizzi.GameSystem.GameFactory;
import com.example.bizzi.GameSystem.GraphicsSubSystem.GameView;
import com.example.bizzi.GameSystem.InputSubSystem.AccelerometerListener;
import com.example.bizzi.GameSystem.InputSubSystem.TouchListener;

public final class MainActivity extends AppCompatActivity{

    private GameView gameView;
    private TouchListener touchListener;
    private AccelerometerListener accelerometerListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Load jLiquidFun library
        System.loadLibrary("liquidfun");
        System.loadLibrary("liquidfun_jni");

        //Keep screen ON
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Toogle Sticky ImmersiveMode
        immersiveMode();

        //SetUp GameFactory
        GameFactory gameFactory=new GameFactory(this);

        //Build basic GameWorld;
        gameFactory.init();

        //Add TouchListener
        setTouchListener();

        gameView=new GameView(gameFactory.gameWorld,this);
        setContentView(gameView);

    }

    private void immersiveMode(){
        View decorView=getWindow().getDecorView();
        int newUiOptions=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_FULLSCREEN
                |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(newUiOptions);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stop accelerometer listner
        ((SensorManager) getSystemService(Context.SENSOR_SERVICE)).unregisterListener(accelerometerListener);

        gameView.pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    public void setTouchListener(TouchListener touchListener){
        this.touchListener=touchListener;
    }

    public void setTouchListener(){
        if (touchListener!=null)
            gameView.setOnTouchListener(touchListener);
    }

    public void setAccelerometerListener(AccelerometerListener accelerometerListner){
        this.accelerometerListener=accelerometerListner;
    }

}
