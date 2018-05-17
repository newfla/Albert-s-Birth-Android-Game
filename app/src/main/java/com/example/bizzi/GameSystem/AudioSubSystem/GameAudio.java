package com.example.bizzi.GameSystem.AudioSubSystem;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.example.bizzi.GameSystem.GameObSubSystem.GameObject;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public final class GameAudio {

    //Map GO to Sound
    public static final Map<GameObject.GameObjectType,AudioObject> AUDIOLIBRARY=new EnumMap<>(GameObject.GameObjectType.class);
    public static boolean SILENCE=false;

    private boolean lastSilence=false;
    private final SoundPool soundPool;
    private final AssetManager assets;
    private static final int SIMULTANEOUS_CHANNELS = 5;
    private static final String SOUND="sound/",
                            MUSIC="music/";
    private final SparseIntArray sounds=new SparseIntArray();
    private final SparseArray<AudioObject.MusicObject> musics=new SparseArray();

    GameAudio(Context context){
        assets=context.getAssets();
        AudioAttributes attributes=new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool=new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(SIMULTANEOUS_CHANNELS)
                .build();
    }

    AudioObject addSound(String filename){
        try {
            AssetFileDescriptor fd=assets.openFd(SOUND+filename);
            AudioObject.SoundObject sound=new AudioObject.SoundObject(soundPool,soundPool.load(fd,1));
            sounds.append(sounds.size(),sound.soundId);
            return sound;
        } catch (IOException e) {
            Log.d("Debug","Couldn't load sound '"+filename+"'" );
        }
        return null;
    }

    AudioObject addMusic(String filename){
        try {
            AssetFileDescriptor fd=assets.openFd(MUSIC+filename);
            MediaPlayer mediaPlayer= new MediaPlayer();
            mediaPlayer.setDataSource(fd);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            AudioObject.MusicObject music=new AudioObject.MusicObject(mediaPlayer);
            musics.append(musics.size(),music);
            return music;
        } catch (IOException e) {
            Log.d("Debug","Couldn't load music '"+filename+"'" );
        }
        return null;
    }

    private void mute(){
        for (int i = 0; i < sounds.size(); i++) {
            soundPool.setVolume(sounds.get(i),0,0);
        }
        for (int i = 0; i < sounds.size(); i++) {
            musics.get(i).player.setVolume(0,0);
        }
    }

    private void unMute(){
        for (int i = 0; i < sounds.size(); i++) {
            soundPool.setVolume(sounds.get(i),AudioObject.SoundObject.VOLUME,AudioObject.SoundObject.VOLUME);
        }
        for (int i = 0; i < sounds.size(); i++) {
            musics.get(i).player.setVolume(1,1);
        }
    }

    public void checkAudio(){
        if (SILENCE==true && lastSilence==false) {
            mute();
            lastSilence=true;
        }
        else if(SILENCE==false && lastSilence==true){
            unMute();
        }
    }

}
