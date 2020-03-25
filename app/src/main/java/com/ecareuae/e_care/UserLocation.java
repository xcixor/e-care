package com.ecareuae.e_care;

public class UserLocation {
    private double mLatitude;
    private double mLongitude;
    private String mUserId;

    public UserLocation(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }
}
