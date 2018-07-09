package com.example.bizzi.GameSystem.GraphicsSubSystem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.bizzi.AlbertBirthActivity.MainActivity;
import com.example.bizzi.GameSystem.AudioSubSystem.GameAudio;
import com.example.bizzi.GameSystem.GameWorld;

import java.util.Arrays;

public final class GameView extends SurfaceView implements Runnable {

    private GameWorld game;
    private final Bitmap frameBuffer;
    private Thread renderThread;
    private final SurfaceHolder surfaceHolder;
    private volatile boolean running = false;
    private final MainActivity activity;


    public GameView(GameWorld game, MainActivity context){
        super(context);
        this.game=game;
        frameBuffer=game.frameBuffer;
        surfaceHolder=getHolder();
        activity=context;
    }

    @Override
    public void run() {
        long fpsTime = System.nanoTime(),
                frameCounter = 0,
                currentTime;

        float fpsDeltaTime;

        Rect dstRect = new Rect();
        Canvas canvas;
        while (running){
            if (!surfaceHolder.getSurface().isValid())
                continue;

            currentTime=System.nanoTime();
            fpsDeltaTime=(currentTime-fpsTime) / 1000000000f;

            game.updateWorld();
            game.renderWorld();

            canvas = surfaceHolder.lockCanvas();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(frameBuffer,null,dstRect,null);
            surfaceHolder.unlockCanvasAndPost(canvas);

            frameCounter++;
            if (fpsDeltaTime > 1) {
              //  //Log.d("GameView", "Current FPS = " + frameCounter);
                frameCounter = 0;
                fpsTime = currentTime;
            }
        }
    }

    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }

    public void pause() {
        game.pause();
        running = false;
        while(true) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
                pause();
            }
        }
    }
}
