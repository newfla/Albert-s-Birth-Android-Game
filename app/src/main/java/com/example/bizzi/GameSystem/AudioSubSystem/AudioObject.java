package com.example.bizzi.GameSystem.AudioSubSystem;

import android.media.MediaPlayer;
import android.media.SoundPool;

import java.io.IOException;

public interface AudioObject {

    void play();

    void recycle();

    void stop();



    final class SoundObject implements AudioObject{

        private final SoundPool soundPool;
        final int soundId;
        static float VOLUME=1;

        @Override
        public void play() {
            if (!GameAudio.SILENCE)
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

        @Override
        public void stop() {

        }
    }

    final class MusicObject implements AudioObject{
        final MediaPlayer player;
        static float VOLUME=0.6f;
        public MusicObject(MediaPlayer player){
            this.player=player;
        }

        @Override
        public void play() {
            player.setVolume(VOLUME,VOLUME);
            if (!GameAudio.SILENCE) {
                if (player.isPlaying())
                    return;
                player.start();
            }
        }

        @Override
        public void recycle() {
            if (player.isPlaying())
                player.stop();
            player.release();
        }

        @Override
        public void stop() {
            if (player.isPlaying())
                player.stop();
            try {
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
