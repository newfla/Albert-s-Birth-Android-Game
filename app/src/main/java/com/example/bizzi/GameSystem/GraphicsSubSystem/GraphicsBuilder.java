package com.example.bizzi.GameSystem.GraphicsSubSystem;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.bizzi.GameSystem.GameObSubSystem.GameObject;
import com.example.bizzi.GameSystem.Utility.Builder;
import com.example.bizzi.GameSystem.Utility.JsonUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public final class GraphicsBuilder implements Builder {

    private final AssetManager assets;
    private static final String GRAPHIC="graphic/";

    public GraphicsBuilder(Context context){
        assets=context.getAssets();
    }



    @Override
    public void build() {
        try {
            JSONObject jsonObject=new JSONObject(JsonUtility.readJsonFromFile(assets,"graphic.json"));
            String gameObject, type, graphic;
            JSONArray jsonArray=jsonObject.getJSONArray("graphic");
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject=jsonArray.getJSONObject(i);
                gameObject=jsonObject.getString("gameobject");
                type=jsonObject.getString("type");
                graphic=jsonObject.getString("file");
                Bitmap bitmap=loadFormFile(graphic);
                if (type.equalsIgnoreCase("static"))
                    GameGraphics.STATICSPRITE.put(GameObject.GameObjectType.valueOf(gameObject),bitmap);
                else {
                    int frameWidth, frameHeight, animations, length;
                    frameWidth=jsonObject.getInt("frameWidth");
                    frameHeight=jsonObject.getInt("frameHeight");
                    animations=jsonObject.getInt("animations");
                    length=jsonObject.getInt("length");
                    Spritesheet spritesheet=new Spritesheet(bitmap,frameWidth,frameHeight,animations,length);
                    GameGraphics.ANIMATEDSPRITE.put(GameObject.GameObjectType.valueOf(gameObject), spritesheet);
                }
            }
        } catch (JSONException e) {
            //Log.d("Debug", "Unable to create JsonOB for graphic");
        }
    }

    private Bitmap loadFormFile(String fileName){
        try {
            InputStream in=assets.open(GRAPHIC+fileName);
            Bitmap bitmap= BitmapFactory.decodeStream(in);
            in.close();
            return bitmap;
        } catch (IOException e) {
            //Log.d("Debug","Couldn't load graphics "+fileName);
        }
        return null;
    }
}
