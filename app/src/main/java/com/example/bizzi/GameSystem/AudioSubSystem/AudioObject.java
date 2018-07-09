package com.example.bizzi.GameSystem.AudioSubSystem;

import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.bizzi.GameSystem.Utility.Recyclable;

import java.io.IOException;

public  abstract class AudioObject implements Recyclable {

    public abstract void play();

    public abstract void stop();

    static GameAudio gameAudio;

    public static void setGameAudio(GameAudio gameAudio) {
        AudioObject.gameAudio = gameAudio;
    }

    final static class SoundObject extends AudioObject{

        private final SoundPool soundPool;
        final int soundId;
        static final float MAXVOLUME =1;

        @Override
        public void play() {
            float volume=0;
            if (!gameAudio.mute)
                volume=MAXVOLUME;
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

    public final static class MusicObject extends AudioObject{
        final MediaPlayer player;
        static final float MAXVOLUME=0.6f;
         MusicObject(MediaPlayer player){
            this.player=player;
        }

        @Override
        public void play() {
             float volume=0;
            if (!gameAudio.mute)
                volume=MAXVOLUME;
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
