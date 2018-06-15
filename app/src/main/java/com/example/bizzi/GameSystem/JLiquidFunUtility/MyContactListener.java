package com.example.bizzi.GameSystem.JLiquidFunUtility;

import android.util.Log;

import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;

public final class MyContactListener extends ContactListener {
    public void beginContact(Contact contact) {
    /*    Fixture fa = contact.getFixtureA(),
                fb = contact.getFixtureB();
        Body ba = fa.getBody(), bb = fb.getBody();
        Object userdataA = ba.getUserData(), userdataB = bb.getUserData();
        GameObject a = (GameObject)userdataA,
                b = (GameObject)userdataB;*/
         Log.d("", "contact bwt "); //+ a.name + " and " + b.name);
    }
}
