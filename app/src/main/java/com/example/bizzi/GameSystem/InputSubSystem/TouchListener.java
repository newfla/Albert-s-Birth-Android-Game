package com.example.bizzi.GameSystem.InputSubSystem;

import android.graphics.Point;
import android.support.v4.util.Pools;
import android.view.MotionEvent;
import android.view.View;

import com.example.bizzi.GameSystem.GameWorld;

final class TouchListener implements View.OnTouchListener {

    private static final int MAXPOOLSIZE = 100;
    static final Pools.SynchronizedPool<InputObject.TouchObject> POOL=new Pools.SynchronizedPool<>(MAXPOOLSIZE);
    private final float scaleX, scaleY;
    private final GameInput gameInput;

    TouchListener (GameInput gameInput, Point point){
        scaleX= GameWorld.BUFFERWIDTH/point.x;
        scaleY=GameWorld.BUFFERHEIGHT/point.y;
        this.gameInput=gameInput;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
            int action = event.getActionMasked();
            InputObject.TouchObject touchObject = POOL.acquire();
            if (touchObject == null)
                touchObject = new InputObject.TouchObject();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    touchObject.type = MotionEvent.ACTION_DOWN;
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchObject.type = MotionEvent.ACTION_MOVE;
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    touchObject.type = MotionEvent.ACTION_UP;
                    break;

                default:
                    POOL.release(touchObject);
                    return true;
            }
            touchObject.x = event.getX() * scaleX;
            touchObject.y = event.getY() * scaleY;
            synchronized (gameInput) {
                gameInput.touchBuffer.append(gameInput.touchBuffer.size(), touchObject);
            }
            return true;
        }
}
