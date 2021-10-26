package com.example.musicapp.ui.listen;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.database.FirebaseReadWrite;
import com.google.firebase.database.DatabaseReference;

public class ListenViewModel extends ViewModel {

    private MutableLiveData<Integer> mTestValue;
    private FirebaseReadWrite mDatabase;
    private DatabaseReference ref;
    final private String TEST_PATH = "test";
    final private String COUNTER_ATTRIBUTE = "counter";
    final private String COUNTER_PATH = "test/counter";

    public ListenViewModel() {
        mTestValue = new MutableLiveData<>();
        mDatabase = FirebaseReadWrite.FirebaseReadWrite(); //singleton class so don't need to call constructor.
        mDatabase.getIntegerData(TEST_PATH, COUNTER_ATTRIBUTE, mTestValue); //gets counter data and stores it in mTestValue.

    }

    public LiveData<Integer> getTestValue() { return mTestValue; }

    public void updateValue(Integer value) {
        mDatabase.updateData(COUNTER_PATH, value); //update database
        mTestValue.setValue(value); //update mTestValue.
    }

    public void incrementTestValue() {
        this.updateValue(mTestValue.getValue() + 1);
    }

}