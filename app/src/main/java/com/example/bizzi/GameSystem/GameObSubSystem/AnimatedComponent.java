package com.example.bizzi.GameSystem.GameObSubSystem;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.util.Pools;

import com.example.bizzi.GameSystem.GraphicsSubSystem.Spritesheet;

public final class AnimatedComponent extends Component {

    private static final Pools.Pool<AnimatedComponent> POOL = new Pools.SimplePool<>(50);

    short x,y,semiWidth, semiHeight;
    int animation=1;
    private int lastAnimation, lastFrame;
    private Bitmap sheet;
    private short frameWidth, frameHeight, animations, lenght;
    private final static Rect RECTSHEET=new Rect(), RECTCANVAS=new Rect();

    static AnimatedComponent getAnimatedComponent(GameObject owner, Spritesheet spritesheet) {
        AnimatedComponent object = POOL.acquire();
        if (object == null)
            object = new AnimatedComponent(owner, spritesheet);
        else
            object.setAttributes(owner, spritesheet);
        return object;
    }

    private void setAttributes(GameObject owner, Spritesheet spritesheet) {
        sheet = spritesheet.getSheet();
        frameHeight =(short) spritesheet.getFrameHeight();
        frameWidth =(short) spritesheet.getFrameWidth();
        animations = (short)spritesheet.getAnimations();
        lenght = (short)spritesheet.getLenght();
        semiWidth = (short)(frameWidth / 2);
        semiHeight =(short)( frameHeight / 2);
        this.owner = owner;
    }

    private AnimatedComponent(GameObject owner, Spritesheet spritesheet) {
        super(ComponentType.ANIMATED, owner);
        setAttributes(owner, spritesheet);
    }

    //https://stackoverflow.com/questions/22589322/what-does-top-left-right-and-bottom-mean-in-android-rect-object?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

    public void draw(Canvas canvas) {
        if (animation!=lastAnimation || lastFrame+1>lenght)
            lastFrame=1;
        else
            lastFrame++;

        if (animation>animations)
            animation=1;
        buildSprite();
        buildBorder();
        canvas.drawBitmap(sheet,RECTSHEET,RECTCANVAS,null);
        lastAnimation=animation;
    }

    private void buildBorder(){
        RECTCANVAS.left = (x-semiWidth);
        RECTCANVAS.top =  (y-semiHeight);
        RECTCANVAS.right = (x + semiWidth) ;
        RECTCANVAS.bottom =  (y + semiHeight);

    }
    
    private void buildSprite(){
        RECTSHEET.left=1+(lastFrame-1)*frameWidth;
        RECTSHEET.top=1+(animation-1)*frameHeight;
        RECTSHEET.right=lastFrame*frameWidth;
        RECTSHEET.bottom=animation*frameHeight;
    }

    @Override
    public void recycle() {
        x = 0;
        y = 0;
        animation = 1;
        POOL.release(this);
    }
}
