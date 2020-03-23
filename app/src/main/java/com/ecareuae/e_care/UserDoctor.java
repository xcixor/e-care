package com.ecareuae.e_care;

public class UserDoctor {
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

    public UserDoctor(String firstName, String surName, String practice,
                      String email, String specialization,
                      String gender)
    {
        this.firstName = firstName;
        this.surName = surName;
        this.practice = practice;
        this.email = email;
        this.specialization = specialization;
        this.gender = gender;
        this.image = image;
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
}
