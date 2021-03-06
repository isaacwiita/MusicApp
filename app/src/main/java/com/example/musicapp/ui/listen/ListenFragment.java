package com.example.musicapp.ui.listen;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.musicapp.R;
import com.example.musicapp.database.FirebaseReadWrite;
import com.example.musicapp.spotify.SpotifyWrapper;

public class ListenFragment extends Fragment {

    private ListenViewModel mViewModel;
    private Button testButton;
    private TextView testTextView;
    private ListenViewModel listenViewModel;
    private SpotifyWrapper spotify;

    public static ListenFragment newInstance() {
        return new ListenFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.spotify = SpotifyWrapper.SpotifyWrapper();
        this.spotify.pause();
        View v = inflater.inflate(R.layout.fragment_listen, container, false);

        testTextView = v.findViewById(R.id.test_text_view);

        listenViewModel = new ViewModelProvider(this).get(ListenViewModel.class);
        listenViewModel.getTestValue().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer i) {
                testTextView.setText("Count: " + i.toString());
            }
        });

        testButton = v.findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenViewModel.incrementTestValue();
            }
        });
        Log.d("SpotifyActivity", "ListenFragment OnCreateView");
        return v;
    }

    @Override
    public void onStart(){
        super.onStart();
        this.spotify.pause();
        Log.d("SpotifyActivity", "ListenFragment OnStart");
    }

    @Override
    public void onResume(){
        super.onResume();
        this.spotify.pause();
        Log.d("SpotifyActivity", "ListenFragment OnResume");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListenViewModel.class);
        // TODO: Use the ViewModel
    }

}