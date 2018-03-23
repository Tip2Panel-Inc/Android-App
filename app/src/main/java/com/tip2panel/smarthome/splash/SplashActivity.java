package com.tip2panel.smarthome.splash;

/**
 * Created by Setsuna F. Seie on 21/07/2017.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.tip2panel.smarthome.dashboard.DashboardActivity;
import com.tip2panel.smarthome.data.source.GatewayDataSource;
import com.tip2panel.smarthome.data.source.SmartHomeRepository;
import com.tip2panel.smarthome.data.source.local.GatewayLocalDataSource;
import com.tip2panel.smarthome.gateway.GatewayActivity;
import com.tip2panel.smarthome.login.LoginActivity;
import com.crashlytics.android.Crashlytics;
import com.tip2panel.smarthome.utils.BaseActivity;
import com.tip2panel.smarthome.utils.DialogUtilities;

import io.fabric.sdk.android.Fabric;

import static com.google.common.base.Preconditions.checkNotNull;

public class SplashActivity extends BaseActivity {
    private  SmartHomeRepository mSmartHomeRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        mSmartHomeRepository = checkNotNull(SmartHomeRepository.getInstance(
                GatewayLocalDataSource.getInstance(getApplicationContext())),
                "smartHomeRepository cannot be null!");


    }

    @Override
    protected void onStart() {
        super.onStart();
        mSmartHomeRepository.checkNetworkState(this, new GatewayDataSource.CheckNetworkStateCallback() {
            @Override
            public void onWifiConnected() {
                mSmartHomeRepository.connectActiveGateway(new GatewayDataSource.GatewayConnectionCallback() {
                    @Override
                    public void onSuccess(AVA88GatewayInfo ava88GatewayInfo) {
                        Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int error, AVA88GatewayInfo gatewayInfo) {
                        DialogUtilities.showGatewayConnectionErrorDialog(SplashActivity.this);
                    }

                });
            }

            @Override
            public void onWifiNotConnected() {
                DialogUtilities.showWifiNotConnectDialog(SplashActivity.this);
            }

            @Override
            public void onDataConnected() {

            }

            @Override
            public void onDataNotConnected() {

            }
        });



    }
}
