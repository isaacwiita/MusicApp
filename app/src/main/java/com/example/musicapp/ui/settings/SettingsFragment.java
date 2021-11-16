package com.example.musicapp.ui.settings;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.musicapp.MainActivity;
import com.example.musicapp.R;
import com.example.musicapp.StartupActivity;
import com.example.musicapp.database.FirebaseReadWrite;
import com.example.musicapp.models.Playlist;
import com.example.musicapp.models.Song;
import com.example.musicapp.spotify.SpotifyWrapper;
import com.example.musicapp.ui.history.HistoryRecyclerViewAdapter;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private Spinner playlistSpinner;
    private Button logout;
    List<Playlist> playlistList;
    SettingsPlaylistSpinnerAdapter adapter;

    MutableLiveData<Playlist> mPlaylist;
    boolean loaded;
    boolean populated;
    private FirebaseReadWrite database;
    private SpotifyWrapper spotify;
    private TextView loggedInText;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.spotify = SpotifyWrapper.SpotifyWrapper();
        this.spotify.pause();

        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        mPlaylist = settingsViewModel.getPlaylistLiveData();
        mPlaylist.observe(getViewLifecycleOwner(), playlistUpdateObserver);

        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        playlistSpinner = v.findViewById(R.id.playlist_spinner);
        logout = v.findViewById(R.id.logout);
        playlistList = new ArrayList<>();

        this.database = FirebaseReadWrite.FirebaseReadWrite();

        loggedInText = v.findViewById(R.id.logged_in_text);
        loggedInText.setText("Logged in as: " + database.getUser().getEmail());

        // get spotify playlists

        MutableLiveData<String> mJsonResponse = new MutableLiveData<>();
        getSpotifyPlaylists(mJsonResponse);
        mJsonResponse.observe(getViewLifecycleOwner(), jsonResponseObserver);
        populated = false;

        // TODO: get spotify playlists here
//        playlistList.add( new Playlist("rap", "url1"));
//        playlistList.add( new Playlist("pop", "url2"));
//        playlistList.add( new Playlist("bluegrass", "url3"));

        // empty, disabled list while loading
        List<Playlist> emptyPlaylistList = new ArrayList<>();
        emptyPlaylistList.add(new Playlist("loading...", ""));
        adapter = new SettingsPlaylistSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, emptyPlaylistList);
        loaded = false;

        playlistSpinner.setAdapter(adapter);

        playlistSpinner.setSelection(0);
        playlistSpinner.setEnabled(false);

        playlistSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (loaded && populated) {
                    settingsViewModel.updatePlaylist(playlistList.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO: what goes here ?
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.logout();
                startActivity(new Intent(getActivity(), StartupActivity.class));
            }
        });
        Log.d("SpotifyActivity", "SettingsFragment OnCreateView");
        return v;
    }

    private void getSpotifyPlaylists(MutableLiveData<String> jsonResponse) {
        Log.i("apitest", "starting");
        String userId = "6izgnv3xya6evl0r5ol49efre";
        String url ="https://api.spotify.com/v1/users/" + userId + "/playlists";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("apitest", "response: " + response);
                jsonResponse.setValue(response);
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
                // TODO: change this to the actual token
                params.put("Authorization", "Bearer " + "BQDAjeaH5ob-Khv5ji8YpWmajY-o1cdCv7ykPZt4bwZXG6Yo3zT2wV_YHdM5WAswM-Ij0pJhfuVzJ4aGme5fnB1pMNRureGjCtnFd7hyvSRkEbFgF3YNooIGN13snyLTb9i0-1KRTPQHRwLagypfxZG9MMGYMxvLsTjayq5ZnMkYziXqqC91qvsUFkGZqg");
                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    Observer<String> jsonResponseObserver = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            String jsonString = s;
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray playlists = jsonObject.getJSONArray("items");
                for (int x = 0; x < playlists.length(); x++) {
                    JSONObject playlist = playlists.getJSONObject(x);
                    String name = playlist.getString("name");
                    String url = playlist.getString("uri");
                    Playlist p = new Playlist(name, url);
                    playlistList.add(p);
                }
                populated = true;
                if (loaded) {
                    setPlaylistSpinner();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Observer<Playlist> playlistUpdateObserver = new Observer<Playlist>() {
        @Override
        public void onChanged(Playlist playlist) {
            loaded = true;
            if (populated) {
                setPlaylistSpinner();
            }
        }
    };

    private void setPlaylistSpinner() {
        playlistSpinner.setEnabled(true);
        adapter.playlists = playlistList;
        String url = mPlaylist.getValue().getUrl();
        int index = 0;
        for (int x = 0; x < playlistList.size(); x++) {
            if (playlistList.get(x).getUrl().equals(url)) {
                index = x;
                break;
            }
        }
        playlistSpinner.setSelection(index);
    }

    @Override
    public void onStart(){
        super.onStart();
        this.spotify.pause();
        Log.d("SpotifyActivity", "SettingsFragment OnStart");
    }

    @Override
    public void onResume(){
        super.onResume();
        this.spotify.pause();
        Log.d("SpotifyActivity", "SettingsFragment OnResume");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel
    }




    private String getSpotifyPlaylistsHardcoded() {

        // sample json
        return "{\n" +
                "    \"href\": \"https://api.spotify.com/v1/users/6izgnv3xya6evl0r5ol49efre/playlists?offset=0&limit=20\",\n" +
                "    \"items\": [\n" +
                "        {\n" +
                "            \"collaborative\": false,\n" +
                "            \"description\": \"\",\n" +
                "            \"external_urls\": {\n" +
                "                \"spotify\": \"https://open.spotify.com/playlist/1AOzOgUtnblvNhltz52cLj\"\n" +
                "            },\n" +
                "            \"href\": \"https://api.spotify.com/v1/playlists/1AOzOgUtnblvNhltz52cLj\",\n" +
                "            \"id\": \"1AOzOgUtnblvNhltz52cLj\",\n" +
                "            \"images\": [],\n" +
                "            \"name\": \"random playlist\",\n" +
                "            \"owner\": {\n" +
                "                \"display_name\": \"isaacwiita\",\n" +
                "                \"external_urls\": {\n" +
                "                    \"spotify\": \"https://open.spotify.com/user/6izgnv3xya6evl0r5ol49efre\"\n" +
                "                },\n" +
                "                \"href\": \"https://api.spotify.com/v1/users/6izgnv3xya6evl0r5ol49efre\",\n" +
                "                \"id\": \"6izgnv3xya6evl0r5ol49efre\",\n" +
                "                \"type\": \"user\",\n" +
                "                \"uri\": \"spotify:user:6izgnv3xya6evl0r5ol49efre\"\n" +
                "            },\n" +
                "            \"primary_color\": null,\n" +
                "            \"public\": true,\n" +
                "            \"snapshot_id\": \"MSw2OWQ3ZjU3OGJhZGUwNDE1MWVjN2JhZWMzYjIyMjllMjllMzJhYmQ1\",\n" +
                "            \"tracks\": {\n" +
                "                \"href\": \"https://api.spotify.com/v1/playlists/1AOzOgUtnblvNhltz52cLj/tracks\",\n" +
                "                \"total\": 0\n" +
                "            },\n" +
                "            \"type\": \"playlist\",\n" +
                "            \"uri\": \"spotify:playlist:1AOzOgUtnblvNhltz52cLj\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"collaborative\": false,\n" +
                "            \"description\": \"\",\n" +
                "            \"external_urls\": {\n" +
                "                \"spotify\": \"https://open.spotify.com/playlist/2owXwX7fZMsSBScYygNAe1\"\n" +
                "            },\n" +
                "            \"href\": \"https://api.spotify.com/v1/playlists/2owXwX7fZMsSBScYygNAe1\",\n" +
                "            \"id\": \"2owXwX7fZMsSBScYygNAe1\",\n" +
                "            \"images\": [],\n" +
                "            \"name\": \"different playlist\",\n" +
                "            \"owner\": {\n" +
                "                \"display_name\": \"isaacwiita\",\n" +
                "                \"external_urls\": {\n" +
                "                    \"spotify\": \"https://open.spotify.com/user/6izgnv3xya6evl0r5ol49efre\"\n" +
                "                },\n" +
                "                \"href\": \"https://api.spotify.com/v1/users/6izgnv3xya6evl0r5ol49efre\",\n" +
                "                \"id\": \"6izgnv3xya6evl0r5ol49efre\",\n" +
                "                \"type\": \"user\",\n" +
                "                \"uri\": \"spotify:user:6izgnv3xya6evl0r5ol49efre\"\n" +
                "            },\n" +
                "            \"primary_color\": null,\n" +
                "            \"public\": true,\n" +
                "            \"snapshot_id\": \"MSxlN2I5YmMwZmM2ZDc4MDNiYTQ2MGExZTc1NDk0YTU5ZjRiNzI5MjNl\",\n" +
                "            \"tracks\": {\n" +
                "                \"href\": \"https://api.spotify.com/v1/playlists/2owXwX7fZMsSBScYygNAe1/tracks\",\n" +
                "                \"total\": 0\n" +
                "            },\n" +
                "            \"type\": \"playlist\",\n" +
                "            \"uri\": \"spotify:playlist:2owXwX7fZMsSBScYygNAe1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"collaborative\": false,\n" +
                "            \"description\": \"\",\n" +
                "            \"external_urls\": {\n" +
                "                \"spotify\": \"https://open.spotify.com/playlist/59yfMBAMu4gP4dLA3rSy2r\"\n" +
                "            },\n" +
                "            \"href\": \"https://api.spotify.com/v1/playlists/59yfMBAMu4gP4dLA3rSy2r\",\n" +
                "            \"id\": \"59yfMBAMu4gP4dLA3rSy2r\",\n" +
                "            \"images\": [],\n" +
                "            \"name\": \"epic playlist\",\n" +
                "            \"owner\": {\n" +
                "                \"display_name\": \"isaacwiita\",\n" +
                "                \"external_urls\": {\n" +
                "                    \"spotify\": \"https://open.spotify.com/user/6izgnv3xya6evl0r5ol49efre\"\n" +
                "                },\n" +
                "                \"href\": \"https://api.spotify.com/v1/users/6izgnv3xya6evl0r5ol49efre\",\n" +
                "                \"id\": \"6izgnv3xya6evl0r5ol49efre\",\n" +
                "                \"type\": \"user\",\n" +
                "                \"uri\": \"spotify:user:6izgnv3xya6evl0r5ol49efre\"\n" +
                "            },\n" +
                "            \"primary_color\": null,\n" +
                "            \"public\": true,\n" +
                "            \"snapshot_id\": \"MSxhYjFlZmQzMTlmOGVmMjFmZWI5YTEwYTNmNDE1ODVmMjU0ZjY4NjQy\",\n" +
                "            \"tracks\": {\n" +
                "                \"href\": \"https://api.spotify.com/v1/playlists/59yfMBAMu4gP4dLA3rSy2r/tracks\",\n" +
                "                \"total\": 0\n" +
                "            },\n" +
                "            \"type\": \"playlist\",\n" +
                "            \"uri\": \"spotify:playlist:59yfMBAMu4gP4dLA3rSy2r\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"collaborative\": false,\n" +
                "            \"description\": \"\",\n" +
                "            \"external_urls\": {\n" +
                "                \"spotify\": \"https://open.spotify.com/playlist/46uUxgsRffOskdkn6n8VqJ\"\n" +
                "            },\n" +
                "            \"href\": \"https://api.spotify.com/v1/playlists/46uUxgsRffOskdkn6n8VqJ\",\n" +
                "            \"id\": \"46uUxgsRffOskdkn6n8VqJ\",\n" +
                "            \"images\": [],\n" +
                "            \"name\": \"chill playlist\",\n" +
                "            \"owner\": {\n" +
                "                \"display_name\": \"isaacwiita\",\n" +
                "                \"external_urls\": {\n" +
                "                    \"spotify\": \"https://open.spotify.com/user/6izgnv3xya6evl0r5ol49efre\"\n" +
                "                },\n" +
                "                \"href\": \"https://api.spotify.com/v1/users/6izgnv3xya6evl0r5ol49efre\",\n" +
                "                \"id\": \"6izgnv3xya6evl0r5ol49efre\",\n" +
                "                \"type\": \"user\",\n" +
                "                \"uri\": \"spotify:user:6izgnv3xya6evl0r5ol49efre\"\n" +
                "            },\n" +
                "            \"primary_color\": null,\n" +
                "            \"public\": true,\n" +
                "            \"snapshot_id\": \"MSw1YzAzNDI1NzFiYjIwNDBjMGQzZGIwZDIyNjdiNjhmNjU0NzM3NzRm\",\n" +
                "            \"tracks\": {\n" +
                "                \"href\": \"https://api.spotify.com/v1/playlists/46uUxgsRffOskdkn6n8VqJ/tracks\",\n" +
                "                \"total\": 0\n" +
                "            },\n" +
                "            \"type\": \"playlist\",\n" +
                "            \"uri\": \"spotify:playlist:46uUxgsRffOskdkn6n8VqJ\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"collaborative\": false,\n" +
                "            \"description\": \"\",\n" +
                "            \"external_urls\": {\n" +
                "                \"spotify\": \"https://open.spotify.com/playlist/3PyGcAm7JoiAprvirbfJvV\"\n" +
                "            },\n" +
                "            \"href\": \"https://api.spotify.com/v1/playlists/3PyGcAm7JoiAprvirbfJvV\",\n" +
                "            \"id\": \"3PyGcAm7JoiAprvirbfJvV\",\n" +
                "            \"images\": [],\n" +
                "            \"name\": \"cool playlist\",\n" +
                "            \"owner\": {\n" +
                "                \"display_name\": \"isaacwiita\",\n" +
                "                \"external_urls\": {\n" +
                "                    \"spotify\": \"https://open.spotify.com/user/6izgnv3xya6evl0r5ol49efre\"\n" +
                "                },\n" +
                "                \"href\": \"https://api.spotify.com/v1/users/6izgnv3xya6evl0r5ol49efre\",\n" +
                "                \"id\": \"6izgnv3xya6evl0r5ol49efre\",\n" +
                "                \"type\": \"user\",\n" +
                "                \"uri\": \"spotify:user:6izgnv3xya6evl0r5ol49efre\"\n" +
                "            },\n" +
                "            \"primary_color\": null,\n" +
                "            \"public\": true,\n" +
                "            \"snapshot_id\": \"MSw5MmI2MWU3ZWQ3Y2NiOWY1MGY2OWI5MmYxNzE0MGJlYmJkZDc3NjFl\",\n" +
                "            \"tracks\": {\n" +
                "                \"href\": \"https://api.spotify.com/v1/playlists/3PyGcAm7JoiAprvirbfJvV/tracks\",\n" +
                "                \"total\": 0\n" +
                "            },\n" +
                "            \"type\": \"playlist\",\n" +
                "            \"uri\": \"spotify:playlist:3PyGcAm7JoiAprvirbfJvV\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"limit\": 20,\n" +
                "    \"next\": null,\n" +
                "    \"offset\": 0,\n" +
                "    \"previous\": null,\n" +
                "    \"total\": 5\n" +
                "}";
    }

}