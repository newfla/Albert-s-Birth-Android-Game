package com.example.bizzi.AlbertBirthActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.bizzi.GameSystem.GameBuilder;
import com.example.bizzi.GameSystem.GameWorld;
import com.example.bizzi.GameSystem.GraphicsSubSystem.GameView;
import com.example.bizzi.GameSystem.NetworkingSubSystem.GameNetworking;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.tasks.Task;
import com.kaushikthedeveloper.doublebackpress.DoubleBackPress;
import com.kaushikthedeveloper.doublebackpress.helper.DoubleBackPressAction;
import com.kaushikthedeveloper.doublebackpress.setup.display.ToastDisplay;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import static com.example.bizzi.GameSystem.NetworkingSubSystem.GameNetworking.RCSIGNIN;


public final class MainActivity extends AppCompatActivity{



    private GameView gameView;
    private View.OnTouchListener touchListener;
    private SensorEventListener accelerometerListener;
    private GameNetworking gameNetworking;
    private GameWorld gameWorld;


    public void setGameNetworking(GameNetworking gameNetworking) {
        this.gameNetworking = gameNetworking;
    }

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

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

       startActivityForResult(GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).getSignInIntent(),RCSIGNIN);

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
        ((SensorManager) Objects.requireNonNull(getSystemService(Context.SENSOR_SERVICE))).unregisterListener(accelerometerListener);
        if (gameView!=null)
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
    protected void onStop() {
        gameNetworking.leaveRoom();
        super.onStop();
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
        assert manager != null;
        Sensor sensor=manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.registerListener(accelerometerListener,sensor,SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RCSIGNIN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account= task.getResult(ApiException.class);
            } catch (ApiException apiException) {
                Log.d("Debug","Activity signIn failure ", apiException.fillInStackTrace());
            }
        }
        else if (requestCode == GameNetworking.RCWAITINGROOM) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    gameWorld.gameStatus = 6;
                    break;
                case GamesActivityResultCodes.RESULT_LEFT_ROOM:
                    // player indicated that they want to leave the room
                    gameNetworking.leaveRoom();
                    break;
                case Activity.RESULT_CANCELED:
                    gameNetworking.leaveRoom();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
