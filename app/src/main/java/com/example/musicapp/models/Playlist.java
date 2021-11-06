package com.example.musicapp.models;

public class Playlist {
    private String name;
    private String url;

    public Playlist(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

}
