package com.example.musicapp;

import static org.junit.Assert.assertEquals;

import com.example.musicapp.models.User;

import org.junit.Test;

/**
 * Unit tests for the User model.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserTest {
    @Test
    public void user_creation() {
        String name = "name";
        User u = new User(name);
        assertEquals(u.name, name);
        assertEquals(u.playlist.getName(), "default");
        assertEquals(u.playlist.getUrl(), "url2");
    }
}