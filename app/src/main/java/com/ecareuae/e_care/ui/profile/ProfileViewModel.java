package com.ecareuae.e_care.ui.profile;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.ecareuae.e_care.models.UserModel;
import com.ecareuae.e_care.repositories.FirebaseUtil;
import com.ecareuae.e_care.repositories.UsersRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProfileViewModel extends ViewModel {

    private static String TAG = "ProfileModel";
    private UsersRepository mRepository;
    private List<UserModel> mUsers;
    private DatabaseReference mDatabaseReference;
    private UserModel mFoundUser;

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

    public UserModel getUserById(String userId){
        Log.d(TAG, "getUserById: " + userId);
//        return mRepository.getUserById(userId);
        mDatabaseReference = FirebaseUtil.getmDatabaseReference();
        DatabaseReference users = mDatabaseReference.child("users").child(userId);
        Log.d(TAG, "getUserById: " + users.toString());
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: listening");
                if (dataSnapshot.exists()) {
                    UserModel user = dataSnapshot.getValue(UserModel.class);
                    mFoundUser = user;
                    Log.d(TAG, "onDataChange: user found  "+ user.toString());
                }else {
                    Log.d(TAG, "onDataChange: user not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: "+ databaseError.getCode());
            }
        });
        return mFoundUser;
    }
}