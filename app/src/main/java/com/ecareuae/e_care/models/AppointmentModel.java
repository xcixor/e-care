package com.ecareuae.e_care.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class AppointmentModel implements Parcelable {
    private String mDoctorsName;
    private String mSpecialization;
    private String mMessage;
    private Date mDate;

    public AppointmentModel(String doctorsName, String specialization, Date date) {
        mDoctorsName = doctorsName;
        mSpecialization = specialization;
        mDate = date;
        mMessage = "Message empty";
    }

    private AppointmentModel(Parcel source){
        mDoctorsName = source.readString();
        mSpecialization = source.readString();
        mSpecialization = source.readString();
//        mDate = source.
    }

    public String getDoctorsName() {
        return mDoctorsName;
    }

    public void setDoctorsName(String doctorsName) {
        mDoctorsName = doctorsName;
    }

    public String getSpecialization() {
        return mSpecialization;
    }

    public void setSpecialization(String specialization) {
        mSpecialization = specialization;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        if (!message.isEmpty())
            this.mMessage = message;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mDoctorsName);
        parcel.writeString(mMessage);
        parcel.writeString(mSpecialization);
    }

    public final static Parcelable.Creator<AppointmentModel> CREATOR =
            new Parcelable.Creator<AppointmentModel>() {

                @Override
                public AppointmentModel createFromParcel(Parcel source) {
                    return new AppointmentModel(source);
                }

                @Override
                public AppointmentModel[] newArray(int size) {
                    return new AppointmentModel[size];
                }
            };
}
