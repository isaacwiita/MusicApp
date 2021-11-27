package com.example.musicapp.spotify;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.musicapp.models.Song;
import com.example.musicapp.services.PlaylistService;
import com.example.musicapp.services.VolleyCallBack;
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

import java.util.Stack;

public class SpotifyWrapper {

    private static SpotifyWrapper single_instance = null;
    private static final String CLIENT_ID = "559ec4392ac04e89a9188f7d85c50903";
    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "com.example.musicapp://callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    private String accessToken = "BQAMLPQwXc6RrofRGa6_-nM7DCgQYNRL4VNM6rOMW57XXPx0OJLJC9n3vR02jdkugJW2QEvCzYYxE_b2lUtAlhhPceXn420HDhXqeu4-74woyV3tJoL2Ncs9S4KdagM8EVjmBJUSqTa9gVYFVQ";
    private boolean set = false;
    private static final String SCOPES = "user-read-recently-played,user-library-modify,user-read-email,user-read-private, streaming, playlist-modify-private, playlist-modify-public";
    private PlaylistService playlists;
    private RequestQueue queue = null;
    private int counter = 0;
    public String spotifyUserId = null;

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

    public void setQ(Context context){
        if (this.queue == null){
            this.queue = Volley.newRequestQueue(context);
        }
    }

    public void auth_lib_connection(Context context){
        if (!this.set) {
            AuthorizationRequest.Builder builder =
                    new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);

            builder.setScopes(new String[]{SCOPES});
            AuthorizationRequest request = builder.build();
            AuthorizationClient.clearCookies(context);
            AuthorizationClient.openLoginActivity((Activity) context, REQUEST_CODE, request);
            this.set = true;
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

    public void resume(){
        if (this.mSpotifyAppRemote != null && this.mSpotifyAppRemote.isConnected()){
            this.mSpotifyAppRemote.getPlayerApi().resume();
        }
    }

    public void setAccessToken(String newAccessToken){
        this.accessToken = newAccessToken;
        Log.d("SpotifyActivity", "Access Token: " + this.accessToken);
    }

    public String getAccessToken(){
        return this.accessToken;
    }

    public void getSongs(int id){
        this.playlists = new PlaylistService(this.queue, id, this.accessToken);
        this.playlists.get(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                Log.e("SpotifyActivity", "ALL DONE!!!");
            }
        });
    }

}
