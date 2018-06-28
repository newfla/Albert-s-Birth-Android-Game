package com.example.bizzi.GameSystem.GameObSubSystem;

import android.support.v4.util.Pools;

import com.example.bizzi.GameSystem.GameWorld;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Vec2;

public final class PhysicComponent extends Component {

    private static final Pools.Pool<PhysicComponent> POOL = new Pools.SimplePool<>(100);

    public static final int XMIN = -15,
            XMAX = 15, YMIN =-10, YMAX = 10,
            PHYSICALHEIGHT=YMAX-YMIN,
            PHYSICALWIDTH=XMAX - XMIN;

    private Body body;

    static PhysicComponent getPhysicComponent(GameObject owner, Body body, float width, float height) {
        PhysicComponent object = POOL.acquire();
        if (object == null)
            object = new PhysicComponent(owner, body, width, height);
        else {
            object.owner = owner;
            object.scalePhysicToGraphic(body, width, height);
        }
        return object;
    }

    private PhysicComponent(GameObject owner, Body body, float width, float height) {
        super(ComponentType.PHYSIC, owner);
        scalePhysicToGraphic(body, width, height);
    }

    Body getBody(){
        return this.body;
    }

    public void updatePosition() {
        DrawableComponent drawable = (DrawableComponent) owner.getComponent(ComponentType.DRAWABLE);
        AnimatedComponent animated = (AnimatedComponent) owner.getComponent(ComponentType.ANIMATED);
        float x=body.getPositionX(),y=body.getPositionY(),
                rotation=(float)Math.toDegrees(body.getAngle());
        x=(x-XMIN)/PHYSICALWIDTH*GameWorld.BUFFERWIDTH;
        y=(y-YMIN)/PHYSICALHEIGHT*GameWorld.BUFFERHEIGHT;
        if (drawable!=null){
            drawable.x=(short)x;
            drawable.y=(short)y;
            drawable.rotation=(short)rotation;
        }

        if (animated!=null){
            animated.x=(short)x;
            animated.y=(short) y;
           // animated.rotation=(int)rotation;
        }
    }

    private void scalePhysicToGraphic(Body body, float width, float height) {
        this.body = body;
        DrawableComponent drawable = (DrawableComponent) owner.getComponent(ComponentType.DRAWABLE);
        AnimatedComponent animated = (AnimatedComponent) owner.getComponent(ComponentType.ANIMATED);
        width=(width/PHYSICALWIDTH*GameWorld.BUFFERWIDTH)/2;
        height=(height/PHYSICALHEIGHT*GameWorld.BUFFERHEIGHT)/2;
        if (drawable!=null){
            drawable.semiWidth=(short) width;
            drawable.semiHeight=(short)height;
        }

        if (animated!=null){
            animated.semiWidth=(short) width;
            animated.semiHeight=(short)height;
        }
    }

    void applyForce(Vec2 vect){
        body.setLinearVelocity(vect);
    }

    @Override
    public void recycle() {
        POOL.release(this);
    }
}
