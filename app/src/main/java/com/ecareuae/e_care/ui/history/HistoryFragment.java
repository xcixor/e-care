package com.ecareuae.e_care.ui.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecareuae.e_care.R;
import com.ecareuae.e_care.models.MedicalAppointmentModel;
import com.ecareuae.e_care.models.UserModel;
import com.ecareuae.e_care.repositories.FirebaseUtil;
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
    private FirebaseDatabase mDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_history, container, false);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        initializeViews();


        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = mDatabase.getReference().child("users");
        Query usersQuery = ref.orderByChild("email").equalTo(mCurrentUser.getEmail());
        usersQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        return mRoot;
    }

    private void showData(DataSnapshot ds) {
        Log.d(TAG, "showData: getting user");
        if (ds.exists()) {
            String key = "";
            for (DataSnapshot dataSnapshot: ds.getChildren()) {
                key = dataSnapshot.getKey();
            }
            UserModel user = new UserModel();
            user = new UserModel(
                    ds.child(key).getValue(UserModel.class).getFirstName(),
                    ds.child(key).getValue(UserModel.class).getSurName(),
                    ds.child(key).getValue(UserModel.class).getEmail(),
                    ds.child(key).getValue(UserModel.class).getGender(),
                    ds.child(key).getValue(UserModel.class).isDoctor());
            Log.d(TAG, "showData: user is " + user.getEmail());
            setData(user);
        }else {
            Log.d(TAG, "onDataChange: user not found");
        }
    }

    private void setData(UserModel user) {
        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = mDatabase.getReference().child("appointments");
        Log.d(TAG, "setData: user is " + user.isDoctor());
        if (user.isDoctor()){
            Query appointmentsQuery = ref.orderByChild("doctorEmail").equalTo(mCurrentUser.getEmail());
            appointmentsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    getData(snapshot, user);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                }
            });
        }else {
            Log.d(TAG, "setData: in user is not doctor");
            Query appointmentsQuery = ref.orderByChild("ownerEmail").equalTo(user.getEmail());
            appointmentsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    getData(snapshot, user);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                }
            });
        }
    }

    private void getData(DataSnapshot snapshot, UserModel user) {
        mAppointments = new ArrayList<>();
        for (DataSnapshot ds: snapshot.getChildren()){
            String key = ds.getKey();
            MedicalAppointmentModel appointment = new MedicalAppointmentModel();
            appointment = ds.getValue(MedicalAppointmentModel.class);
            appointment.setIsDoctor(user.isDoctor());
            mAppointments.add(appointment);
        }
        Log.d(TAG, "getData: user data is  " + mAppointments);
        initializeAppointments(mAppointments);
    }

    private void initializeViews() {
        mRecyclerAppointments = mRoot.findViewById(R.id.appointment_list);
    }


    private void initializeAppointments(ArrayList<MedicalAppointmentModel> appointments){
        if (getActivity() != null) {
            mAdapter = new AppointmentRecyclerAdapter(getContext(), appointments, this);
            mAppointmentsLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerAppointments.setLayoutManager(mAppointmentsLayoutManager);
            mRecyclerAppointments.setAdapter(mAdapter);
            Log.d(TAG, "initializeAppointments: initialized!");
        }
    }

    private ArrayList<MedicalAppointmentModel> getAppointments() {
        return null;
    }

    private void openEditScreen(int position){
        MedicalAppointmentModel appointment = mAppointments.get(position);
        if (appointment != null){
            Bundle bundle = new Bundle();
            bundle.putParcelable("key", appointment);
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.frag_edit_appointment, bundle);
        }
    }

    @Override
    public void onAppointmentClicked(int position) {
        openEditScreen(position);
    }

    @Override
    public void onDeleteIconClick(int position) {
        MedicalAppointmentModel toDelete = mAppointments.get(position);
        Log.d(TAG, "onDeleteIconClick: " + toDelete);
        DatabaseReference ref = FirebaseUtil.getmDatabaseReference().child("appointments");
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
    }
}