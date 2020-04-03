package com.ecareuae.e_care.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ecareuae.e_care.R;
import com.ecareuae.e_care.ui.user_type_selection.UserTypeSelectionFragment;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private LoginViewModel mLoginViewModel;
    private TextView mRegisterPrompt;
    private View mRoot;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mLoginViewModel =
                ViewModelProviders.of(this).get(LoginViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_login, container, false);
        instantiateViews();
        mRegisterPrompt.setOnClickListener(this);
        return mRoot;
    }

    private void instantiateViews() {
        mRegisterPrompt = mRoot.findViewById(R.id.tv_register);
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = new UserTypeSelectionFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(this.getId(), fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }
}