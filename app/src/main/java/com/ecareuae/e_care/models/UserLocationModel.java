package com.ecareuae.e_care.models;

public class UserLocationModel {
    private String mLatitude;
    private String mLongitude;
    private UserModel mUser;

    public UserLocationModel(String latitude, String longitude, UserModel user) {
        mLatitude = latitude;
        mLongitude = longitude;
        mUser = user;
    }

    public UserLocationModel(){}

    public String getLatitude() {
        return mLatitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public UserModel getUser() {
        return mUser;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public void setUser(UserModel user) {
        mUser = user;
    }

    @Override
    public String toString() {
        return "UserLocation{" +
                "mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                '}';
    }
}
