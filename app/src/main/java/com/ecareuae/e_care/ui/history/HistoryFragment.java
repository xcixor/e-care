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

public class HistoryFragment extends Fragment implements View.OnClickListener, AppointmentRecyclerAdapter.OnAppointmentListener {

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
        mHistoryViewModel.getMedicalAppointments().observe(getViewLifecycleOwner(), new Observer<List<MedicalAppointment>>() {
            @Override
            public void onChanged(List<MedicalAppointment> medicalAppointments) {
                mAdapter.notifyDataSetChanged();
            }
        });
        initializeViews();
        initializeAppointments();
        return mRoot;
    }

    private void initializeViews() {
        mRecyclerAppointments = mRoot.findViewById(R.id.appointment_list);
    }


    private void initializeAppointments(){
        mAdapter = new AppointmentRecyclerAdapter(getContext(), mHistoryViewModel.getMedicalAppointments().getValue(), this);
        mAppointmentsLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerAppointments.setLayoutManager(mAppointmentsLayoutManager);
        mRecyclerAppointments.setAdapter(mAdapter);
        Log.d(TAG, "initializeAppointments: initialized!");
    }

    private ArrayList<MedicalAppointment> getAppointments() {
        return null;
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: switching!");
        switch (view.getId()){
            case R.id.ic_appointment_edit:
                openEditAppointmentScren();
                break;
            case R.id.ic_appointment_delete:
                deleteAppointment();
                break;
            default: break;
        }
    }

    private void deleteAppointment() {
        Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
    }

    private void openEditAppointmentScren() {
        Toast.makeText(getContext(), "Edited!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAppointmentClicked(int position) {
        Log.d(TAG, "onAppointmentClicked: clicked! "+ mHistoryViewModel.getMedicalAppointment(position).getValue().getDoctor());
    }
}