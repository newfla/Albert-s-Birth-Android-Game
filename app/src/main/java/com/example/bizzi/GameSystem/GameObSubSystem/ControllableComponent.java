package com.example.bizzi.GameSystem.GameObSubSystem;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import com.example.bizzi.GameSystem.AudioSubSystem.AudioObject;
import com.example.bizzi.GameSystem.AudioSubSystem.GameAudio;
import com.example.bizzi.GameSystem.InputSubSystem.InputObject;

public final class ControllableComponent extends Component{

    private final Rect rect=new Rect();

    ControllableComponent(GameObject owner) {
        super(ComponentType.CONTROLLABLE, owner);
    }

    public void notifyTouch(InputObject.TouchObject touch){
        if (owner.type== GameObject.GameObjectType.STARTBUTTON)
            Log.d("Debug","In gameObject event type: "+String.valueOf(touch.type));
        DrawableComponent drawable=(DrawableComponent) owner.getComponent(ComponentType.DRAWABLE);
        AnimatedComponent animated=(AnimatedComponent) owner.getComponent(ComponentType.ANIMATED);
        if (drawable!=null) {
            rect.left = (int) drawable.x - (drawable.bitmap.getWidth() / 2);
            rect.top = (int) drawable.y - (drawable.bitmap.getHeight() / 2);
            rect.right = (int) drawable.x + (drawable.bitmap.getWidth() / 2);
            rect.bottom = (int) drawable.y + (drawable.bitmap.getHeight() / 2);
        }
        else {
            rect.left = (int) animated.x - (animated.frameWidth/ 2);
            rect.top = (int) animated.y - (animated.frameHeight/ 2);
            rect.right = (int) animated.x + (animated.frameWidth / 2);
            rect.bottom = (int) animated.y + (animated.frameHeight / 2);
        }
        if (touch.type==MotionEvent.ACTION_UP && rect.contains((int)touch.x, (int)touch.y)) {
            AudioObject audio;
            switch (owner.type) {
                case STARTBUTTON:
                    audio=GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.STARTBUTTON);
                    if (audio != null)
                        audio.play();
                    break;

                case SOUNDBUTTON:
                    GameAudio.SILENCE=!GameAudio.SILENCE;
                    if (animated.animation==2)
                        animated.animation=1;
                    else
                        animated.animation=2;
                    break;

                case QUITBUTTON:
                    audio = GameAudio.AUDIOLIBRARY.get(GameObject.GameObjectType.QUITBUTTON);
                    if (audio != null)
                        audio.play();
                    System.exit(0);
                    break;


            }
        }
    }

    public void notifyAccelerometer(){

    }
}
