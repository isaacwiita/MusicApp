package com.example.musicapp;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.musicapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("lifecycle method", "onCreate has been called");

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lifecycle method", "onDestroy has been called");
    }

}