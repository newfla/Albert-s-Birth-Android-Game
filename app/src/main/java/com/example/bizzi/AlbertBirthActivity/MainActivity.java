package com.example.bizzi.AlbertBirthActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.example.bizzi.GameSystem.GameBuilder;
import com.example.bizzi.GameSystem.GraphicsSubSystem.GameView;
import com.kaushikthedeveloper.doublebackpress.DoubleBackPress;
import com.kaushikthedeveloper.doublebackpress.helper.DoubleBackPressAction;
import com.kaushikthedeveloper.doublebackpress.setup.display.ToastDisplay;


public final class MainActivity extends AppCompatActivity{

    private GameView gameView;
    private View.OnTouchListener touchListener;
    private SensorEventListener accelerometerListener;

    private DoubleBackPress doubleBackPress=new DoubleBackPress()
            .withDoublePressDuration(3000)
            .withFirstBackPressAction(new ToastDisplay().standard(this, "Press again to close"))
            .withDoubleBackPressAction(new DoubleBackPressAction() {
                @Override
                public void actionCall() {
                    finish();
                    System.exit(0);
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Load jLiquidFun library
        System.loadLibrary("liquidfun");
        System.loadLibrary("liquidfun_jni");

        //Keep screen ON
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        //SetUp GameBuilder
        GameBuilder gameFactory=new GameBuilder(this);

        //Build basic GameWorld;
        gameFactory.build();

        gameView=new GameView(gameFactory.gameWorld,this);

        //Add touchListener
        registerTouchListener();

        //Add accelerometerListener
        registerAccelerometerListener();

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
        immersiveMode();
        registerAccelerometerListener();
        gameView.resume();

    }

    @Override
    public void onBackPressed() {
        doubleBackPress.onBackPressed();
    }

    public void setTouchListener(View.OnTouchListener touchListener){
        this.touchListener=touchListener;
    }

    public void registerTouchListener(){
        if (touchListener!=null && gameView!=null)
            gameView.setOnTouchListener(touchListener);
    }

    public void setAccelerometerListener(SensorEventListener accelerometerListner){
        this.accelerometerListener=accelerometerListner;
    }

    public void registerAccelerometerListener(){
        SensorManager manager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor=manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.registerListener(accelerometerListener,sensor,SensorManager.SENSOR_DELAY_GAME);

    }

}
