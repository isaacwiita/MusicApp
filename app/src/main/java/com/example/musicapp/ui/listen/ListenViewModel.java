package com.example.musicapp.ui.listen;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListenViewModel extends ViewModel {

    private MutableLiveData<Integer> mTestValue;
    private DatabaseReference mDatabase;
    final private String FB_TAG = "FirebaseDatabase";

    public ListenViewModel() {
        mTestValue = new MutableLiveData<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("test").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter = Integer.parseInt(snapshot.child("counter").getValue().toString());
                mTestValue.setValue(counter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public LiveData<Integer> getTestValue() { return mTestValue; }

    public void setTestValue(Integer value) {
        mDatabase.child("test").child("counter").setValue(value).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(FB_TAG, "counter data updated succesfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(FB_TAG, "counter data upload failed");
            }
        });
        mTestValue.setValue(value);
    }

    public void incrementTestValue() {
        this.setTestValue(mTestValue.getValue() + 1);
    }

}