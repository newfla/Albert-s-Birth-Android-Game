package com.example.bizzi.GameSystem.NetworkingSubSystem;


import android.content.Context;
import android.util.SparseArray;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;

public final class GameNetworking {

    final static int MINPLAYERS=2, MAXPLAYERS=2;

    GoogleSignInClient googleSignInClient;

    RoomConfig roomConfig;

    private final Context context;

    SparseArray<RealTimeMessage> messagesBuffer , messagesFront;

    GameNetworking(Context context){
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
        Games.getRealTimeMultiplayerClient(context, account).create(roomConfig);
    }

}
