package com.example.bizzi.GameSystem;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.bizzi.GameSystem.AudioSubSystem.AudioFactory;
import com.example.bizzi.GameSystem.GameObSubSystem.GameObFactory;
import com.example.bizzi.GameSystem.GraphicsSubSystem.GraphicsFactory;
import com.example.bizzi.GameSystem.InputSubSystem.InputFactory;
import com.example.bizzi.GameSystem.JLiquidFunListener.MyContactListener;
import com.google.fpl.liquidfun.World;

public final  class GameFactory implements Factory {
    private static final int XMIN = -10, XMAX = 10, YMIN = -15, YMAX = 15,
                            XGRAVITY=0, YGRAVITY=0;


    private final Context context;
    private final AssetManager assetManager;
    public GameWorld gameWorld;



    public GameFactory(Context context) {
        this.context=context;
        assetManager=context.getAssets();
    }

    @Override
    public void init(){
        if (gameWorld!=null)
            return;

        World world=new World(XGRAVITY,YGRAVITY);
        world.setContactListener(new MyContactListener());

        //Used to store xmin,ymin,xmax,ymin NOT FOR CANVAS
        Rect physicsSize=new Rect(XMIN,YMIN,XMAX,YMAX);

        //init Audio Game
        AudioFactory audioFactory=new AudioFactory(context);
        audioFactory.init();

        //init Graphics Game
        GraphicsFactory graphicsfactory=new GraphicsFactory(context);
        graphicsfactory.init();

        //init Input Game
        InputFactory inputFactory=new InputFactory(context);
        inputFactory.init();

        //init GameOB
        GameObFactory gameObFactory=new GameObFactory(context);
        gameObFactory.init();

        gameWorld=new GameWorld(world,physicsSize,audioFactory.gameAudio,graphicsfactory.gameGraphics,inputFactory.gameInput,gameObFactory);


        //TODO create mainScreen

        //TODO start RealGame

    }


    private Rect buildTouchScaleSize(){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager= (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return new Rect((XMAX-XMIN)/metrics.widthPixels,(YMAX-YMIN)/metrics.heightPixels,XMIN,YMIN);
    }
}
