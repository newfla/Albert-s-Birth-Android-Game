package com.example.bizzi.GameSystem.JLiquidFunUtility;

import android.util.Log;

import com.example.bizzi.GameSystem.AudioSubSystem.AudioObject;
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

            //It's time to play sound effects
            if (GameWorld.gameStatus==1) {
                if ((a.getType() == GameObject.GameObjectType.EINSTEIN && b.getType() == GameObject.GameObjectType.WALL)
                        || a.getType() == GameObject.GameObjectType.SPERMATOZOON && b.getType() == GameObject.GameObjectType.WALL)
                    GameAudio.AUDIOLIBRARY.get(a.getType()).play();

                else if ((b.getType() == GameObject.GameObjectType.EINSTEIN && a.getType() == GameObject.GameObjectType.WALL)
                        || b.getType() == GameObject.GameObjectType.SPERMATOZOON && a.getType() == GameObject.GameObjectType.WALL)
                    GameAudio.AUDIOLIBRARY.get(b.getType()).play();

                if (a.getType() == GameObject.GameObjectType.EGGCELL || b.getType() == GameObject.GameObjectType.EGGCELL) {

                   //EndGame music should wait eggCell sound
                    GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.BACKGROUND).stop();
                    AudioObject.MusicObject music=(AudioObject.MusicObject) GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.EGGCELL);
                    music.play();
                    while (music.isPlaying()){}

                    //Stop future contacts
                    GameWorld.gameStatus=2;

                    //Choose a endGameScreen
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
                            gameWorld.endGameType = GameObject.GameObjectType.DEFEAT2;
                            break;
                        case SPERMATOZOON:
                            //Hitler
                            //
                            gameWorld.endGameType= GameObject.GameObjectType.DEFEAT1;
                            Log.d("Debug", "Hitler");
                            break;
                        case EINSTEIN:
                            //Vittoria
                            //
                            Log.d("Debug", "Mexicooo");
                            gameWorld.endGameType = GameObject.GameObjectType.VICTORY;
                            break;

                    }
                }
            }
        }
    }
}