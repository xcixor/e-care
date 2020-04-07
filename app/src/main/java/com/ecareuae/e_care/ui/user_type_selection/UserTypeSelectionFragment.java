package com.ecareuae.e_care.ui.user_type_selection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.ecareuae.e_care.R;
import com.ecareuae.e_care.ui.doctor_registration.DoctorRegistrationFragment;
import com.ecareuae.e_care.ui.normal_user_registration.NormalUserRegistrationFragment;

public class UserTypeSelectionFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup mRadioGroup;
    private View mRoot;
    private View mRadioButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Toolbar actionBar = getActivity().findViewById(R.id.toolbar);
        actionBar.setTitle("User Type Selection");
        mRoot = inflater.inflate(R.layout.fragment_user_type_selection, container, false);
        instantiateViews();
        mRadioGroup.setOnCheckedChangeListener(this);
        return mRoot;
    }

    private void instantiateViews() {
        mRadioGroup = mRoot.findViewById(R.id.user_selection_radios);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        mRadioButton = radioGroup.findViewById(checkedId);
        switch(mRadioButton.getId()) {
            case R.id.radio_doctor:
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.doctor_registration);
                break;
            case R.id.radio_patient:
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.frag_normal_user);
                break;
        }
    }

}
