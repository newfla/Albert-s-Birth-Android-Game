package com.example.bizzi.GameSystem.GameObSubSystem;

public abstract class Component {

    public enum ComponentType{DRAWABLE, PHYSIC, ANIMATED}

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
