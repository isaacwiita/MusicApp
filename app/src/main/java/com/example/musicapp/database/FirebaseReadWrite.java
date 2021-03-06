package com.example.musicapp.database;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.musicapp.RegisterActivity;
import com.example.musicapp.models.Playlist;
import com.example.musicapp.models.Song;
import com.example.musicapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//singleton class for database access
public class FirebaseReadWrite {

    private final String SONGS_ATTRIBUTE_NAME = "songs";
    private final String SONG_NAME_KEY = "name";
    private final String SONG_ARTIST_KEY = "artist";
    private final String SONG_URL_KEY = "url";

    private final String LIKED_SONGS_ATTRIBUTE_NAME = "liked_songs";
    private final String DISLIKED_SONGS_ATTRIBUTE_NAME = "disliked_songs";

//    private final String LIKED_SONGS_INDEX_NAME = "liked_index";

    private final String PLAYLIST_ATTRIBUTE_NAME = "playlist";
    private final String PLAYLIST_NAME_KEY = "name";
    private final String PLAYLIST_URL_KEY = "url";

    private final String USERS = "users";

    private static FirebaseReadWrite single_instance = null;
    private DatabaseReference mDatabase;
    final private String FB_TAG = "FirebaseDatabase";

    private FirebaseAuth auth;
    private String uid;
    private FirebaseUser user;

    //private constructor for singleton purposes
    private FirebaseReadWrite(){
        this.uid = "0";
        this.auth = FirebaseAuth.getInstance();
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

    public void userLikeSong(Song song) {
        Map<String, Object> songs = new HashMap<>();
        String songId = song.getUrl().substring(song.getUrl().lastIndexOf(":") + 1);
        songs.put(songId, song);
        Log.d(FB_TAG, songs.toString());
        this.mDatabase.child(getUserPathTo(SONGS_ATTRIBUTE_NAME)).updateChildren(songs).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(FB_TAG, "Song successfully liked: " + song.getName());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(FB_TAG, "Song like failed: " + song.getName());
            }
        });
    }

    private void getSongCount(MutableLiveData<Long> mCount) {
        this.mDatabase.child(getUserPathTo(SONGS_ATTRIBUTE_NAME)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mCount.setValue(snapshot.getChildrenCount());
                Log.d(FB_TAG, "Read liked song count: " + mCount.getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(FB_TAG, "Failed to get liked song count");
            }
        });
    }

    // TODO: optionally implement
    public void userDislikeSong(Song song){
    }

    // get songs list
    public void getSongListOfUser(MutableLiveData<List<Song>> mSongsList){
        this.mDatabase.child(getUserPathTo(SONGS_ATTRIBUTE_NAME)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Song> songs = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Song s = ds.getValue(Song.class);
                    songs.add(s);
                }
//                for (int x = 0; x < snapshot.getChildrenCount(); x++) {
//                    String name = snapshot.child(String.valueOf(x)).child(SONG_NAME_KEY).getValue(String.class);
//                    String artistName = snapshot.child(String.valueOf(x)).child(SONG_ARTIST_KEY).getValue(String.class);
//                    String url = snapshot.child(String.valueOf(x)).child(SONG_URL_KEY).getValue(String.class);
//                    songs.add(new Song(name, artistName, url));
//                }
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
    public void getPlaylistOfUser(MutableLiveData<Playlist> mPlaylist){
        this.mDatabase.child(getUserPathTo(PLAYLIST_ATTRIBUTE_NAME)).addListenerForSingleValueEvent(new ValueEventListener() {
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
    public void updatePlaylistOfUser(Playlist p){
        mDatabase.child(getUserPathTo(PLAYLIST_ATTRIBUTE_NAME + "/" + PLAYLIST_URL_KEY)).setValue(p.getUrl()).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        mDatabase.child(getUserPathTo(PLAYLIST_ATTRIBUTE_NAME + "/" + PLAYLIST_NAME_KEY)).setValue(p.getName()).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private String getUserPathTo(String userAttributeName) {
        return "users/" + this.getUid() + "/" + userAttributeName;
    }

    public FirebaseAuth getAuth(){
        return this.auth;
    }

    public void setUser(FirebaseUser newUser){
        this.user = newUser;
        setUid(this.user.getUid());
        Log.d(FB_TAG, this.uid);
    }

    public void setUid(String id){
        this.uid = id;
    }

    public FirebaseUser getUser(){
        return this.user;
    }

    public String getUid(){
        return this.uid;
    }

    public void createUser(String name){
        Map<String, Object> users = new HashMap<>();
        users.put(this.uid, new User(name));
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mDatabase.child(USERS).updateChildren(users).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void logout(){
        this.auth.signOut();
    }

    //further development ...
}
