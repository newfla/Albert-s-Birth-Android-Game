package com.example.bizzi.GameSystem.NetworkingSubSystem;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;

import java.util.List;

final class MyRoomStatusUpdateCallback extends RoomStatusUpdateCallback {

    private final GameNetworking gameNetworking;

    MyRoomStatusUpdateCallback(GameNetworking gameNetworking){
        this.gameNetworking=gameNetworking;
    }

    @Override
    public void onRoomConnecting(@Nullable Room room) {
        gameNetworking.updateRoom(room);
    }

    @Override
    public void onRoomAutoMatching(@Nullable Room room) {
       gameNetworking.updateRoom(room);
    }

    @Override
    public void onPeerInvitedToRoom(@Nullable Room room, @NonNull List<String> list) {
        gameNetworking.updateRoom(room);
    }

    @Override
    public void onPeerDeclined(@Nullable Room room, @NonNull List<String> list) {
       gameNetworking.updateRoom(room);
    }

    @Override
    public void onPeerJoined(@Nullable Room room, @NonNull List<String> list) {
        gameNetworking.updateRoom(room);
    }

    @Override
    public void onPeerLeft(@Nullable Room room, @NonNull List<String> list) {
       gameNetworking.updateRoom(room);
    }

    @Override
    public void onConnectedToRoom(@Nullable Room room) {
        gameNetworking.updateRoom(room);
        if ( room!=null && gameNetworking.roomId==null)
            gameNetworking.roomId=room.getRoomId();
        gameNetworking.myMessageId= room.getParticipantId(gameNetworking.myPlayerId);
    }

    @Override
    public void onDisconnectedFromRoom(@Nullable Room room) {
        gameNetworking.leaveRoom();

    }

    @Override
    public void onPeersConnected(@Nullable Room room, @NonNull List<String> list) {
        gameNetworking.updateRoom(room);
    }

    @Override
    public void onPeersDisconnected(@Nullable Room room, @NonNull List<String> list) {
        gameNetworking.updateRoom(room);
    }

    @Override
    public void onP2PConnected(@NonNull String s) {

    }

    @Override
    public void onP2PDisconnected(@NonNull String s) {

    }
}
