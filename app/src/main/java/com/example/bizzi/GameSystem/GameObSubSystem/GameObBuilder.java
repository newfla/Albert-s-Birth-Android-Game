package com.example.bizzi.GameSystem.GameObSubSystem;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.Pools;
import android.util.SparseArray;

import com.example.bizzi.GameSystem.Builder;
import com.example.bizzi.GameSystem.GraphicsSubSystem.GameGraphics;
import com.example.bizzi.GameSystem.GraphicsSubSystem.Spritesheet;
import com.google.fpl.liquidfun.World;

public final class GameObBuilder implements Builder {

    private static final int MAXPOOLSIZE = 300;
    static final  Pools.SimplePool<GameObject> POOL=new Pools.SimplePool<>(MAXPOOLSIZE);
    private final Context context;
    private final World world;


    public GameObBuilder(Context context, World world){
        this.context=context;
        this.world=world;
    }

    private GameObject getGameOB(){
        GameObject object=POOL.acquire();
        if (object==null)
            object=new GameObject();
        return object;
    }

    @Override
    public void build() {
        //TODO
    }
    public SparseArray<GameObject> buildMenu(){
        SparseArray<GameObject> array=new SparseArray<>();
        GameObject gameOB;
        Bitmap bitmap;
        DrawableComponent drawable;

        //Create background_menu
        gameOB=getGameOB();
        gameOB.type= GameObject.GameObjectType.MENU;
        bitmap=GameGraphics.STATICSPRITE.get(gameOB.type);
        drawable=new DrawableComponent(gameOB, bitmap );
        drawable.x=1920/2;
        drawable.y=1080/2;
        gameOB.components.put(drawable.getType(),drawable);
        array.append(array.size(),gameOB);

        //Create menuTitle
        gameOB=getGameOB();
        gameOB.type= GameObject.GameObjectType.MENUTITLE;
        bitmap=GameGraphics.STATICSPRITE.get(gameOB.type);
        drawable=new DrawableComponent(gameOB, bitmap);
        drawable.x=1920/2;
        drawable.y=1080/2+40;
        gameOB.components.put(drawable.getType(),drawable);
        array.append(array.size(),gameOB);
        float previousY=drawable.y+bitmap.getHeight()/2;

        //Create startButton
        gameOB=getGameOB();
        gameOB.type= GameObject.GameObjectType.STARTBUTTON;
        bitmap=GameGraphics.STATICSPRITE.get(gameOB.type);
        drawable=new DrawableComponent(gameOB, bitmap);
        drawable.x=1920/2;
        drawable.y=previousY+ 70;
        gameOB.components.put(drawable.getType(),drawable);
        array.append(array.size(),gameOB);
        previousY=drawable.y;


        //Create quitButton
        gameOB=getGameOB();
        gameOB.type= GameObject.GameObjectType.QUITBUTTON;
        bitmap=GameGraphics.STATICSPRITE.get(gameOB.type);
        drawable=new DrawableComponent(gameOB, bitmap);
        drawable.x=1920/2;
        drawable.y=previousY+ bitmap.getHeight()+70;
        gameOB.components.put(drawable.getType(),drawable);
        array.append(array.size(),gameOB);

        //Create soundButton
        gameOB=getGameOB();
        gameOB.type= GameObject.GameObjectType.SOUNDBUTTON;
        Spritesheet spritesheet=GameGraphics.ANIMATEDSPRITE.get(gameOB.type);
        AnimatedComponent animated=new AnimatedComponent(gameOB,spritesheet);
        animated.x=6.5f*1920/8;
        animated.y=1080/10;
        gameOB.components.put(animated.getType(),animated);
        array.append(array.size(),gameOB);


        //TODO build Menu Object
        return array;
    }
    //TODO factory object methods

}
