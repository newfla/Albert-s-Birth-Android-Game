package com.example.bizzi.GameSystem.JLiquidFunUtility;

import android.util.Log;
import android.util.SparseArray;

import com.example.bizzi.GameSystem.GameObSubSystem.PhysicComponent;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.PrismaticJointDef;
import com.google.fpl.liquidfun.World;

public  final class WallJoint {
    static SparseArray<Joint> joint = new SparseArray<Joint>();
    public static void buildPrismaticDoor(Body a, Body b, World word,  float wallHeight,float thikness) {

        //a is the south wall body
        //b is the sliding Door body

        PrismaticJointDef jointDef = new PrismaticJointDef();
        jointDef.setBodyA(b);
        jointDef.setBodyB(a);
        jointDef.setLocalAnchorB(0, -(PhysicComponent.PHYSICALHEIGHT-thikness-wallHeight )/2);
        //Log.d("Debug","cx e cy :"+cx+"    "+cy);
        jointDef.setLocalAnchorA(0, wallHeight/2);

        // asse
        jointDef.setLocalAxisA(0, 1.0f);

        jointDef.setEnableLimit(true);
        if(wallHeight==2)
            jointDef.setLowerTranslation(-wallHeight*2-2);    //-wallHeight*2
        else
            jointDef.setLowerTranslation(-wallHeight*2);
        jointDef.setUpperTranslation(-wallHeight/2);    //-wallHeight/2


        jointDef.setEnableMotor(false);
        jointDef.setMotorSpeed(-20f);
        jointDef.setMaxMotorForce(-20f);
        joint.append(joint.size(),word.createJoint(jointDef));
        jointDef.delete();
    }
}
