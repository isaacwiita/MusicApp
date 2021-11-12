package com.example.musicapp.ui.player.home;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.musicapp.R;

public class PlayerViewModel extends ViewModel {

    public static int genreButtonId;

    public PlayerViewModel() {
        Log.i("testing", String.valueOf(genreButtonId));
    }

    public String getCategoryName() {
        switch(genreButtonId) {
            case(R.id.button_rec):
                return "Recommendations";
            case(R.id.button_pop):
                return "Pop";
            case(R.id.button_hiphop):
                return "Hip Hop";
            case(R.id.button_country):
                return "Country";
            case(R.id.button_rap):
                return "Rap";
            case(R.id.button_happy):
                return "Happy";
            case(R.id.button_sad):
                return "Sad";
            case(R.id.button_workout):
                return "Workout";
            default:
                return "category";

        }
    }

}