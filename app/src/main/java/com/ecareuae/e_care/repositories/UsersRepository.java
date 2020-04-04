package com.ecareuae.e_care.repositories;

import android.util.Log;

import com.ecareuae.e_care.models.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersRepository {
    private static String TAG = "UsersRepository";
    private static UsersRepository sUsersRepositoryInstance;
    private ArrayList<UserModel> mUsers = new ArrayList<>();
    private DatabaseReference mDatabaseReference;
    private UserModel mFoundUser;

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

    public UserModel getUserById(String userId){
//        mFoundUser = null;
        Log.d(TAG, "getUserById: " + userId);
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
//        ValueEventListener listener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onDataChange: listening");
//                if (dataSnapshot.exists()) {
//                    UserModel user = dataSnapshot.getValue(UserModel.class);
//                    mFoundUser = user;
//                    Log.d(TAG, "onDataChange: user found  "+ user.toString());
//                }else {
//                    Log.d(TAG, "onDataChange: user not found");
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d(TAG, "onCancelled: "+ databaseError.getCode());
//            }
//        };

        return mFoundUser;
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
