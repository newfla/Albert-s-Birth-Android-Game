package com.example.bizzi.GameSystem.NetworkingSubSystem;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;

final class MyRoomUpdateCallBack extends RoomUpdateCallback {
        @Override
        public void onRoomCreated(int i, @Nullable Room room) {

        }

        @Override
        public void onJoinedRoom(int i, @Nullable Room room) {

        }

        @Override
        public void onLeftRoom(int i, @NonNull String s) {

        }

        @Override
        public void onRoomConnected(int i, @Nullable Room room) {
        }
}
