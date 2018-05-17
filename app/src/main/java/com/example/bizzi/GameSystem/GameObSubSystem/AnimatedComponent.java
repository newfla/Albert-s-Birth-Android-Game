package com.example.bizzi.GameSystem.GameObSubSystem;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.bizzi.GameSystem.GraphicsSubSystem.Spritesheet;

public final class AnimatedComponent extends Component {

    float x,y;
    int animation=1;
    private int lastAnimation, lastFrame;
    private final Bitmap sheet;
    final int frameWidth, frameHeight, animations, lenght;
    private final static Rect RECTSHEET=new Rect(), RECTCANVAS=new Rect();

    AnimatedComponent( GameObject owner, Spritesheet spritesheet) {
        super(ComponentType.ANIMATED, owner);
        sheet = spritesheet.getSheet();
        frameHeight = spritesheet.getFrameHeight();
        frameWidth = spritesheet.getFrameWidth();
        animations = spritesheet.getAnimations();
        lenght = spritesheet.getLenght();
    }

    //https://stackoverflow.com/questions/22589322/what-does-top-left-right-and-bottom-mean-in-android-rect-object?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

    public void draw(Canvas canvas) {
        if (animation!=lastAnimation || lastFrame+1>lenght)
            lastFrame=1;
        else
            lastFrame++;

        if (animation>animations)
            animation=1;
        //TODO check rotation

        RECTSHEET.left=1+(lastFrame-1)*frameWidth;
        RECTSHEET.top=1+(animation-1)*frameHeight;
        RECTSHEET.right=lastFrame*frameWidth;
        RECTSHEET.bottom=animation*frameHeight;

        RECTCANVAS.left=(int)x-frameWidth/2;
        RECTCANVAS.top=(int)y-frameHeight/2;
        RECTCANVAS.right=(int)x+frameWidth/2;
        RECTCANVAS.bottom=(int)y+frameHeight/2;

        canvas.drawBitmap(sheet,RECTSHEET,RECTCANVAS,null);
        lastAnimation=animation;
    }
}
