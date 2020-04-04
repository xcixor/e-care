package com.ecareuae.e_care.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {
    private String firstName;
    private String surName;
    private String practice;
    private String mobilePhoneNumber;
    private String email;
    private String specialization;
    private String gender;
    private String password;
    private String image;
    private String location;
    private String ID;
    private boolean isDoctor;
    private String mUserImageName;
    private String mCountryCode;


    public UserModel(String firstName, String surName, String email, String gender, boolean isDoctor)
    {
        this.firstName = firstName;
        this.surName = surName;
        this.email = email;
        this.gender = gender;
        this.isDoctor = isDoctor;
    }

    public UserModel(){}

    private UserModel(Parcel in){
        firstName = in.readString();
        surName = in.readString();
        email = in.readString();
        gender = in.readString();
        isDoctor = in.readInt() == 1;
        image = in.readString();
        specialization = in.readString();
    }

    public boolean isDoctor() {
        return isDoctor;
    }

    public void setDoctor(boolean doctor) {
        isDoctor = doctor;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurName() {
        return surName;
    }

    public String getPractice() {
        return practice;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }

    public String getImage() {
        return image;
    }

    public String getLocation() {
        return location;
    }

    public void setMobilePhoneNumber(String phoneNumber) {
        this.mobilePhoneNumber = phoneNumber;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setPractice(String practice) {
        if (!practice.isEmpty() && practice != null) {
            this.practice = practice;
        }else {
            this.practice = "Practise name not provided";
        }

    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserImageName() {
        return mUserImageName;
    }

    public void setUserImageName(String userImageName) {
        mUserImageName = userImageName;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "firstName='" + firstName + '\'' +
                ", surName='" + surName + '\'' +
                ", practice='" + practice + '\'' +
                ", mobilePhoneNumber='" + mobilePhoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", specialization='" + specialization + '\'' +
                ", gender='" + gender + '\'' +
                ", password='" + password + '\'' +
                ", image='" + image + '\'' +
                ", location='" + location + '\'' +
                ", ID='" + ID + '\'' +
                ", isDoctor=" + isDoctor +
                ", mUserImageName='" + mUserImageName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(firstName);
        parcel.writeString(surName);
        parcel.writeString(email);
        parcel.writeString(gender);
        parcel.writeString(image);
        parcel.writeString(specialization);
        parcel.writeInt(isDoctor ? 1 : 0);
    }

    public final static Parcelable.Creator<UserModel> CREATOR =
            new Parcelable.Creator<UserModel>(){

                @Override
                public UserModel createFromParcel(Parcel parcel) {
                    return new UserModel(parcel);
                }

                @Override
                public UserModel[] newArray(int i) {
                    return new UserModel[i];
                }
            };
}
