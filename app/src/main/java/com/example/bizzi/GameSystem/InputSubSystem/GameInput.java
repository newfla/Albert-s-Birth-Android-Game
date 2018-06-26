package com.example.bizzi.GameSystem.InputSubSystem;


import android.util.SparseArray;

public final class GameInput {

    InputObject.AccelerometerObject accelerometerObject;
    SparseArray<InputObject.TouchObject>touchBuffer;
    private SparseArray<InputObject.TouchObject> touchFront;

    GameInput(){
        touchBuffer=new SparseArray<>();
        touchFront= new SparseArray<>();
    }

    public InputObject.AccelerometerObject getAccelerometerEvent(){
        InputObject.AccelerometerObject backup;
            synchronized (this){
                backup=new InputObject.AccelerometerObject();
                backup.x=accelerometerObject.x;
                backup.y=accelerometerObject.y;

            }
            return backup;
    }

    public SparseArray<InputObject.TouchObject> getTouchEvents(){
        synchronized (this) {
            touchFront.clear();
            SparseArray<InputObject.TouchObject> temp = touchFront;
            touchFront = touchBuffer;
            touchBuffer = temp;
            return touchFront;
        }
    }
}
