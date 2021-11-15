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

import com.example.musicapp.MainActivity;
import com.example.musicapp.R;
import com.example.musicapp.StartupActivity;
import com.example.musicapp.database.FirebaseReadWrite;
import com.example.musicapp.models.Playlist;
import com.example.musicapp.models.Song;
import com.example.musicapp.ui.history.HistoryRecyclerViewAdapter;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private Spinner playlistSpinner;
    private Button logout;
    List<Playlist> playlistList;
    SettingsPlaylistSpinnerAdapter adapter;
    boolean loaded;
    private FirebaseReadWrite database;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        MutableLiveData<Playlist> mPlaylist = settingsViewModel.getPlaylistLiveData();
        mPlaylist.observe(getViewLifecycleOwner(), playlistUpdateObserver);

        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        playlistSpinner = v.findViewById(R.id.playlist_spinner);
        logout = v.findViewById(R.id.logout);
        playlistList = new ArrayList<>();

        this.database = FirebaseReadWrite.FirebaseReadWrite();

        // TODO: get spotify playlists here
        playlistList.add( new Playlist("rap", "url1"));
        playlistList.add( new Playlist("pop", "url2"));
        playlistList.add( new Playlist("bluegrass", "url3"));

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
                if (loaded) {
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
        return v;
    }

    Observer<Playlist> playlistUpdateObserver = new Observer<Playlist>() {
        @Override
        public void onChanged(Playlist playlist) {
            loaded = true;
            playlistSpinner.setEnabled(true);
            adapter.playlists = playlistList;
            String url = playlist.getUrl();
            int index = 0;
            for (int x = 0; x < playlistList.size(); x++) {
                if (playlistList.get(x).getUrl().equals(url)) {
                    index = x;
                    break;
                }
            }
            playlistSpinner.setSelection(index);
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel
    }

}