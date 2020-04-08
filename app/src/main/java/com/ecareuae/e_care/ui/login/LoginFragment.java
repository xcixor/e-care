package com.ecareuae.e_care.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.ecareuae.e_care.MainActivity;
import com.ecareuae.e_care.R;
import com.ecareuae.e_care.ui.profile.ProfileFragment;
import com.ecareuae.e_care.ui.user_type_selection.UserTypeSelectionFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class LoginFragment extends Fragment implements View.OnClickListener, OnCompleteListener, OnFailureListener {

    private static String TAG = "LoginFragment";
    private TextView mRegisterPrompt;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView mEmail;
    private TextView mPassword;
    private Button mLoginButton;
    private View mRoot;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_login, container, false);
        instantiateViews();
        mRegisterPrompt.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    ((MainActivity)getActivity()).instantiateViews();
                    toastMessage("Welcome " + user.getEmail());
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).
                            navigate(R.id.nav_profile);
                }else{
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }
            }
        };

        return mRoot;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void instantiateViews() {
        mRegisterPrompt = mRoot.findViewById(R.id.tv_register);
        mAuth = FirebaseAuth.getInstance();
        mEmail = mRoot.findViewById(R.id.et_auth_email);
        mPassword = mRoot.findViewById(R.id.et_auth_password);
        mLoginButton = mRoot.findViewById(R.id.login_button);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_register:
                Navigation.findNavController(view).navigate(R.id.user_selection);
                break;
            case R.id.login_button:
                initiateLogin();
                break;
            default: break;
        }

    }

    private void initiateLogin() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        if (!email.equals("") && !password.equals("")){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this)
                    .addOnFailureListener(this);
        }else{
            toastMessage("Check your authentication details!");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (!task.isSuccessful()){
            toastMessage(task.getException().getMessage());
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        toastMessage(e.getMessage());
    }
}