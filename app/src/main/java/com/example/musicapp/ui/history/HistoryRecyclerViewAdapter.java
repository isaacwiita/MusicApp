package com.example.musicapp.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.models.Song;

import java.util.List;


// code adapted from https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {

    private List<Song> mSongs;
    private LayoutInflater mInflator;
//    private ItemClickListener mItemClickListener;

    // constructor
    HistoryRecyclerViewAdapter(Context context, List<Song> songs) {
        this.mInflator = LayoutInflater.from(context);
        this.mSongs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflator.inflate(R.layout.song_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = mSongs.get(position);
        holder.nameTextView.setText(song.getName());
        holder.artistTextView.setText(song.getArtist());
    }

    @Override
    public int getItemCount() {
        if (mSongs == null) {
            return 0;
        }
        return mSongs.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {// implements View.OnClickListener {
        TextView nameTextView;
        TextView artistTextView;
        Button songButton;

        ViewHolder(View songView) {
            super(songView);
            nameTextView = songView.findViewById(R.id.playlist_name_text);
            artistTextView = songView.findViewById(R.id.artist_text);
            songButton = songView.findViewById(R.id.song_button);
            songButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(songView.getContext(), "play " + nameTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

//        @Override
//        public void onClick(View view) {
//            if (mItemClickListener != null) mItemClickListener.onItemClick(view, getAdapterPosition());
//        }
    }

    Song getSong(int index) {
        return mSongs.get(index);
    }

//    void setmOnItemClickListener(ItemClickListener itemClickListener) {
//        this.mItemClickListener = itemClickListener;
//    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
