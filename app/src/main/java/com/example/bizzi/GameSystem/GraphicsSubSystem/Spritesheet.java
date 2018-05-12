package com.example.bizzi.GameSystem.GraphicsSubSystem;

import android.graphics.Bitmap;

public final class Spritesheet {
   private final Bitmap sheet;
   private final int frameWidth, frameHeight, animations, lenght;

   Spritesheet(Bitmap sheet, int frameWidth, int frameHeight, int animations, int lenght){
       this.sheet=sheet;
       this.frameWidth=frameWidth;
       this.frameHeight=frameHeight;
       this.animations=animations;
       this.lenght=lenght;
   }

    public Bitmap getSheet() {
        return sheet;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public int getAnimations() {
        return animations;
    }

    public int getLenght() {
        return lenght;
    }
}
