package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicapp.database.FirebaseReadWrite;
import com.google.firebase.auth.FirebaseAuth;

public class StartupActivity extends AppCompatActivity {

    private Button register;
    private Button login;

    FirebaseReadWrite database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        database = FirebaseReadWrite.FirebaseReadWrite();
        FirebaseAuth auth = this.database.getAuth();
        if (auth.getCurrentUser() != null) {
            database.setUser(auth.getCurrentUser());
            startActivity(new Intent(StartupActivity.this, MainActivity.class));
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartupActivity.this, RegisterActivity.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartupActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}