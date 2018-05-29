package com.example.bizzi.GameSystem.GameObSubSystem;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;

public class DrawableComponent extends Component {

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

    public final static class PaintDrawableComponent extends DrawableComponent {

        public static Paint getWallPaint(){
            Paint paint=new Paint();
            paint.setARGB(255,255, 139, 255);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(50);
            return paint;
        }

        private final Paint paint;
        private final BitmapShader bitmapShader;

        PaintDrawableComponent(GameObject owner, Bitmap bitmap) {
            super(owner, bitmap);
            paint=new Paint();
            bitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            paint.setShader(bitmapShader);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawRect(DEST.left,DEST.top, DEST.right, DEST.bottom, paint);
        }
    }


}
