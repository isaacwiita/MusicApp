package com.example.musicapp;

import static org.junit.Assert.assertEquals;

import com.example.musicapp.models.Song;

import org.junit.Test;

/**
 * Unit tests for the Song model.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SongTest {
    @Test
    public void song_creation() {
        String name = "name";
        String artist = "artist";
        String url = "url";
        Song s = new Song(name, artist, url);
        assertEquals(s.getName(), name);
        assertEquals(s.getArtist(), artist);
        assertEquals(s.getUrl(), url);
    }
}