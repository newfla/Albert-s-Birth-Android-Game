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
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public final class GameNetworking {

    final static int MINPLAYERS=2, MAXPLAYERS=2;

    GoogleSignInClient googleSignInClient;

    private RealTimeMultiplayerClient client;

    RoomConfig roomConfig;

    private final Context context;

    Room room;

    String myPlayerId, myMessageId, friendMessageId;

    SparseArray<RealTimeMessage> messagesBuffer;

    public boolean server=false;

    private RealTimeMessage last=null;


   public GameNetworking(Context context){
        this.context=context;
        messagesBuffer=new SparseArray<>();
    }

    public void consumeMessages(){
        synchronized (this){
            last=messagesBuffer.get(0);
            messagesBuffer.remove(0);
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

    public void sendFirstMessageToCLient(SparseArray<GameObject> array){
       byte[]arrayByte=new byte[20*array.size()];
        for (int i = 0; i < array.size(); i++) {
            GameObject go=array.get(i);
            byte[] temp=GameObject.serializeGameObject(go,true);
            for (int j = 0; j < temp.length; j++)
                arrayByte[i*20]=temp[j];
            client.sendUnreliableMessage(arrayByte,room.getRoomId(),friendMessageId);
        }
    }

    public void receiveFirstMessageFromServer(byte[]array){
       int j=(array.length+1)/20;
        for (int i = 0; i < j; i++) {
            GameObject.deSerializeGameObject(array,i,20);
        }
    }

    public void initCommunication(SparseArray<GameObject> array){
       if (server)
           sendFirstMessageToCLient(array);
       else {
           consumeMessages();
           receiveFirstMessageFromServer(last.getMessageData());
       }
    }
}
