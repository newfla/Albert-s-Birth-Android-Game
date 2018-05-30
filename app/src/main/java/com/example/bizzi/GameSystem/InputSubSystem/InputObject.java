package com.example.bizzi.GameSystem.InputSubSystem;

import com.example.bizzi.GameSystem.Utility.Recyclable;

public abstract class InputObject {

    public float x=-1, y=-1;

    public static final class AccelerometerObject extends InputObject{


        AccelerometerObject(){}
    }

    public static final class TouchObject extends InputObject implements Recyclable {

        public int type;

        @Override
        public void recycle() { TouchListener.POOL.release(this);
        }

        TouchObject(){}
    }
}
