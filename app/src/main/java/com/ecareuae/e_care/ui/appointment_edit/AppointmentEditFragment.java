package com.ecareuae.e_care.ui.appointment_edit;

import android.os.Bundle;
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

import java.util.Date;

public class AppointmentEditFragment extends Fragment implements View.OnClickListener{

    private static String TAG = "AppointmentEditFragment";
    private AppointmentEditViewModel mAppointmentEditViewModel;
    private MedicalAppointmentModel mAppointment;
    private TextView mHeadline;
    private CalendarView mCalender;
    private TextView mMessage;
    private FloatingActionButton mSend;
    private View mRoot;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mAppointmentEditViewModel =
                ViewModelProviders.of(this).get(AppointmentEditViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_appointment_edit, container, false);
        Bundle bundle = getArguments();
        instantiateViews();
        if(null != bundle) {
            mAppointment = bundle.getParcelable("key");
            setAppointmentDetails(mAppointment);
        }
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
    }
}