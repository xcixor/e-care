package com.ecareuae.e_care.repositories;

import android.util.Log;

import com.ecareuae.e_care.models.UserLocationModel;
import com.ecareuae.e_care.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class LocationsRepository {
    private static String TAG = "AppointmentsRepository";
    private static LocationsRepository sLocationsRepositoryInstance;
    private ArrayList<UserLocationModel> mLocations = new ArrayList<>();

    public static LocationsRepository getInstance(){
        if(sLocationsRepositoryInstance == null)
            sLocationsRepositoryInstance = new LocationsRepository();
        return sLocationsRepositoryInstance;
    }

    public List<UserLocationModel> getLocations(){
        return setDummyLocations();
    }

    private List<UserLocationModel> setDummyLocations() {
//        Log.d(TAG, "setDummyLocations: user " + mRepo.getUserByEmail("samir@ahmed.com"));
        mLocations.add(
                new UserLocationModel(
                        25.314791,
                        55.491886, new UserModel(
                        "Dr",
                        "Rajesh",
                        "dr@rajesh.com",
                        "male",
                        true
                )));
        mLocations.add(
                new UserLocationModel(
                        25.310174,
                        55.510554,
                        new UserModel(
                                "Dr",
                                "Rajesh",
                                "dr@rajesh.com",
                                "male",
                                true
                        )));
        mLocations.add(new UserLocationModel(25.315683, 55.531926, new UserModel(
                "Dr",
                "Zainab",
                "zainab@zainabu.com",
                "female",
                true
        )));
        mLocations.add(new UserLocationModel(25.291473, 55.695691, new UserModel(
                "Dr",
                "Zainab",
                "zainab@zainabu.com",
                "female",
                true
        )));
        mLocations.add(new UserLocationModel(25.285265, 55.891471, new UserModel(
                "Dr",
                "ahmed",
                "ahmed@musa.com",
                "male",
                true
        )));
        return mLocations;
    }
}
