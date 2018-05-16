package com.example.bizzi.GameSystem.GameObSubSystem;

import com.example.bizzi.GameSystem.GameWorld;
import com.google.fpl.liquidfun.Body;

public final class PhysicComponent extends Component {

    private static final int XMIN = -10,
            XMAX = 10, YMIN = -15, YMAX = 15,
            PHYSICALHEIGHT=YMAX-YMIN,
            PHYSICALWIDTH=XMAX - XMIN;
    private static int frameHeight, frameWidth;
    private final Body body;

    public static void setFrameHeight(int frameHeight) {
        PhysicComponent.frameHeight = frameHeight;
    }

    public static void setFrameWidth(int frameWidth) {
        PhysicComponent.frameWidth = frameWidth;
    }

    PhysicComponent(GameObject owner, Body body) {
        super(ComponentType.PHYSIC, owner);
        this.body = body;
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
}
