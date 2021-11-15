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
        songs.push(new Song("moon", "kanye west", "spotify:track:7CC6UbCs4iGsePSzFxYxNn"));
        songs.push(new Song("ode to sleep", "twenty one pilots", "spotify:track:773xZHHSHJvC1z3AAtWgH6"));
        songs.push(new Song("send the fisherman", "caamp", "spotify:track:7kfpyjz5awLGwUCrMiODWw"));
        songs.push(new Song("brightside", "the lumineers", "spotify:track:1TX0ImiGMYiikRash29a2b"));
        songs.push(new Song("jail", "kanye west", "spotify:track:6d8HN8MqqbqrEUI2bvx0aG"));
        songs.push(new Song("donda chant", "kanye west", "spotify:track:7eSSmgq26BXr7xay3WKjfi"));
        currentSong = songs.pop();
    }

    // TODO: use spotify
    public Song getNextSong() {
        if (songs.size() > 0) {
            currentSong = songs.pop();
            return currentSong;
        }
        else {
            songs.push(new Song("moon", "kanye west", "spotify:track:7CC6UbCs4iGsePSzFxYxNn"));
            songs.push(new Song("ode to sleep", "twenty one pilots", "spotify:track:773xZHHSHJvC1z3AAtWgH6"));
            songs.push(new Song("send the fisherman", "caamp", "spotify:track:7kfpyjz5awLGwUCrMiODWw"));
            songs.push(new Song("brightside", "the lumineers", "spotify:track:1TX0ImiGMYiikRash29a2b"));
            songs.push(new Song("jail", "kanye west", "spotify:track:6d8HN8MqqbqrEUI2bvx0aG"));
            songs.push(new Song("donda chant", "kanye west", "spotify:track:7eSSmgq26BXr7xay3WKjfi"));
            currentSong = songs.pop();
            return currentSong;
        }
    }

    public Song getCurrentSong(){
        return this.currentSong;
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