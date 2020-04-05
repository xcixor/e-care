package com.ecareuae.e_care.ui.home;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.ecareuae.e_care.MainActivity;
import com.ecareuae.e_care.helpers.CustomInfoWindowAdapter;
import com.ecareuae.e_care.repositories.FirebaseUtil;
import com.ecareuae.e_care.helpers.MarkerInfo;
import com.ecareuae.e_care.R;
import com.ecareuae.e_care.models.UserLocationModel;
import com.ecareuae.e_care.models.UserModel;
import com.ecareuae.e_care.ui.appointment_booking.BookAppointmentFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements
        OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{

    public static final String TAG = "HomeFragment";
    public static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 200;
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final float DEFAULT_ZOOM = 7f;
    private Boolean mMLocationPermissionGranted = false;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private EditText mSearchInput;
    private ImageView mGpsLocater;
    private ArrayList<UserLocationModel> mPlaces;
    private ArrayList<UserModel> mUserModels;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)getActivity()).toggleMenutItems();
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mSearchInput = root.findViewById(R.id.et_search);
        mGpsLocater = root.findViewById(R.id.ic_gps);

        if(isServiceOk()){
            init();
        }

        DatabaseReference ref = FirebaseUtil.getmDatabaseReference().child("userLocations");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                instantiateDoctorsLocations(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }

    private void instantiateDoctorsLocations(DataSnapshot dataSnapshot) {
        mPlaces = new ArrayList<>();
        for (DataSnapshot ds: dataSnapshot.getChildren()){
            String key = ds.getKey();
            UserLocationModel location = new UserLocationModel();
            location = ds.getValue(UserLocationModel.class);
            mPlaces.add(location);
        }
        setDoctorsLocations(mPlaces);
    }

    private void init(){
        getLocationPermissions();
    }

    private void initSearch(){
        Log.d(TAG, "initSearch: initialize search");
        mSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                Log.d(TAG, "initSearch: focus changed to input search ...........");
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    Log.d(TAG, "initSearch: something has been typed ...........");
                    geoLocate();

                }
                return false;
            }
        });
        mGpsLocater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
            }
        });
        hideSoftKeyboard();
    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: searching........");
        String searchInput = mSearchInput.getText().toString();
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addressList = new ArrayList<>();
        try {
            addressList = geocoder.getFromLocationName(searchInput, 1);
        }catch (IOException e){
            Toast.makeText(getContext(), "Slow network! try again", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "IOException: "+ e.getMessage());
        }
        if (addressList.size() > 0){
            Address address = addressList.get(0);
            Log.d(TAG, "geoLocate: " + address.toString());
            moveCamera(
                    new LatLng(address.getLatitude(), address.getLongitude()),
                    DEFAULT_ZOOM,
                    address.getAddressLine(0)
            );
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getContext(), "Map is ready", Toast.LENGTH_SHORT).show();
        mGoogleMap = googleMap;
        mGoogleMap.setOnInfoWindowClickListener(this);
        if(mMLocationPermissionGranted){
            getDeviceLocation();
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            initSearch();
        }
    }

    private void setDoctorsLocations(ArrayList<UserLocationModel> locations) {
        MarkerOptions markerOptions = new MarkerOptions();
//        Location aLocation =  new Location();
        if (locations.size() > 0) {
            for (UserLocationModel location : locations) {
                Log.d(TAG, "setDoctorsLocations: name " + location.getUser().getEmail());
                LatLng latLng = new LatLng(Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongitude()));
                Log.d(TAG, "setDoctorsLocations: location " + latLng);
                MarkerInfo markerInfo = new MarkerInfo();
                if (location.getUser().isDoctor())
                    markerInfo.setName("Dr. " + location.getUser().getSurName());
                markerInfo.setAddress(location.getUser().getPractice());
                markerInfo.setMobile(location.getUser().getMobilePhoneNumber());
                markerInfo.setUserId(location.getUser().getEmail());
                markerInfo.setUser(location.getUser());
                Gson gson = new Gson();
                String markerInfoString = gson.toJson(markerInfo);
                markerOptions
                        .position(latLng)
                        .snippet(markerInfoString).
                        title(location.getUser().getSurName());
                mGoogleMap.addMarker(markerOptions);
                mGoogleMap.setInfoWindowAdapter(
                        new CustomInfoWindowAdapter(getContext()));
            }
        }
    }

    public boolean isServiceOk(){
        Log.d(TAG, "isServiceOk: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
        if (available == ConnectionResult.SUCCESS){
//            ok user can make map requests
            return true;
        }else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "Resolvable error");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(getContext(), "You can't make map requests", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

    public void getLocationPermissions(){
        Log.d(TAG, "getLocationPermissions: getting permissions");
        String [] permissions = {FINE_LOCATION, COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(getContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mMLocationPermissionGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mMLocationPermissionGranted = false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            return;
                        }
                    }
                    Log.d(TAG, "Permissions Result: granted");
                    mMLocationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getDeviceLocation(){
        Log.d(TAG, "Device Location: getting device location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        try {
            if (mMLocationPermissionGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Location currentLocation = (Location)task.getResult();
                            moveCamera(
                                    new LatLng(currentLocation.getLatitude(),
                                            currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location");
                        }else{
                            Toast.makeText(getContext(), "Unable to set your location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.d(TAG, "getDeviceLocation: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);
        mGoogleMap.addMarker(markerOptions);
        hideSoftKeyboard();
    }

    private void hideSoftKeyboard(){
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getActivity().getCurrentFocus() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getTitle().equals("My Location"))
            return;
        Gson gson = new Gson();
        MarkerInfo markerInfo = gson.fromJson(marker.getSnippet(), MarkerInfo.class);
        Fragment frag = new BookAppointmentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("key", markerInfo.getUser());
        frag.setArguments(bundle);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(this.getId(), frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

}