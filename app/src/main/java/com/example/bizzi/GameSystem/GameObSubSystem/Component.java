package com.example.bizzi.GameSystem.GameObSubSystem;

import com.example.bizzi.GameSystem.Utility.Recyclable;

public abstract class Component implements Recyclable {

    public enum ComponentType{DRAWABLE, PHYSIC, ANIMATED, CONTROLLABLE}

    protected GameObject owner;

    private final ComponentType type;

    Component (ComponentType type,GameObject owner){
        this.type=type;
        this.owner=owner;
    }


    public ComponentType getType() {
        return type;
    }
}
