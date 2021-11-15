package com.example.musicapp.models;

public class User {

    public String name;
    public Playlist playlist;
    public Song songs;

    public User(String user){
        this.name = user;
        this.playlist = new Playlist("default", "url2");
    }
}
