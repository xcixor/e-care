package com.ecareuae.e_care.helpers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ecareuae.e_care.R;
import com.ecareuae.e_care.helpers.MarkerInfo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;
    public static final String TAG = "CustomInfoWindowAdapter";

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void renderWindowInfo(Marker marker, View view){
        Gson gson = new Gson();
        Log.d(TAG, "renderWindowInfo: " + marker.getTitle());
        MarkerInfo markerInfo = gson.fromJson(marker.getSnippet(), MarkerInfo.class);
        TextView tvName = (TextView) view.findViewById(R.id.info_window_tv_name);
        tvName.setText(markerInfo.getName());
        TextView tvAddress = (TextView) view.findViewById(R.id.info_window_tv_practice);
        tvAddress.setText(markerInfo.getAddress());
        TextView tvMobile = (TextView) view.findViewById(R.id.info_window_tv_mobile);
        tvMobile.setText(markerInfo.getMobile());

    }

    @Override
    public View getInfoWindow(Marker marker) {
        if (!marker.getTitle().equals("My Location")) {
            renderWindowInfo(marker, mWindow);
            return mWindow;
        }else {
            return null;
        }
    }

    @Override
    public View getInfoContents(Marker marker) {
        if (!marker.getTitle().equals("My Location")) {
            renderWindowInfo(marker, mWindow);
            return mWindow;
        }else{
            return null;
        }
    }

}
