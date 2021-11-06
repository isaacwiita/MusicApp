package com.example.musicapp.ui.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.musicapp.R;
import com.example.musicapp.models.Playlist;

import java.util.List;

public class SettingsPlaylistSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    List<Playlist> playlists;
    Context context;
    int resource;

    public SettingsPlaylistSpinnerAdapter(Context context, int resource, List<Playlist> playlists) {
        this.context = context;
        this.resource = resource;
        this.playlists = playlists;
    }

    @Override
    public int getCount() {
        return playlists.size();
    }

    @Override
    public Object getItem(int i) {
        return playlists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Playlist playlist = (Playlist) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.playlist_row, parent, false);
        }
        TextView playlistName = convertView.findViewById(R.id.playlist_name_text);
        playlistName.setText(playlist.getName());
        return convertView;
    }

//    @Override
    public View getDropdownView(int position, View convertView, ViewGroup parent) {
        Playlist playlist = (Playlist) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.playlist_row, parent, false);
        }
        TextView playlistName = convertView.findViewById(R.id.playlist_name_text);
        playlistName.setText(playlist.getName());
        return convertView;
    }


}
