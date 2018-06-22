package com.example.bizzi.GameSystem.NetworkingSubSystem;


import android.content.Context;
import android.util.SparseArray;

import com.example.bizzi.GameSystem.GameObSubSystem.GameObNetworking;
import com.example.bizzi.GameSystem.GameObSubSystem.GameObject;
import com.example.bizzi.GameSystem.InputSubSystem.AccelerometerNetworking;
import com.example.bizzi.GameSystem.InputSubSystem.InputObject;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;

public final class GameNetworking {

    final static int MINPLAYERS=2, MAXPLAYERS=2;

    //Google Play Games variables
    GoogleSignInClient googleSignInClient;
    GoogleSignInAccount googleSignInAccount;
    RealTimeMultiplayerClient realTimeMultiplayerClient;
    String myPlayerId; //Play Games id different from idMessage

    RoomConfig roomConfig;

    private final Context context;

    Room room;

    String myMessageId, friendMessageId;

    private Boolean server;
    private boolean slidingWall=false;

    RealTimeMessage last;

    private byte[] lastMessage;

    SparseArray<GameObject> list;
    InputObject.AccelerometerObject accelerometer;

    //List of NetworkingUtility
    private final AccelerometerNetworking accelerometerNetworking;
    private final GameObNetworking gameObNetworking;


   public GameNetworking(Context context, AccelerometerNetworking accelerometerNetworking, GameObNetworking gameObNetworking){
        this.context=context;
        this.accelerometerNetworking=accelerometerNetworking;
        this.gameObNetworking=gameObNetworking;
    }

    public void consumeMessage(){
        synchronized (this) {
            lastMessage = last.getMessageData();
            switch (lastMessage[0]) {
                case 3:
                    gameObNetworking.deserealizeGameObDimensions(lastMessage);
                    break;

                case 4:
                    gameObNetworking.deserializeGameOb(lastMessage,list);
                    break;

                default:
                    accelerometer=accelerometerNetworking.deserializeAccelerometer(lastMessage);
                    break;
            }
        }
    }


    private byte[] serializeAccelerometerSlidingWall(InputObject.AccelerometerObject accelerometer){
       byte[] array=accelerometerNetworking.serializeAccelerometer(accelerometer);
       array[0]=1;
       return array;
    }

    private byte[] serializeAccelerometerWorld(InputObject.AccelerometerObject accelerometer){
       byte[] array=accelerometerNetworking.serializeAccelerometer(accelerometer);
       array[0]=2;
       return array;
    }

    void startOnline(SparseArray<GameObject>list){
       if (myPlayerId.compareTo(friendMessageId)<0) {
           server = true;
           gameObNetworking.serializeGameObDimensions(list);
       }
       else {
           server = false;
           slidingWall=true;
       }
    }

    public void send(InputObject.AccelerometerObject accelerometerObject, SparseArray<GameObject> list){
       byte[] array;
       if (server){
           array=gameObNetworking.serializeGameObCenter(list);
       }
       else {
           if (slidingWall)
               array=serializeAccelerometerSlidingWall(accelerometerObject);
           else
               array=serializeAccelerometerWorld(accelerometerObject);
       }
        //TODO sendMessage
    }

}
