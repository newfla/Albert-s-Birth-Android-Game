package com.example.bizzi.GameSystem.AudioSubSystem;

import android.util.Log;
import android.util.SparseIntArray;

import com.example.bizzi.GameSystem.GameObSubSystem.GameObject;

public final class GameAudioNetworking {

    public byte[] serializeGameAudio(SparseIntArray audio){
        int n=audio.size();
        byte[] array=new byte[1+n];
        array[0]=2;
        for (int i = 0; i < n; i++) {
            array[i+1]=(byte)audio.get(i);
        }
        return array;
    }

    public void deserializeAudio(byte[] array){
        int n=array.length;
        GameObject.GameObjectType[] values=GameObject.GameObjectType.values();
        for (int i = 1; i < n; i++) {
           AudioObject audioObject= GameAudio.AUDIOLIBRARY.get(values[array[i]]);
         //   //Log.d("Debug","sound:"+i);
           if (audioObject!=null)
               audioObject.play();
        }
    }
}
