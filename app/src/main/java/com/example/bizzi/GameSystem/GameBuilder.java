package com.example.bizzi.GameSystem;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.bizzi.GameSystem.AudioSubSystem.AudioBuilder;
import com.example.bizzi.GameSystem.GameObSubSystem.GameObBuilder;
import com.example.bizzi.GameSystem.GraphicsSubSystem.GraphicsBuilder;
import com.example.bizzi.GameSystem.InputSubSystem.InputBuilder;
import com.example.bizzi.GameSystem.JLiquidFunListener.MyContactListener;
import com.google.fpl.liquidfun.World;

public final  class GameBuilder implements Builder {
    private static final int XGRAVITY=0, YGRAVITY=0;


    private final Context context;
    private final AssetManager assetManager;
    public GameWorld gameWorld;



    public GameBuilder(Context context) {
        this.context=context;
        assetManager=context.getAssets();
    }

    @Override
    public void build(){
        if (gameWorld!=null)
            return;

        //init Physics
        World world=new World(XGRAVITY,YGRAVITY);
        world.setContactListener(new MyContactListener());

        //init Audio Game
        AudioBuilder audioFactory=new AudioBuilder(context);
        audioFactory.build();

        //init Graphics Game
        GraphicsBuilder graphicsfactory=new GraphicsBuilder(context);
        graphicsfactory.build();

        //init Input Game
        InputBuilder inputFactory=new InputBuilder(context);
        inputFactory.build();

        //init GameOB
        GameObBuilder gameObFactory=new GameObBuilder(context, world);
        gameObFactory.build();

        gameWorld=new GameWorld(world,audioFactory.gameAudio,inputFactory.gameInput,gameObFactory);



    }
}
