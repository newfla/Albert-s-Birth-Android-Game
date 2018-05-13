package com.example.bizzi.GameSystem.GameObSubSystem;

import android.content.Context;
import android.support.v4.util.Pools;

import com.example.bizzi.GameSystem.Factory;
import com.google.fpl.liquidfun.World;

public final class GameObFactory implements Factory{

    private static final int MAXPOOLSIZE = 100;
    static final  Pools.SimplePool<GameObject> POOL=new Pools.SimplePool<>(MAXPOOLSIZE);
    private final Context context;
    private final World world;


    public GameObFactory(Context context, World world){
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
    public void init() {
        //TODO
    }

    //TODO factory object methods

}
