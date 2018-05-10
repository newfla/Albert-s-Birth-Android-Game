package com.example.bizzi.GameSystem.AudioSubSystem;

import android.media.MediaPlayer;
import android.media.SoundPool;

public interface AudioObject {

    void play();

    void recycle();



    final class SoundObject implements AudioObject{

        private final SoundPool soundPool;
        final int soundId;
        static float VOLUME=0.7f;

        @Override
        public void play() {
            soundPool.play(soundId,VOLUME,VOLUME,0,0,1);
        }

        @Override
        public void recycle() {
            soundPool.unload(soundId);
        }

        public SoundObject(SoundPool soundPool, int soundId){
            this.soundPool=soundPool;
            this.soundId=soundId;
        }
    }

    final class MusicObject implements AudioObject{
        final MediaPlayer player;

        public MusicObject(MediaPlayer player){
            this.player=player;
        }

        @Override
        public void play() {
            if (player.isPlaying())
                return;
            player.start();
        }

        @Override
        public void recycle() {
            if (player.isPlaying())
                player.stop();
            player.release();
        }
    }
}
