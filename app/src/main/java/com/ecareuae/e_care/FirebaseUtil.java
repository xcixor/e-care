package com.ecareuae.e_care;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtil {
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    private static FirebaseUtil firebaseUtil;
    private static StorageReference mStorageReference;
    public static FirebaseStorage mStorage;
//    private static String objectId;

    private FirebaseUtil(){}

    public static DatabaseReference getmDatabaseReference(){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        return mDatabaseReference;
    }

    public static StorageReference getmStorageReference(){
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference().child("user_images");
        return mStorageReference;
    }

}
