package com.example.musicapp.ui.player.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.R;
import com.example.musicapp.database.FirebaseReadWrite;
import com.example.musicapp.models.Playlist;
import com.example.musicapp.models.Song;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PlayerViewModel extends ViewModel {

    public static int genreButtonId;
    private Stack<Song> songs;
    private Song currentSong;

    private FirebaseReadWrite mDatabase;
    private DatabaseReference ref;
    final private String ISAAC_ID = "3";

    public PlayerViewModel() {
        mDatabase = FirebaseReadWrite.FirebaseReadWrite();
        songs = new Stack<>();
        songs.push(new Song("moon", "kanye west", "url"));
        songs.push(new Song("ode to sleep", "twenty one pilots", "url"));
        songs.push(new Song("send the fisherman", "caamp", "url"));
        songs.push(new Song("brightside", "the lumineers", "url"));
        songs.push(new Song("jail", "kanye west", "url"));
        songs.push(new Song("donda chant", "kanye west", "url"));
        currentSong = songs.pop();
    }

    // TODO: use spotify
    public Song getNextSong() {
        if (songs.size() > 0) {
            currentSong = songs.pop();
            return currentSong;
        }
        else {
            songs.push(new Song("moon", "kanye west", "url"));
            songs.push(new Song("ode to sleep", "twenty one pilots", "url"));
            songs.push(new Song("send the fisherman", "caamp", "url"));
            songs.push(new Song("brightside", "the lumineers", "url"));
            songs.push(new Song("jail", "kanye west", "url"));
            songs.push(new Song("donda chant", "kanye west", "url"));
            currentSong = songs.pop();
            return currentSong;
        }
    }

    public void likeCurrentSong() {
        mDatabase.userLikeSong(ISAAC_ID, currentSong);
    }

    public void dislikeCurrentSong() {
        mDatabase.userDislikeSong(ISAAC_ID, currentSong);
    }

    public String getCategoryName() {
        switch(genreButtonId) {
            case(R.id.button_rec):
                return "Recommendations";
            case(R.id.button_pop):
                return "Pop";
            case(R.id.button_hiphop):
                return "Hip Hop";
            case(R.id.button_country):
                return "Country";
            case(R.id.button_rap):
                return "Rap";
            case(R.id.button_happy):
                return "Happy";
            case(R.id.button_sad):
                return "Sad";
            case(R.id.button_workout):
                return "Workout";
            default:
                return "category";

        }
    }

}