package com.ecareuae.e_care.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ecareuae.e_care.R;
import com.ecareuae.e_care.models.UserModel;

public class ProfileFragment extends Fragment {

    private static String TAG = "ProfileFragment";
    private ProfileViewModel mProfileViewModel;
    private TextView mUsername, mProfession, mFullname, mEmail, mPhone;
    private ImageView mUserImage;
    private UserModel mUser;
    private View mRoot;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mProfileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_profile, container, false);
        instantiateViews();
        getUser("samir@ahmed.com");
        setData();
        return mRoot;
    }

    private void getUser(String email) {
        mUser = mProfileViewModel.getUserByEmail(email);
    }

    private void instantiateViews() {
        mUsername = mRoot.findViewById(R.id.profile_user_name);
        mProfession = mRoot.findViewById(R.id.profile_profession);
        mUserImage = mRoot.findViewById(R.id.profile_user_image);
        mFullname = mRoot.findViewById(R.id.profile_fullname);
        mEmail = mRoot.findViewById(R.id.profile_email);
        mPhone = mRoot.findViewById(R.id.profile_mobile);
    }

    private void setData() {
        mUsername.setText(mUser.getEmail());
        if (mUser.getSpecialization() != null){
            mProfession.setText(mUser.getSpecialization());
        }else{
            mProfession.setText("N/A");
        }
        if (mUser.getMobilePhoneNumber() != null){
            mPhone.setText(mUser.getMobilePhoneNumber());
        }else{
            mPhone.setText("N/A");
        }

        mFullname.setText(mUser.getFirstName() + " " + mUser.getSurName());
        mEmail.setText(mUser.getEmail());
    }
}