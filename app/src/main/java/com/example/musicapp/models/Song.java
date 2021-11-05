package com.example.musicapp.models;

public class Song {
    private String name;
    private String artistName;
    private String url;

    public Song(String name, String artistName, String url) {
        this.name = name;
        this.artistName = artistName;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getUrl() {
        return url;
    }

}
