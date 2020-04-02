package com.ecareuae.e_care.ui.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecareuae.e_care.models.MedicalAppointment;
import com.ecareuae.e_care.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment implements AppointmentRecyclerAdapter.OnAppointmentListener {

    private static String TAG = "HistoryFragment";
    private HistoryViewModel mHistoryViewModel;
    private View mRoot;
    private RecyclerView mRecyclerAppointments;
    private LinearLayoutManager mAppointmentsLayoutManager;
    private AppointmentRecyclerAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mHistoryViewModel =
                ViewModelProviders.of(this).get(HistoryViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_history, container, false);

        mHistoryViewModel.init();
        initializeViews();
        initializeAppointments();
        return mRoot;
    }

    private void initializeViews() {
        mRecyclerAppointments = mRoot.findViewById(R.id.appointment_list);
    }


    private void initializeAppointments(){
        mAdapter = new AppointmentRecyclerAdapter(getContext(), mHistoryViewModel.getMedicalAppointments(), this);
        mAppointmentsLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerAppointments.setLayoutManager(mAppointmentsLayoutManager);
        mRecyclerAppointments.setAdapter(mAdapter);
        Log.d(TAG, "initializeAppointments: initialized!");
    }

    private ArrayList<MedicalAppointment> getAppointments() {
        return null;
    }


    private void openEditAppointmentScren() {
        Toast.makeText(getContext(), "Edited!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAppointmentClicked(int position) {
        Log.d(TAG, "onAppointmentClicked: clicked! "+ mHistoryViewModel.getMedicalAppointment(position).getDoctor());
    }

    @Override
    public void onDeleteIconClick(int position) {
        if (mHistoryViewModel.removeAppointment(position)) {
            mAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Appointment Deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEditIconClick(int position) {
        Log.d(TAG, "onEditClicked: clicked! "+ mHistoryViewModel.getMedicalAppointment(position).getDate());
    }
}