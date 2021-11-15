package com.example.musicapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.musicapp.R;
import com.example.musicapp.spotify.SpotifyWrapper;
import com.example.musicapp.ui.player.home.PlayerViewModel;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;

    private Button button_rec;
    private Button button_pop;
    private Button button_hiphop;
    private Button button_country;
    private Button button_rap;
    private Button button_happy;
    private Button button_sad;
    private Button button_workout;

    private SpotifyWrapper spotify;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.spotify = SpotifyWrapper.SpotifyWrapper();
        this.spotify.pause();
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        button_rec = v.findViewById(R.id.button_rec);
        button_pop = v.findViewById(R.id.button_pop);
        button_hiphop = v.findViewById(R.id.button_hiphop);
        button_country = v.findViewById(R.id.button_country);
        button_rap = v.findViewById(R.id.button_rap);
        button_happy = v.findViewById(R.id.button_happy);
        button_sad = v.findViewById(R.id.button_sad);
        button_workout = v.findViewById(R.id.button_workout);

        button_rec.setOnClickListener(this);
        button_pop.setOnClickListener(this);
        button_hiphop.setOnClickListener(this);
        button_country.setOnClickListener(this);
        button_rap.setOnClickListener(this);
        button_happy.setOnClickListener(this);
        button_sad.setOnClickListener(this);
        button_workout.setOnClickListener(this);

//        button_rec.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_playerFragment);
//            }
//        });
        Log.d("SpotifyActivity", "HomeFragment OnCreateView");
        return v;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        this.spotify.pause();
        Log.d("SpotifyActivity", "HomeFragment OnDestroyView");
    }

    @Override
    public void onClick(View view) {
        PlayerViewModel.genreButtonId = view.getId();
        Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_playerFragment);
        Log.d("SpotifyActivity", "HomeFragment OnClick");
    }
}