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
        mAppointments.add(new MedicalAppointmentModel(
                "Dr. Rajesh",
                new Date(),
                "Hi doc, I would like to have plastic surgery please advise")
        );
        mAppointments.add(new MedicalAppointmentModel(
                "Dr. Zainab",
                new Date(),
                "Hello, can i see you about a troubling appendix..")
        );
        mAppointments.add(new MedicalAppointmentModel(
                "Dr. Akipeteshi",
                new Date(),
                "I would like to see you on the mentioned date for a colonoscopy")
        );
        mAppointments.add(new MedicalAppointmentModel(
                "Dr. Rajesh",
                new Date(),
                "My child has a fever, can I see you then for a check up?!")
        );
        mAppointments.add(new MedicalAppointmentModel(
                "Dr. Nina",
                new Date(),
                "Id like to fuck your fat pussy, oooooowi!")
        );
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
