package com.example.bizzi.GameSystem.GameObSubSystem;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.v4.util.Pools;

public class DrawableComponent extends Component {

    private static final Pools.Pool<DrawableComponent> POOL = new Pools.SimplePool<>(50);

    short x, y, rotation, semiWidth, semiHeight;

    private final static Rect DEST=new Rect();

    private Bitmap bitmap;

    static DrawableComponent getDrawableComponent(GameObject owner, Bitmap bitmap) {
        DrawableComponent object = POOL.acquire();
        if (object == null)
            object = new DrawableComponent(owner, bitmap);
        else
            object.setAttributes(owner, bitmap);
        return object;
    }

    private void setAttributes(GameObject owner, Bitmap bitmap) {
        this.bitmap = bitmap;
        semiWidth = (short)(bitmap.getWidth() / 2);
        semiHeight = (short)(bitmap.getHeight() / 2);
        this.owner = owner;
    }

    private DrawableComponent(GameObject owner, Bitmap bitmap) {
        super(ComponentType.DRAWABLE, owner);
        setAttributes(owner, bitmap);
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(rotation, x, y);
        buildBorder();
        canvas.drawBitmap(bitmap, null, DEST, null);
        canvas.restore();
    }
    
    private void buildBorder(){
        DEST.left = x-semiWidth;
        DEST.top = y-semiHeight;
        DEST.right = x + semiWidth;
        DEST.bottom = y + semiHeight;
        
    }

    void cleanAttributes() {
        x = 0;
        y = 0;
        rotation = 0;
    }

    @Override
    public void recycle() {
        cleanAttributes();
        POOL.release(this);
    }

    public final static class PaintDrawableComponent extends DrawableComponent {

        private static final Pools.Pool<PaintDrawableComponent> POOL = new Pools.SimplePool<>(25);

        private final Paint paint;
        private BitmapShader bitmapShader;


        static DrawableComponent getPaintDrawableComponent(GameObject owner, Bitmap bitmap) {
            PaintDrawableComponent object = POOL.acquire();
            if (object == null)
                object = new PaintDrawableComponent(owner, bitmap);
            else {
                ((DrawableComponent) object).setAttributes(owner, bitmap);
                object.setShader(bitmap);
            }
            return object;
        }

        private void setShader(Bitmap bitmap) {
            bitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            paint.setShader(bitmapShader);
        }

        public static Paint getWallPaint(){
            Paint paint=new Paint();
            paint.setARGB(255,255, 139, 255);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(50);
            return paint;
        }

        private PaintDrawableComponent(GameObject owner, Bitmap bitmap) {
            super(owner, bitmap);
            paint=new Paint();
            setShader(bitmap);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.save();
            canvas.rotate(rotation, x, y);
            super.buildBorder();
            canvas.drawRect(DEST.left,DEST.top, DEST.right, DEST.bottom, paint);
            canvas.restore();
        }

        @Override
        public void recycle() {
            cleanAttributes();
            POOL.release(this);
        }
    }


}
