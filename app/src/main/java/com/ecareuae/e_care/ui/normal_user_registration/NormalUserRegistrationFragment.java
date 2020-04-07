package com.ecareuae.e_care.ui.normal_user_registration;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.ecareuae.e_care.R;
import com.ecareuae.e_care.helpers.CustomTextWatcher;
import com.ecareuae.e_care.models.UserModel;
import com.ecareuae.e_care.repositories.FirebaseUtil;
import com.ecareuae.e_care.ui.profile.ProfileFragment;
import com.ecareuae.e_care.utils.ValidationUtil;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class NormalUserRegistrationFragment extends Fragment {
    private static String TAG = "PatientRegistrationFragment";
    private Button mRegister;
    private TextInputEditText mFirstNameET;
    private EditText mSurnameET;
    private Spinner mCountryCodeSpinnerET, mGenderSpinnerET;
    private EditText mPhoneNumberET, mEmailET, mSpecializationET,
            mPass1ET, mPass2ET;
    private String mFName, mSurname,
            mEmail, mSpecialization, mPassOne, mPassTwo, mGender,
            mCountryCode, mMobile, mUserImagePath,
            mUserImageName, mSavedPatientId;
    private TextInputLayout mFNameLayout, mSurnameLayout, mEmailLayout,
            mPassOneLayout, mPassTwoLayout,
            mSpecializationLayout;
    private static final int PERMISSION_ID = 200;
    private String latitude;
    private String longitude;
    private ImageView mUserImageView;
    private Uri mImageUri;
    private final int PICK_IMAGE_REQUEST = 71;
    private TextView mUploadPhoto;
    private Transformation transformation;
    private FirebaseAuth mAuth;
    private View mRoot;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mRoot = inflater.inflate(R.layout.fragment_normal_user_registration, container, false);
        initializeGenderDropdown();
        initializeCountryCodesDropdown();

        mAuth = FirebaseAuth.getInstance();
        instantiateLayouts();
        instantiateViews();
        addTextChangedListeners();
        mUserImageView = mRoot.findViewById(R.id.user_image);
        mUploadPhoto = mRoot.findViewById(R.id.tv_upload_photo);
        mRegister = mRoot.findViewById(R.id.register_btn);

        mUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPatient();
            }
        });

//        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.frag_normal_user);
//            }
//        };

//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        return mRoot;
    }

    private void initializeCountryCodesDropdown() {
        Spinner spinner = mRoot.findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void initializeGenderDropdown() {
        Spinner codeSpinner =  mRoot.findViewById(R.id.number_prefix_spinner);
        ArrayAdapter<CharSequence> codeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.country_codes_array, android.R.layout.simple_spinner_item);
        codeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        codeSpinner.setAdapter(codeAdapter);
    }

    private void instantiateLayouts() {
        this.mFNameLayout = mRoot.findViewById(R.id.registration_firstname_et_input_layout);
        this.mSurnameLayout = mRoot.findViewById(R.id.registration_surname_et_input_layout);
        this.mEmailLayout = mRoot.findViewById(R.id.registration_email_et_input_layout);
        this.mPassOneLayout = mRoot.findViewById(R.id.registration_password_one_et_input_layout);
        this.mPassTwoLayout = mRoot.findViewById(R.id.registration_password_two_et_input_layout);
        this.mSpecializationLayout = mRoot.findViewById(R.id.registration_specialization_et_input_layout);
    }

    private void instantiateViews() {
        mFirstNameET = mRoot.findViewById(R.id.et_first_name);
        mSurnameET = mRoot.findViewById(R.id.et_surname);
        mCountryCodeSpinnerET = mRoot.findViewById(R.id.number_prefix_spinner);
        mGenderSpinnerET = mRoot.findViewById(R.id.gender_spinner);
        mPhoneNumberET = mRoot.findViewById(R.id.et_mobile);
        mEmailET = mRoot.findViewById(R.id.et_email);
        mSpecializationET = mRoot.findViewById(R.id.et_specialization);
        mPass1ET = mRoot.findViewById(R.id.et_pass_1);
        mPass2ET = mRoot.findViewById(R.id.et_pass_2);
        mUserImageName = "";
        mUserImagePath = "";
    }

    private void addTextChangedListeners() {
        this.mFirstNameET.addTextChangedListener(new CustomTextWatcher(this.mFNameLayout));
        this.mSurnameET.addTextChangedListener(new CustomTextWatcher(this.mSurnameLayout));
        this.mPass1ET.addTextChangedListener(new CustomTextWatcher(this.mPassOneLayout));
        this.mPass2ET.addTextChangedListener(new CustomTextWatcher(this.mPassTwoLayout));
        this.mSpecializationET.addTextChangedListener(new CustomTextWatcher(this.mSpecializationLayout));
    }

    //    image saving
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }

    private void savePatient(UserModel patient) {
        if (patient != null)
            if (!mUserImagePath.equals("") && !mUserImageName.equals(""))
                patient.setImage(mUserImagePath);
                patient.setUserImageName(mUserImageName);
            Log.d(TAG, "savePatient: **  " + patient.toString());
            mAuth.createUserWithEmailAndPassword(patient.getEmail(), patient.getPassword())
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "New user registration: " + task.isSuccessful());

                            if (!task.isSuccessful()) {
                                Log.d(TAG, "onComplete: error" + task.getException());
                                toastMessage(task.getException().getMessage());
                            } else {
                                mRegister.setEnabled(false);
                                DatabaseReference docRef = FirebaseUtil.getmDatabaseReference().child("users").push();
                                mSavedPatientId = docRef.getKey();
                                docRef.setValue(patient);
                                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_login);
                            }
                        }
                    });
    }

    private void toastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void getPatient() {
        getDataFromViews();
    }

    private void getDataFromViews() {
        mFName = mFirstNameET.getText().toString();
        mSurname = mSurnameET.getText().toString();
        mEmail = mEmailET.getText().toString();
        mSpecialization = mSpecializationET.getText().toString();
        mPassOne = mPass1ET.getText().toString();
        mPassTwo = mPass2ET.getText().toString();
        mGender = mGenderSpinnerET.getSelectedItem().toString();
        mCountryCode = mCountryCodeSpinnerET.getSelectedItem().toString();
        mMobile = mPhoneNumberET.getText().toString();
        validateUserData();
    }

    private void validateUserData() {
        isValidFirstName();
        isValidSurname();
        isValidEmail();
        isValidPasswordOne();
        isValidSpecialization();
        isValidPasswordTwo();
        if(isValidFirstName()
            && isValidSurname()
            && isValidEmail()
            && isValidPasswordOne()
            && isValidSpecialization()
            && isValidPasswordTwo()){
            instantiatePatient();
        }else{
            Toast.makeText(getContext(), "Please validate your data!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidSpecialization() {
        if (ValidationUtil.stringIsEmpty(this.mSpecialization)) {
            this.mSpecializationLayout.setError("Profession Required");
            return false;
        }
        else if (ValidationUtil.hasSpecialCharacters(this.mSpecialization)){
            this.mSpecializationLayout.setError("Invalid Input");
            return false;
        }else{
            this.mSpecializationLayout.setError(null);
            return true;
        }
    }

    private boolean isValidPasswordTwo() {
        if (ValidationUtil.stringIsEmpty(mPassTwo)){
            mPassTwoLayout.setError("Confirm Password!");
            return false;
        }else if (!mPassOne.equals(mPassTwo)){
            mPassTwoLayout.setError("Passwords don't match");
            this.mPass2ET.setText("");
            return false;
        }else{
            mPassOneLayout.setError(null);
            return true;
        }
    }

    private boolean isValidPasswordOne() {
        if (ValidationUtil.stringIsEmpty(this.mPassOne)){
            this.mPassOneLayout.setError("Password Required");
            return false;
        }else if (ValidationUtil.passwordLengthNotGreaterThanSixCharacters(this.mPassOne)){
            this.mPassOneLayout.setError("Password should be greater than six characters");
            return false;
        }else {
            this.mPassOneLayout.setError(null);
            return true;
        }
    }

    private boolean isValidEmail() {
        if (ValidationUtil.stringIsEmpty(this.mEmail)){
            this.mEmailLayout.setError("Email Required");
            return false;
        }
        else if (!ValidationUtil.isEmail(this.mEmail)){
            this.mEmailLayout.setError("Invalid email");
            return false;
        }else {
            this.mEmailLayout.setError(null);
            return true;
        }
    }

    private boolean isValidSurname() {
        if (ValidationUtil.stringIsEmpty(this.mSurname)) {
            this.mSurnameLayout.setError("Surname Required");
            return false;
        }
        else if (ValidationUtil.hasSpecialCharacters(this.mSurname)){
            this.mSurnameLayout.setError("Invalid Input");
            return false;
        }else{
            this.mSurnameLayout.setError(null);
            return true;
        }
    }

    private boolean isValidFirstName() {
        if (ValidationUtil.stringIsEmpty(this.mFName)) {
            this.mFNameLayout.setError("Firstname Required!");
            return false;
        }else if (ValidationUtil.hasSpecialCharacters(this.mFName)){
            this.mFNameLayout.setError("Invalid Input");
            return false;
        }else {
            this.mFNameLayout.setError(null);
            return true;
        }
    }

    private void instantiatePatient() {
        UserModel patient = new UserModel(
                this.mFName,
                this.mSurname,
                this.mEmail,
                this.mGender,
                true
        );
        patient.setPassword(this.mPassOne);
        patient.setCountryCode(this.mCountryCode);
        patient.setMobilePhoneNumber(this.mCountryCode + " " + this.mMobile);
        patient.setSpecialization(this.mSpecialization);
        savePatient(patient);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null && data.getData() != null )
        {
            mImageUri = data.getData();
            int radius = Resources.getSystem().getDisplayMetrics().widthPixels;
            int margin = 0;
            if (mImageUri != null) {
                transformation = new RoundedCornersTransformation(radius, margin);
                Picasso.get()
                        .load(mImageUri.toString())
                        .transform(transformation)
                        .placeholder(R.drawable.ic_account)
                        .error(R.drawable.ic_account)
                        .fit()
                        .into(mUserImageView);
                saveImage();
            }

        }
    }

    private void saveImage(){
        if(mImageUri != null)
        {
            final StorageReference ref = FirebaseUtil.getmStorageReference().child(mImageUri.getLastPathSegment());
            UploadTask uploadTask = ref.putFile(mImageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        mUserImagePath = downloadUri.toString();
                        mUserImageName = ref.getName();
                    } else {
                        showImageSavingError();
                    }
                }
            });
        }
    }

    private void showImageSavingError() {
        Toast.makeText(getContext(), "Image failed to upload, contact admin!", Toast.LENGTH_SHORT).show();
    }

}
