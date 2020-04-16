package com.ecareuae.e_care.ui.doctor_registration;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.ecareuae.e_care.helpers.CustomTextWatcher;
import com.ecareuae.e_care.repositories.FirebaseUtil;
import com.ecareuae.e_care.R;
import com.ecareuae.e_care.models.UserLocationModel;
import com.ecareuae.e_care.models.UserModel;
import com.ecareuae.e_care.ui.profile.ProfileFragment;
import com.ecareuae.e_care.utils.ValidationUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

//import static android.app.Activity.RESULT_OK;

public class DoctorRegistrationFragment extends Fragment implements FragmentManager.OnBackStackChangedListener {
    private static String TAG = "DoctorRegistrationFragment";
    private Button mRegister;
    private TextInputEditText mFirstNameET;
    private EditText mSurnameET, mPracticeET;
    private Spinner mCountryCodeSpinnerET, mGenderSpinnerET;
    private EditText mPhoneNumberET, mEmailET, mSpecializationET,
            mPass1ET, mPass2ET;
    private String mFName, mSurname, mPractice,
            mEmail, mSpecialization, mPassOne, mPassTwo, mGender,
            mCountryCode, mMobile, mUserImagePath,
            mUserImageName, mSavedDoctorId;
    private TextInputLayout mFNameLayout, mSurnameLayout, mEmailLayout,
            mPassOneLayout, mPassTwoLayout, mPracticeLayout,
            mSpecializationLayout;
    private static final int PERMISSION_ID = 200;
    private FusedLocationProviderClient mFusedLocationClient;
    private String latitude;
    private String longitude;
    private ImageView mUserImageView;
    private Uri mImageUri;
    private final int PICK_IMAGE_REQUEST = 71;
    private TextView mUploadPhoto;
    private Transformation transformation;
    private FirebaseAuth mAuth;

    private View mRoot;
    private Location mLocation;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mRoot = inflater.inflate(R.layout.fragment_doctor_registration, container, false);
        initializeGenderDropdown();
        initializeCountryCodesDropdown();

        mAuth = FirebaseAuth.getInstance();
        instantiateLayouts();
        instantiateViews();
        addTextChangedListeners();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
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
                getDoctor();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.user_selection);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

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
        this.mPracticeLayout = mRoot.findViewById(R.id.registration_hospital_et_input_layout);
    }

    private void instantiateViews() {
        mFirstNameET = mRoot.findViewById(R.id.et_first_name);
        mSurnameET = mRoot.findViewById(R.id.et_surname);
        mPracticeET = mRoot.findViewById(R.id.et_hospital);
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
        this.mPracticeET.addTextChangedListener(new CustomTextWatcher(this.mPracticeLayout));
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

    private void InstantiateSaveDoctor(UserModel doctor) {
        Log.d(TAG, "saveDoctor: before check  " + doctor);
        if (doctor != null)
            Log.d(TAG, "saveDoctor: in if not null  " + doctor);
            getLastLocation(doctor);
    }

    private void toastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void getDoctor() {
        getDataFromViews();
    }

    private void getDataFromViews() {
        mFName = mFirstNameET.getText().toString();
        mSurname = mSurnameET.getText().toString();
        mPractice = mPracticeET.getText().toString();
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
        isValidSpecialization();
        isValidPasswordOne();
        isValidPasswordTwo();
        if(isValidFirstName() && isValidSurname() && isValidEmail() && isValidPasswordOne() && isValidSpecialization() && isValidPasswordTwo()){
            instantiateDoctor();
        }else{
            Toast.makeText(getContext(), "Please validate your data!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidSpecialization() {
        if (ValidationUtil.stringIsEmpty(this.mSpecialization)) {
            this.mSpecializationLayout.setError("Specialization Required");
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
        if (ValidationUtil.stringIsEmpty(this.mPassTwo)){
            this.mPassTwoLayout.setError("Confirm Password!");
            return false;
        }else if (!this.mPassOne.equals(this.mPassTwo)){
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

    private void instantiateDoctor() {
        UserModel doctor = new UserModel(
                this.mFName,
                this.mSurname,
                this.mEmail,
                this.mGender,
                true
        );
        doctor.setPassword(this.mPassOne);
        doctor.setCountryCode(this.mCountryCode);
        doctor.setMobilePhoneNumber(this.mMobile);
        doctor.setPractice(this.mPractice);
        doctor.setSpecialization(this.mSpecialization);
        InstantiateSaveDoctor(doctor);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(UserModel doctor){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        task -> {
                            mLocation = task.getResult();
                            if (mLocation == null) {
                                requestNewLocationData();
                            } else {
                                latitude = Double.toString(mLocation.getLatitude());
                                longitude = Double.toString(mLocation.getLongitude());
                                saveDoctor(doctor);

                            }
                        }
                );
            } else {
                Toast.makeText(getContext(), "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private void saveDoctor(UserModel doctor) {
        UserLocationModel userLocationModel = new UserLocationModel(latitude, longitude, doctor);
        if (!mUserImagePath.equals("") && !mUserImageName.equals(""))
            doctor.setImage(mUserImagePath);
        doctor.setUserImageName(mUserImageName);
        mAuth.createUserWithEmailAndPassword(doctor.getEmail(), doctor.getPassword())
                .addOnCompleteListener(getActivity(), task -> {
                    Log.d(TAG, "New user registration: " + task.isSuccessful());

                    if (!task.isSuccessful()) {
                        Log.d(TAG, "onComplete: error" + task.getException());
                        toastMessage(task.getException().getMessage());
                    } else {
                        mRegister.setEnabled(false);
                        DatabaseReference docRef = FirebaseUtil.getmDatabaseReference().child("users").push();
                        mSavedDoctorId = docRef.getKey();
                        docRef.setValue(doctor);

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(doctor.getSurName())
                                .setPhotoUri(Uri.parse(mUserImagePath))
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User profile updated.");
                                        }
                                    }
                                });

                        FirebaseUtil.getmDatabaseReference().child("userLocations").push().setValue(userLocationModel);
                        Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_login);
                    }
                });
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = Double.toString(mLastLocation.getLatitude());
            longitude = Double.toString(mLastLocation.getLongitude());
        }
    };

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

    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onBackStackChanged() {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.user_selection);
    }
}
