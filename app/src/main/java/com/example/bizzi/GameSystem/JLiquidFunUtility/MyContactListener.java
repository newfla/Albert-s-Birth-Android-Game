package com.example.bizzi.GameSystem.JLiquidFunUtility;

import android.util.Log;

import com.example.bizzi.GameSystem.AudioSubSystem.GameAudio;
import com.example.bizzi.GameSystem.GameObSubSystem.GameObject;
import com.example.bizzi.GameSystem.GameWorld;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.Fixture;


public final class MyContactListener extends ContactListener {

    public void beginContact(Contact contact) {

        if(  contact.getFixtureA() != null && contact.getFixtureB() != null){

            Fixture fa = contact.getFixtureA();
            Fixture fb = contact.getFixtureB();
            Body ba = fa.getBody(), bb = fb.getBody();
            Object userdataA = ba.getUserData(), userdataB = bb.getUserData();
            GameObject a = (GameObject) userdataA,
                    b = (GameObject) userdataB;

           /* GameAudio.AUDIOLIBRARY.get(a).play();
                      GameAudio.AUDIOLIBRARY.get(b).play();
            */
            if (a.getType() == GameObject.GameObjectType.EGGCELL || b.getType() == GameObject.GameObjectType.EGGCELL) {
                GameObject Pretender;
                if (b.getType() != GameObject.GameObjectType.EGGCELL)
                    Pretender = b;
                else
                    Pretender = a;
                switch (Pretender.getType()) {


                    case PILL:
                        //NESSUN FIGLIO;
                        //
                        Log.d("Debug", "Nessun figlio");
                        GameWorld.finish= GameObject.GameObjectType.DEFEAT2
                    case SPERMATOZOON:
                        //Hitler
                        //
                        GameWorld.finish= GameObject.GameObjectType.DEFEAT1
                        Log.d("Debug", "Hitler");
                    case EINSTEIN:
                        //Vittoria
                        //
                        Log.d("Debug", "Mexicooo");
                        GameWorld.finish= GameObject.GameObjectType.VICTORY

                }
            }
        }
    }
}