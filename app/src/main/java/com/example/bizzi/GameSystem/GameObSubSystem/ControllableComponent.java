package com.example.bizzi.GameSystem.GameObSubSystem;

import android.graphics.Rect;
import android.support.v4.util.Pools;
import android.util.Log;
import android.view.MotionEvent;

import com.example.bizzi.GameSystem.AudioSubSystem.AudioObject;
import com.example.bizzi.GameSystem.AudioSubSystem.GameAudio;
import com.example.bizzi.GameSystem.GameWorld;
import com.example.bizzi.GameSystem.InputSubSystem.InputObject;
import com.google.fpl.liquidfun.Vec2;

public abstract class ControllableComponent extends Component {

    private static GameWorld gameWorld;

    public static void setGameWorld(GameWorld gameWorld) {
        ControllableComponent.gameWorld = gameWorld;
    }

    private ControllableComponent(GameObject owner) {
        super(ComponentType.CONTROLLABLE, owner);
    }

    public abstract void notifyTouch(InputObject.TouchObject touch);

    public abstract void notifyAccelerometer(InputObject.AccelerometerObject accelorometer);

    public static final class ControllableWidgetComponent extends ControllableComponent {

        private static final Pools.Pool<ControllableWidgetComponent> POOL = new Pools.SimplePool<>(10);

        static ControllableComponent getControllableWidgetComponent(GameObject owner) {
            ControllableWidgetComponent object = POOL.acquire();
            if (object == null)
                object = new ControllableWidgetComponent(owner);
            else
                object.owner = owner;
            return object;
        }

        private final Rect rect = new Rect();

        private ControllableWidgetComponent(GameObject owner) {
            super(owner);
        }

        @Override
        public void notifyTouch(InputObject.TouchObject touch) {
            DrawableComponent drawable = (DrawableComponent) owner.getComponent(ComponentType.DRAWABLE);
            AnimatedComponent animated = (AnimatedComponent) owner.getComponent(ComponentType.ANIMATED);
            if (drawable != null) {
                rect.left = drawable.x - (drawable.semiWidth);
                rect.top = drawable.y - (drawable.semiHeight);
                rect.right = drawable.x + (drawable.semiWidth);
                rect.bottom = drawable.y + (drawable.semiHeight);
            } else {
                rect.left = animated.x - (animated.semiWidth);
                rect.top =  animated.y - (animated.semiHeight);
                rect.right =  animated.x + (animated.semiWidth);
                rect.bottom = animated.y + (animated.semiHeight);
            }
            if (touch.type == MotionEvent.ACTION_UP && rect.contains((int) touch.x, (int) touch.y)) {
                AudioObject audio;
                switch (owner.type) {
                    case STARTBUTTON:
                        audio = GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.STARTBUTTON);
                        if (audio != null)
                            audio.play();
                        //SinglePlayer
                        //gameWorld.gameStatus=1;

                        //Multiplayer
                        gameWorld.gameStatus=3;
                        GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.MENU).stop();
                        break;

                    case SOUNDBUTTON:
                        GameAudio.SILENCE = !GameAudio.SILENCE;
                        if (animated.animation == 2)
                            animated.animation = 1;
                        else
                            animated.animation = 2;
                        break;

                    case QUITBUTTON:
                        audio = GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.QUITBUTTON);
                        if (audio != null)
                            audio.play();
                        System.exit(0);
                        break;

                    case HOMEBUTTON:
                        audio = GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.STARTBUTTON);
                        audio.play();
                        gameWorld.gameStatus=0;
                        GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.DEFEAT1).stop();
                        GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.DEFEAT2).stop();
                        GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.VICTORY).stop();
                        GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.MENU).play();
                        break;
                }
            }
        }

        @Override
        public void notifyAccelerometer(InputObject.AccelerometerObject accelorometer) {

        }

        @Override
        public void recycle() {
            POOL.release(this);
        }
    }

    public static final class ControllableAccelerometerComponent extends ControllableComponent {

        private Vec2 vect=new Vec2();
        private PhysicComponent physicComponent;
        private static final Pools.Pool<ControllableAccelerometerComponent> POOL = new Pools.SimplePool<>(10);

        static ControllableComponent getControllableAccelorometerComponent(GameObject owner) {
            ControllableAccelerometerComponent object = POOL.acquire();
            if (object == null)
                object = new ControllableAccelerometerComponent(owner);
            else {
                object.owner = owner;
                object.physicComponent = (PhysicComponent) owner.getComponent(ComponentType.PHYSIC);
            }
            return object;
        }

        private ControllableAccelerometerComponent(GameObject owner) {
            super(owner);
            physicComponent= (PhysicComponent) owner.getComponent(ComponentType.PHYSIC);

        }

        @Override
        public void notifyTouch(InputObject.TouchObject touch) {

        }

        @Override
        public void notifyAccelerometer(InputObject.AccelerometerObject accelerometer) {
            vect.set(0,accelerometer.y/2.2f);
           // Log.d("Debug","Forza y:"+accelerometer.y);
            physicComponent.applyForce(vect);
        }

        @Override
        public void recycle() {
            POOL.release(this);
        }
    }
}
