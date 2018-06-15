package com.example.bizzi.GameSystem.NetworkingSubSystem;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.bizzi.GameSystem.GameWorld;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;

import java.util.List;

final class MyRoomUpdateCallBack extends RoomUpdateCallback {
    private final GameNetworking gameNetworking;

    MyRoomUpdateCallBack(GameNetworking gameNetworking){
        this.gameNetworking=gameNetworking;
    }

        @Override
        public void onRoomCreated(int i, @Nullable Room room) {
            if (i!= GamesCallbackStatusCodes.OK)
                Log.d("Debug","Issue with Online Room");
            else
                gameNetworking.room=room;

        }

        @Override
        public void onJoinedRoom(int i, @Nullable Room room) {
        gameNetworking.room=room;
            if (i!= GamesCallbackStatusCodes.OK)
                Log.d("Debug","Issue with Online Room");

        }

        @Override
        public void onLeftRoom(int i, @NonNull String s) {
            if (i!= GamesCallbackStatusCodes.OK)
                Log.d("Debug","Issue with Online Room");
            gameNetworking.room=null;
            GameWorld.home=true;

        }

        @Override
        public void onRoomConnected(int i, @Nullable Room room) {
            if (i!= GamesCallbackStatusCodes.OK)
                Log.d("Debug","Issue with Online Room");
            try {
               List<String> partecipants= room.getParticipantIds();
               String x;
                for (int j = 0; j < partecipants.size(); j++) {
                    x=partecipants.get(j);
                    if (!x.equalsIgnoreCase(gameNetworking.myMessageId))
                        gameNetworking.friendMessageId=x;
                }
            }catch (NullPointerException e){
                Log.d("Debug", "Ok status and No partecipants on RoomConnected\nThis sound really strange");
            }
        }
}
