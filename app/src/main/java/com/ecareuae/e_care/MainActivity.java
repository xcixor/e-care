package com.ecareuae.e_care;

import android.app.FragmentManager;
import android.content.res.Resources;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

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
    private TextView mHeaderSubTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        instantiateViews();

        addLogoutFunctionality();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_login, R.id.nav_history, R.id.nav_profile, R.id.nav_logout)
                .setDrawerLayout(mDrawer)
                .build();
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, mNavController);
        toggleMenutItems();
    }

    public void instantiateViews() {
        mCurrentUser = mAuth.getCurrentUser();
        View headerView = mNavigationView.getHeaderView(0);
        TextView nav_sub_title = headerView.findViewById(R.id.nav_header_subtitle);
        ImageView profileImage = headerView.findViewById(R.id.nav_header_image);
        if (mCurrentUser != null) {
            Log.d(TAG, "instantiateViews: setting display stuff");
            if (!mCurrentUser.getDisplayName().equals("")) {
                nav_sub_title.setText(mCurrentUser.getDisplayName());
            } else {
                nav_sub_title.setText(mCurrentUser.getEmail());
            }
            if (mCurrentUser.getPhotoUrl() != null && !mCurrentUser.getPhotoUrl().equals("")) {
                int radius = Resources.getSystem().getDisplayMetrics().widthPixels;
                int margin = 0;
                Transformation transformation = new RoundedCornersTransformation(radius, margin);
                Picasso.get()
                        .load(mCurrentUser.getPhotoUrl())
                        .transform(transformation)
                        .placeholder(R.drawable.ic_account)
                        .error(R.drawable.ic_account)
                        .fit()
                        .into(profileImage);
            }
        }else{
            profileImage.setImageResource(R.drawable.ic_hospital_marker);
            nav_sub_title.setText(R.string.nav_header_subtitle);
        }
    }


    public void toggleMenutItems() {
        mCurrentUser = mAuth.getCurrentUser();
        Menu nav_Menu = mNavigationView.getMenu();
        if (mCurrentUser == null){
//            logged out
            mDrawer.bringToFront();

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
            mDrawer.closeDrawer(GravityCompat.START, true);
            mAuth.signOut();
            mNavController.navigate(R.id.nav_home);
            return true;
        });
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

}
