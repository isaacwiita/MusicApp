package com.example.musicapp.models;

public class Song {
    public String name;
    public String artist;
    public String url;

    public Song(String name, String artistName, String url) {
        this.name = name;
        this.artist = artistName;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getArtistName() {
        return artist;
    }

    public String getUrl() {
        return url;
    }

}
