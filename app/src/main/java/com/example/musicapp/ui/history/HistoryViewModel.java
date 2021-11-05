package com.example.musicapp.ui.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.database.FirebaseReadWrite;
import com.example.musicapp.models.Song;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class HistoryViewModel extends ViewModel {

    private MutableLiveData<List<Song>> mSongsList;

    private FirebaseReadWrite mDatabase;
    private DatabaseReference ref;
    final private String TEST_PATH = "test";
    final private String SONGS_ATTRIBUTE = "songs";
    final private String SONGS_PATH = "test/songs";

    public HistoryViewModel() {
        mSongsList = new MutableLiveData<>();
        mDatabase = FirebaseReadWrite.FirebaseReadWrite(); //singleton class so don't need to call constructor.
        mDatabase.getSongListData(SONGS_PATH, SONGS_ATTRIBUTE, mSongsList); //gets counter data and stores it in mTestValue.
    }

    public MutableLiveData<List<Song>> getSongsListLiveData() {
        return mSongsList;
    }
}