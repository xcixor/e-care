package com.ecareuae.e_care.ui.history;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecareuae.e_care.models.MedicalAppointmentModel;
import com.ecareuae.e_care.R;

import java.util.Date;
import java.util.List;

public class AppointmentRecyclerAdapter extends RecyclerView.Adapter<AppointmentRecyclerAdapter.ViewHolder>{
    private static String TAG = "HistoryFragment";
    private final Context mContext;
    private final LayoutInflater mInflater;
    private final List<MedicalAppointmentModel> mAppointments;
    private OnAppointmentListener mOnAppointmentListener;
    private HistoryViewModel mViewModel;
    public ImageView mDeleteIcon;
    private ImageView mEditIcon;


    public AppointmentRecyclerAdapter(Context context, List<MedicalAppointmentModel> appointments, OnAppointmentListener onAppointmentListener) {
        mContext = context;
        mAppointments = appointments;
        mInflater = LayoutInflater.from(mContext);
        this.mOnAppointmentListener = onAppointmentListener;
        mViewModel = new HistoryViewModel();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.appointment_item_container, parent, false);
        return new ViewHolder(itemView, mOnAppointmentListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MedicalAppointmentModel appointment = mAppointments.get(position);
        String title = "Appointment with " + appointment.getDoctor();
        Date date = appointment.getDate();
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        holder.mTvDate.setText(df.format("yyyy-MM-dd hh:mm a", date));
        holder.mTvTitle.setText(title);
        holder.mTvDescription.setText(appointment.getMessage());

    }

    @Override
    public int getItemCount() {
        return mAppointments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mTvTitle;
        public final TextView mTvDescription;
        private final TextView mTvDate;
        public OnAppointmentListener mAppointmentListener;

        public ViewHolder(@NonNull View itemView, OnAppointmentListener onAppointmentListener) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.appointment_title);
            mTvDescription = itemView.findViewById(R.id.appointment_description);
            mTvDescription.setMovementMethod(new ScrollingMovementMethod());
            mTvDate = itemView.findViewById(R.id.appointment_date);
            this.mAppointmentListener = onAppointmentListener;
            itemView.setOnClickListener(this);
            mDeleteIcon = itemView.findViewById(R.id.ic_appointment_delete);
            mDeleteIcon.setOnClickListener(this);
            mEditIcon = itemView.findViewById(R.id.ic_appointment_edit);
            mEditIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.appointment:
                    mAppointmentListener.onAppointmentClicked(getAdapterPosition());
                    break;
                case R.id.ic_appointment_delete:
                    mAppointmentListener.onDeleteIconClick(getAdapterPosition());
                    break;
                case R.id.ic_appointment_edit:
                    mAppointmentListener.onEditIconClick(getAdapterPosition());
                    break;
                default: break;
            }
        }
    }

    public interface OnAppointmentListener{
        void onAppointmentClicked(int position);
        void onDeleteIconClick(int position);
        void onEditIconClick(int position);
    }
}
