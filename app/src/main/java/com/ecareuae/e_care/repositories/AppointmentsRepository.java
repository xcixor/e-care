package com.ecareuae.e_care.repositories;

import android.util.Log;

import com.ecareuae.e_care.models.MedicalAppointmentModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Fetches user medical appointments from database
 */

public class AppointmentsRepository {
//    build functionality/access points for accessing data source
//    eg web service or db
    private static String TAG = "AppointmentsRepository";
    private static AppointmentsRepository sAppointmentsRepositoryInstance;
    private ArrayList<MedicalAppointmentModel> mAppointments = new ArrayList<>();

    public static AppointmentsRepository getInstance(){
        if(sAppointmentsRepositoryInstance == null)
            sAppointmentsRepositoryInstance = new AppointmentsRepository();
        return sAppointmentsRepositoryInstance;
    }

//    pretending to get data online
    public List<MedicalAppointmentModel> getAppointments(){
//        fetch data here
        setDummyAppointments();
        return mAppointments;
    }


    public MedicalAppointmentModel getAppointment(int position){
        Log.d(TAG, "getAppointment: position is "+ position);
        MedicalAppointmentModel appointment = mAppointments.get(position);
        return appointment;
    }

    public void setDummyAppointments(){
    }

    public boolean removeAppointment(int position) {
        MedicalAppointmentModel appointment = getAppointment(position);
        if (mAppointments.contains(appointment)) {
            mAppointments.remove(position);
            return true;
        }
        return false;
    }
}
