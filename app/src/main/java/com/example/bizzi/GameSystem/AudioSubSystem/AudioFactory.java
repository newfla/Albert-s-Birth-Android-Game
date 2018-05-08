package com.example.bizzi.GameSystem.AudioSubSystem;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.bizzi.GameSystem.Entity.GameObject;
import com.example.bizzi.GameSystem.Factory;
import com.example.bizzi.GameSystem.Utility.JsonUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class AudioFactory implements Factory{

    public final GameAudio gameAudio;
    private final AssetManager assets;

    public AudioFactory(Context context){
        assets=context.getAssets();
        gameAudio=new GameAudio(context);
    }

    public void init(){
        try {
            JSONObject jsonObject=new JSONObject(JsonUtility.readJsonFromFile(assets,"audio.json"));
            String gameObject, type, sound;
            JSONArray jsonArray=jsonObject.getJSONArray("audio");
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject=jsonArray.getJSONObject(i);
                gameObject=jsonObject.getString("gameobject");
                type=jsonObject.getString("type");
                sound=jsonObject.getString("sound");
                if (type.equalsIgnoreCase("Sound"))
                    gameAudio.audioLibrary.put(GameObject.GameObjectType.valueOf(gameObject),gameAudio.addSound(sound));
                else
                    gameAudio.audioLibrary.put(GameObject.GameObjectType.valueOf(gameObject),gameAudio.addMusic(sound));
            }
        } catch (JSONException e) {
            Log.d("Debug", "Unable to create JsonOB for audio");
        }
    }
}
