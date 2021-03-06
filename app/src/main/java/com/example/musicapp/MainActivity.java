package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.musicapp.spotify.SpotifyWrapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.musicapp.databinding.ActivityMainBinding;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class MainActivity extends AppCompatActivity {

private ActivityMainBinding binding;

    private SpotifyWrapper spotify;
    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("lifecycle method", "onCreate has been called");

        this.spotify = SpotifyWrapper.SpotifyWrapper();
        //this.spotify.auth_lib_connection(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_listen, R.id.navigation_history, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lifecycle method", "onStart has been called");
//        this.spotify = SpotifyWrapper.SpotifyWrapper();
//        this.spotify.connectUserSpotify(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lifecycle method", "onResume has been called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lifecycle method", "onPause has been called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lifecycle method", "onStop has been called");
        //this.spotify.disconnectRemote();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lifecycle method", "onDestroy has been called");
        this.spotify.disconnectRemote();
        Log.d("SpotifyActivity", "MainActivity OnDestroy");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    this.spotify.setAccessToken(response.getAccessToken());
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

}