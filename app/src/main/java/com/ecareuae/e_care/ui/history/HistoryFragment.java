package com.ecareuae.e_care.ui.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecareuae.e_care.models.MedicalAppointmentModel;
import com.ecareuae.e_care.R;
import com.ecareuae.e_care.ui.appointment_edit.AppointmentEditFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryFragment extends Fragment implements AppointmentRecyclerAdapter.OnAppointmentListener {

    private static String TAG = "HistoryFragment";
    private HistoryViewModel mHistoryViewModel;
    private View mRoot;
    private RecyclerView mRecyclerAppointments;
    private LinearLayoutManager mAppointmentsLayoutManager;
    private AppointmentRecyclerAdapter mAdapter;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private ArrayList<MedicalAppointmentModel> mAppointments;
    private String mUserId;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mHistoryViewModel =
                ViewModelProviders.of(this).get(HistoryViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_history, container, false);

        mHistoryViewModel.init();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        initializeViews();
//        initializeAppointments();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("appointments");
        Query appointmentsQuery = ref.orderByChild("ownerEmail").equalTo(mCurrentUser.getEmail());
        appointmentsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
        return mRoot;
    }

    private void getData(DataSnapshot snapshot) {
        mAppointments = new ArrayList<>();
        for (DataSnapshot ds: snapshot.getChildren()){
            String key = ds.getKey();
            MedicalAppointmentModel location = new MedicalAppointmentModel();
            location = ds.getValue(MedicalAppointmentModel.class);
            mAppointments.add(location);
        }
        initializeAppointments(mAppointments);
        Log.d(TAG, "getData: " + mAppointments);
    }

    private void initializeViews() {
        mRecyclerAppointments = mRoot.findViewById(R.id.appointment_list);
    }


    private void initializeAppointments(ArrayList<MedicalAppointmentModel> appointments){
        mAdapter = new AppointmentRecyclerAdapter(getContext(), appointments, this);
        mAppointmentsLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerAppointments.setLayoutManager(mAppointmentsLayoutManager);
        mRecyclerAppointments.setAdapter(mAdapter);
        Log.d(TAG, "initializeAppointments: initialized!");
    }

    private ArrayList<MedicalAppointmentModel> getAppointments() {
        return null;
    }

    private void openEditScreen(int position){
        MedicalAppointmentModel appointment = mHistoryViewModel.getMedicalAppointment(position);
        if (appointment != null){
            Fragment frag = new AppointmentEditFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("key", appointment);
            frag.setArguments(bundle);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(this.getId(), frag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }
//        Fragment frag = new AppointmentEditFragment();
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.replace(this.getId(), frag);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        ft.addToBackStack(null);
//        ft.commit();
    }

    @Override
    public void onAppointmentClicked(int position) {
        openEditScreen(position);
        Log.d(TAG, "onAppointmentClicked: clicked! "+ mHistoryViewModel.getMedicalAppointment(position).getDoctor());
    }

    @Override
    public void onDeleteIconClick(int position) {
//        if (mHistoryViewModel.removeAppointment(position)) {
//            mAdapter.notifyDataSetChanged();
//            Toast.makeText(getContext(), "Appointment Deleted!", Toast.LENGTH_SHORT).show();
//        }
        MedicalAppointmentModel toDelete = mAppointments.get(position);
        Log.d(TAG, "onDeleteIconClick: " + toDelete);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("appointments");
        Query deleteQuery = ref.orderByChild("message").equalTo(toDelete.getMessage());
        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot appointmentSnapshot: dataSnapshot.getChildren()) {
                    appointmentSnapshot.getRef().removeValue();
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public void onEditIconClick(int position) {
        openEditScreen(position);
        Log.d(TAG, "onEditClicked: clicked! "+ mHistoryViewModel.getMedicalAppointment(position).getDate());
    }
}