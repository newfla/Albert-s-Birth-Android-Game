package com.example.bizzi.Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.util.ArraySet;

import com.example.bizzi.Game.AudioSubSystem.GameAudio;
import com.example.bizzi.Game.Entity.GameObject;
import com.example.bizzi.Game.JLiquidFunListner.MyContactListener;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.World;

public final class GameWorld {

    //Rendering variables
    private static final int BUFFERWIDTH=1920,
                                BUFFERHEIGHT=1080;
    public final Bitmap frameBuffer;
    private final Canvas canvas;

    //Physics variables
    private final ContactListener contactListener;
    private final World world;
    private static final float TIMESTEP=1/50f; //60FPS
    private final Rect physicsSize;

    //GameObjects
    private final ArraySet<GameObject> gameObjects=new ArraySet<>();

    //Father ref
    private final GameFactory gameFactory;

    //Audio SubSystem ref
    private final GameAudio gameAudio;

    public void updateWorld(){
        //TODO physics world simulation
        //TODO handle movements/touch

    }

    public void renderWorld(){
        //TODO Update frameBuffer
    }

    public GameWorld(World world, Rect physicsSize, GameFactory gameFactory, GameAudio gameAudio){
        this.gameAudio=gameAudio;
        this.gameFactory=gameFactory;
        this.world=world;
        this.physicsSize=physicsSize;
        this.contactListener=new MyContactListener();
        world.setContactListener(contactListener);
        frameBuffer=Bitmap.createBitmap(BUFFERWIDTH,BUFFERHEIGHT, Bitmap.Config.ARGB_8888);
        canvas=new Canvas(frameBuffer);
    }
}
