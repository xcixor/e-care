package com.ecareuae.e_care;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;
import android.content.Intent;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.LayoutRes;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class BasicNavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mTopToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_navigation);
    }

    @Override
    public final void setContentView(@LayoutRes int layoutResID) {
//        ViewStub stub = findViewById(R.id.navdrawer_stub);
//        stub.setLayoutResource(layoutResID);
//        stub.inflate();
        super.setContentView(layoutResID);
    }

    protected void onCreateDrawer() {
        mTopToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTopToolbar.setTitle("e-care");
        setSupportActionBar(mTopToolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView=findViewById(R.id.nav_view);
        mNavigationView.bringToFront();
        ActionBarDrawerToggle toggle=new
                ActionBarDrawerToggle(
                this, mDrawerLayout, mTopToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setCheckedItem(R.id.nav_home);
    }

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        mTopToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mTopToolbar.setTitle("e-care");
//        setSupportActionBar(mTopToolbar);
//        mDrawerLayout = findViewById(R.id.drawer_layout);
//        mNavigationView=findViewById(R.id.nav_view);
//        mNavigationView.bringToFront();
//        ActionBarDrawerToggle toggle=new
//                ActionBarDrawerToggle(
//                        this, mDrawerLayout, mTopToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//        mNavigationView.setNavigationItemSelectedListener(this);
//        mNavigationView.setCheckedItem(R.id.nav_home);
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
            case R.id.nav_home: break;
            case R.id.nav_history:
//                handleSelections("Bookings");
//                Intent intent = new Intent(MainActivity.this, BookAppointmentActivity.class);
//                startActivity(intent);
                break;
            case R.id.nav_login:
                Intent regIntent = new Intent(BasicNavigationActivity.this, LoginActivity.class);
                startActivity(regIntent);
                break;
            case R.id.nav_profile: menu.findItem(R.id.nav_logout).setVisible(true);
                menu.findItem(R.id.nav_profile).setVisible(true);
                menu.findItem(R.id.nav_login).setVisible(false);
                break;
            case R.id.nav_logout: menu.findItem(R.id.nav_logout).setVisible(false);
                menu.findItem(R.id.nav_profile).setVisible(false);
                menu.findItem(R.id.nav_login).setVisible(true);
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
