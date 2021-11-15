package com.example.musicapp.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;

import com.example.musicapp.database.FirebaseReadWrite;
import com.example.musicapp.databinding.FragmentHistoryBinding;
import com.example.musicapp.models.Song;
import com.example.musicapp.spotify.SpotifyWrapper;
import com.google.api.ResourceDescriptor;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment { //implements HistoryRecyclerViewAdapter.ItemClickListener {

    private HistoryViewModel historyViewModel;
    private FragmentHistoryBinding binding;
    private RecyclerView recyclerView;
    private HistoryRecyclerViewAdapter adapter;
    private SpotifyWrapper spotify;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.spotify = SpotifyWrapper.SpotifyWrapper();
        this.spotify.pause();


        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        MutableLiveData<List<Song>> mSongsList = historyViewModel.getSongsListLiveData();
        mSongsList.observe(getViewLifecycleOwner(), songsListUpdateObserver);

        View v = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = v.findViewById(R.id.history_recycler);

//        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new HistoryRecyclerViewAdapter(this.getActivity(), mSongsList.getValue());
//        adapter.setmOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        return v;
    }

    Observer<List<Song>> songsListUpdateObserver = new Observer<List<Song>>() {
        @Override
        public void onChanged(List<Song> songs) {
            adapter = new HistoryRecyclerViewAdapter(getContext(), songs);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }
    };

    @Override
    public void onStart(){
        super.onStart();
        this.spotify.pause();
    }

    @Override
    public void onResume(){
        super.onResume();
        this.spotify.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    @Override
//    public void onItemClick(View view, int position) {
//        Toast.makeText(this.getContext(), "You clicked " + adapter.getSong(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
//    }
}