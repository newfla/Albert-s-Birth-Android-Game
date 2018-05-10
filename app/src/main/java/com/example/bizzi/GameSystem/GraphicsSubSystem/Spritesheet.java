package com.example.bizzi.GameSystem.GraphicsSubSystem;

import android.graphics.Bitmap;

public final class Spritesheet {
   private final Bitmap sheet;
   private final int frameWidth, frameHeight, animations, lenght;

   Spritesheet(Bitmap sheet, int frameWidth, int frameHeight, int animantions, int lenght){
       this.sheet=sheet;
       this.frameWidth=frameWidth;
       this.frameHeight=frameHeight;
       this.animations=animantions;
       this.lenght=lenght;
   }
}
