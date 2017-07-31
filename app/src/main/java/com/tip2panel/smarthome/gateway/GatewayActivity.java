package com.tip2panel.smarthome.gateway;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tip2panel.smarthome.R;
import com.tip2panel.smarthome.dashboard.DashboardActivity;
import com.tip2panel.smarthome.data.source.SmartHomeRepository;
import com.tip2panel.smarthome.data.source.local.GatewayLocalDataSource;
import com.tip2panel.smarthome.devices.DevicesActivity;
import com.tip2panel.smarthome.utils.ActivityUtils;
import com.tip2panel.smarthome.utils.BaseActivity;

public class GatewayActivity extends BaseActivity{
    private GatewayPresenter mGatewayPresenter;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_template);
        //setup toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setupNavigationDrawerContent(navigationView);
        contentView = findViewById(R.id.contentMain);
        //setup nav drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        setTitle(getString(R.string.gateway));
        GatewayFragment gatewayFragment =
                (GatewayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (gatewayFragment == null) {
            // Create the fragment
            gatewayFragment = GatewayFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), gatewayFragment, R.id.fragment_container);
        }

        mGatewayPresenter = new GatewayPresenter(
                SmartHomeRepository.getInstance(
                        GatewayLocalDataSource.getInstance(getApplicationContext())),
                gatewayFragment);

    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        // Handle navigation view item clicks here.
                        int id = item.getItemId();
                        Intent intent=null;
                        if (id ==  R.id.nav_dashboard) {
                            intent = new Intent(GatewayActivity.this, DashboardActivity.class);
                        } else if (id == R.id.nav_devices) {
                            intent = new Intent(GatewayActivity.this, DevicesActivity.class);

                        } else if (id == R.id.nav_devdisco) {

                        } else if (id == R.id.nav_gateway) {

                        } else if (id == R.id.nav_settings) {

                        } else if (id == R.id.nav_logout) {
                            finish();
                        }

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);

                        // refresh toolbar menu
                        invalidateOptionsMenu();
                        if (intent!=null) {
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                        }
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



}
