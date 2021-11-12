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
import com.example.musicapp.ui.settings.SettingsFragment;

public class PlayerFragment extends Fragment {

    private PlayerViewModel playerViewModel;
    private TextView playerText;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        View v = inflater.inflate(R.layout.fragment_player, container, false);

        playerText = v.findViewById(R.id.player_text);
        playerText.setText(playerViewModel.getCategoryName());

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}