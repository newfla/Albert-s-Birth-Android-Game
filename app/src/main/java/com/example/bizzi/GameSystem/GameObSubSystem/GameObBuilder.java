package com.example.bizzi.GameSystem.GameObSubSystem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.util.Pools;
import android.util.Log;
import android.util.SparseArray;

import com.example.bizzi.GameSystem.Builder;
import com.example.bizzi.GameSystem.GraphicsSubSystem.GameGraphics;
import com.example.bizzi.GameSystem.GraphicsSubSystem.Spritesheet;
import com.example.bizzi.GameSystem.Utility.JsonUtility;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.World;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class GameObBuilder implements Builder {

    private static final int MAXPOOLSIZE = 300;
    static final  Pools.SimplePool<GameObject> POOL=new Pools.SimplePool<>(MAXPOOLSIZE);
    private final Context context;
    private final World world;
    private static final String LEVELS="levels/";


    public GameObBuilder(Context context, World world){
        this.context=context;
        this.world=world;
    }

    private GameObject getGameOB(){
        GameObject object=POOL.acquire();
        if (object==null)
            object=new GameObject();
        return object;
    }

    @Override
    public void build() {
        //TODO
    }
    public SparseArray<GameObject> buildMenu(){
        SparseArray<GameObject> array=new SparseArray<>();
        GameObject gameOB;
        Bitmap bitmap;
        DrawableComponent drawable;
        ControllableComponent controllable;

        //Create background_menu
        gameOB=getGameOB();
        gameOB.type= GameObject.GameObjectType.MENU;
        bitmap=GameGraphics.STATICSPRITE.get(gameOB.type);
        drawable=new DrawableComponent(gameOB, bitmap);
        drawable.x=1920/2;
        drawable.y=1080/2;
        gameOB.components.put(drawable.getType(),drawable);

        array.append(array.size(),gameOB);

        //Create menuTitle
        gameOB=getGameOB();
        gameOB.type= GameObject.GameObjectType.MENUTITLE;
        bitmap=GameGraphics.STATICSPRITE.get(gameOB.type);
        drawable=new DrawableComponent(gameOB, bitmap);
        drawable.x=1920/2;
        drawable.y=1080/2+40;;
        gameOB.components.put(drawable.getType(),drawable);
        array.append(array.size(),gameOB);
        float previousY=drawable.y+bitmap.getHeight()/2;

        //Create startButton
        gameOB=getGameOB();
        gameOB.type= GameObject.GameObjectType.STARTBUTTON;
        bitmap=GameGraphics.STATICSPRITE.get(gameOB.type);
        drawable=new DrawableComponent(gameOB, bitmap);
        drawable.x=1920/2;
        drawable.y=previousY+100;
        gameOB.components.put(drawable.getType(),drawable);
        controllable=new ControllableComponent(gameOB);
        gameOB.components.put(controllable.getType(),controllable);
        array.append(array.size(),gameOB);
        previousY=drawable.y;


        //Create quitButton
        gameOB=getGameOB();
        gameOB.type= GameObject.GameObjectType.QUITBUTTON;
        bitmap=GameGraphics.STATICSPRITE.get(gameOB.type);
        drawable=new DrawableComponent(gameOB, bitmap);
        drawable.x=1920/2;
        drawable.y=previousY+ bitmap.getHeight()+50;
        gameOB.components.put(drawable.getType(),drawable);
        controllable=new ControllableComponent(gameOB);
        gameOB.components.put(controllable.getType(),controllable);
        array.append(array.size(),gameOB);

        //Create soundButton
        gameOB=getGameOB();
        gameOB.type= GameObject.GameObjectType.SOUNDBUTTON;
        Spritesheet spritesheet=GameGraphics.ANIMATEDSPRITE.get(gameOB.type);
        AnimatedComponent animated=new AnimatedComponent(gameOB,spritesheet);
        animated.x=7.5f*1920/8;
        animated.y=1080/10;
        gameOB.components.put(animated.getType(),animated);
        controllable=new ControllableComponent(gameOB);
        gameOB.components.put(controllable.getType(),controllable);
        array.append(array.size(),gameOB);

        return array;
    }
    //TODO factory object methods

    public SparseArray<GameObject> buildLevel(String level){
        SparseArray<GameObject> array=new SparseArray<>();
        JSONObject description;
        try {
            //Obtain level description
            description= new JSONObject(JsonUtility.readJsonFromFile(context.getAssets(),LEVELS+level));

            //Building walls
            JSONArray slidingWalls=description.getJSONArray("walls");
            for (int i = 0; i <slidingWalls.length(); i++)
                array.append(array.size(),buildSlidingWall(slidingWalls.getJSONObject(i)));


            //Building spermatozoon
            JSONObject spermatozoon=description.getJSONArray("spermatozoon").getJSONObject(0);
            array.append(array.size(),buildSpermatozoon(spermatozoon));

            //Building Egg cell
            JSONObject cell=description.getJSONArray("eggcell").getJSONObject(0);
            array.append(array.size(),buildEggCell(cell));

            //Building enemies
            JSONArray enemies=description.getJSONArray("enemies");
            for (int i = 0; i <enemies.length(); i++){
                switch (enemies.getJSONObject(i).getString("type")){
                    case "spermatoozon":
                        array.append(array.size(),buildEnemySpermatozoon(slidingWalls.getJSONObject(i)));
                        break;
                    case "pill":
                        array.append(array.size(),buildEnemyPill(slidingWalls.getJSONObject(i)));
                        break;
                }
            }


        } catch (JSONException e) {
            Log.d("Debug","Unable to create JsonOB for level: "+level);
            return null;
        }

        return array;
    }

    private GameObject buildSlidingWall(JSONObject wall){
        GameObject go=new GameObject();
        return go;
    }

    private GameObject buildSpermatozoon(JSONObject spermatozoon){

        GameObject go=new GameObject();
        go.type= GameObject.GameObjectType.SPERMATOZOON;

        //DrawableComponent
            DrawableComponent drawableComponent;
            Bitmap bitmap;
            bitmap=GameGraphics.STATICSPRITE.get(go.type);
            drawableComponent=new DrawableComponent(go,bitmap);
            go.setComponent(drawableComponent);



        //PhysicComponent

            PhysicComponent physicComponent;
            FixtureDef fixturedef;
            FixtureDef fixtesta;
            BodyDef bdef;
            bdef = new BodyDef();
            bdef.setPosition(0, 0);
            bdef.setType(BodyType.dynamicBody);
            Body body = world.createBody(bdef);
            body.setSleepingAllowed(false);
            body.setUserData(this);
            CircleShape testa = new CircleShape();
            PolygonShape coda = new PolygonShape();
            testa.setRadius(0.25f);
            coda.setAsBox(1.5f / 2, 0.01f / 2);
            testa.setPosition(0.125f,0f);
            coda.setCentroid(-0.85f,-0.25f);
            fixturedef = new FixtureDef();
            fixtesta = new FixtureDef();

            //Setup Testa

            fixtesta.setShape(testa);
            fixtesta.setFriction(0.1f);
            fixtesta.setRestitution(0.4f);
            fixtesta.setDensity(0.1f);

            //Setup Coda

            fixturedef.setShape(coda);
            fixturedef.setFriction(0.1f);       // default 0.2
            fixturedef.setRestitution(0.3f);    // default 0
            fixturedef.setDensity(0.4f);
            body.createFixture(fixtesta);
            body.createFixture(fixturedef);

            //Clean up

            fixtesta.delete();
            fixturedef.delete();
            bdef.delete();
            coda.delete();
            testa.delete();


            physicComponent = new PhysicComponent(go,body,5.5f,4f);
            go.setComponent(physicComponent);

        return go;
    }

    private GameObject buildEggCell(JSONObject cell){
        GameObject go=new GameObject();
        return go;
    }

    private GameObject buildEnemySpermatozoon(JSONObject spermatozoon){
        GameObject go=new GameObject();
        return go;
    }

    private GameObject buildEnemyPill(JSONObject pill){
        GameObject go=new GameObject();
        return go;
    }

}
