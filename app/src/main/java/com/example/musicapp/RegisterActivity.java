package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.musicapp.database.FirebaseReadWrite;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private EditText email;
    private Button register;
    private FirebaseReadWrite database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        database = FirebaseReadWrite.FirebaseReadWrite();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_name = name.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this, "Empty Credentials!",
                            Toast.LENGTH_SHORT).
                            show();
                }else if (txt_password.length() < 6){
                    Toast.makeText
                            (RegisterActivity.this,
                                    "Please make sure your password is atleast 6 characters long",
                                    Toast.LENGTH_SHORT)
                            .show();
                }else {
                    registerUser(txt_email, txt_password, txt_name);
                }
            }
        });
    }

    private void registerUser(String email, String password, String name){
        FirebaseAuth auth = this.database.getAuth();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    database.setUser(auth.getCurrentUser());
                    database.createUser(name);
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                }else {
                    Toast.makeText(RegisterActivity.this, "Registration Failed! Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}