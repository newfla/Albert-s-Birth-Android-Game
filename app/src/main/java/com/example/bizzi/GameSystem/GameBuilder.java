package com.example.bizzi.GameSystem;

import com.example.bizzi.AlbertBirthActivity.MainActivity;
import com.example.bizzi.GameSystem.AudioSubSystem.AudioBuilder;
import com.example.bizzi.GameSystem.GameObSubSystem.GameObBuilder;
import com.example.bizzi.GameSystem.GraphicsSubSystem.GraphicsBuilder;
import com.example.bizzi.GameSystem.InputSubSystem.InputBuilder;
import com.example.bizzi.GameSystem.JLiquidFunUtility.MyContactListener;
import com.example.bizzi.GameSystem.NetworkingSubSystem.NetworkingBuilder;
import com.example.bizzi.GameSystem.Utility.Builder;
import com.google.fpl.liquidfun.ContactListener;

public final  class GameBuilder implements Builder {

    private final MainActivity mainActivity;
    public GameWorld gameWorld;
    private ContactListener contactListener;



    public GameBuilder(MainActivity activity) {
        mainActivity=activity;
    }

    @Override
    public void build(){
        if (gameWorld!=null)
            return;

        //init Audio Game
        AudioBuilder audioFactory=new AudioBuilder(mainActivity);
        audioFactory.build();

        //init Graphics Game
        GraphicsBuilder graphicsfactory=new GraphicsBuilder(mainActivity);
        graphicsfactory.build();

        //init Input Game
        InputBuilder inputFactory=new InputBuilder(mainActivity);
        inputFactory.build();

        //init GameOB
        GameObBuilder gameObFactory=new GameObBuilder(mainActivity);
        gameObFactory.build();

        //init multiplayer
        NetworkingBuilder networkingBuilder=new NetworkingBuilder(mainActivity);
        networkingBuilder.build();

        gameWorld=new GameWorld(new MyContactListener(),audioFactory.gameAudio,inputFactory.gameInput,gameObFactory,networkingBuilder.gameNetworking);



    }
}
