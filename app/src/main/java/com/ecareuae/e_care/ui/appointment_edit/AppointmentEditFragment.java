package com.ecareuae.e_care.ui.appointment_edit;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ecareuae.e_care.R;
import com.ecareuae.e_care.models.MedicalAppointmentModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AppointmentEditFragment extends Fragment implements View.OnClickListener, CalendarView.OnDateChangeListener{

    private static String TAG = "AppointmentEditFragment";
    private MedicalAppointmentModel mAppointment;
    private TextView mHeadline;
    private CalendarView mCalender;
    private TextView mMessage;
    private FloatingActionButton mSend;
    private View mRoot;
    private Calendar mCalendar;
    private Date mDate;
    private Long mAppointmentBookDate;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_appointment_edit, container, false);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        Bundle bundle = getArguments();
        instantiateViews();
        if(null != bundle) {
            mAppointment = bundle.getParcelable("key");
            setAppointmentDetails(mAppointment);
        }
        mDate = mAppointment.getDate();
        return mRoot;
    }

    private void instantiateViews() {
        mHeadline = mRoot.findViewById(R.id.tv_appointment_detail_doctor);
        mCalender = mRoot.findViewById(R.id.select_date);
        mMessage = mRoot.findViewById(R.id.et_appointment_message);
        mSend = mRoot.findViewById(R.id.send_edited_msg);
        mSend.setOnClickListener(this);
    }

    private void setAppointmentDetails(MedicalAppointmentModel appointment) {
        Date date = appointment.getDate();
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        mHeadline.setText(mAppointment.getDoctor() + " : " + df.format("yy-MM-dd hh:mm a", date));
        long timeInMilliseconds = date.getTime();
        mCalender.setDate(timeInMilliseconds);
        mMessage.setText(mAppointment.getMessage());
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(getContext(), "Submit new msg", Toast.LENGTH_SHORT).show();
        String message =  mMessage.getText().toString();
        mAppointment = new MedicalAppointmentModel(mAppointment.getDoctor(), mDate, message, mCurrentUser.getEmail(), mAppointment.getDoctorEmail());
        saveAppointment(mAppointment);
        sendEmail(message);

    }

    private void saveAppointment(MedicalAppointmentModel appointment) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("appointments");
        Query editQuery = ref.orderByChild("message").equalTo(mAppointment.getMessage());
        editQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot appointmentSnapshot: dataSnapshot.getChildren()) {
                    String key = appointmentSnapshot.getKey();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/appointments/" + key, appointment);
                    ref.updateChildren(childUpdates);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    private void sendEmail(String message){
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
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{mAppointment.getDoctorEmail()});
        i.putExtra(Intent.EXTRA_SUBJECT, "Appointment Request");
        i.putExtra(Intent.EXTRA_TEXT   , emailMessage);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There are no email clients installed. Email not sent!", Toast.LENGTH_SHORT).show();
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
}