package com.ecareuae.e_care.ui.history;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ecareuae.e_care.models.MedicalAppointmentModel;
import com.ecareuae.e_care.repositories.AppointmentsRepository;

import java.util.List;

public class HistoryViewModel extends ViewModel {

    private List<MedicalAppointmentModel> mMutableLiveAppointments;
    private AppointmentsRepository mRepository;
    private MutableLiveData<MedicalAppointmentModel> mLiveAppointment;

    public HistoryViewModel() {}

    public void init(){
        if (mRepository != null){
            return;
        }
        mRepository = AppointmentsRepository.getInstance();
        mMutableLiveAppointments = mRepository.getAppointments();
    }

    public List<MedicalAppointmentModel> getMedicalAppointments(){
        return mMutableLiveAppointments;
    }

    public MedicalAppointmentModel getMedicalAppointment(int position){
        return mRepository.getAppointment(position);
    }

    public boolean removeAppointment(int position) {
        return mRepository.removeAppointment(position);
    }
}