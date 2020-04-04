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
import androidx.lifecycle.ViewModelProvider;

import com.ecareuae.e_care.R;
import com.ecareuae.e_care.ui.doctor_registration.DoctorRegistrationFragment;
import com.ecareuae.e_care.ui.normal_normal_user.NormalUserRegistrationFragment;

public class UserTypeSelectionFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private static final String BACK_STACK_ROOT_TAG = "user_type_selection_fragment";
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
//        int index = radioGroup.indexOfChild(radioButton);
        switch(mRadioButton.getId()) {
            case R.id.radio_doctor:
                Fragment fragment = new DoctorRegistrationFragment();
                startFragmentTransaction(fragment);
                break;
            case R.id.radio_patient:
                Fragment userFragment = new NormalUserRegistrationFragment();
                startFragmentTransaction(userFragment);
                break;
        }
    }

    public void startFragmentTransaction(Fragment fragment){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(this.getId(), fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(this.getClass().getName());
        ft.commit();
    }

}
