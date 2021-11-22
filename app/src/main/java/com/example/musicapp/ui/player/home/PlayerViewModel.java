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
import com.example.musicapp.spotify.SpotifyWrapper;

public class PlayerViewModel extends ViewModel {

    public static int genreButtonId;
    public static Stack<Song> songs = new Stack<>();
    private Song currentSong;
    private SpotifyWrapper spotify;

    private FirebaseReadWrite mDatabase;

    public PlayerViewModel() {
        mDatabase = FirebaseReadWrite.FirebaseReadWrite();
        this.spotify = SpotifyWrapper.SpotifyWrapper();
        if (genreButtonId == R.id.button_rap){
            this.spotify.getSongs(0);
            songs.push(new Song("Moon", "Kanye West", "spotify:track:7CC6UbCs4iGsePSzFxYxNn"));
            songs.push(new Song("Jail", "Kanye West", "spotify:track:6d8HN8MqqbqrEUI2bvx0aG"));
            songs.push(new Song("Donda Chant", "Kanye West", "spotify:track:7eSSmgq26BXr7xay3WKjfi"));
        }else if (genreButtonId == R.id.button_country){
            this.spotify.getSongs(1);
            songs.push(new Song("Life is a Highway", "Rascal Flatts", "spotify:track:2Fs18NaCDuluPG1DHGw1XG"));
            songs.push(new Song("Take Me Home, Country Roads", "John Denver", "spotify:track:13HaPtzk6OEREfoWcGY8xu"));
            songs.push(new Song("Cruise", "Florida Georgia Line", "spotify:track:0i5el041vd6nxrGEU8QRxy"));
        }else if (genreButtonId == R.id.button_pop){
            this.spotify.getSongs(2);
            songs.push(new Song("Circles", "Post Malone", "spotify:track:21jGcNKet2qwijlDFuPiPb"));
            songs.push(new Song("Perfect", "Ed Sheeran", "spotify:track:0tgVpDi06FyKpA1z0VMD4v"));
            songs.push(new Song("I Like Me Better", "Lauv", "spotify:track:1wjzFQodRWrPcQ0AnYnvQ9"));
        }else if (genreButtonId == R.id.button_happy){
            this.spotify.getSongs(4);
            songs.push(new Song("Levitating (feat. DaBaby)", "Dua Lipa", "spotify:track:463CkQjx2Zk1yXoBuierM9"));
            songs.push(new Song("Blinding Lights", "The Weeknd", "spotify:track:0VjIjW4GlUZAMYd2vXMi3b"));
            songs.push(new Song("Close to Me (with Diplo) (feat. Swae Lee)", "Ellie Goulding", "spotify:track:5JEx7HbmvHQQswJCsoo9rA"));
        }else if (genreButtonId == R.id.button_hiphop){
            this.spotify.getSongs(3);
            songs.push(new Song("Earfquake", "Tyler the Creator", "spotify:track:5hVghJ4KaYES3BFUATCYn0"));
            songs.push(new Song("Ballin' (with Roddy Rich)", "Mustard", "spotify:track:3QzAOrNlsabgbMwlZt7TAY"));
            songs.push(new Song("100 Degrees", "Rich Brian", "spotify:track:2ZDpSQfBdgkooeXw6oj3Uz"));
        }else if (genreButtonId == R.id.button_sad){
            this.spotify.getSongs(5);
            songs.push(new Song("SAD!", "XXXTENTACION", "spotify:track:3ee8Jmje8o58CHK66QrVC2"));
            songs.push(new Song("SLOW DANCING IN THE DARK", "Joji", "spotify:track:0rKtyWc8bvkriBthvHKY8d"));
            songs.push(new Song("Take Me to Church", "Hozier", "spotify:track:1CS7Sd1u5tWkstBhpssyjP"));
        }else if (genreButtonId == R.id.button_workout){
            this.spotify.getSongs(6);
            songs.push(new Song("Heat Waves", "Glass Animals", "spotify:track:7rZDh00Yae9leGR1AAYoKO"));
            songs.push(new Song("Laugh Now Cry Later (feat. Lil Durk)", "Drake", "spotify:track:2SAqBLGA283SUiwJ3xOUVI"));
            songs.push(new Song("ROCKSTAR (feat. Roddy Rich)", "DaBaby", "spotify:track:7ytR5pFWmSjzHJIeQkgog4"));
        } else {
            songs.push(new Song("moon", "kanye west", "spotify:track:7CC6UbCs4iGsePSzFxYxNn"));
            songs.push(new Song("ode to sleep", "twenty one pilots", "spotify:track:773xZHHSHJvC1z3AAtWgH6"));
            songs.push(new Song("send the fisherman", "caamp", "spotify:track:7kfpyjz5awLGwUCrMiODWw"));
            songs.push(new Song("brightside", "the lumineers", "spotify:track:1TX0ImiGMYiikRash29a2b"));
            songs.push(new Song("jail", "kanye west", "spotify:track:6d8HN8MqqbqrEUI2bvx0aG"));
            songs.push(new Song("donda chant", "kanye west", "spotify:track:7eSSmgq26BXr7xay3WKjfi"));
        }

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
        mDatabase.userLikeSong(currentSong);
    }

    public void dislikeCurrentSong() {
        mDatabase.userDislikeSong(currentSong);
    }

    public static void addToStack(Song s){
        songs.push(s);
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