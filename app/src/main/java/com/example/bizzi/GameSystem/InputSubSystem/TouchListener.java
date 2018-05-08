package com.example.bizzi.GameSystem.InputSubSystem;

import android.graphics.Point;
import android.support.v4.util.Pools;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import com.example.bizzi.GameSystem.GameWorld;

public final class TouchListener implements View.OnTouchListener {

    static Pools.SynchronizedPool<InputObject.TouchObject> pool;
    private final SparseArray<InputObject.TouchObject> list;
    private static final int MAXPOOLSIZE = 100;
    private final float scaleX, scaleY;

    TouchListener (GameInput gameInput, Point point){
        pool=new Pools.SynchronizedPool<>(MAXPOOLSIZE);
        list=gameInput.touchBuffer;
        scaleX= GameWorld.BUFFERWIDTH/point.x;
        scaleY=GameWorld.BUFFERHEIGHT/point.y;
    }

    @Override
    public synchronized boolean onTouch(View v, MotionEvent event) {

        int action=event.getActionMasked();
        InputObject.TouchObject touchObject=pool.acquire();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                touchObject.type=MotionEvent.ACTION_DOWN;
                break;
            case MotionEvent.ACTION_MOVE:
                touchObject.type=MotionEvent.ACTION_MOVE;
                break;
            case MotionEvent.ACTION_UP:
                touchObject.type=MotionEvent.ACTION_UP;
                break;

                default:
                    action=-1;
                    pool.release(touchObject);
                    break;
        }
        if (action>-1) {
            touchObject.x = event.getX()*scaleX;
            touchObject.y = event.getY()*scaleY;
            //TODO manipulate
            list.append(list.size(), touchObject);
        }
        return true;
    }
}
