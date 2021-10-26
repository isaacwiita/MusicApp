package com.example.musicapp.ui.listen;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListenViewModel extends ViewModel {

    private MutableLiveData<Integer> mTestValue;
    private DatabaseReference mDatabase;

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
        mTestValue.setValue(value);
    }

    public void incrementTestValue() {
        mTestValue.setValue(mTestValue.getValue() + 1);
    }

}