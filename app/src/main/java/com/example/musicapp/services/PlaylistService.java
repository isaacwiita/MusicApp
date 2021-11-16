package com.example.musicapp.services;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.musicapp.models.Song;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class PlaylistService {

    private static final String RAP_ENDPOINT = "https://api.spotify.com/v1/playlists/37i9dQZF1DX0XUsuxWHRQd/" +
            "tracks?market=US&fields=items(track(name%2C%20id%2C%20artists(name)))&limit=10";
    private RequestQueue mqueue;
    private Song user;
    private int playlist_id;
    private String token;

    public PlaylistService(RequestQueue rq, int id, String tok){
        this.mqueue = rq;
        this.playlist_id = id;
        this.token = tok;
    }

    public void get(final VolleyCallBack callBack){
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(RAP_ENDPOINT, null, response -> {
//            Gson gson = new Gson();
//            //user = gson.fromJson(response.toString(), Song.class);
//            Log.d("SpotifyActivity", response.toString());
//            callBack.onSuccess();
//        }, error -> get(() -> {} )) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                String auth = "Bearer " + token;
//                headers.put("Authorization: ", auth);
//                return headers;
//            }
//        };
//        mqueue.add(jsonObjectRequest);
    }
}
