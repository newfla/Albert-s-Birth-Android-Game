package com.example.bizzi.GameSystem.AudioSubSystem;

import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.bizzi.GameSystem.Utility.Recyclable;

import java.io.IOException;

public interface AudioObject extends Recyclable {

    void play();

    void stop();



    final class SoundObject implements AudioObject{

        private final SoundPool soundPool;
        final int soundId;
        static float VOLUME=1;
        private float volume;
        @Override
        public void play() {
            if (!GameAudio.SILENCE)
                volume=VOLUME;
            else
                volume=0;
            soundPool.play(soundId,volume,volume,0,0,1);
        }

        @Override
        public void recycle() {
            soundPool.unload(soundId);
        }

        SoundObject(SoundPool soundPool, int soundId){
            this.soundPool=soundPool;
            this.soundId=soundId;
        }

        @Override
        public void stop() {
            soundPool.stop(soundId);
        }
    }

    final class MusicObject implements AudioObject{
        final MediaPlayer player;
        static float VOLUME=0.6f;
        private float volume;
         MusicObject(MediaPlayer player){
            this.player=player;
        }

        @Override
        public void play() {
            if (!GameAudio.SILENCE)
                volume=VOLUME;
            else
                volume=0;
            player.setVolume(volume,volume);
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

        @Override
        public void stop() {
            if(player.isPlaying()) {
                player.stop();

                try {
                    player.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalStateException ex) {
                    stop();
                }
            }
        }

        public boolean isPlaying(){
            return player.isPlaying();
        }
    }
}
