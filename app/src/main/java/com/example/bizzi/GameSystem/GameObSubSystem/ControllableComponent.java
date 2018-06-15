package com.example.bizzi.GameSystem.GameObSubSystem;

import android.graphics.Rect;
import android.support.v4.util.Pools;
import android.view.MotionEvent;

import com.example.bizzi.GameSystem.AudioSubSystem.AudioObject;
import com.example.bizzi.GameSystem.AudioSubSystem.GameAudio;
import com.example.bizzi.GameSystem.GameWorld;
import com.example.bizzi.GameSystem.InputSubSystem.InputObject;

public abstract class ControllableComponent extends Component {

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
                rect.left = (int) drawable.x - (drawable.bitmap.getWidth() / 2);
                rect.top = (int) drawable.y - (drawable.bitmap.getHeight() / 2);
                rect.right = (int) drawable.x + (drawable.bitmap.getWidth() / 2);
                rect.bottom = (int) drawable.y + (drawable.bitmap.getHeight() / 2);
            } else {
                rect.left = (int) animated.x - (animated.frameWidth / 2);
                rect.top = (int) animated.y - (animated.frameHeight / 2);
                rect.right = (int) animated.x + (animated.frameWidth / 2);
                rect.bottom = (int) animated.y + (animated.frameHeight / 2);
            }
            if (touch.type == MotionEvent.ACTION_UP && rect.contains((int) touch.x, (int) touch.y)) {
                AudioObject audio;
                switch (owner.type) {
                    case STARTBUTTON:
                        audio = GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.STARTBUTTON);
                        if (audio != null)
                            audio.play();
                        GameWorld.home=false;
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
                        GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.BACKGROUND).stop();
                        audio = GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.STARTBUTTON);
                        if (audio != null);
                            audio.play();
                        GameWorld.home=true;
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

        private static final Pools.Pool<ControllableAccelerometerComponent> POOL = new Pools.SimplePool<>(10);

        static ControllableComponent getControllableWidgetComponent(GameObject owner) {
            ControllableAccelerometerComponent object = POOL.acquire();
            if (object == null)
                object = new ControllableAccelerometerComponent(owner);
            else
                object.owner = owner;
            return object;
        }

        private ControllableAccelerometerComponent(GameObject owner) {
            super(owner);
        }

        @Override
        public void notifyTouch(InputObject.TouchObject touch) {

        }

        @Override
        public void notifyAccelerometer(InputObject.AccelerometerObject accelorometer) {

        }

        @Override
        public void recycle() {
            POOL.release(this);
        }
    }
}
