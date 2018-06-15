package com.example.bizzi.GameSystem.NetworkingSubSystem;

import android.support.annotation.NonNull;

import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;

final class MessageListener implements OnRealTimeMessageReceivedListener {

    private final GameNetworking gameNetworking;

        @Override
        public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
            synchronized (gameNetworking) {
                gameNetworking.messagesBuffer.append(gameNetworking.messagesBuffer.size(), realTimeMessage);
            }
        }

        MessageListener(GameNetworking gameNetworking){
                this.gameNetworking=gameNetworking;
        }
}

