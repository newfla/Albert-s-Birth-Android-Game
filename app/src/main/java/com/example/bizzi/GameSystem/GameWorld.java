package com.example.bizzi.GameSystem;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

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
            VELOCITYITERATION = 8,
            POSITIONITERATION = 3,
            PARTICLEITERATION = 3;
    private static final float TIMESTEP = 1 / 50f; //60FPS

    //Game status
    public static byte gameStatus; //0=home, 1=level, 2=endGame, 3=loadingScreen, 4=launchWaitingRoom, 5=inWaitingRoom, 6=initMultiplayer, 7=sendResult, 8=leaveRoom
    public GameObject.GameObjectType endGameType = null;

    public final Bitmap frameBuffer;
    private final Canvas canvas;

    //Physic subsystem
    private static final int XGRAVITY = 0, YGRAVITY = 0;
    private final MyContactListener myContactListener;
    private World world;

    //Audio SubSystem ref
    private final GameAudio gameAudio;
    private final AudioObject menuMusic;

    //Input SubSystem ref
    private final GameInput gameInput;

    //GameObBuilder
    private final GameObBuilder gameObFactory;

    //GameNetwork
    private final GameNetworking gameNetworking;


    //GameObject collections
    private SparseArray<GameObject> mainMenu;
    private SparseArray<GameObject> level;
    private SparseArray<GameObject> toBeRendered;

    //Audio array
    public SparseIntArray audio=new SparseIntArray();


    GameWorld(MyContactListener contactListener, GameAudio gameAudio, GameInput gameInput, GameObBuilder gameObFactory, GameNetworking gameNetworking) {
        this.gameObFactory = gameObFactory;
        this.gameAudio = gameAudio;
        this.gameInput = gameInput;
        this.gameNetworking = gameNetworking;
        gameStatus = 0;
        frameBuffer = Bitmap.createBitmap(BUFFERWIDTH, BUFFERHEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(frameBuffer);
        myContactListener = contactListener;
        myContactListener.setGameWorld(this);
        menuMusic = GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.MENU);
        gameNetworking.setGameWorld(this);
    }

    public void updateWorld() {
        gameAudio.checkAudio();
        switch (gameStatus) {
            case 0:
                menuScreen();
                break;
            case 1:
                //levelScreen(gameInput.getAccelerometerEvent());
               levelScreen();
                break;
            case 2:
                endGameScreen();
                break;
            case 3:
                waitScreen();
                break;
            case 4:
                if (level==null) {
                    level = new SparseArray<>();
                    gameNetworking.level=level;
                    toBeRendered = level;
                    gameNetworking.myAccelerometer = gameInput.getAccelerometerEvent();
                    gameNetworking.audio=audio;
                }
                gameNetworking.quickGame();
                break;
            case 6:
                initMultiplayer();
                break;
            case 7:
                sendResult();
                break;
            case 8:
                leaveRoom();
                break;
        }
    }

    private void parseAccelerometer(InputObject.AccelerometerObject[] array) {
        InputObject.AccelerometerObject accelerometer;

        //array[0] world gravity
        accelerometer = array[0];
        if (accelerometer != null)
            world.setGravity(accelerometer.y, accelerometer.x);

        //array[1] force on walls
        accelerometer = array[1];
        if (accelerometer != null) {
            for (int j = 0; j < toBeRendered.size(); j++) {
                ControllableComponent controllableComponent = (ControllableComponent) toBeRendered.get(j).getComponent(Component.ComponentType.CONTROLLABLE);
                if (controllableComponent != null)
                    controllableComponent.notifyAccelerometer(accelerometer);
            }
        }
    }

    private void parseTouch() {
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
    }

    private void updatePhysicsPosition() {
        for (int j = 0; j < toBeRendered.size(); j++) {
            PhysicComponent physicComponent = (PhysicComponent) toBeRendered.get(j).getComponent(Component.ComponentType.PHYSIC);
            if (physicComponent != null)
                physicComponent.updatePosition();
        }
    }

    public void pause() {
        gameAudio.pauseAudio();
    }

    private String casualLevel() {
        //TODO maybe one day...
        return "lv1.json";
    }

    private void endGameScreen() {
        //EndGame music should wait eggCell sound
        GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.BACKGROUND).stop();
        if (level.size() > 2) {
            AudioObject.MusicObject music=(AudioObject.MusicObject) GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.EGGCELL);
            music.play();
            while (music.isPlaying()){}
            toBeRendered = gameObFactory.buildFinish(endGameType);
            for (int i = 0; i < level.size(); i++)
                level.get(i).recycle();
            level = toBeRendered;
            GameAudio.AUDIOLIBRARY.get(endGameType).play();
        }
        parseTouch();
    }

    private void menuScreen() {
        //Game ended. Need to go back mainMenu
        if (level != null) {
            for (int i = 0; i < level.size(); i++)
                level.get(i).recycle();
            if (world!=null)
                world.delete();
            level = null;
        }
        //status=3 loadingScreen
        if (mainMenu != null && mainMenu.size() == 1) {
            for (int i = 0; i < mainMenu.size(); i++)
                mainMenu.get(i).recycle();
            mainMenu = null;
        }
        if (mainMenu == null) {
            mainMenu = gameObFactory.buildMenu();
            toBeRendered = mainMenu;
        }
        menuMusic.play();
        parseTouch();
    }

    private void waitScreen() {
        if (level != null) {
            for (int i = 0; i < level.size(); i++)
                level.get(i).recycle();
            world.delete();
            level = null;
        }
        if (mainMenu != null) {
            for (int i = 0; i < mainMenu.size(); i++)
                mainMenu.get(i).recycle();
            mainMenu = null;
        }
        GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.MENU).stop();
        mainMenu = gameObFactory.buildWait();
        toBeRendered = mainMenu;
        gameStatus = 4;
    }

    private void initMultiplayer() {
        //Clean mainMenu
        if (mainMenu != null) {
            for (int i = 0; i < mainMenu.size(); i++)
                mainMenu.get(i).recycle();
            mainMenu = null;
        }
        //Choose roles
        gameNetworking.chooseRoles();

        //Build Level in server
        if (gameNetworking.server) {
            world = new World(XGRAVITY, YGRAVITY);
            world.setContactListener(myContactListener);
            gameObFactory.setWorld(world);
            level = gameObFactory.buildLevel(casualLevel());
            toBeRendered = level;

            //Scale on FrameBuffer
            updatePhysicsPosition();
            gameNetworking.level=level;
            gameNetworking.firstSend();
            gameStatus = 1;
        }
    }

    private void levelScreen(InputObject.AccelerometerObject accelerometer) {
        if (mainMenu != null) {
            for (int i = 0; i < mainMenu.size(); i++)
                mainMenu.get(i).recycle();
            mainMenu = null;
        }
        if (level == null) {
            world = new World(XGRAVITY, YGRAVITY);
            world.setContactListener(myContactListener);
            gameObFactory.setWorld(world);
            level = gameObFactory.buildLevel(casualLevel());
            toBeRendered = level;
            GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.BACKGROUND).play();
        }
        gameObFactory.buildSpawner(toBeRendered);

        world.setGravity(accelerometer.y, accelerometer.x);
        world.step(TIMESTEP, VELOCITYITERATION, POSITIONITERATION, PARTICLEITERATION);
        updatePhysicsPosition();
    }

    private void levelScreen() {
        //Update accelerometer
        InputObject.AccelerometerObject accelerometerObject = gameInput.getAccelerometerEvent();
        gameNetworking.myAccelerometer.x = accelerometerObject.x;
        gameNetworking.myAccelerometer.y = accelerometerObject.y;

       // Log.d("Debug","L'accellerometro dell'altro "+gameNetworking.friendAccelerometer.x+gameNetworking.friendAccelerometer.y);

        if (gameNetworking.server) {
            //gameObFactory.buildSpawner(toBeRendered);
            gameNetworking.switchAccelerometer();
            parseAccelerometer(gameNetworking.accelerometers);
            world.step(TIMESTEP, VELOCITYITERATION, POSITIONITERATION, PARTICLEITERATION);
            updatePhysicsPosition();
        }
        gameNetworking.send();
    }

    private void sendResult(){
        gameNetworking.lastSend(endGameType);
        gameStatus=8;
    }

    private void leaveRoom(){
        gameStatus=2;
        gameNetworking.leaveRoom();

    }

    public void renderWorld() {

        GameObject gameObject;

        if (gameStatus < 5) {
            canvas.drawARGB(255, 0, 0, 0);
            for (int i = 0; i < toBeRendered.size(); i++) {
                gameObject = toBeRendered.get(i);
                if (gameObject!=null) {
                    DrawableComponent drawableComponent = (DrawableComponent) gameObject.getComponent(Component.ComponentType.DRAWABLE);
                    if (drawableComponent != null)
                        drawableComponent.draw(canvas);

                    AnimatedComponent animatedComponent = (AnimatedComponent) gameObject.getComponent(Component.ComponentType.ANIMATED);
                    if (animatedComponent != null)
                        animatedComponent.draw(canvas);
                }
            }
        }

    }

}
