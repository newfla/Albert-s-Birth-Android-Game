package com.example.bizzi.GameSystem.NetworkingSubSystem;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.bizzi.GameSystem.GameWorld;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;

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
            if (i!= GamesCallbackStatusCodes.OK)
                Log.d("Debug","Issue with Online Room");

        }

        @Override
        public void onLeftRoom(int i, @NonNull String s) {
            if (i!= GamesCallbackStatusCodes.OK)
                Log.d("Debug","Issue with Online Room");
            gameNetworking.room=null;
            gameNetworking.participants=null;
            GameWorld.home=true;

        }

        @Override
        public void onRoomConnected(int i, @Nullable Room room) {
            if (i!= GamesCallbackStatusCodes.OK)
                Log.d("Debug","Issue with Online Room");
            try {
                gameNetworking.participants=room.getParticipants();
            }catch (NullPointerException e){
                Log.d("Debug", "Ok status and No partecipants on RoomConnected\nThis sound really strange");
            }
        }
}
