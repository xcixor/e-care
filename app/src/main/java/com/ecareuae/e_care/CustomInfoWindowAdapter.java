package com.ecareuae.e_care;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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
//        mWindow.setLayoutParams(
//                new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                        120
//                )
//        );
    }

    private void renderWindowInfo(Marker marker, View view){
        Gson gson = new Gson();
        MarkerInfo markerInfo = gson.fromJson(marker.getSnippet(), MarkerInfo.class);
        TextView tvName = (TextView) view.findViewById(R.id.info_window_tv_name);
        tvName.setText("Dr. " + markerInfo.getName());
        TextView tvAddress = (TextView) view.findViewById(R.id.info_window_tv_practice);
        tvAddress.setText(markerInfo.getAddress());
        TextView tvMobile = (TextView) view.findViewById(R.id.info_window_tv_mobile);
        tvMobile.setText(markerInfo.getMobile());
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowInfo(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowInfo(marker, mWindow);
        return mWindow;
    }

//    @Override
//    public void onInfoWindowClick(Marker marker) {
////        Toast.makeText(mContext, "Window clicked", Toast.LENGTH_SHORT).show();
////        Log.d(TAG, "onWindowClick: Clicked");
//        Intent intent = new Intent(mContext, UserTypeSelectionActivity.class);
//    }
}
