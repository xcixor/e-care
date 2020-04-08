package com.ecareuae.e_care.ui.appointment_booking;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.ecareuae.e_care.R;
import com.ecareuae.e_care.models.MedicalAppointmentModel;
import com.ecareuae.e_care.models.UserModel;
import com.ecareuae.e_care.repositories.FirebaseUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Date;

public class BookAppointmentFragment extends Fragment implements CalendarView.OnDateChangeListener, View.OnClickListener {

    private Calendar mCalendar;
    private CalendarView mCalendarView;
    private UserModel mDoctor;
    private FloatingActionButton mCreateAppointmentFAB;
    private TextView mAppointmentDoctor, mAppointmentMessage;
    private Date mDate;
    private View mRoot;
    private Long mAppointmentBookDate;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_book_appointment, container, false);
        mDate = new Date();
        Bundle bundle = getArguments();
        instantiateViews();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        setCalenderMinDate();
        mCalendarView.setOnDateChangeListener(this);
        mCreateAppointmentFAB.setOnClickListener(this);
        if(null != bundle) {
            mDoctor = bundle.getParcelable("key");
            mAppointmentDoctor.setText("Dr. " + mDoctor.getSurName() + " : " + mDoctor.getSpecialization());
        }
        return mRoot;
    }

    private void instantiateViews() {
        mAppointmentDoctor = mRoot.findViewById(R.id.tv_appointment_doctor);
        mAppointmentMessage = mRoot.findViewById(R.id.appointment_message);
        mCalendarView = mRoot.findViewById(R.id.appointment_calender);
        mCreateAppointmentFAB = mRoot.findViewById(R.id.send_booking_appointment);
    }

    private void setCalenderMinDate() {
        if (android.os.Build.VERSION.SDK_INT > 24){
            // Do something for lollipop and above versions
            mCalendar = Calendar.getInstance();
            mCalendar.set(Calendar.DATE,Calendar.getInstance().getActualMinimum(Calendar.DATE));
            long date = mCalendar.getTime().getTime();
            mCalendarView.setMinDate(date);
        } else{
            // do something for phones running an SDK before lollipop
        }
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mCalendar.set(year, month, day);
            mAppointmentBookDate = mCalendar.getTimeInMillis();
            mDate = new Date(mAppointmentBookDate);
        }
    }

    @Override
    public void onClick(View view) {
        if (mCurrentUser != null) {
            createAppointment(mDoctor);
            Navigation.findNavController(view).navigate(R.id.nav_history);
        }else {
            toast("Please login to perform this action");
        }
    }

    private void createAppointment(UserModel doctor) {
        String message =  mAppointmentMessage.getText().toString();
        MedicalAppointmentModel appointment = new MedicalAppointmentModel(
                doctor.getSurName(),
                mDate,
                message,
                mCurrentUser.getEmail(),
                doctor.getEmail()
        );
        if (!mCurrentUser.getEmail().equals(doctor.getEmail())) {
            saveAppointment(appointment);
            sendEmail(message);
            toast("Appointment with " + appointment.getDoctor() + " on " + appointment.getDate() + " created.");
        }else{
            toast("You cannot make an appointment with yourself");
        }
    }

    private void toast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void sendEmail(String message) {
        String emailMessage = "";
        if (message.isEmpty()) {
            emailMessage = "Request for appointment on " + mDate.toString();
        }else{
            emailMessage = message;
            emailMessage += "\n\n";
            emailMessage += "Appointment Date: " + mDate.toString();
        }

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{mDoctor.getEmail()});
        i.putExtra(Intent.EXTRA_SUBJECT, "Appointment Request");
        i.putExtra(Intent.EXTRA_TEXT   , emailMessage);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            toast("There are no email clients installed. Email not sent!");
        }
    }

    private void saveAppointment(MedicalAppointmentModel appointment) {
        //        save to db
        FirebaseUtil.getmDatabaseReference().child("appointments").push().setValue(appointment);
    }
}