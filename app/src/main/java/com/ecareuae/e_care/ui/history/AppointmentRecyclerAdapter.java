package com.ecareuae.e_care.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecareuae.e_care.models.MedicalAppointment;
import com.ecareuae.e_care.R;

import java.util.ArrayList;
import java.util.List;

public class AppointmentRecyclerAdapter extends RecyclerView.Adapter<AppointmentRecyclerAdapter.ViewHolder>{
    private final Context mContext;
    private final LayoutInflater mInflater;
    private final List<MedicalAppointment> mAppointments;

    public AppointmentRecyclerAdapter(Context context, List<MedicalAppointment> appointments) {
        mContext = context;
        mAppointments = appointments;
        mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.appointment_container, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MedicalAppointment appointment = mAppointments.get(position);
        String title = "Appointment with " + appointment.getDoctor() + " on " + appointment.getDate().toString();
        holder.mTvTitle.setText(title);
        holder.mTvDescription.setText(appointment.getMessage());

    }

    @Override
    public int getItemCount() {
        return mAppointments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView mTvTitle;
        public final TextView mTvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.appointment_title);
            mTvDescription = itemView.findViewById(R.id.appointment_description);
        }
    }
}
