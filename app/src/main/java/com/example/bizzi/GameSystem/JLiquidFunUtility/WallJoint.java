package com.example.bizzi.GameSystem.JLiquidFunUtility;

import com.example.bizzi.GameSystem.GameObSubSystem.Component;
import com.example.bizzi.GameSystem.GameObSubSystem.GameObject;
import com.example.bizzi.GameSystem.GameObSubSystem.PhysicComponent;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.PrismaticJointDef;
import com.google.fpl.liquidfun.World;

public final class WallJoint {

    public static void buildPrismaticDoor(Body a, Body b, World word, float cx, float cy) {
        PrismaticJointDef jointDef = new PrismaticJointDef();
        jointDef.setBodyA(a);
        jointDef.setBodyB(b);
        jointDef.setLocalAnchorA(0, 0);
        jointDef.setLocalAnchorB(cx, cy);
        // asse
        jointDef.setLocalAxisA(1.0f, 0f);
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
