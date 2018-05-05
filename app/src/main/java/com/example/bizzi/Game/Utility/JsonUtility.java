package com.example.bizzi.Game.Utility;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class JsonUtility {

    public static String readJsonFromFile(AssetManager assets, String filename) {
        String result=null;
        try {
            InputStream inputStream = assets.open("filename");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (IOException e) {
            Log.d("Debug", "Unable open audio description file");
        }
        return result;
    }
}
