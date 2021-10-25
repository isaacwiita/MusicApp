package com.example.musicapp.ui.listen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListenViewModel extends ViewModel {

    private MutableLiveData<Integer> mTestValue;

    public ListenViewModel() {
        mTestValue = new MutableLiveData<>();
        mTestValue.setValue(0);
    }

    public LiveData<Integer> getTestValue() { return mTestValue; }

    public void setTestValue(Integer value) {
        mTestValue.setValue(value);
    }

    public void incrementTestValue() {
        mTestValue.setValue(mTestValue.getValue() + 1);
    }

}