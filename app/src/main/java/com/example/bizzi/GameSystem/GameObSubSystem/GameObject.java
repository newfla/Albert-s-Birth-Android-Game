package com.example.bizzi.GameSystem.GameObSubSystem;

import android.support.v4.util.Pools;

import com.example.bizzi.GameSystem.Utility.Recyclable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class GameObject implements Recyclable {

    public enum GameObjectType{MENU, STARTBUTTON, QUITBUTTON, MENUTITLE,SOUNDBUTTON, ENCLOSURE, SPERMATOZOON,BACKGROUND, DOOR,PILL,WALL}

    private static int count=0;

    public final int id;

    final Map<Component.ComponentType,Component> components;

    private static final Pools.Pool<GameObject> POOL=new Pools.SimplePool<>(200);

    GameObjectType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameObject that = (GameObject) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Component getComponent (Component.ComponentType type){
       return components.get(type);
    }

    void setComponent (Component component){
        components.put(component.getType(),component);
    }

    GameObject(){
        id=count++;
        components= new EnumMap<>(Component.ComponentType.class);
    }

    public GameObjectType getType() {
        return type;
    }

    public void recycle(){
        GameObBuilder.POOL.release(this);
    }
}
