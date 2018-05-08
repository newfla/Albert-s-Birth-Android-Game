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
        SparseArray<InputObject.AccelerometerObject> temp=acceloremeterFront;
        acceloremeterFront=accelerometerBuffer;
        accelerometerBuffer=temp;
        return acceloremeterFront;
    }

    public synchronized SparseArray<InputObject.TouchObject> getTouchEvents(){
        touchFront.clear();
        SparseArray<InputObject.TouchObject> temp=touchFront;
        touchFront=touchBuffer;
        touchBuffer=temp;
        return touchFront;
    }
}
