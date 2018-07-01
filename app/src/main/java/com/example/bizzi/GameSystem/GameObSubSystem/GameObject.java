package com.example.bizzi.GameSystem.GameObSubSystem;

import android.support.v4.util.Pools;

import com.example.bizzi.GameSystem.Utility.Recyclable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class GameObject implements Recyclable {

    public enum GameObjectType{MENU, EINSTEIN, STARTBUTTON, QUITBUTTON, MENUTITLE,SOUNDBUTTON,
        ENCLOSURE, SPERMATOZOON, BACKGROUND, DOOR, PILL, WALL,
        EGGCELL, VICTORY, DEFEAT1, DEFEAT2, HOMEBUTTON, WAIT, SCHEMA, SCHEMAWORLD, SCHEMAWALL}

    private static int count=0;

    public int id;

    final Map<Component.ComponentType,Component> components;

    private static final Pools.Pool<GameObject> POOL=new Pools.SimplePool<>(100);

    GameObjectType type;

    static GameObject getGameOB() {
        GameObject object = POOL.acquire();
        if (object == null)
            object = new GameObject();
        return object;
    }

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

    private GameObject(){
        id=count++;
        components= new EnumMap<>(Component.ComponentType.class);
    }

    public GameObjectType getType() {
        return type;
    }

    public void recycle(){
        for (Component component : components.values())
            component.recycle();
        components.clear();
        POOL.release(this);
    }
}
