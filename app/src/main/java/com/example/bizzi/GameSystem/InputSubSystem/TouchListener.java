package com.example.bizzi.GameSystem.InputSubSystem;

import android.graphics.Point;
import android.support.v4.util.Pools;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import com.example.bizzi.GameSystem.GameWorld;

public final class TouchListener implements View.OnTouchListener {

    private static final int MAXPOOLSIZE = 100;
    static final Pools.SynchronizedPool<InputObject.TouchObject> POOL=new Pools.SynchronizedPool<>(MAXPOOLSIZE);
    private final SparseArray<InputObject.TouchObject> list;
    private final float scaleX, scaleY;

    TouchListener (GameInput gameInput, Point point){
        list=gameInput.touchBuffer;
        scaleX= GameWorld.BUFFERWIDTH/point.x;
        scaleY=GameWorld.BUFFERHEIGHT/point.y;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("Debug", "captured in onTouch: "+String.valueOf(event.getAction()));
        int action=event.getActionMasked();
        InputObject.TouchObject touchObject=POOL.acquire();
        if (touchObject==null)
            touchObject=new InputObject.TouchObject();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                touchObject.type=MotionEvent.ACTION_DOWN;
                break;
            case MotionEvent.ACTION_MOVE:
                touchObject.type=MotionEvent.ACTION_MOVE;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                touchObject.type=MotionEvent.ACTION_UP;
                break;

                default:
                    action=-1;
                    POOL.release(touchObject);
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
