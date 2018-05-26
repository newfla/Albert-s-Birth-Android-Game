package com.example.bizzi.GameSystem.NetworkingSubSystem;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;

import java.util.List;

final class MyRoomStatusUpdateCallback extends RoomStatusUpdateCallback {
    @Override
    public void onRoomConnecting(@Nullable Room room) {

    }

    @Override
    public void onRoomAutoMatching(@Nullable Room room) {

    }

    @Override
    public void onPeerInvitedToRoom(@Nullable Room room, @NonNull List<String> list) {

    }

    @Override
    public void onPeerDeclined(@Nullable Room room, @NonNull List<String> list) {

    }

    @Override
    public void onPeerJoined(@Nullable Room room, @NonNull List<String> list) {

    }

    @Override
    public void onPeerLeft(@Nullable Room room, @NonNull List<String> list) {

    }

    @Override
    public void onConnectedToRoom(@Nullable Room room) {

    }

    @Override
    public void onDisconnectedFromRoom(@Nullable Room room) {

    }

    @Override
    public void onPeersConnected(@Nullable Room room, @NonNull List<String> list) {

    }

    @Override
    public void onPeersDisconnected(@Nullable Room room, @NonNull List<String> list) {

    }

    @Override
    public void onP2PConnected(@NonNull String s) {

    }

    @Override
    public void onP2PDisconnected(@NonNull String s) {

    }
}
