package com.ecareuae.e_care.ui.profile;

import androidx.lifecycle.ViewModel;

import com.ecareuae.e_care.models.UserModel;
import com.ecareuae.e_care.repositories.UsersRepository;

import java.util.List;

public class ProfileViewModel extends ViewModel {

    private UsersRepository mRepository;
    private List<UserModel> mUsers;

    public ProfileViewModel() {
        if (mRepository != null){
            return;
        }
        mRepository = UsersRepository.getInstance();
        mUsers = mRepository.getUsers();
    }

    public List<UserModel> getUsers(){
        return mUsers;
    }

    public UserModel getUserByEmail(String email){
        return mRepository.getUserByEmail(email);
    }
}