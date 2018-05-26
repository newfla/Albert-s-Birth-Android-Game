package com.example.bizzi.GameSystem.NetworkingSubSystem;

import android.content.Context;
import android.os.Bundle;

import com.example.bizzi.GameSystem.Builder;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;

final public class NetworkingBuilder implements Builder {

    public final GameNetworking gameNetworking;
    private final Context context;

    public NetworkingBuilder(Context context){
        gameNetworking=new GameNetworking(context);
        this.context=context;
    }

    @Override
    public void build() {
       gameNetworking.googleSignInClient=GoogleSignIn.getClient(context,
          new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());

       Bundle autoMatchCriteria= RoomConfig.createAutoMatchCriteria(gameNetworking.MINPLAYERS,gameNetworking.MAXPLAYERS,0);
       gameNetworking.roomConfig=RoomConfig.builder(new MyRoomUpdateCallBack())
               .setOnMessageReceivedListener(new MessageListener(gameNetworking))
               .setRoomStatusUpdateCallback(new MyRoomStatusUpdateCallback())
               .setAutoMatchCriteria(autoMatchCriteria)
               .build();
    }
}
