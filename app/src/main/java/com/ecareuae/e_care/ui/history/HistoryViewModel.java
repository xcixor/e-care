package com.ecareuae.e_care.ui.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ecareuae.e_care.models.MedicalAppointment;
import com.ecareuae.e_care.repositories.AppointmentsRepository;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel extends ViewModel {

    private MutableLiveData<List<MedicalAppointment>> mMutableLiveAppointments;
    private AppointmentsRepository mRepository;
    private MutableLiveData<MedicalAppointment> mLiveAppointment;

    public HistoryViewModel() {

    }

    public void init(){
        if (mRepository != null){
            return;
        }
        mRepository = AppointmentsRepository.getInstance();
        mMutableLiveAppointments = mRepository.getAppointments();
    }

    public LiveData<List<MedicalAppointment>> getMedicalAppointments(){
        return mMutableLiveAppointments;
    }

    public LiveData<MedicalAppointment> getMedicalAppointment(int position){
        return mLiveAppointment = mRepository.getAppointment(position);
    }

}