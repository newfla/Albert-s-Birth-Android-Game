package com.example.bizzi.GameSystem.GameObSubSystem;

import com.example.bizzi.GameSystem.GameObSubSystem.Components.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class GameObject {

    public enum GameObjectType{}

    private static int count=0;

    public final int id;

    private final Map<Component.ComponentType,Component> components;

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

    public void recycle(){
        GameObFactory.POOL.release(this);
    }
}
