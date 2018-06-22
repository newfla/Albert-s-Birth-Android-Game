package com.example.bizzi.GameSystem.GameObSubSystem;

import android.graphics.Point;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.example.bizzi.GameSystem.GraphicsSubSystem.GameGraphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.EnumMap;

public final class GameObNetworking {

    private final EnumMap<GameObject.GameObjectType,SparseArray<Point>> dimensions=new EnumMap<>(GameObject.GameObjectType.class);
    private final SparseIntArray indexDimensions=new SparseIntArray();

    public byte[] serializeGameObDimensions(SparseArray<GameObject> list){

        GameObject go;
        int n=list.size(),offset=0, width, height;
        byte[] array=new byte[1+ 9*n];
        //Declare arrayOfDimensions
        array[0]=3;
        for (int i = 0; i < n; i++) {
            go=list.get(i);
            //Find GameObType
            array[++offset]=(byte) go.getType().ordinal();
            DrawableComponent drawableComponent=(DrawableComponent) go.getComponent(Component.ComponentType.DRAWABLE);
            if (drawableComponent!=null){
                width=drawableComponent.semiWidth;
                height=drawableComponent.semiHeight;
            }
            else {
                AnimatedComponent animatedComponent=(AnimatedComponent) go.getComponent(Component.ComponentType.ANIMATED);
                width=animatedComponent.semiWidth;
                height=animatedComponent.semiHeight;
            }
            ByteBuffer.wrap(array,++offset,4).order(ByteOrder.LITTLE_ENDIAN).putInt(width);
            offset+=4;
            ByteBuffer.wrap(array,offset,4).order(ByteOrder.LITTLE_ENDIAN).putInt(height);
        }
        return array;
    }

    public void deserealizeGameObDimensions(byte[]array){
        int offset=0, width, height;
        while (offset<array.length){

            //Extract info from right array portion
            GameObject.GameObjectType type= GameObject.GameObjectType.values()[++offset];
            width=ByteBuffer.wrap(array,++offset,4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            offset+=4;
            height=ByteBuffer.wrap(array,offset,4).order(ByteOrder.LITTLE_ENDIAN).getInt();

            SparseArray<Point> points=dimensions.get(type);

            //First time? Add link in Map
            if (points==null) {
                points=new SparseArray<>();
                dimensions.put(type, points);
            }

            //Fill SparseArray
            points.append(points.size(),new Point(width,height));
        }
    }

    public byte[] serializeGameObCenter(SparseArray<GameObject> list){
        GameObject go;
        int n=list.size(), offset=0, x, y;

        byte[] array=new byte[1+n*12], rotation=new byte[2];
        array[offset]=4;

        for (int i = 0; i < n; i++) {
            go=list.get(i);
            array[++offset]=(byte)go.id;
            array[++offset]=(byte)go.type.ordinal();

            DrawableComponent drawableComponent=(DrawableComponent) go.getComponent(Component.ComponentType.DRAWABLE);
            if (drawableComponent!=null){
                x=drawableComponent.x;
                y=drawableComponent.y;
                if (!(drawableComponent.rotation>90)) {
                    rotation[0] = 0;
                    rotation[1]=(byte) drawableComponent.rotation;
                }
                else if (!(drawableComponent.rotation>180)){
                    rotation[0] = 1;
                    rotation[1]=(byte) (drawableComponent.rotation - 90);
                }
                else if (!(drawableComponent.rotation>270)){
                    rotation[0] = 2;
                    rotation[1]=(byte) (drawableComponent.rotation - 180);
                }
                else if (!(drawableComponent.rotation>360)){
                    rotation[0] = 3;
                    rotation[1]=(byte) (drawableComponent.rotation - 270);
                }
            }
            else {
                AnimatedComponent animatedComponent=(AnimatedComponent) go.getComponent(Component.ComponentType.ANIMATED);
                x=animatedComponent.x;
                y=animatedComponent.y;
                rotation[0]=4;
                rotation[1]=(byte)animatedComponent.animation;
            }

            ByteBuffer.wrap(array,++offset,4).order(ByteOrder.LITTLE_ENDIAN).putInt(x);
            offset+=4;
            ByteBuffer.wrap(array,offset,4).order(ByteOrder.LITTLE_ENDIAN).putInt(y);
        }
        return array;
    }

    private void addDimensionsToGameObject(GameObject go){
        DrawableComponent drawableComponent= (DrawableComponent) go.getComponent(Component.ComponentType.DRAWABLE);
        AnimatedComponent animatedComponent=(AnimatedComponent) go.getComponent(Component.ComponentType.ANIMATED);
        SparseArray<Point> dim=dimensions.get(go.type);
        Point point;

        //Skip
        if ((animatedComponent!=null && animatedComponent.semiHeight!=0) ||
                (drawableComponent!=null && drawableComponent.semiHeight!=0))
            return;

        if (dim.size()==1)
            point=dim.get(0);
        else {
            int index= indexDimensions.get(go.type.ordinal());
            point=dim.get(index);
            indexDimensions.put(go.type.ordinal(),++index);
        }


        if (drawableComponent!=null){
            if (drawableComponent.semiWidth==0){
                drawableComponent.semiWidth=point.x;
                drawableComponent.semiHeight=point.y;
            }
        }
        else {
            if (animatedComponent.semiWidth==0){
                animatedComponent.semiWidth=point.x;
                animatedComponent.semiHeight=point.y;
            }
        }
    }

    public void deserializeGameOb(byte[] array, SparseArray<GameObject> list){
        int offset=1, x,y, n=array.length;
        GameObject go;
        while (offset<n) {
            //Find the right go
            go = list.get(array[offset]);

            //Update x-y
            x=ByteBuffer.wrap(array,offset+2,4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            y=ByteBuffer.wrap(array,offset+6,4).order(ByteOrder.LITTLE_ENDIAN).getInt();

            //First time here?
            if (go == null) {
                go = GameObject.getGameOB();
                go.id = array[offset];
                list.put(go.id,go);
                go.type = GameObject.GameObjectType.values()[array[++offset]];
                if (array[offset + 9] < 4) {
                    DrawableComponent drawableComponent = DrawableComponent.getDrawableComponent(go, GameGraphics.STATICSPRITE.get(go.type));
                    drawableComponent.rotation = array[offset + 9] * 90 + array[offset + 10];
                    go.setComponent(drawableComponent);
                } else {
                    AnimatedComponent animatedComponent = AnimatedComponent.getAnimatedComponent(go, GameGraphics.ANIMATEDSPRITE.get(go.type));
                    animatedComponent.animation = array[offset + 10];
                    go.setComponent(animatedComponent);
                }
                addDimensionsToGameObject(go);
            }

            if (array[offset + 9] < 4) {
                DrawableComponent drawableComponent = (DrawableComponent) go.getComponent(Component.ComponentType.DRAWABLE);
                drawableComponent.x =x;
                drawableComponent.y=y;
            } else {
                AnimatedComponent animatedComponent = AnimatedComponent.getAnimatedComponent(go, GameGraphics.ANIMATEDSPRITE.get(go.type));
                animatedComponent.x=x;
                animatedComponent.y=y;
            }
        }

    }


}
