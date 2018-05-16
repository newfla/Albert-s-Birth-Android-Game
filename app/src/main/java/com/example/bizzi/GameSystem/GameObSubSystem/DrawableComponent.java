package com.example.bizzi.GameSystem.GameObSubSystem;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public final class DrawableComponent extends Component {

    float x, y, rotation;

    private final static Rect DEST = new Rect();

    private final Bitmap bitmap;


    DrawableComponent(GameObject owner, Bitmap bitmap) {
        super(ComponentType.DRAWABLE, owner);
        this.bitmap = bitmap;
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(rotation, x, y);
        DEST.left = (int) x-(bitmap.getWidth()/2);
        DEST.top = (int) y-(bitmap.getHeight()/2);
        DEST.right = (int) x + (bitmap.getWidth()/2) ;
        DEST.bottom = (int) y + (bitmap.getHeight()/2) ;
        canvas.drawBitmap(bitmap, null, DEST, null);
        canvas.restore();
    }
}
