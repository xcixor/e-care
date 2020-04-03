package com.ecareuae.e_care.repositories;

import com.ecareuae.e_care.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UsersRepository {
    private static String TAG = "AppointmentsRepository";
    private static UsersRepository sUsersRepositoryInstance;
    private ArrayList<UserModel> mUsers = new ArrayList<>();

    public static UsersRepository getInstance(){
        if (sUsersRepositoryInstance == null)
            sUsersRepositoryInstance = new UsersRepository();
        return sUsersRepositoryInstance;
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
        mUsers.add(new UserModel(
                "Dr",
                "Zainab",
                "zainabu@zainab.com",
                "female",
                true
        ));
    }

}
