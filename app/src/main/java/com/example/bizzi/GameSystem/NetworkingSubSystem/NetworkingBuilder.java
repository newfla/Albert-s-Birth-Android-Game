package com.example.bizzi.GameSystem.NetworkingSubSystem;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.bizzi.AlbertBirthActivity.MainActivity;
import com.example.bizzi.GameSystem.AudioSubSystem.GameAudioNetworking;
import com.example.bizzi.GameSystem.GameObSubSystem.GameObNetworking;
import com.example.bizzi.GameSystem.InputSubSystem.AccelerometerNetworking;
import com.example.bizzi.GameSystem.Utility.Builder;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

final public class NetworkingBuilder implements Builder {

    public final GameNetworking gameNetworking;
    private final Context context;

    public NetworkingBuilder(MainActivity activity){
        gameNetworking=new GameNetworking(activity,new AccelerometerNetworking(), new GameObNetworking(),new GameAudioNetworking());
        this.context=activity;
    }

    @Override
    public void build() {
     gameNetworking.googleSignInClient=GoogleSignIn.getClient(context,GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
       gameNetworking.googleSignInClient.silentSignIn().addOnCompleteListener((Activity) context, new OnCompleteListener<GoogleSignInAccount>() {
           @Override
           public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
               if (task.isSuccessful()) {
                   //Log.d("Debug", " Networking-Builder signIn success");
                   onConnected(task.getResult());
               } else {
                   //Log.d("Debug", "Networking-Builder signIn failure", task.getException());
                   onDisconnected();
               }
           }
       });

       Bundle autoMatchCriteria= RoomConfig.createAutoMatchCriteria(GameNetworking.MINOPPONENTS,GameNetworking.MAXOPPONENTS,0);
       gameNetworking.roomConfig=RoomConfig.builder(new MyRoomUpdateCallBack(gameNetworking))
               .setOnMessageReceivedListener(new MessageListener(gameNetworking))
               .setRoomStatusUpdateCallback(new MyRoomStatusUpdateCallback(gameNetworking))
               .setAutoMatchCriteria(autoMatchCriteria)
               .build();


    }

    private void onConnected(GoogleSignInAccount googleSignInAccount){
        gameNetworking.googleSignInAccount=googleSignInAccount;
        gameNetworking.realTimeMultiplayerClient=Games.getRealTimeMultiplayerClient(context,googleSignInAccount);
        Games.getPlayersClient(context,googleSignInAccount).getCurrentPlayer()
                .addOnSuccessListener(new OnSuccessListener<Player>() {
                    @Override
                    public void onSuccess(Player player) {
                        while (gameNetworking.myPlayerId==null)
                            gameNetworking.myPlayerId=player.getPlayerId();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.d("Debug", "There was a problem getting the player id!");
                    }
                });
    }

    private void onDisconnected(){
        //Log.d("Debug","Disconnected");
        gameNetworking.realTimeMultiplayerClient=null;
    }
}
