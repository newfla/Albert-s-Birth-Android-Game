package com.example.bizzi.GameSystem;

import android.content.Context;

import com.example.bizzi.GameSystem.AudioSubSystem.AudioBuilder;
import com.example.bizzi.GameSystem.GameObSubSystem.GameObBuilder;
import com.example.bizzi.GameSystem.GraphicsSubSystem.GraphicsBuilder;
import com.example.bizzi.GameSystem.InputSubSystem.InputBuilder;
import com.example.bizzi.GameSystem.JLiquidFunUtility.MyContactListener;
import com.example.bizzi.GameSystem.NetworkingSubSystem.NetworkingBuilder;
import com.example.bizzi.GameSystem.Utility.Builder;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.World;

public final  class GameBuilder implements Builder {
    private static final int XGRAVITY=0, YGRAVITY=0;


    private final Context context;
    public GameWorld gameWorld;
    private ContactListener contactListener;



    public GameBuilder(Context context) {
        this.context=context;
    }

    @Override
    public void build(){
        if (gameWorld!=null)
            return;

        //init Physics
        World world=new World(XGRAVITY,YGRAVITY);
        contactListener= new MyContactListener();
        world.setContactListener(contactListener);

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

        //init multiplayer
        NetworkingBuilder networkingBuilder=new NetworkingBuilder(context);
       // networkingBuilder.build();

        gameWorld=new GameWorld(world,audioFactory.gameAudio,inputFactory.gameInput,gameObFactory,networkingBuilder.gameNetworking);



    }
}
