package com.ecareuae.e_care.ui.appointment_booking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BookAppointmentViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BookAppointmentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is send fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}