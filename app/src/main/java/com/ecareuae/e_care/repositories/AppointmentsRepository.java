package com.ecareuae.e_care.repositories;

import androidx.lifecycle.MutableLiveData;

import com.ecareuae.e_care.models.MedicalAppointment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Fetches user medical appointments from database
 */

public class AppointmentsRepository {
//    build functionality/access points for accessing data source
//    eg web service or db
    private static AppointmentsRepository sAppointmentsRepositoryInstance;
    private ArrayList<MedicalAppointment> mAppointments = new ArrayList<>();

    public static AppointmentsRepository getInstance(){
        if(sAppointmentsRepositoryInstance == null)
            sAppointmentsRepositoryInstance = new AppointmentsRepository();
        return sAppointmentsRepositoryInstance;
    }

//    pretending to get data online
    public MutableLiveData<List<MedicalAppointment>> getAppointments(){
//        fetch data here
        setDummyAppointments();
        MutableLiveData<List<MedicalAppointment>> dataSet = new MutableLiveData<>();
        dataSet.setValue(mAppointments);
        return dataSet;
    }

    public MutableLiveData<MedicalAppointment> getAppointment(int position){
        MedicalAppointment appointment = mAppointments.get(position);
        MutableLiveData<MedicalAppointment> appointmentMutableLiveData = new MutableLiveData<>();
        appointmentMutableLiveData.setValue(appointment);
        return appointmentMutableLiveData;
    }

    public void setDummyAppointments(){
        mAppointments.add(new MedicalAppointment(
                "Dr. Rajesh",
                new Date(),
                "Hi doc, my dick hurts, I'm peeing blood and its oozing a yellow liquid")
        );
        mAppointments.add(new MedicalAppointment(
                "Dr. Zainab",
                new Date(),
                "Hi doc, I missed my periods can i see you?")
        );
        mAppointments.add(new MedicalAppointment(
                "Dr. Akipeteshi",
                new Date(),
                "Hi doc, you are a moron, the last time i came to you for help you asked sex as payement, you are a pest and a shame and blight to this stupidly religious but rich arabic country that has suprisingly rich architecture but questionable morals")
        );
        mAppointments.add(new MedicalAppointment(
                "Dr. Rajesh",
                new Date(),
                "Hi go fuck yourself")
        );
        mAppointments.add(new MedicalAppointment(
                "Dr. Nina",
                new Date(),
                "Id like to fuck your fat pussy, oooooowi!")
        );
    }

}
