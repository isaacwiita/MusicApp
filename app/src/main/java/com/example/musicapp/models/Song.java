package com.example.musicapp.models;

public class Song {
    private String name;
    private String artist;
    private String url;

    public Song() {
        this.name = null;
        this.artist = null;
        this.url = null;
    }

    public Song(String name, String artist, String url) {
        this.name = name;
        this.artist = artist;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) { this.name = name; }

    public void setArtist(String artist) { this.artist = artist; }

    public void setUrl(String url) { this.url = url; }

}
