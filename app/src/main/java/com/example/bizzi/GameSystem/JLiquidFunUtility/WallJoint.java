package com.example.bizzi.GameSystem.JLiquidFunUtility;

import android.util.SparseArray;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.PrismaticJointDef;
import com.google.fpl.liquidfun.World;

public  final class WallJoint {
    static SparseArray<Joint> joint = new SparseArray<Joint>();
    public static void buildPrismaticDoor(Body a, Body b, World word, float cx, float cy, float aHeight,float bHeight) {

        //a is sliding
        //b is sudWall
        PrismaticJointDef jointDef = new PrismaticJointDef();
        jointDef.setBodyA(a); //A is the sliding
        jointDef.setBodyB(b); //B is the south
        //  jointDef.setLocalAnchorB(0,-(PhysicComponent.PHYSICALHEIGHT-thickness-wallHeight)/2);
        //Log.d("Debug","cx e cy :"+cx+"    "+cy);
        jointDef.setLocalAnchorA(0, aHeight/2);
        jointDef.setLocalAnchorB(0,bHeight/2);

        // asse
        jointDef.setLocalAxisA(0, 1.0f);

        jointDef.setEnableLimit(true);

        jointDef.setUpperTranslation(aHeight);


        jointDef.setEnableMotor(false);
        jointDef.setMotorSpeed(-20f);
        jointDef.setMaxMotorForce(-20f);
        joint.append(joint.size(),word.createJoint(jointDef));
        jointDef.delete();
    }
}
