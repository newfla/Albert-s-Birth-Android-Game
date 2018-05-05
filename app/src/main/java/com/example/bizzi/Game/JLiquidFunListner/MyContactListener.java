package com.example.bizzi.Game.JLiquidFunListner;

import com.example.bizzi.Game.Entity.GameObject;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.Fixture;

public final class MyContactListener extends ContactListener {
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA(),
                fb = contact.getFixtureB();
        Body ba = fa.getBody(), bb = fb.getBody();
        Object userdataA = ba.getUserData(), userdataB = bb.getUserData();
        GameObject a = (GameObject)userdataA,
                b = (GameObject)userdataB;
        // Log.d("MyContactListener", "contact bwt " + a.name + " and " + b.name);
    }
}
