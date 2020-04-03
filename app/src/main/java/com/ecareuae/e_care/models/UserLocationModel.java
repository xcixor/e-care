package com.ecareuae.e_care.models;

public class UserLocationModel {
    private double mLatitude;
    private double mLongitude;
    private UserModel mUser;

    public UserLocationModel(double latitude, double longitude, UserModel user) {
        mLatitude = latitude;
        mLongitude = longitude;
        mUser = user;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public UserModel getUser() {
        return mUser;
    }

    public void setUser(UserModel user) {
        mUser = user;
    }

    @Override
    public String toString() {
        return "UserLocation{" +
                "mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mUserId='" + mUser.getEmail() + '\'' +
                '}';
    }
}
