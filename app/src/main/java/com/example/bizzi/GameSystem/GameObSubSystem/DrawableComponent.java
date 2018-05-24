package com.example.bizzi.GameSystem.GameObSubSystem;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public final class DrawableComponent extends Component {

    float x, y, rotation, semiWidth, semiHeight;

    private final static Rect DEST=new Rect();

    final Bitmap bitmap;

    DrawableComponent(GameObject owner, Bitmap bitmap) {
        super(ComponentType.DRAWABLE, owner);
        this.bitmap = bitmap;
        semiWidth=bitmap.getWidth()/2;
        semiHeight=bitmap.getHeight()/2;
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(rotation, x, y);
        buildBorder();
        canvas.drawBitmap(bitmap, null, DEST, null);
        canvas.restore();
    }
    
    private void buildBorder(){
        DEST.left = (int) (x-semiWidth);
        DEST.top = (int) (y-semiHeight);
        DEST.right = (int) (x + semiWidth) ;
        DEST.bottom = (int) (y + semiHeight);
        
    }
}
