package com.ecareuae.e_care;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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

        mRegister = findViewById(R.id.register_btn);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDoctor();
            }
        });

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
            FirebaseUtil.getmDatabaseReference().child("users").push().setValue(doctor);
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

}
