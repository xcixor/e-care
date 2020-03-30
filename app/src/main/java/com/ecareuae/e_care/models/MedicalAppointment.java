package com.ecareuae.e_care.models;

import java.util.Date;

public class MedicalAppointment {
    private String mDoctor;
    private Date mDate;
    private String mMessage;

    public MedicalAppointment(String doctor, Date date, String message) {
        mDoctor = doctor;
        mDate = date;
        mMessage = message;
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
}
