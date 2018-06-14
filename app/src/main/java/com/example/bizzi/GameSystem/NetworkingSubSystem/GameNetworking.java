package com.example.bizzi.GameSystem.NetworkingSubSystem;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import com.example.bizzi.GameSystem.GameObSubSystem.GameObject;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public final class GameNetworking {

    final static int MINPLAYERS=2, MAXPLAYERS=2;

    GoogleSignInClient googleSignInClient;

    RoomConfig roomConfig;

    private final Context context;

    Room room;

    String myPlayerId, myMessageId;

    List<Participant> participants;

    SparseArray<RealTimeMessage> messagesBuffer , messagesFront;


   public GameNetworking(Context context){
        this.context=context;
        messagesBuffer=new SparseArray<>();
        messagesFront=new SparseArray<>();
    }

    public void consumeMessages(){
        synchronized (this){
            //TODO
            messagesFront.clear();
            SparseArray<RealTimeMessage> temp = messagesFront;
            messagesFront = messagesBuffer;
            messagesBuffer = temp;
        }
    }

    public void autoMatch(){
        GoogleSignInAccount account=null;
        while (account==null)
            account=GoogleSignIn.getLastSignedInAccount(context);

        //Room Creation
        Games.getRealTimeMultiplayerClient(context, account).create(roomConfig);

        Games.getPlayersClient(context,account)
                .getCurrentPlayerId().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                myPlayerId=s;
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Debug","Issue  getting myPlayerId");
                    }
                });
    }

    public void sendFirstMessage(SparseArray<GameObject> array){
       byte[]arrayByte=new byte[20*array.size()];
        for (int i = 0; i < array.size(); i++) {
            GameObject go=array.get(i);
            byte[] temp=GameObject.serializeGameObject(go,true);
            for (int j = 0; j < temp.length; j++) {
                arrayByte[i*21]=temp[j];
            }
            //TODO first send
        }
    }

    public void receiveFirstMessage(byte[]array){
       int j=(array.length+1)/20;
        for (int i = 0; i < j; i++) {
            GameObject.deSerializeGameObject(array,i,20);
        }
        //TODO add in List
    }
}
