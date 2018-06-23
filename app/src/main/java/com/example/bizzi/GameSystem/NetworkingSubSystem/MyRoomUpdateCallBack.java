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

    MyRoomUpdateCallBack(GameNetworking gameNetworking) {
        this.gameNetworking = gameNetworking;
    }

    @Override
    public void onRoomCreated(int i, @Nullable Room room) {
        if (i != GamesCallbackStatusCodes.OK)
            issue(i);
        else {
            gameNetworking.room = room;
            gameNetworking.showWaitingRoom();
        }

    }

    @Override
    public void onJoinedRoom(int i, @Nullable Room room) {
        if (i != GamesCallbackStatusCodes.OK)
            issue(i);
        else {
            gameNetworking.room = room;
            gameNetworking.showWaitingRoom();
        }

    }

    @Override
    public void onLeftRoom(int i, @NonNull String s) {
       issue(i);

    }

    @Override
    public void onRoomConnected(int i, @Nullable Room room) {
        if (i != GamesCallbackStatusCodes.OK)
            issue(i);
        else {
         updatePartecipants(room);
        }
    }

    private void updatePartecipants(Room room) {
        if (room != null) {
            gameNetworking.participants = room.getParticipants();
        }
    }

    private void issue(int i){
        Log.d("Debug", "Issue with Online Room " + i);
        gameNetworking.room = null;
        GameWorld.gameStatus = 0;
    }
}
