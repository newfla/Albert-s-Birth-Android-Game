package com.example.bizzi.GameSystem.NetworkingSubSystem;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import com.example.bizzi.AlbertBirthActivity.MainActivity;
import com.example.bizzi.GameSystem.GameObSubSystem.GameObNetworking;
import com.example.bizzi.GameSystem.GameObSubSystem.GameObject;
import com.example.bizzi.GameSystem.GameWorld;
import com.example.bizzi.GameSystem.InputSubSystem.AccelerometerNetworking;
import com.example.bizzi.GameSystem.InputSubSystem.InputObject;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public final class GameNetworking {

    final static int MINOPPONENTS=1, MAXOPPONENTS=1;
    public final static  int RCSIGNIN = 9001, RCWAITINGROOM = 10002;

    //Google Play Games variables
    GoogleSignInClient googleSignInClient;
    GoogleSignInAccount googleSignInAccount;
    RealTimeMultiplayerClient realTimeMultiplayerClient;
    String myPlayerId; //Play Games id different from idMessage

    //Room variables
    Room room;
    RoomConfig roomConfig;
    ArrayList<Participant> participants;

    private final Context context;



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
       ((MainActivity)context).setGameNetworking(this);
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

    public void quickGame(){
       Log.d("Debug","QuickGame");
       realTimeMultiplayerClient.create(roomConfig);
        GameWorld.gameStatus=5;
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

    void showWaitingRoom(){
        realTimeMultiplayerClient.getWaitingRoomIntent(room, MINOPPONENTS)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        Log.d("Debug","Start waiting room");
                        // show waiting room UI
                        ((Activity)context).startActivityForResult(intent, RCWAITINGROOM);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Debug","There was a problem getting the waiting room!");
                        GameWorld.gameStatus=0;
                    }
                });
    }

    public void leaveRoom(){
        if (room!=null){
            realTimeMultiplayerClient.leave(roomConfig,room.getRoomId())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            room=null;
                            GameWorld.gameStatus=0;
                        }
                    });
        }
    }


}
