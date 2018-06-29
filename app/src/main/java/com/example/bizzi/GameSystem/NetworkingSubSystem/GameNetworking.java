package com.example.bizzi.GameSystem.NetworkingSubSystem;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.example.bizzi.AlbertBirthActivity.MainActivity;
import com.example.bizzi.GameSystem.AudioSubSystem.GameAudioNetworking;
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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;

public final class GameNetworking implements Recyclable {

    private static final int NPRIMES=6000;

    private final MainActivity mainActivity;
    //List of NetworkingUtility
    private final AccelerometerNetworking accelerometerNetworking;
    private final GameObNetworking gameObNetworking;
    private final GameAudioNetworking gameAudioNetworking;

    //Google Play Games variables
    GoogleSignInClient googleSignInClient;
    GoogleSignInAccount googleSignInAccount;
    RealTimeMultiplayerClient realTimeMultiplayerClient;
    String myPlayerId;
    final static int MINOPPONENTS = 1, MAXOPPONENTS = 1;
    public final static int RCSIGNIN = 9001, RCWAITINGROOM = 10002;

    //Room variables
    Room room;
    RoomConfig roomConfig;
    ArrayList<Participant> participants;
    String roomId, myMessageId, friendMessageId;


    //Multyplayer variables
    private GameWorld gameWorld;
    public boolean server;
    private boolean slidingWall = false;
    public SparseArray<GameObject> level;
    public InputObject.AccelerometerObject myAccelerometer;
    private InputObject.AccelerometerObject friendAccelerometer;
    public final InputObject.AccelerometerObject[] accelerometers= new InputObject.AccelerometerObject[2];
    public SparseIntArray audio;
    public long timePrime;

    @Override
    public void recycle() {
        room = null;
        roomId = null;
        level=null;
        myAccelerometer=null;
        friendAccelerometer=null;
        accelerometers[0]=null;
        accelerometers[1]=null;
        myMessageId=null;
        friendMessageId=null;
        participants=null;
        audio=null;
        if (gameWorld.gameStatus!=2)
            gameWorld.gameStatus = 0;
    }

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        mainActivity.setGameWorld(gameWorld);
    }

    GameNetworking(MainActivity activity, AccelerometerNetworking accelerometerNetworking, GameObNetworking gameObNetworking, GameAudioNetworking gameAudioNetworking) {
        mainActivity = activity;
        this.accelerometerNetworking = accelerometerNetworking;
        this.gameObNetworking = gameObNetworking;
        this.gameAudioNetworking = gameAudioNetworking;
        mainActivity.setGameNetworking(this);
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
                    gameWorld.gameStatus=1;
                    break;

                case 1:
                    gameWorld.endGameType= GameObject.GameObjectType.values()[lastMessage[1]];
                    gameWorld.gameStatus=8;
                    break;

                case 0:
                    friendAccelerometer = accelerometerNetworking.deserializeAccelerometer(lastMessage);
                    break;

                case 2:
                    gameAudioNetworking.deserializeAudio(lastMessage);
                    break;
                case 5:
                    chooseServer(lastMessage);
                    break;
            }
        }
    }

    public void quickGame() {
        gameObNetworking.recycle();
        realTimeMultiplayerClient.create(roomConfig);
        sieveOfEratosthenes(NPRIMES);
        gameWorld.gameStatus = 5;

    }

    public void findId() {
        String temp;
        //Find
        for (int i = 0; i < participants.size(); i++) {
            temp = participants.get(i).getParticipantId();
            if (!temp.equals(myMessageId)) {
                friendMessageId = temp;
                break;
            }
        }
    }

    public void sendBootTime(){
       // Log.d("Debug","myMessage"+myMessageId+"  friend"+friendMessageId);
        if (myMessageId.compareTo(friendMessageId)<0){
            byte[] array=new byte[9];
            array[0]=5;
            ByteBuffer.wrap(array,1,8).order(ByteOrder.BIG_ENDIAN).putLong(timePrime);
            realTimeMultiplayerClient.sendReliableMessage(array, roomId, friendMessageId, new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
                @Override
                public void onRealTimeMessageSent(int i, int i1, String s) {
                   // Log.d("Debug","invio primes");
                }
            });
        }

    }

    private void chooseServer(byte[] array) {
        if (array.length > 2) {
            byte[] array2=new  byte[2];
            array2[0]=5;
           // Log.d("Debug", "Ricevuto il time");
            long friendTime = ByteBuffer.wrap(array, 1, 8).order(ByteOrder.BIG_ENDIAN).getLong();
            int result = Long.compare(timePrime, friendTime);
            if (result <= 0) {
                server = true;
                slidingWall = false;

                array2[1]=0;
            } else {
                slidingWall = true;
                server = false;
                array2[1]=1;
            }

            realTimeMultiplayerClient.sendReliableMessage(array2, roomId, friendMessageId, new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
                @Override
                public void onRealTimeMessageSent(int i, int i1, String s) {
                   // Log.d("Debug","inviato server avversario");
                }
            });
            //Log.d("Debug","myTime "+timePrime+" friendTime"+friendTime);
            gameWorld.gameStatus=7;
        }
        else {
           // Log.d("Debug","Ricevuto ruolo");
            if (array[1]==0)
                server=false;
            else
                server=true;
            gameWorld.gameStatus=7;
        }
    }

    public void firstSend() {
        if (server) {
           realTimeMultiplayerClient.sendUnreliableMessage(gameObNetworking.serializeGameObDimensions(level),roomId,friendMessageId);
           realTimeMultiplayerClient.sendUnreliableMessage(gameObNetworking.serializeGameObCenter(level),roomId,friendMessageId);
        }
    }

    public void send() {
        byte[] array;
        if (server) {
            array = gameObNetworking.serializeGameObCenter(level);
            realTimeMultiplayerClient.sendUnreliableMessage(array,roomId,friendMessageId);
            synchronized (gameWorld) {
                array = gameAudioNetworking.serializeGameAudio(audio);
                audio.clear();
                if (array.length<1)
                    return;
            }

        }
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
                        mainActivity.startActivityForResult(intent, RCWAITINGROOM);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Debug", "There was a problem getting the waiting room!");
                        gameWorld.gameStatus = 0;
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


    private void sieveOfEratosthenes(int n) {
        //http://www.baeldung.com/java-generate-prime-numbers
        long temp=System.nanoTime(), x=0;
        boolean prime[] = new boolean[n + 1];
        Arrays.fill(prime, true);
        for (int p = 2; p * p <= n; p++) {
            if (prime[p]) {
                for (int i = p * 2; i <= n; i += p) {
                    prime[i] = false;
                }
            }
        }
        for (int i = 2; i <= n; i++) {
            if (prime[i])
                x+=i;
        }
        timePrime= System.nanoTime()-temp;
    }
}
