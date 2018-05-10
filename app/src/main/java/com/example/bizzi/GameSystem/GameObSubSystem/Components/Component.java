package com.example.bizzi.GameSystem.GameObSubSystem.Components;

import com.example.bizzi.GameSystem.GameObSubSystem.GameObject;

public abstract class Component {

    public enum ComponentType{}

    protected GameObject owner;

    private final ComponentType type;

    Component (ComponentType type){
        this.type=type;
    }

    public ComponentType getType() {
        return type;
    }
}
