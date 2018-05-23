package com.example.bizzi.GameSystem.InputSubSystem;


import android.util.SparseArray;

public final class GameInput {

    SparseArray<InputObject.AccelerometerObject> accelerometerBuffer,acceloremeterFront;
    SparseArray<InputObject.TouchObject>touchBuffer, touchFront;

    GameInput(){
        accelerometerBuffer=new SparseArray<>();
        touchBuffer=new SparseArray<>();
        touchFront= new SparseArray<>();
        acceloremeterFront=new SparseArray<>();
    }

    public synchronized SparseArray<InputObject.AccelerometerObject> getAccelerometerEvents(){
            acceloremeterFront.clear();
            SparseArray<InputObject.AccelerometerObject> temp = acceloremeterFront;
            acceloremeterFront = accelerometerBuffer;
            accelerometerBuffer = temp;
            return acceloremeterFront;
    }

    public SparseArray<InputObject.TouchObject> getTouchEvents(){
        synchronized (this) {
            for (int i = 0; i < touchFront.size(); i++)
                touchFront.get(i).recycle();
            touchFront.clear();
            SparseArray<InputObject.TouchObject> temp = touchFront;
            touchFront = touchBuffer;
            touchBuffer = temp;
            return touchFront;
        }
    }
}
