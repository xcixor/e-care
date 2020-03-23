package com.ecareuae.e_care;

public class User {
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

    public User(String firstName, String surName, String email, String gender, boolean isDoctor)
    {
        this.firstName = firstName;
        this.surName = surName;
        this.email = email;
        this.gender = gender;
        this.isDoctor = isDoctor;
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

    public void setMobilePhoneNumber(String phoneNumber, String countryCode) {
        String userNumber = countryCode + phoneNumber;
        this.mobilePhoneNumber = userNumber;
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
}
