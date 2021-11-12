package com.example.musicapp.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.musicapp.models.Playlist;
import com.example.musicapp.models.Song;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//singleton class for database access
public class FirebaseReadWrite {

    private final String SONGS_ATTRIBUTE_NAME = "songs";
    private final String SONG_NAME_KEY = "name";
    private final String SONG_ARTIST_KEY = "artist";
    private final String SONG_URL_KEY = "url";

    private final String LIKED_SONGS_ATTRIBUTE_NAME = "liked_songs";
    private final String DISLIKED_SONGS_ATTRIBUTE_NAME = "disliked_songs";

    private final String PLAYLIST_ATTRIBUTE_NAME = "playlist";
    private final String PLAYLIST_NAME_KEY = "name";
    private final String PLAYLIST_URL_KEY = "url";

    private static FirebaseReadWrite single_instance = null;
    private DatabaseReference mDatabase;
    final private String FB_TAG = "FirebaseDatabase";

    //private constructor for singleton purposes
    private FirebaseReadWrite(){
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseReadWrite FirebaseReadWrite(){
        if (single_instance == null){
            single_instance = new FirebaseReadWrite();
        }
        return single_instance;
    }

    //Get int data based on precise path and attribute info and store into mTestValue.
    public void getIntegerData(String path, String attribute, MutableLiveData<Integer> mTestValue){
        this.mDatabase.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mTestValue.setValue(Integer.parseInt(snapshot.child(attribute).getValue().toString()));
                Log.d(FB_TAG, "Read Value: " + mTestValue.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(FB_TAG, "Failed to get Value");
            }
        });
    }

    // TODO: implement. look at using childByAutoId
    public void userLikeSong(String userId, Song song){
    }

    // TODO: implement. look at using childByAutoId
    public void userDislikeSong(String userId, Song song){
    }

    // get songs list
    public void getSongListOfUser(String userId, MutableLiveData<List<Song>> mSongsList){
        this.mDatabase.child(getUserPathTo(userId, SONGS_ATTRIBUTE_NAME)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Song> songs = new ArrayList<>();
                for (int x = 0; x < snapshot.getChildrenCount(); x++) {
                    String name = snapshot.child(String.valueOf(x)).child(SONG_NAME_KEY).getValue(String.class);
                    String artistName = snapshot.child(String.valueOf(x)).child(SONG_ARTIST_KEY).getValue(String.class);
                    String url = snapshot.child(String.valueOf(x)).child(SONG_URL_KEY).getValue(String.class);
                    songs.add(new Song(name, artistName, url));
                }
                mSongsList.setValue(songs);
                Log.d(FB_TAG, "Read songs: " + mSongsList.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(FB_TAG, "Failed to get list of songs");
            }
        });
    }

    // get playlist
    //Get int data based on precise path and attribute info and store into mTestValue.
    public void getPlaylistOfUser(String userId, MutableLiveData<Playlist> mPlaylist){
        Log.d(FB_TAG, getUserPathTo(userId, PLAYLIST_ATTRIBUTE_NAME));
        this.mDatabase.child(getUserPathTo(userId, PLAYLIST_ATTRIBUTE_NAME)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child(PLAYLIST_NAME_KEY).getValue().toString();
                String url = snapshot.child(PLAYLIST_URL_KEY).getValue().toString();
                Log.d(FB_TAG, name + " " + url);
                mPlaylist.setValue(new Playlist(name, url));
                Log.d(FB_TAG, "Read playlist: " + mPlaylist.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(FB_TAG, "Failed to get playlist");
            }
        });
    }

    //used to update the database. Requires precise path and value information.
    public void updatePlaylistOfUser(String userId, Playlist p){
        mDatabase.child(getUserPathTo(userId, PLAYLIST_ATTRIBUTE_NAME + "/" + PLAYLIST_URL_KEY)).setValue(p.getUrl()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(FB_TAG, "Playlist URL updated successfully to " + p.getUrl());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(FB_TAG, "Playlist URL upload failed");
            }
        });
        mDatabase.child(getUserPathTo(userId, PLAYLIST_ATTRIBUTE_NAME + "/" + PLAYLIST_NAME_KEY)).setValue(p.getName()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(FB_TAG, "Playlist name updated successfully to " + p.getName());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(FB_TAG, "Playlist name upload failed");
            }
        });
    }

    //used to update the database. Requires precise path and value information.
    public void updateData(String path, Object value){
        mDatabase.child(path).setValue(value).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(FB_TAG, "Data updated successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(FB_TAG, "Data upload failed");
            }
        });
    }

    private String getUserPathTo(String userId, String userAttributeName) {
        return "users/" + userId + "/" + userAttributeName;
    }

    //further development ...
}
