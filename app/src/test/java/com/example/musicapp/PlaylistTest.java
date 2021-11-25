package com.example.musicapp;

import static org.junit.Assert.assertEquals;

import com.example.musicapp.models.Playlist;

import org.junit.Test;

/**
 * Unit tests for the Playlist model.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PlaylistTest {
    @Test
    public void playlist_creation() {
        String name = "name";
        String url = "url";
        Playlist p = new Playlist(name, url);
        assertEquals(p.getName(), name);
        assertEquals(p.getUrl(), url);
    }
}