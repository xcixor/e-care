package com.ecareuae.e_care;

import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;

import com.ecareuae.e_care.ui.home.HomeFragment;
import com.ecareuae.e_care.ui.profile.ProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

import static androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    private NavigationView mNavigationView;
    private NavController mNavController;
    private FirebaseUser mCurrentUser;
    private DrawerLayout mDrawer;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);


        addLogoutFunctionality();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_login, R.id.nav_history,
                R.id.nav_tools, R.id.nav_profile, R.id.nav_logout)
                .setDrawerLayout(mDrawer)
                .build();
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, mNavController);
        toggleMenutItems();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.invalidateOptionsMenu();
        }
    }

    public void toggleMenutItems() {
        mCurrentUser = mAuth.getCurrentUser();
        Menu nav_Menu = mNavigationView.getMenu();
        if (mCurrentUser == null){
//            logged out
            nav_Menu.findItem(R.id.nav_login).setVisible(true);
            nav_Menu.findItem(R.id.nav_history).setVisible(false);
            nav_Menu.findItem(R.id.nav_profile).setVisible(false);
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);

        }else{
            nav_Menu.findItem(R.id.nav_login).setVisible(false);
            nav_Menu.findItem(R.id.nav_history).setVisible(true);
            nav_Menu.findItem(R.id.nav_profile).setVisible(true);
            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
        }

    }


    private void addLogoutFunctionality() {
        mNavigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            mDrawer.closeDrawer(GravityCompat.START, false);
            mAuth.signOut();
            goHome();
            toggleMenutItems();
            return true;
        });
    }

    private void goHome() {
        androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new HomeFragment();
        fragmentTransaction.add(R.id.drawer_layout, fragment);
        fragmentTransaction.setTransition(TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }

    }
//
//    @Override
//    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//        Menu nav_Menu = mNavigationView.getMenu();
//        if (mCurrentUser != null){
//            nav_Menu.findItem(R.id.nav_login).setVisible(false);
//            nav_Menu.findItem(R.id.nav_history).setVisible(true);
//            nav_Menu.findItem(R.id.nav_profile).setVisible(true);
//            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
//        }else{
//            nav_Menu.findItem(R.id.nav_login).setVisible(false);
//            nav_Menu.findItem(R.id.nav_history).setVisible(true);
//            nav_Menu.findItem(R.id.nav_profile).setVisible(true);
//            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
//            Log.d(TAG, "onAuthStateChanged: signed out");
////                    ((MainActivity)getActivity()).toggleMenutItems();
////                    toastMessage("Successfully signed out!");
//        }
//    }
}
