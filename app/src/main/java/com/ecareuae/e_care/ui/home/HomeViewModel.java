package com.ecareuae.e_care.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ecareuae.e_care.models.MedicalAppointmentModel;
import com.ecareuae.e_care.models.UserLocationModel;
import com.ecareuae.e_care.models.UserModel;
import com.ecareuae.e_care.repositories.AppointmentsRepository;
import com.ecareuae.e_care.repositories.LocationsRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private List<UserLocationModel> mLocations;
    private LocationsRepository mRepository;

    public HomeViewModel() {
        if (mRepository != null){
            return;
        }
        mRepository = LocationsRepository.getInstance();
        mLocations = mRepository.getLocations();
    }

    public List<UserLocationModel> getLocations(){
        return mLocations;
    }

}