package com.example.musicapp.spotify;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Repeat;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class SpotifyWrapper {

    private static SpotifyWrapper single_instance = null;
    private static final String CLIENT_ID = "559ec4392ac04e89a9188f7d85c50903";
    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "com.example.musicapp://callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    private String accessToken = "false";

    private int counter = 0;

    private SpotifyWrapper(){

    }

    public static SpotifyWrapper SpotifyWrapper(){
        if (single_instance == null){
            single_instance = new SpotifyWrapper();
        }
        return single_instance;
    }

    public SpotifyAppRemote getRemote(){
        return this.mSpotifyAppRemote;
    }

    public void connectUserSpotify(Context context, String uri){
        auth_lib_connection(context);
        auth_remote_connection(context, uri);

    }

    private void auth_lib_connection(Context context){
        if (this.accessToken.equals("false")) {
            AuthorizationRequest.Builder builder =
                    new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);

            builder.setScopes(new String[]{"streaming"});
            AuthorizationRequest request = builder.build();

            AuthorizationClient.openLoginActivity((Activity) context, REQUEST_CODE, request);
        }
    }

    private void auth_remote_connection(Context context, String uri){
        if (this.mSpotifyAppRemote == null || !this.mSpotifyAppRemote.isConnected()){
            counter++;
            Log.d("SpotifyActivity", "counter: " + counter);
            ConnectionParams connectionParams =
                    new ConnectionParams.Builder(CLIENT_ID)
                            .setRedirectUri(REDIRECT_URI)
                            .showAuthView(true)
                            .build();
            SpotifyAppRemote.connect(context, connectionParams,
                    new Connector.ConnectionListener() {

                        @Override
                        public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                            mSpotifyAppRemote = spotifyAppRemote;
                            Log.d("SpotifyActivity", "Connected! Yay!");

                            connected(uri);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.e("SpotifyActivity", throwable.getMessage(), throwable);

                            // Something went wrong when attempting to connect! Handle errors here
                        }
                    });
        }else {
            connected(uri);
        }
    }

    private void connected(String uri) {
        mSpotifyAppRemote.getPlayerApi().play(uri);

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("SpotifyActivity", track.name + " by " + track.artist.name);
                    }
                });
    }

    public void disconnectRemote(){
        if (this.mSpotifyAppRemote != null && this.mSpotifyAppRemote.isConnected()){
            mSpotifyAppRemote.disconnect(mSpotifyAppRemote);
        }
    }

    public void pause(){
        if (this.mSpotifyAppRemote != null && this.mSpotifyAppRemote.isConnected()){
            this.mSpotifyAppRemote.getPlayerApi().pause();
        }
    }

    public void setAccessToken(String newAccessToken){
        this.accessToken = newAccessToken;
        Log.d("SpotifyActivity", "Access Token: " + this.accessToken);
    }
}
