package com.example.bizzi.GameSystem.InputSubSystem;

import android.support.v4.util.Pools;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

public final class TouchListener implements View.OnTouchListener {

    static Pools.SynchronizedPool<InputObject.TouchObject> pool;
    private final SparseArray<InputObject.TouchObject> list;

    TouchListener (GameInput gameInput){
        pool=new Pools.SynchronizedPool<>(20);
        list=gameInput.touchBuffer;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
