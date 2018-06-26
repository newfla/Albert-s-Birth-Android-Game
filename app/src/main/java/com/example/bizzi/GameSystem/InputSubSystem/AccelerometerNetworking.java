package com.example.bizzi.GameSystem.InputSubSystem;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class AccelerometerNetworking {

    public byte[] serializeAccelerometer(InputObject.AccelerometerObject accelerometer){
        byte[] array=new byte[9];
        array[0]=0;
        ByteBuffer.wrap(array,1,4).order(ByteOrder.LITTLE_ENDIAN).putFloat(accelerometer.x);
        ByteBuffer.wrap(array,5,4).order(ByteOrder.LITTLE_ENDIAN).putFloat(accelerometer.y);
        return array;
    }

    public InputObject.AccelerometerObject deserializeAccelerometer(byte[] array){
        InputObject.AccelerometerObject accelerometer=new InputObject.AccelerometerObject();
        accelerometer.x=ByteBuffer.wrap(array,1,4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        accelerometer.y=ByteBuffer.wrap(array,5,4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        return accelerometer;
    }
}
