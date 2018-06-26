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
import com.example.bizzi.GameSystem.Utility.Recyclable;
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

public final class GameNetworking implements Recyclable {

    final static int MINOPPONENTS = 1, MAXOPPONENTS = 1;
    public final static int RCSIGNIN = 9001, RCWAITINGROOM = 10002;
    private final Context context;
    //List of NetworkingUtility
    private final AccelerometerNetworking accelerometerNetworking;
    private final GameObNetworking gameObNetworking;

    //Google Play Games variables
    GoogleSignInClient googleSignInClient;
    GoogleSignInAccount googleSignInAccount;
    RealTimeMultiplayerClient realTimeMultiplayerClient;
    String myPlayerId;

    //Room variables
    Room room;
    RoomConfig roomConfig;
    ArrayList<Participant> participants;
    String roomId, myMessageId, friendMessageId;


    //Multyplayer variables
    private GameWorld gameWorld;
    public boolean server = false;
    private boolean slidingWall = false;
    public SparseArray<GameObject> level;
    public InputObject.AccelerometerObject myAccelerometer;
    private InputObject.AccelerometerObject friendAccelerometer;
    public final InputObject.AccelerometerObject[] accelerometers= new InputObject.AccelerometerObject[2];

    @Override
    public void recycle() {
        room = null;
        roomId = null;
        server=false;
        level=null;
        myAccelerometer=null;
        friendAccelerometer=null;
        accelerometers[0]=null;
        accelerometers[1]=null;
        myMessageId=null;
        friendMessageId=null;
        participants=null;
        if (GameWorld.gameStatus!=2)
            GameWorld.gameStatus = 0;
    }

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    GameNetworking(Context context, AccelerometerNetworking accelerometerNetworking, GameObNetworking gameObNetworking) {
        this.context = context;
        this.accelerometerNetworking = accelerometerNetworking;
        this.gameObNetworking = gameObNetworking;
        ((MainActivity) context).setGameNetworking(this);
    }

    public void consumeMessage(RealTimeMessage message) {
        synchronized (this) {
           byte[] lastMessage = message.getMessageData();
            switch (lastMessage[0]) {
                case 3:
                    gameObNetworking.deserealizeGameObDimensions(lastMessage);
                    break;

                case 4:
                    gameObNetworking.deserializeGameOb(lastMessage, level);
                    GameWorld.gameStatus=1;
                    break;

                case 1:
                    gameWorld.endGameType= GameObject.GameObjectType.values()[lastMessage[1]];
                    GameWorld.gameStatus=8;
                    break;

                default:
                    friendAccelerometer = accelerometerNetworking.deserializeAccelerometer(lastMessage);
                    break;
            }
        }
    }

    public void quickGame() {
        Log.d("Debug", "QuickGame");
        gameObNetworking.recycle();
        realTimeMultiplayerClient.create(roomConfig);
        GameWorld.gameStatus = 5;
    }

    public void chooseRoles() {
        String temp;
        //Find
        for (int i = 0; i < participants.size(); i++) {
            temp = participants.get(i).getParticipantId();
            if (!temp.equals(myMessageId)) {
                friendMessageId = temp;
                break;
            }
        }
        if (myMessageId.compareTo(friendMessageId) < 0)
            server = true;
        else
            slidingWall = true;
     //   Log.d("Debug","FriendMessageId: "+friendMessageId+" MyMessageId: "+myMessageId +" server: "+server);
        Log.d("Debug","server: "+server+" - slidingWall: "+slidingWall);
    }

    public void firstSend() {
        if (server) {
           realTimeMultiplayerClient.sendUnreliableMessage(gameObNetworking.serializeGameObDimensions(level),roomId,friendMessageId);
           realTimeMultiplayerClient.sendUnreliableMessage(gameObNetworking.serializeGameObCenter(level),roomId,friendMessageId);
        }
    }

    public void send() {
        byte[] array;
        if (server)
            array = gameObNetworking.serializeGameObCenter(level);
        else
            array = accelerometerNetworking.serializeAccelerometer(myAccelerometer);

        realTimeMultiplayerClient.sendUnreliableMessage(array,roomId,friendMessageId);
    }

    public void lastSend(GameObject.GameObjectType end){
        byte[] array=new byte[2];
        array[0]=1;
        array[1]=(byte) end.ordinal();
        if (server)
            realTimeMultiplayerClient.sendUnreliableMessage(array,roomId,friendMessageId);
    }

    void showWaitingRoom() {
        realTimeMultiplayerClient.getWaitingRoomIntent(room, MINOPPONENTS)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        Log.d("Debug", "Start waiting room");
                        // show waiting room UI
                        ((Activity) context).startActivityForResult(intent, RCWAITINGROOM);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Debug", "There was a problem getting the waiting room!");
                        GameWorld.gameStatus = 0;
                    }
                });
    }

    public void leaveRoom() {
        if (roomId != null) {
            realTimeMultiplayerClient.leave(roomConfig, roomId)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            recycle();
                        }
                    });
        }
    }

    void updateRoom(Room room) {
        if (room != null) {
            participants = room.getParticipants();
            this.room = room;
        }
    }

    public void switchAccelerometer(){
        //array[0] world gravity
        //array[1] force on walls
        if (slidingWall){
            accelerometers[1]=myAccelerometer;
            accelerometers[0]=friendAccelerometer;
        }
        else {
            accelerometers[0]=myAccelerometer;
            accelerometers[1]=friendAccelerometer;
        }
    }
}
