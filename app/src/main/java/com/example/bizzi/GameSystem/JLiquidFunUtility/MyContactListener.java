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

    private GameWorld gameWorld;

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void beginContact(Contact contact) {

        if (contact.getFixtureA() != null && contact.getFixtureB() != null) {

            Fixture fa = contact.getFixtureA();
            Fixture fb = contact.getFixtureB();
            Body ba = fa.getBody(), bb = fb.getBody();
            Object userdataA = ba.getUserData(), userdataB = bb.getUserData();
            GameObject a = (GameObject) userdataA,
                    b = (GameObject) userdataB;
            GameObject.GameObjectType type=null;

            //It's time to play sound effects
            if (gameWorld.gameStatus==1) {
                if ((a.getType() == GameObject.GameObjectType.EINSTEIN && (b.getType() == GameObject.GameObjectType.WALL || b.getType()== GameObject.GameObjectType.ENCLOSURE))
                        || a.getType() == GameObject.GameObjectType.SPERMATOZOON && (b.getType() == GameObject.GameObjectType.WALL || b.getType()== GameObject.GameObjectType.ENCLOSURE))
                    type=a.getType();

                else if ((b.getType() == GameObject.GameObjectType.EINSTEIN && (a.getType() == GameObject.GameObjectType.WALL || a.getType()== GameObject.GameObjectType.ENCLOSURE))
                        || b.getType() == GameObject.GameObjectType.SPERMATOZOON && (a.getType() == GameObject.GameObjectType.WALL || a.getType()== GameObject.GameObjectType.ENCLOSURE))
                    type=b.getType();
                if (type!=null) {
                     synchronized (gameWorld.audio){
                       gameWorld.audio.put(gameWorld.audio.size(),type.ordinal());
                     }
                    GameAudio.AUDIOLIBRARY.get(type).play();
                }

                if (a.getType() == GameObject.GameObjectType.EGGCELL || b.getType() == GameObject.GameObjectType.EGGCELL) {
                                        //Choose a endGameScreen
                    GameObject pretender;
                    if (b.getType() != GameObject.GameObjectType.EGGCELL)
                        pretender = b;
                    else
                        pretender = a;

                    synchronized (gameWorld.audio){
                        gameWorld.audio.put(gameWorld.audio.size(), GameObject.GameObjectType.EGGCELL.ordinal());
                    }
                    switch (pretender.getType()) {

                        case PILL:
                            //NESSUN FIGLIO;
                            //
                            //Log.d("Debug", "Nessun figlio");
                            gameWorld.endGameType = GameObject.GameObjectType.DEFEAT2;
                            break;
                        case SPERMATOZOON:
                            //Hitler
                            //
                            gameWorld.endGameType= GameObject.GameObjectType.DEFEAT1;
                            //Log.d("Debug", "Hitler");
                            break;
                        case EINSTEIN:
                            //Vittoria
                            //
                            //Log.d("Debug", "Mexicooo");
                            gameWorld.endGameType = GameObject.GameObjectType.VICTORY;
                            break;

                    }

                    //Stop future contacts
                    gameWorld.gameStatus=9;
                }
            }
        }
    }
}