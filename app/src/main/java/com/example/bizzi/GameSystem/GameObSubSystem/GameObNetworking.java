package com.example.bizzi.GameSystem.GameObSubSystem;

import android.graphics.Point;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.example.bizzi.GameSystem.GraphicsSubSystem.GameGraphics;
import com.example.bizzi.GameSystem.Utility.Recyclable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.EnumMap;

public final class GameObNetworking implements Recyclable {

    private final EnumMap<GameObject.GameObjectType,SparseArray<Point>> dimensions=new EnumMap<>(GameObject.GameObjectType.class);

    private final SparseArray<GameObject> tempList=new SparseArray<>();

    private final SparseIntArray indexDimensions=new SparseIntArray();

    private boolean ready=false;

    public byte[] serializeGameObDimensions(SparseArray<GameObject> list){
        tempList.clear();
        GameObject go;
        int n=list.size(),offset=0;
        short width, height;

        //Declare arrayOfDimensions
        byte[] array=new byte[1+ 5*n];
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
            ByteBuffer.wrap(array,++offset,2).order(ByteOrder.BIG_ENDIAN).putShort(width);
            offset+=2;
            ByteBuffer.wrap(array,offset,2).order(ByteOrder.BIG_ENDIAN).putShort(height);
            offset++;
        }
        return array;
    }

    public void deserealizeGameObDimensions(byte[]array){
        tempList.clear();
        int offset=0, width, height, n=array.length-1;
        while (offset<n){
            GameObject.GameObjectType type= GameObject.GameObjectType.values()[array[++offset]];
            width=ByteBuffer.wrap(array,++offset,2).order(ByteOrder.BIG_ENDIAN).getShort();
            offset+=2;
            height=ByteBuffer.wrap(array,offset,2).order(ByteOrder.BIG_ENDIAN).getShort();
            offset++;
            SparseArray<Point> points=dimensions.get(type);

            //First time? Add link in Map
            if (points==null) {
                points=new SparseArray<>();
                dimensions.put(type, points);
            }

            //Fill SparseArray
            points.append(points.size(),new Point(width,height));
        }
        ready=true;
    }

    public byte[] serializeGameObCenter(SparseArray<GameObject> list){
        GameObject go;
        int n=list.size(), offset=0;
        short x, y;

        byte[] array=new byte[1+n*8], rotation=new byte[2];
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
                if(go.type== GameObject.GameObjectType.EINSTEIN)
                    Log.d("Debug","Rotation :"+ rotation[0]*90+rotation[1]);
            }
            else {
                AnimatedComponent animatedComponent=(AnimatedComponent) go.getComponent(Component.ComponentType.ANIMATED);
                x=animatedComponent.x;
                y=animatedComponent.y;
                rotation[0]=4;
                rotation[1]=(byte)animatedComponent.animation;
            }
            array[++offset]=rotation[0];
            array[++offset]=rotation[1];
            ByteBuffer.wrap(array,++offset,2).order(ByteOrder.BIG_ENDIAN).putShort(x);
            offset+=2;
            ByteBuffer.wrap(array,offset,2).order(ByteOrder.BIG_ENDIAN).putShort(y);
            offset++;
        }
        return array;
    }

    private void addDimensionsToGameObject(GameObject go){
        DrawableComponent drawableComponent= (DrawableComponent) go.getComponent(Component.ComponentType.DRAWABLE);
        AnimatedComponent animatedComponent=(AnimatedComponent) go.getComponent(Component.ComponentType.ANIMATED);
        SparseArray<Point> dim=dimensions.get(go.type);
        Point point;

        if (dim.size()==1)
            point=dim.get(0);
        else {
            int index= indexDimensions.get(go.type.ordinal());
            point=dim.get(index);
            indexDimensions.put(go.type.ordinal(),++index);
        }
     //   Log.d("Debug","tipo: "+go.type+" Width: "+point.x+" Heigth: "+point.y);


        if (drawableComponent!=null){
                drawableComponent.semiWidth=(short)point.x;
                drawableComponent.semiHeight=(short)point.y;
        }
        else {
                animatedComponent.semiWidth=(short)point.x;
                animatedComponent.semiHeight=(short) point.y;
        }
    }

    public void deserializeGameOb(byte[] array, SparseArray<GameObject> list){
        int offset=1, n=array.length, id, type;
        short x,y;
        int[] rotation=new int[2];
        GameObject go;
        while (offset<n) {
            //Find the right go
            id=array[offset];
            go = tempList.get(id);
            type=array[++offset];
            rotation[0]=array[++offset];
            rotation[1]=array[++offset];
            //Update x-y
            x=ByteBuffer.wrap(array,++offset,2).order(ByteOrder.BIG_ENDIAN).getShort();
            offset+=2;
            y=ByteBuffer.wrap(array,offset,2).order(ByteOrder.BIG_ENDIAN).getShort();
            offset+=2;

            while(!ready){}

            //First time here?
            if (go == null) {
                go = GameObject.getGameOB();
                go.id = id;
                tempList.put(go.id,go);
                list.append(list.size(),go);
                go.type = GameObject.GameObjectType.values()[type];
                if (rotation[0] < 4) {
                    DrawableComponent drawableComponent;
                    if(go.type == GameObject.GameObjectType.ENCLOSURE||go.type == GameObject.GameObjectType.WALL||go.type== GameObject.GameObjectType.DOOR)
                        drawableComponent = DrawableComponent.PaintDrawableComponent.getPaintDrawableComponent(go, GameGraphics.STATICSPRITE.get(go.type));
                    else
                        drawableComponent = DrawableComponent.getDrawableComponent(go, GameGraphics.STATICSPRITE.get(go.type));

                    drawableComponent.rotation =(short)( rotation[0] * 90 + rotation[1]);
                    go.setComponent(drawableComponent);
                    drawableComponent.x =x;
                    drawableComponent.y=y;
                    if (go.type== GameObject.GameObjectType.EINSTEIN)
                        Log.d("Debug","Rotation: "+rotation);
                }
                else {
                    AnimatedComponent animatedComponent = AnimatedComponent.getAnimatedComponent(go, GameGraphics.ANIMATEDSPRITE.get(go.type));
                    animatedComponent.animation = rotation[1];
                    go.setComponent(animatedComponent);
                    animatedComponent.x=x;
                    animatedComponent.y=y;
                }
                addDimensionsToGameObject(go);
            }
            else {
                if (rotation[0] < 4) {
                    DrawableComponent drawableComponent=(DrawableComponent) go.getComponent(Component.ComponentType.DRAWABLE);
                    drawableComponent.rotation = (short)(rotation[0] * 90 + rotation[1]);
                    go.setComponent(drawableComponent);
                    drawableComponent.x =x;
                    drawableComponent.y=y;
                }
                else {
                    AnimatedComponent animatedComponent =(AnimatedComponent) go.getComponent(Component.ComponentType.ANIMATED);
                    animatedComponent.animation = rotation[1];
                    go.setComponent(animatedComponent);
                    animatedComponent.x=x;
                    animatedComponent.y=y;
                }
            }
        }

    }


    @Override
    public void recycle() {
        dimensions.clear();
        tempList.clear();
        indexDimensions.clear();
        ready=false;
    }
}
