package com.example.musicapp.ui.history;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.database.FirebaseReadWrite;
import com.example.musicapp.models.Song;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class HistoryViewModel extends ViewModel {

    private MutableLiveData<List<Song>> mSongsList;

    private FirebaseReadWrite mDatabase;

    public HistoryViewModel() {
        mSongsList = new MutableLiveData<>();
        mDatabase = FirebaseReadWrite.FirebaseReadWrite(); //singleton class so don't need to call constructor.
        mDatabase.getSongListOfUser(mSongsList); //gets counter data and stores it in mTestValue.
    }

    public MutableLiveData<List<Song>> getSongsListLiveData() {
        return mSongsList;
    }
}