package com.example.bizzi.Game.Entity;

import com.example.bizzi.Game.Components.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public abstract class GameObject {

    public enum GameObjectType{}

    private static int count=0;

    public final int id=count+1;

    public final Map<Component.ComponentType,Component> components= new EnumMap<>(Component.ComponentType.class);

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
}
