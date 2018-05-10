package com.example.bizzi.GameSystem.InputSubSystem;

public abstract class InputObject {

    public float x=-1, y=-1;


    public abstract void recycle();


    public static final class AccelerometerObject extends InputObject{

        @Override
        public void recycle() {
           AccelerometerListener.POOL.release(this);
        }

        AccelerometerObject(){}
    }

    public static final class TouchObject extends InputObject{

        public int type;

        @Override
        public void recycle() {
            TouchListener.POOL.release(this);
        }

        TouchObject(){}
    }
}
