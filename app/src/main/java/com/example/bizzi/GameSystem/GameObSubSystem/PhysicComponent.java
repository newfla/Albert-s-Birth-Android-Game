package com.example.bizzi.GameSystem.GameObSubSystem;

import com.example.bizzi.GameSystem.GameWorld;
import com.google.fpl.liquidfun.Body;

public final class PhysicComponent extends Component {

    private static final int XMIN = -10,
            XMAX = 10, YMIN = -15, YMAX = 15,
            PHYSICALHEIGHT=YMAX-YMIN,
            PHYSICALWIDTH=XMAX - XMIN;
    private final Body body;

    PhysicComponent(GameObject owner, Body body,float width, float height) {
        super(ComponentType.PHYSIC, owner);
        this.body = body;
        scalePhysicToGraphic(width, height);
    }

    public void updatePosition() {
        DrawableComponent drawable = (DrawableComponent) owner.getComponent(ComponentType.DRAWABLE);
        AnimatedComponent animated = (AnimatedComponent) owner.getComponent(ComponentType.ANIMATED);
        float x=body.getPositionX(),y=body.getPositionY(),
                rotation=(float)Math.toDegrees(body.getAngle());
        x=(x-XMIN)/PHYSICALWIDTH* GameWorld.BUFFERWIDTH;
        y=(y-YMIN)/PHYSICALHEIGHT*GameWorld.BUFFERHEIGHT;


        if (drawable!=null){
            drawable.x=x;
            drawable.y=y;
            drawable.rotation=rotation;
        }

        if (animated!=null){
            animated.x=x;
            animated.y=y;
            //animated.rotation=rotation;
        }
    }

    private void scalePhysicToGraphic(float width, float height){
        DrawableComponent drawable = (DrawableComponent) owner.getComponent(ComponentType.DRAWABLE);
        AnimatedComponent animated = (AnimatedComponent) owner.getComponent(ComponentType.ANIMATED);
        width=(width/PHYSICALWIDTH*GameWorld.BUFFERWIDTH)/2;
        height=(height/PHYSICALHEIGHT*GameWorld.BUFFERHEIGHT)/2;
        if (drawable!=null){
            drawable.semiWidth=width;
            drawable.semiHeight=height;
        }

        if (animated!=null){
            animated.semiWidth=width;
            animated.semiHeight=height;
        }
    }
}
