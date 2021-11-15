package com.example.musicapp.ui.settings;

import android.provider.Settings;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.database.FirebaseReadWrite;
import com.example.musicapp.models.Playlist;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class SettingsViewModel extends ViewModel {

    private MutableLiveData<Playlist> mPlaylist;

    private FirebaseReadWrite mDatabase;

    public SettingsViewModel() {
        mPlaylist = new MutableLiveData<>();
        mDatabase = FirebaseReadWrite.FirebaseReadWrite(); //singleton class so don't need to call constructor.
        mDatabase.getPlaylistOfUser(mPlaylist); //gets counter data and stores it in mTestValue.
    }

    public MutableLiveData<Playlist> getPlaylistLiveData() {
        return mPlaylist;
    }

    public void updatePlaylist(Playlist p) {
        mDatabase.updatePlaylistOfUser(p);
        mPlaylist.setValue(p);
    }

}