package com.ecareuae.e_care.ui.profile;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ecareuae.e_care.MainActivity;
import com.ecareuae.e_care.R;
import com.ecareuae.e_care.models.UserModel;
import com.ecareuae.e_care.repositories.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ProfileFragment extends Fragment {

    private static String TAG = "ProfileFragment";
    private TextView mUsername, mProfession, mFullname, mEmail, mPhone;
    private ImageView mUserImage;
    private UserModel mUser;
    private View mRoot;
    private String mUserId;
    private Transformation transformation;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)getActivity()).toggleMenutItems();
        mRoot = inflater.inflate(R.layout.fragment_profile, container, false);
        instantiateViews();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("users");
        Query usersQuery = ref.orderByChild("email").equalTo(mCurrentUser.getEmail());
        usersQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
        return mRoot;
    }

    private void showData(DataSnapshot ds) {
        Log.d(TAG, "onDataChange: listening");
        if (ds.exists()) {
            String key = "";
            for (DataSnapshot dataSnapshot: ds.getChildren()) {
                key = dataSnapshot.getKey();
            }
            Log.d(TAG, "showData: snapshot  "+ ds);
            UserModel user = new UserModel();
            Log.d(TAG, "showData: key  " + ds.child(key).getValue(UserModel.class));
            user = new UserModel(
                    ds.child(key).getValue(UserModel.class).getFirstName(),
                    ds.child(key).getValue(UserModel.class).getSurName(),
                    ds.child(key).getValue(UserModel.class).getEmail(),
                    ds.child(key).getValue(UserModel.class).getGender(),
                    ds.child(key).getValue(UserModel.class).isDoctor());
            user.setCountryCode(ds.child(key).getValue(UserModel.class).getCountryCode());
            user.setMobilePhoneNumber(ds.child(key).getValue(UserModel.class).getMobilePhoneNumber());
            user.setSpecialization(ds.child(key).getValue(UserModel.class).getSpecialization());
            user.setImage(ds.child(key).getValue(UserModel.class).getImage());
            user.setDoctor(ds.child(key).getValue(UserModel.class).isDoctor());
            setData(user);
            Log.d(TAG, "showData: " + user.toString());
        }else {
            Log.d(TAG, "onDataChange: user not found");
        }
    }

    private void instantiateViews() {
        mUsername = mRoot.findViewById(R.id.profile_user_name);
        mProfession = mRoot.findViewById(R.id.profile_profession);
        mUserImage = mRoot.findViewById(R.id.profile_user_image);
        mFullname = mRoot.findViewById(R.id.profile_fullname);
        mEmail = mRoot.findViewById(R.id.profile_email);
        mPhone = mRoot.findViewById(R.id.profile_mobile);
    }

    private void setData(UserModel user) {
        mUsername.setText(user.getEmail());
        if (user.getSpecialization() != null){
            mProfession.setText(user.getSpecialization());
        }else{
            mProfession.setText("N/A");
        }
        if (user.getMobilePhoneNumber() != null){
            mPhone.setText(user.getMobilePhoneNumber());
        }else{
            mPhone.setText("N/A");
        }

        if (user.getImage() != null && !user.getImage().isEmpty()){
            int radius = Resources.getSystem().getDisplayMetrics().widthPixels;
            int margin = 0;
            transformation = new RoundedCornersTransformation(radius, margin);
            Picasso.get()
                    .load(user.getImage())
                    .transform(transformation)
                    .placeholder(R.drawable.ic_account)
                    .error(R.drawable.ic_account)
                    .fit()
                    .into(mUserImage);
        }

        mFullname.setText(user.getFirstName() + " " + user.getSurName());
        mEmail.setText(user.getEmail());
    }
}