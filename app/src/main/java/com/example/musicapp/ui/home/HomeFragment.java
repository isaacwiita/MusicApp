package com.example.musicapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.musicapp.R;
import com.example.musicapp.databinding.FragmentHomeBinding;
import com.example.musicapp.models.Playlist;
import com.example.musicapp.ui.history.HistoryFragment;
import com.example.musicapp.ui.settings.SettingsFragment;
import com.example.musicapp.ui.settings.SettingsPlaylistSpinnerAdapter;
import com.example.musicapp.ui.settings.SettingsViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private Button button_rec;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        button_rec = v.findViewById(R.id.button_rec);

        button_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // adapted from https://stackoverflow.com/questions/32700818/how-to-open-a-fragment-on-button-click-from-a-fragment-in-android
    // not working
    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(((ViewGroup)getView().getParent()).getId(), someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}