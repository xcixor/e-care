package com.ecareuae.e_care.ui.home;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import androidx.navigation.Navigation;

import com.ecareuae.e_care.MainActivity;
import com.ecareuae.e_care.R;
import com.ecareuae.e_care.helpers.CustomInfoWindowAdapter;
import com.ecareuae.e_care.helpers.MarkerInfo;
import com.ecareuae.e_care.models.UserLocationModel;
import com.ecareuae.e_care.repositories.FirebaseUtil;
import com.ecareuae.e_care.ui.appointment_booking.BookAppointmentFragment;
import com.ecareuae.e_care.ui.appointment_booking.BookAppointmentFragmentDirections;
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
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements
        OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, View.OnClickListener {

    public static final String TAG = "HomeFragment";
    public static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 200;
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final float DEFAULT_ZOOM = 6f;
    private Boolean mMLocationPermissionGranted = false;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private EditText mSearchInput;
    private ImageView mGpsLocater, mCancelSearch;
    private ArrayList<UserLocationModel> mPlaces;
    private ArrayList<Marker> mMarkers;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)getActivity()).toggleMenutItems();
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mSearchInput = root.findViewById(R.id.et_search);
        mGpsLocater = root.findViewById(R.id.ic_gps);
        mCancelSearch = root.findViewById(R.id.ic_cancel);
        mCancelSearch.setOnClickListener(this);
        mPlaces = new ArrayList<>();
        mMarkers = new ArrayList<>();

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
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
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
        String searchInput = mSearchInput.getText().toString();
        fetchDoctorsFromDatabase(searchInput);
    }

    private void fetchDoctorsFromDatabase(String searchInput) {
        Log.d(TAG, "fetchDoctorsFromDatabase: searching for " + searchInput);
        String search = searchInput.toLowerCase();
        ArrayList<UserLocationModel> foundLocations = new ArrayList<>();
        if (mPlaces.size() > 0){
            for (UserLocationModel location : mPlaces){
                if (
                        location.getUser().getSpecialization().toLowerCase().equals(search) ||
                                location.getUser().getEmail().toLowerCase().equals(search) ||
                                location.getUser().getFirstName().toLowerCase().equals(search) ||
                                location.getUser().getSurName().toLowerCase().equals(search) ||
                                location.getUser().getMobilePhoneNumber().toLowerCase().equals(search) ||
                                location.getUser().getGender().toLowerCase().equals(search)
                ){
                    foundLocations.add(location);
                }
            }
            if (foundLocations.size() > 0) {
                setDoctorsLocations(foundLocations);
            }else {
                toast("No results found for: " + searchInput);
            }
        }
    }

    public void toast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getContext(), "welcome", Toast.LENGTH_SHORT).show();
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
        for (Marker marker : mMarkers){
            marker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        if (locations.size() > 0) {
            int position = 0;
            for (UserLocationModel location : locations) {
                Log.d(TAG, "setDoctorsLocations: name " + location.getUser().getEmail());
                LatLng latLng = new LatLng(Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongitude()));
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
                    Marker marker = mGoogleMap.addMarker(markerOptions);
                    mGoogleMap.setInfoWindowAdapter(
                            new CustomInfoWindowAdapter(getContext()));
                    mMarkers.add(marker);
//                    moveCamera(latLng, DEFAULT_ZOOM, "Dr. " + location.getUser().getSurName());
                    position++;
            }
        }
    }

    public boolean isServiceOk(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
        if (available == ConnectionResult.SUCCESS){
            return true;
        }else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(getContext(), "You can't make map requests", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

    public void getLocationPermissions(){
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
        if (getActivity().getCurrentFocus() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().
                getCurrentFocus().
                getWindowToken(), 0);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getTitle().equals("My Location"))
            return;
        Gson gson = new Gson();
        MarkerInfo markerInfo = gson.fromJson(marker.getSnippet(), MarkerInfo.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("key", markerInfo.getUser());
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                .navigate(R.id.frag_book_appointment, bundle);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: cancelling search  ....");
        setDoctorsLocations(mPlaces);
    }
}