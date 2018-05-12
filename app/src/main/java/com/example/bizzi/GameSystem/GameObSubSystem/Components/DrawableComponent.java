package com.example.bizzi.GameSystem.GameObSubSystem.Components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.bizzi.GameSystem.GameObSubSystem.GameObject;

public final class DrawableComponent extends Component{

    private final static Rect DEST=new Rect();

    private final Bitmap bitmap;


    DrawableComponent(ComponentType type, GameObject owner, Bitmap bitmap) {
        super(type, owner);
        this.bitmap=bitmap;
    }

    public void draw(Canvas canvas, int rotation,float x, float y) {
        canvas.save();
        canvas.rotate(rotation,x,y);
        new Rect();
        DEST.left=(int)x;
        DEST.top=(int)y;
        DEST.right=(int)x+bitmap.getWidth()-1;
        DEST.bottom=(int)y+bitmap.getHeight()-1;
        canvas.drawBitmap(bitmap,null,DEST,null);
        canvas.restore();
    }

}
