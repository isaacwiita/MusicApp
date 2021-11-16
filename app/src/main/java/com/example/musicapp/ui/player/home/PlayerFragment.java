package com.example.musicapp.ui.player.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.musicapp.LoginActivity;
import com.example.musicapp.R;
import com.example.musicapp.database.FirebaseReadWrite;
import com.example.musicapp.models.Playlist;
import com.example.musicapp.models.Song;
import com.example.musicapp.spotify.SpotifyWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PlayerFragment extends Fragment implements View.OnClickListener {

    private PlayerViewModel playerViewModel;
    private TextView playerText;
    private TextView songNameText;
    private Button likeButton;
    private Button dislikeButton;
    private Button playButton;
    private Button pauseButton;
    private Button spotifyButton;
    private FirebaseReadWrite mDatabase;
    private MutableLiveData<Playlist> mPlaylist;

    private SpotifyWrapper spotify;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDatabase = FirebaseReadWrite.FirebaseReadWrite();
        mPlaylist = new MutableLiveData<>();
        mDatabase.getPlaylistOfUser(mPlaylist);

        this.spotify = SpotifyWrapper.SpotifyWrapper();
        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        View v = inflater.inflate(R.layout.fragment_player, container, false);

        playerText = v.findViewById(R.id.player_text);
        playerText.setText(playerViewModel.getCategoryName());

        songNameText = v.findViewById(R.id.song_name_text);
        Song s = playerViewModel.getCurrentSong();
        songNameText.setText(s.getName() + "\n" + s.getArtist());

        likeButton = v.findViewById(R.id.like_button);
        likeButton.setOnClickListener(this);
        dislikeButton = v.findViewById(R.id.dislike_button);
        dislikeButton.setOnClickListener(this);

        playButton = v.findViewById(R.id.play_button);
        pauseButton = v.findViewById(R.id.pause_button);
        spotifyButton = v.findViewById(R.id.spotify_button);
        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        spotifyButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onStart(){
        super.onStart();
        this.spotify = SpotifyWrapper.SpotifyWrapper();
        Song song = playerViewModel.getCurrentSong();
        this.spotify.connectUserSpotify(getContext(), song.getUrl());

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.spotify.disconnectRemote();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.like_button:
                playerViewModel.likeCurrentSong();
                addSongToPlaylist();
                Song song = playerViewModel.getNextSong();
                songNameText.setText(song.getName() + "\n" + song.getArtist());
                this.spotify.connectUserSpotify(getContext(), song.getUrl());
                break;
            case R.id.dislike_button:
                playerViewModel.dislikeCurrentSong();
                Song song1 = playerViewModel.getNextSong();
                songNameText.setText(song1.getName() + "\n" + song1.getArtist());
                this.spotify.connectUserSpotify(getContext(), song1.getUrl());
                break;
            case R.id.play_button:
                Toast.makeText(this.getContext(), "play button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pause_button:
                Toast.makeText(this.getContext(), "pause button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.spotify_button:
                Intent launcher = new Intent(Intent.ACTION_VIEW, Uri.parse(playerViewModel.getCurrentSong().getUrl()));
                getContext().startActivity(launcher);
                break;
            default:
                break;
        }
    }

    private void addSongToPlaylist() {

        String playlistId = mPlaylist.getValue().getUrl();
        playlistId = playlistId.substring(playlistId.lastIndexOf(":") + 1);

//        String playlistId = "59yfMBAMu4gP4dLA3rSy2r";
        String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";

        url = url + "?uris=" + playerViewModel.getCurrentSong().getUrl();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("apitest", "response: " + response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("apitest", "error: " + error);
            }
        }) {

            // bearer token
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + spotify.getAccessToken());
                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);



//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.i("apitest", "response: " + response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("apitest", "error: " + error);
//            }
//        }) {
//            // bearer token
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", "Bearer " + "BQB0ODdLg_D49HviPZdxymSRAuq3NUmi9mFSO1atMh2GiC1xgQhyFfDb8fiEIqEgCESM6zx9pHJj2m2JYMCXBtK8bHc7BHxQr22n-fKTuCjT_ji-zUNPnfYGCreGRPANy9wiRluTBC2rHqnkGdc7gKClIYCcfoB_5X7JoKIeuIwPvXFNoPILur8XNT0v8jA4wd6Q64i2WZ_h_OchNXkf4yxZhPQ9HTqE");
////                params.put("Authorization", "Bearer " + spotify.getAccessToken());
//                return params;
//            }
//        };
//
//        RequestQueue queue = Volley.newRequestQueue(getContext());
//        queue.add(jsonObjectRequest);

    }

//    Observer<Playlist> playlistObserver = new Observer<Playlist>() {
//        @Override
//        public void onChanged(Playlist playlist) {
//        }
//    };

}