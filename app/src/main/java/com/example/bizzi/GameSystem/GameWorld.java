package com.example.bizzi.GameSystem;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.util.ArraySet;

import com.example.bizzi.GameSystem.AudioSubSystem.GameAudio;
import com.example.bizzi.GameSystem.Entity.GameObject;
import com.example.bizzi.GameSystem.InputSubSystem.GameInput;
import com.example.bizzi.GameSystem.JLiquidFunListener.MyContactListener;
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

    //Input SubSystem ref
    private final GameInput gameInput;

    public void updateWorld(){
        //TODO physics world simulation
        //TODO handle movements/touch

    }

    public void renderWorld(){
        //TODO Update frameBuffer
    }

    public GameWorld(World world, Rect physicsSize, GameFactory gameFactory, GameAudio gameAudio, GameInput gameInput){
        this.gameAudio=gameAudio;
        this.gameInput=gameInput;
        this.gameFactory=gameFactory;
        this.world=world;
        this.physicsSize=physicsSize;
        this.contactListener=new MyContactListener();
        world.setContactListener(contactListener);
        frameBuffer=Bitmap.createBitmap(BUFFERWIDTH,BUFFERHEIGHT, Bitmap.Config.ARGB_8888);
        canvas=new Canvas(frameBuffer);
    }
}
