package com.ecareuae.e_care.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class HistoryFragment extends Fragment {

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
        mRecyclerAppointments = mRoot.findViewById(R.id.appointment_list);

        mHistoryViewModel.init();
        mHistoryViewModel.getMedicalAppointments().observe(this, new Observer<List<MedicalAppointment>>() {
            @Override
            public void onChanged(List<MedicalAppointment> medicalAppointments) {
                mAdapter.notifyDataSetChanged();
            }
        });
        initializeAppointments();
//        initializeViews();
        return mRoot;
    }


    private void initializeAppointments(){
        mAdapter = new AppointmentRecyclerAdapter(getContext(), mHistoryViewModel.getMedicalAppointments().getValue());
        mAppointmentsLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerAppointments.setLayoutManager(mAppointmentsLayoutManager);
        mRecyclerAppointments.setAdapter(mAdapter);
    }

    private ArrayList<MedicalAppointment> getAppointments() {
        return null;
    }
}