package com.example.musicapp.ui.player.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.R;
import com.example.musicapp.models.Song;
import com.example.musicapp.ui.settings.SettingsFragment;

public class PlayerFragment extends Fragment implements View.OnClickListener {

    private PlayerViewModel playerViewModel;
    private TextView playerText;
    private TextView songNameText;
    private Button likeButton;
    private Button dislikeButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        View v = inflater.inflate(R.layout.fragment_player, container, false);

        playerText = v.findViewById(R.id.player_text);
        playerText.setText(playerViewModel.getCategoryName());

        songNameText = v.findViewById(R.id.song_name_text);
        Song s = playerViewModel.getNextSong();
        songNameText.setText(s.getName() + "\n" + s.getArtist());

        likeButton = v.findViewById(R.id.like_button);
        likeButton.setOnClickListener(this);
        dislikeButton = v.findViewById(R.id.dislike_button);
        dislikeButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.like_button:
                playerViewModel.likeCurrentSong();
                Song song = playerViewModel.getNextSong();
                songNameText.setText(song.getName() + "\n" + song.getArtist());
                break;
            case R.id.dislike_button:
                playerViewModel.dislikeCurrentSong();
                Song song1 = playerViewModel.getNextSong();
                songNameText.setText(song1.getName() + "\n" + song1.getArtist());
                break;
            default:
                break;
        }
    }
}