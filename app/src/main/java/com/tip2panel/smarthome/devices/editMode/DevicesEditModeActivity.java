package com.tip2panel.smarthome.devices.editMode;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.tip2panel.smarthome.R;
import com.tip2panel.smarthome.dashboard.DashboardFragment;
import com.tip2panel.smarthome.dashboard.DashboardPresenter;
import com.tip2panel.smarthome.data.source.SmartHomeRepository;
import com.tip2panel.smarthome.data.source.local.GatewayLocalDataSource;
import com.tip2panel.smarthome.devices.DevicesActivity;
import com.tip2panel.smarthome.gateway.GatewayActivity;
import com.tip2panel.smarthome.utils.ActivityUtils;
import com.tip2panel.smarthome.utils.BaseActivity;

public class DevicesEditModeActivity extends BaseActivity{
    public static final int REQUEST_EDIT_MODE = 1;
    private DevicesEditModePresenter mDevicesEditModePresenter;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private View contentView;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_template);
        //setup toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        contentView = findViewById(R.id.contentMain);
        //setup nav drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);

        setTitle(getString(R.string.edit_devices_location));
        DevicesEditModeFragment mDevicesEditModeFragment =
                (DevicesEditModeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        String location = getIntent().getStringExtra(DevicesEditModeFragment.ARGUMENT_EDIT_MODE);
        if (mDevicesEditModeFragment == null) {
            // Create the fragment
            mDevicesEditModeFragment = DevicesEditModeFragment.newInstance();
            if(getIntent().hasExtra(DevicesEditModeFragment.ARGUMENT_EDIT_MODE)){
                Bundle bundle = new Bundle();
                bundle.putString(DevicesEditModeFragment.ARGUMENT_EDIT_MODE, location);
                mDevicesEditModeFragment.setArguments(bundle);
            }
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mDevicesEditModeFragment, R.id.fragment_container);
        }

        mDevicesEditModePresenter = new DevicesEditModePresenter(
                SmartHomeRepository.getInstance(
                        GatewayLocalDataSource.getInstance(getApplicationContext())),
                mDevicesEditModeFragment);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
