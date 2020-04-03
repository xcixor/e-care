package com.ecareuae.e_care.ui.normal_normal_user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ecareuae.e_care.R;

public class NormalUserRegistrationFragment extends Fragment {
    private View mRoot;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mRoot = inflater.inflate(R.layout.fragment_normal_user_registration, container, false);
        return mRoot;
    }
}
