package com.ecareuae.e_care.repositories;

import com.ecareuae.e_care.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static String TAG = "AppointmentsRepository";
    private static UserRepository sUserRepositoryInstance;
    private ArrayList<UserModel> mUsers = new ArrayList<>();

    public static UserRepository getInstance(){
        if (sUserRepositoryInstance == null)
            sUserRepositoryInstance = new UserRepository();
        return sUserRepositoryInstance;
    }

//    get all users
    public List<UserModel> getUsers(){
        getUsersFromDb();
        return mUsers;
    }

    public UserModel getUserByEmail(String email){
        UserModel foundUser = null;
        for (UserModel user : mUsers){
            if (user.getEmail().equals(email))
                foundUser = user;
        }
        return foundUser;
    }


    private void getUsersFromDb() {
        mUsers.add(new UserModel(
                "samir",
                "ahmed",
                "samir@ahmed.com",
                "male",
                false)
        );
        mUsers.add(new UserModel(
                "Dr",
                "Rajesh",
                "dr@rajesh.com",
                "male",
                true
        ));
    }

}
