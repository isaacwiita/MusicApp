package com.example.musicapp.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//singleton class for database access
public class FirebaseReadWrite {
    private static FirebaseReadWrite single_instance = null;
    private DatabaseReference mDatabase;
    final private String FB_TAG = "FirebaseDatabase";

    //private constructor for singleton purposes
    private FirebaseReadWrite(){
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseReadWrite FirebaseReadWrite(){
        if (single_instance == null){
            single_instance = new FirebaseReadWrite();
        }
        return single_instance;
    }

    //Get int data based on precise path and attribute info and store into mTestValue.
    public void getIntegerData(String path, String attribute, MutableLiveData<Integer> mTestValue){
        this.mDatabase.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mTestValue.setValue(Integer.parseInt(snapshot.child(attribute).getValue().toString()));
                Log.d(FB_TAG, "Read Value: " + mTestValue.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(FB_TAG, "Failed to get Value");
            }
        });
    }

    //used to update the database. Requires precise path and value information.
    public void updateData(String path, Object value){
        mDatabase.child(path).setValue(value).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(FB_TAG, "Data updated successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(FB_TAG, "Data upload failed");
            }
        });
    }
    //further development ...
}
