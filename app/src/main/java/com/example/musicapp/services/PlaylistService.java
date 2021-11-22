package com.example.musicapp.services;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.example.musicapp.models.Song;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.example.musicapp.ui.player.home.PlayerViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PlaylistService {

    private static final String RAP_ENDPOINT = "https://api.spotify.com/v1/playlists/37i9dQZF1DX0XUsuxWHRQd/" +
            "tracks?market=US&fields=items(track(name%2C%20id%2C%20artists(name)))&limit=50";
    private static final String COUNTRY_ENDPOINT = "https://api.spotify.com/v1/playlists/37i9dQZF1DX13ZzXoot6Jc/" +
            "tracks?market=US&fields=items(track(name%2C%20id%2C%20artists(name)))&limit=50";
    private static final String POP_ENDPOINT = "https://api.spotify.com/v1/playlists/37i9dQZF1DWUa8ZRTfalHk/" +
            "tracks?market=US&fields=items(track(name%2C%20id%2C%20artists(name)))&limit=50";
    private static final String HIPHOP_ENDPOINT = "https://api.spotify.com/v1/playlists/37i9dQZF1DWT5MrZnPU1zD/" +
            "tracks?market=US&fields=items(track(name%2C%20id%2C%20artists(name)))&limit=50";
    private static final String HAPPY_ENDPOINT = "https://api.spotify.com/v1/playlists/37i9dQZF1DXdPec7aLTmlC/" +
            "tracks?market=US&fields=items(track(name%2C%20id%2C%20artists(name)))&limit=50";
    private static final String SAD_ENDPOINT = "https://api.spotify.com/v1/playlists/37i9dQZF1DWSqBruwoIXkA/" +
            "tracks?market=US&fields=items(track(name%2C%20id%2C%20artists(name)))&limit=50";
    private static final String WORKOUT_ENDPOINT = "https://api.spotify.com/v1/playlists/37i9dQZF1DX76Wlfdnj7AP/" +
            "tracks?market=US&fields=items(track(name%2C%20id%2C%20artists(name)))&limit=50";
    private static final String SONG_URL = "spotify:track:";
    private RequestQueue mqueue;
    private int playlist_id;
    private String token;

    public PlaylistService(RequestQueue rq, int id, String tok){
        this.mqueue = rq;
        this.playlist_id = id;
        this.token = tok;
    }

    public void get(final VolleyCallBack callBack){
        String endpoint = "";
        switch (this.playlist_id){
            case 0:
                endpoint = RAP_ENDPOINT;
                break;
            case 1:
                endpoint = COUNTRY_ENDPOINT;
                break;
            case 2:
                endpoint = POP_ENDPOINT;
                break;
            case 3:
                endpoint = HIPHOP_ENDPOINT;
                break;
            case 4:
                endpoint = HAPPY_ENDPOINT;
                break;
            case 5:
                endpoint = SAD_ENDPOINT;
                break;
            case 6:
                endpoint = WORKOUT_ENDPOINT;
                break;
            default:
                endpoint = RAP_ENDPOINT;
                break;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(endpoint, null, response -> {
            Iterator<String> keys = response.keys();
            String key = keys.next();
            try {
                Log.d("SpotifyActivity", key);
                JSONArray tmp = response.getJSONArray("items");
                Log.d("SpotifyActivity", "" + tmp.length());
                for (int i = 0; i < tmp.length(); i++){
                    JSONObject track = tmp.getJSONObject(i);
                    JSONObject song = track.getJSONObject("track");
                    JSONArray artistsArray = song.getJSONArray("artists");
                    JSONObject a = artistsArray.getJSONObject(0);
                    String artist = a.getString("name");
                    String id = song.getString("id");
                    String name = song.getString("name");
                    String url = SONG_URL + id;
                    Log.d("SpotifyActivity", name);
                    Log.d("SpotifyActivity", artist);
                    Log.d("SpotifyActivity", url);
                    PlayerViewModel.addToStack(new Song(name, artist, url));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            callBack.onSuccess();
        }, error -> get(() -> {} )) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String,String>();
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        mqueue.add(jsonObjectRequest);
    }
}
