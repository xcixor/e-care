package com.ecareuae.e_care.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class MedicalAppointmentModel implements Parcelable {
    private String mDoctor;
    private Date mDate;
    private String mMessage;
    private String mSpecialization;
    private String mOwnerEmail;
    private String mDoctorEmail;
    private boolean isDoctor;


    public MedicalAppointmentModel(String doctor, Date date, String message, String ownerEmail, String doctorEmail) {
        mDoctor = doctor;
        mDate = date;
        mMessage = message;
        mOwnerEmail = ownerEmail;
        mDoctorEmail = doctorEmail;
    }

    public boolean getIsDoctor() {
        return isDoctor;
    }

    public void setIsDoctor(boolean doctor) {
        isDoctor = doctor;
    }

    public MedicalAppointmentModel(){}

    public String getDoctorEmail() {
        return mDoctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        mDoctorEmail = doctorEmail;
    }

    public String getOwnerEmail() {
        return mOwnerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.mOwnerEmail = ownerEmail;
    }

    private MedicalAppointmentModel(Parcel source){
        mDoctor = source.readString();
        mSpecialization = source.readString();
        mSpecialization = source.readString();
        long tmpDate = source.readLong();
        mDate = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public String getDoctor() {
        return mDoctor;
    }

    public void setDoctor(String doctor) {
        mDoctor = doctor;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getSpecialization() {
        return mSpecialization;
    }

    public void setSpecialization(String specialization) {
        mSpecialization = specialization;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mDoctor);
        parcel.writeString(mMessage);
        parcel.writeString(mSpecialization);
        parcel.writeLong(mDate.getTime());
    }

    public final static Parcelable.Creator<MedicalAppointmentModel> CREATOR =
            new Parcelable.Creator<MedicalAppointmentModel>() {

                @Override
                public MedicalAppointmentModel createFromParcel(Parcel source) {
                    return new MedicalAppointmentModel(source);
                }

                @Override
                public MedicalAppointmentModel[] newArray(int size) {
                    return new MedicalAppointmentModel[size];
                }
            };

    @Override
    public String toString() {
        return "MedicalAppointmentModel{" +
                "mDoctor='" + mDoctor + '\'' +
                ", mDate=" + mDate +
                ", mMessage='" + mMessage + '\'' +
                ", mSpecialization='" + mSpecialization + '\'' +
                '}';
    }
}
