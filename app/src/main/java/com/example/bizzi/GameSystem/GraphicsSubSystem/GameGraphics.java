package com.example.bizzi.GameSystem.GraphicsSubSystem;

import android.graphics.Bitmap;

import com.example.bizzi.GameSystem.GameObSubSystem.GameObject;

import java.util.EnumMap;
import java.util.Map;

public final class GameGraphics {

    public static final Map<GameObject.GameObjectType, Bitmap> STATICSPRITE=new EnumMap<GameObject.GameObjectType, Bitmap>(GameObject.GameObjectType.class);
    public static final Map<GameObject.GameObjectType,Spritesheet> ANIMATEDSPRITE=new EnumMap<>(GameObject.GameObjectType.class);

    GameGraphics(){}


}
