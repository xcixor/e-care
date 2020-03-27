package com.ecareuae.e_care;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.widget.CalendarView;

public class BookAppointmentActivity extends AppCompatActivity {

    private Calendar mCalendar;
    private CalendarView mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        setCalenderMinDate();
    }

    private void setCalenderMinDate() {
        mCalendarView = (CalendarView)findViewById(R.id.appointment_calender);
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
