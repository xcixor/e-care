package com.ecareuae.e_care.ui.appointment_booking;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ecareuae.e_care.R;

public class BookAppointmentFragment extends Fragment {

    private BookAppointmentViewModel sendViewModel;
    private Calendar mCalendar;
    private CalendarView mCalendarView;
    private View mRoot;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(BookAppointmentViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_book_appointment, container, false);
        setCalenderMinDate();
        return mRoot;
    }

    private void setCalenderMinDate() {
        mCalendarView = (CalendarView)mRoot.findViewById(R.id.appointment_calender);
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
}