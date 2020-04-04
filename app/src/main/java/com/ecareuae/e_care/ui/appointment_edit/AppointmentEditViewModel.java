package com.ecareuae.e_care.ui.appointment_edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AppointmentEditViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AppointmentEditViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is tools fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}