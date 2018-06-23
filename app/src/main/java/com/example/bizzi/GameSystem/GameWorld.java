package com.example.bizzi.GameSystem;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v4.util.ArraySet;
import android.util.SparseArray;

import com.example.bizzi.GameSystem.AudioSubSystem.AudioObject;
import com.example.bizzi.GameSystem.AudioSubSystem.GameAudio;
import com.example.bizzi.GameSystem.GameObSubSystem.AnimatedComponent;
import com.example.bizzi.GameSystem.GameObSubSystem.Component;
import com.example.bizzi.GameSystem.GameObSubSystem.ControllableComponent;
import com.example.bizzi.GameSystem.GameObSubSystem.DrawableComponent;
import com.example.bizzi.GameSystem.GameObSubSystem.GameObBuilder;
import com.example.bizzi.GameSystem.GameObSubSystem.GameObject;
import com.example.bizzi.GameSystem.GameObSubSystem.PhysicComponent;
import com.example.bizzi.GameSystem.InputSubSystem.GameInput;
import com.example.bizzi.GameSystem.InputSubSystem.InputObject;
import com.example.bizzi.GameSystem.JLiquidFunUtility.MyContactListener;
import com.example.bizzi.GameSystem.NetworkingSubSystem.GameNetworking;
import com.google.fpl.liquidfun.World;

public final class GameWorld {

    //Rendering variables
    public static final int BUFFERWIDTH = 1920,
            BUFFERHEIGHT = 1080,
            VELOCITYITERATION=8,
            POSITIONITERATION=3,
            PARTICLEITERATION=3;
    private static final float TIMESTEP = 1 / 50f; //60FPS

    //Game status
    public static byte gameStatus; //0=home, 1=level, 2=endGame, 3=loadingScreen, 4=launchWaitingRoom, 5=inWaitingRoom
    public GameObject.GameObjectType endGameType=null;

    public final Bitmap frameBuffer;
    private final Canvas canvas;

    //Physic subsystem
    private static final int XGRAVITY=0, YGRAVITY=0;
    private final MyContactListener myContactListener;
    private World world;

    //GameObjects
    private final ArraySet<GameObject> gameObjects = new ArraySet<>();


    //Audio SubSystem ref
    private final GameAudio gameAudio;
    private final AudioObject.MusicObject menuMusic;

    //Input SubSystem ref
    private final GameInput gameInput;

    //GameObBuilder
    private final GameObBuilder gameObFactory;

    //GameNetwork
    private final GameNetworking gameNetworking;

    private SparseArray<GameObject> mainMenu;
    private SparseArray<GameObject> level;
    private SparseArray<GameObject> toBeRendered;


    GameWorld(MyContactListener contactListener, GameAudio gameAudio, GameInput gameInput, GameObBuilder gameObFactory, GameNetworking gameNetworking) {
        this.gameObFactory = gameObFactory;
        this.gameAudio = gameAudio;
        this.gameInput = gameInput;
        this.gameNetworking=gameNetworking;
        gameStatus = 0;
        frameBuffer = Bitmap.createBitmap(BUFFERWIDTH, BUFFERHEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(frameBuffer);
        myContactListener=contactListener;
        myContactListener.setGameWorld(this);
        menuMusic=(AudioObject.MusicObject)GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.MENU);
    }

    public void updateWorld() {
        InputObject.AccelerometerObject accelerometer=gameInput.getAccelerometerEvent();
        gameAudio.checkAudio();
        switch (gameStatus){
            case 0:
                menuScreen();
                break;
            case 1:
                levelScreen(accelerometer);
                break;
            case 2:
                endGameScreen();
                break;
            case 3:
                waitScreen();
                break;
            case 4:
                gameNetworking.quickGame();
                break;
        }
            //Parse all touch events
            SparseArray<InputObject.TouchObject> touches = gameInput.getTouchEvents();
            for (int i = 0; i < touches.size(); i++) {
                InputObject.TouchObject touch = touches.get(i);
                if (touch != null) {
                    for (int j = 0; j < toBeRendered.size(); j++) {
                        ControllableComponent controllableComponent = (ControllableComponent) toBeRendered.get(j).getComponent(Component.ComponentType.CONTROLLABLE);
                        if (controllableComponent != null)
                            controllableComponent.notifyTouch(touch);
                    }
                }
                touch.recycle();
            }

            //Parse last accelerometer event
            if (accelerometer != null) {
                for (int j = 0; j < toBeRendered.size(); j++) {
                    ControllableComponent controllableComponent = (ControllableComponent) toBeRendered.get(j).getComponent(Component.ComponentType.CONTROLLABLE);
                    if (controllableComponent != null)
                        controllableComponent.notifyAccelerometer(accelerometer);
                }
            }
            for (int j = 0; j < toBeRendered.size(); j++) {
                PhysicComponent physicComponent = (PhysicComponent) toBeRendered.get(j).getComponent(Component.ComponentType.PHYSIC);
                if (physicComponent != null)
                    physicComponent.updatePosition();
            }
        }

    public void renderWorld() {

        GameObject gameObject;

        if (gameStatus<5) {
            canvas.drawARGB(255, 0, 0, 0);
            for (int i = 0; i < toBeRendered.size(); i++) {
                gameObject = toBeRendered.get(i);
                DrawableComponent drawableComponent = (DrawableComponent) gameObject.getComponent(Component.ComponentType.DRAWABLE);
                if (drawableComponent != null)
                    drawableComponent.draw(canvas);

                AnimatedComponent animatedComponent = (AnimatedComponent) gameObject.getComponent(Component.ComponentType.ANIMATED);
                if (animatedComponent != null)
                    animatedComponent.draw(canvas);
            }
        }

    }

    public void pause() {
        gameAudio.pauseAudio();
    }

    private String casualLevel() {
        //TODO maybe one day...
        return "lv1.json";
    }

    private void endGameScreen(){
        if (level.size()>2) {
            toBeRendered = gameObFactory.buildFinish(endGameType);
            for (int i = 0; i < level.size(); i++)
                level.get(i).recycle();
            level = toBeRendered;
            GameAudio.AUDIOLIBRARY.get(endGameType).play();
        }
    }

    private void menuScreen(){
        if (level != null) {
            for (int i = 0; i < level.size(); i++)
                level.get(i).recycle();
            world.delete();
            level=null;
        }
        if(mainMenu!=null){
            for (int i = 0; i <mainMenu.size() ; i++)
                mainMenu.get(i).recycle();

        }

        mainMenu = gameObFactory.buildMenu();
        toBeRendered = mainMenu;
        if (!menuMusic.isPlaying())
            menuMusic.play();


    }

    private void waitScreen(){
        if (level != null) {
            for (int i = 0; i < level.size(); i++)
                level.get(i).recycle();
            world.delete();
            level=null;
        }
        if (mainMenu != null) {
            for (int i = 0; i < mainMenu.size(); i++)
                mainMenu.get(i).recycle();
            mainMenu=null;
        }
        mainMenu=gameObFactory.buildWait();
        toBeRendered=mainMenu;
        gameStatus=4;
    }

    private void levelScreen(InputObject.AccelerometerObject accelerometer){
        if (mainMenu != null) {
            for (int i = 0; i < mainMenu.size(); i++)
                mainMenu.get(i).recycle();
            mainMenu = null;
        }
        if (level == null) {
            world=new World(XGRAVITY,YGRAVITY);
            world.setContactListener(myContactListener);
            gameObFactory.setWorld(world);
            gameStatus=1;
            level = gameObFactory.buildLevel(casualLevel());
            toBeRendered = level;
            GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.BACKGROUND).play();
        }
        gameObFactory.buildSpawner(toBeRendered);

        world.setGravity(accelerometer.y, accelerometer.x);
        world.step(TIMESTEP, VELOCITYITERATION, POSITIONITERATION, PARTICLEITERATION);
    }


}
