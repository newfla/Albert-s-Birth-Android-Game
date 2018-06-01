package com.example.bizzi.GameSystem.GameObSubSystem;

import com.google.android.gms.games.Game;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.PrismaticJointDef;
import com.google.fpl.liquidfun.World;

public final class jointBuilder {




    public static void buildPrismaticDoor(GameObject ag, GameObject bg, World word, float cx, float cy){

       PhysicComponent aphysicComponent= (PhysicComponent) ag.getComponent(Component.ComponentType.PHYSIC);
        PhysicComponent bphysicComponent= (PhysicComponent) bg.getComponent(Component.ComponentType.PHYSIC);
        Body a =aphysicComponent.getBody();
        Body b =bphysicComponent.getBody();

        PrismaticJointDef jointDef = new PrismaticJointDef();
        jointDef.setBodyA(a);
        jointDef.setBodyB(b);
        jointDef.setLocalAnchorA(0, 0);
        jointDef.setLocalAnchorB(cx, cy);
        // asse
        jointDef.setLocalAxisA(1.0f,0f);
        //Limite inferiore e superiore
        jointDef.setEnableLimit(true);
        // PEr farlo aprire solo da un lato -1.5
        jointDef.setLowerTranslation(-4.5f);
        jointDef.setUpperTranslation(1.5f);

        // add friction
        jointDef.setEnableMotor(false);
        jointDef.setMotorSpeed(-20f);
        jointDef.setMaxMotorForce(-20f);
        Joint joint = word.createJoint(jointDef);
        jointDef.delete();
    }
}
