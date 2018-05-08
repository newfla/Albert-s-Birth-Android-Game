package com.example.bizzi.GameSystem.InputSubSystem;

public abstract class InputObject {

    public float x=-1, y=-1;


    public abstract void recycle();


    public static final class AccelerometerObject extends InputObject{

        @Override
        public void recycle() {
           AccelerometerListener.pool.release(this);
        }
    }

    public static final class TouchObject extends InputObject{

        public int type;

        @Override
        public void recycle() {
            TouchListener.pool.release(this);
        }
    }
}
