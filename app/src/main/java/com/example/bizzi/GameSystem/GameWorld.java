package com.example.bizzi.GameSystem;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.util.ArraySet;
import android.util.SparseArray;

import com.example.bizzi.GameSystem.AudioSubSystem.GameAudio;
import com.example.bizzi.GameSystem.GameObSubSystem.Components.Component;
import com.example.bizzi.GameSystem.GameObSubSystem.Components.DrawableComponent;
import com.example.bizzi.GameSystem.GameObSubSystem.GameObFactory;
import com.example.bizzi.GameSystem.GameObSubSystem.GameObject;
import com.example.bizzi.GameSystem.GraphicsSubSystem.GameGraphics;
import com.example.bizzi.GameSystem.InputSubSystem.GameInput;
import com.google.fpl.liquidfun.World;

public final class GameWorld {

    //Rendering variables
    public static final int BUFFERWIDTH=1920,
                                BUFFERHEIGHT=1080;
    public final Bitmap frameBuffer;
    private final Canvas canvas;
    private static boolean home;
    private final World world;
    private static final float TIMESTEP=1/50f; //60FPS

    //GameObjects
    private final ArraySet<GameObject> gameObjects=new ArraySet<>();


    //Audio SubSystem ref
    private final GameAudio gameAudio;

    //Graphics SubSystem ref
    private final GameGraphics gameGraphics;

    //Input SubSystem ref
    private final GameInput gameInput;

    //GameObFactory
    private final GameObFactory gameObFactory;

    private SparseArray<GameObject> mainMenu;


    public void updateWorld(){
        //TODO handle movements/touch
        if(home==true) {
            if (mainMenu == null)
                mainMenu=gameObFactory.buildMenu();
            //TODO create mainScreen
        }
        else{
            //TODO start RealGame
            //TODO physics world simulation

        }
    }

    public void renderWorld(){
        canvas.drawARGB(255, 0, 0, 0);
        if(home==true){
            for (int i = 0; i < mainMenu.size(); i++) {
                GameObject gameObject= mainMenu.get(i);
                DrawableComponent drawableComponent=( DrawableComponent)gameObject.getComponent(Component.ComponentType.DRAWABLE);
                drawableComponent.draw(canvas);
            }
        }
        else {
            //TODO Update frameBuffer
        }



    }

    GameWorld(World world, GameAudio gameAudio, GameGraphics gameGraphics,GameInput gameInput, GameObFactory gameObFactory){
        this.gameObFactory=gameObFactory;
        this.gameAudio=gameAudio;
        this.gameGraphics=gameGraphics;
        this.gameInput=gameInput;
        this.world=world;
        home=true;
        frameBuffer=Bitmap.createBitmap(BUFFERWIDTH,BUFFERHEIGHT, Bitmap.Config.ARGB_8888);
        canvas=new Canvas(frameBuffer);
    }

    public void pause(){
        gameAudio.mute();
    }
}
