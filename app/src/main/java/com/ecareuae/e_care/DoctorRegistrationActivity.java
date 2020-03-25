package com.ecareuae.e_care;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;


import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class DoctorRegistrationActivity extends AppCompatActivity {
    private Button mRegister;
    private TextInputEditText mFirstNameET;
    private EditText mSurnameET;
    private EditText mPracticeET;
    private Spinner mCountryCodeSpinnerET;
    private Spinner mGenderSpinnerET;
    private EditText mPhoneNumberET;
    private EditText mEmailET;
    private EditText mSpecializationET;
    private EditText mPass1ET;
    private EditText mPass2ET;
    private String mFName;
    private String mSurname;
    private String mPractice;
    private String mEmail;
    private String mSpecialization;
    private String mPassOne;
    private String mPassTwo;
    private String mGender;
    private String mCountryCode;
    private String mMobile;
    private TextInputLayout mFNameLayout;
    private TextInputLayout mSurnameLayout;
    private TextInputLayout mEmailLayout;
    private TextInputLayout mPassOneLayout;
    private TextInputLayout mPassTwoLayout;
    private TextInputLayout mPracticeLayout;
    private TextInputLayout mSpecializationLayout;
    private static final int PERMISSION_ID = 200;
    private FusedLocationProviderClient mFusedLocationClient;
    private double latitude, longitude;
    private ImageView mUserImageView;
    private Uri mImageUri;
    private String mUserImagePath;
    private String mUserImageName;
    private final int PICK_IMAGE_REQUEST = 71;
    private TextView mUploadPhoto;
    private FirebaseStorage mStorage;
    private Transformation transformation;
    private String mSavedDoctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeGenderDropdown();
        initializeCountryCodesDropdown();

        instantiateLayouts();
        instantiateViews();
        addTextChangedListeners();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mUserImageView = findViewById(R.id.user_image);
        mUploadPhoto = findViewById(R.id.tv_upload_photo);
        mStorage = FirebaseStorage.getInstance();

        mUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        mRegister = findViewById(R.id.register_btn);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDoctor();
            }
        });

    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            mImageUri = data.getData();
            int radius = Resources.getSystem().getDisplayMetrics().widthPixels;
            int margin = 0;
            if (mImageUri != null) {
                transformation = new RoundedCornersTransformation(radius, margin);
//                int width = Resources.getSystem().getDisplayMetrics().widthPixels;
                Picasso.get()
                        .load(mImageUri.toString())
                        .transform(transformation)
                        .placeholder(R.drawable.account_user)
                        .error(R.drawable.account_user)
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
        Toast.makeText(this, "Image failed to upload, contact admin!", Toast.LENGTH_SHORT).show();
    }


    private void instantiateLayouts() {
        this.mFNameLayout = (TextInputLayout) findViewById(R.id.registration_firstname_et_input_layout);
        this.mSurnameLayout = (TextInputLayout) findViewById(R.id.registration_surname_et_input_layout);
        this.mEmailLayout = (TextInputLayout) findViewById(R.id.registration_email_et_input_layout);
        this.mPassOneLayout = (TextInputLayout) findViewById(R.id.registration_password_one_et_input_layout);
        this.mPassTwoLayout = (TextInputLayout) findViewById(R.id.registration_password_two_et_input_layout);
        this.mSpecializationLayout = (TextInputLayout) findViewById(R.id.registration_specialization_et_input_layout);
        this.mPracticeLayout = (TextInputLayout) findViewById(R.id.registration_hospital_et_input_layout);
    }

    private void instantiateViews() {
        mFirstNameET = (TextInputEditText) findViewById(R.id.et_first_name);
        mSurnameET = (EditText) findViewById(R.id.et_surname);
        mPracticeET = (EditText) findViewById(R.id.et_hospital);
        mCountryCodeSpinnerET = (Spinner) findViewById(R.id.number_prefix_spinner);
        mGenderSpinnerET = (Spinner) findViewById(R.id.gender_spinner);
        mPhoneNumberET = (EditText) findViewById(R.id.et_mobile);
        mEmailET = (EditText) findViewById(R.id.et_email);
        mSpecializationET = (EditText) findViewById(R.id.et_specialization);
        mPass1ET = (EditText) findViewById(R.id.et_pass_1);
        mPass2ET = (EditText) findViewById(R.id.et_pass_2);
    }

    private void addTextChangedListeners() {
        this.mFirstNameET.addTextChangedListener(new CustomTextWatcher(this.mFNameLayout));
        this.mSurnameET.addTextChangedListener(new CustomTextWatcher(this.mSurnameLayout));
        this.mPracticeET.addTextChangedListener(new CustomTextWatcher(this.mPracticeLayout));
        this.mPass1ET.addTextChangedListener(new CustomTextWatcher(this.mPassOneLayout));
        this.mPass2ET.addTextChangedListener(new CustomTextWatcher(this.mPassTwoLayout));
        this.mSpecializationET.addTextChangedListener(new CustomTextWatcher(this.mSpecializationLayout));
    }

    private void saveDoctor() {
        User doctor = getDoctor();
        if (doctor != null)

            getLastLocation();
            UserLocation userLocation = new UserLocation(latitude, longitude);

            doctor.setImageUrl(mUserImagePath);
            doctor.setUserImageName(mUserImageName);

            Log.d("doc", doctor.toString());

            DatabaseReference docRef = FirebaseUtil.getmDatabaseReference().child("users").push();
            mSavedDoctorId = docRef.getKey();
            docRef.setValue(doctor);
            userLocation.setUserId(mSavedDoctorId);
            FirebaseUtil.getmDatabaseReference().child("userLocations").push().setValue(userLocation);
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();

//            open another activity
    }

    private User getDoctor() {
        return getDataFromViews();

    }

    private User getDataFromViews() {
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

        return validateUserData();
    }

    private User validateUserData() {

        if(
            isValidFirstName()
                    && isValidSurname()
                    && isValidEmail()
                    && isValidPasswordOne()
                    && isValidSpecialization()
                    && isValidPasswordTwo()){
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
            return instantiateDoctor();
        }else{
            Toast.makeText(this, "Please validate your data!", Toast.LENGTH_SHORT).show();
            return null;
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


    private User instantiateDoctor() {
        User doctor = new User(
                    this.mFName,
                    this.mSurname,
                    this.mEmail,
                    this.mGender,
                true
            );
        doctor.setPassword(this.mPassOne);
        doctor.setMobilePhoneNumber(this.mMobile, this.mCountryCode);
        doctor.setPractice(this.mPractice);
        doctor.setSpecialization(this.mSpecialization);
        return doctor;
    }


    private void initializeCountryCodesDropdown() {
        Spinner spinner = (Spinner) findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void initializeGenderDropdown() {
        Spinner codeSpinner = (Spinner) findViewById(R.id.number_prefix_spinner);
        ArrayAdapter<CharSequence> codeAdapter = ArrayAdapter.createFromResource(this,
                R.array.country_codes_array, android.R.layout.simple_spinner_item);
        codeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        codeSpinner.setAdapter(codeAdapter);
    }

//    location stuff
    private void saveUserLocation(){

    }

    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Granted. Start getting the location information
            }
        }
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
    };

}
