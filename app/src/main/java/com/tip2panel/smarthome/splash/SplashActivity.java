package com.tip2panel.smarthome.splash;

/**
 * Created by Setsuna F. Seie on 21/07/2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ConnectivityPredicate;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.tip2panel.smarthome.R;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

public class SplashActivity extends AppCompatActivity {
    private  SmartHomeRepository mSmartHomeRepository;
    private Disposable networkDisposable;
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
        networkDisposable = ReactiveNetwork.observeNetworkConnectivity(getApplicationContext())
                .subscribeOn(Schedulers.io())
                .filter(ConnectivityPredicate.hasType(ConnectivityManager.TYPE_WIFI))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Connectivity>() {
                    @Override public void accept(final Connectivity connectivity) {
                        if(connectivity.isAvailable()) {
                            Log.d("WIFI", "WIFI CONNECTED!!!");
                            Log.d("WIFI", "WIFI CONNECTED 2!!!");
                            if(!mSmartHomeRepository.isGatewayConnected()){
                                Log.d("WIFI", "WIFI CONNECTED!!! But no gateway");
                                mSmartHomeRepository.connectActiveGateway(new GatewayDataSource.GatewayConnectionCallback() {
                                    @Override
                                    public void onSuccess(AVA88GatewayInfo ava88GatewayInfo) {
                                        Intent intent;
                                        Log.d("WIFI", "WIFI CONNECTED!!! connected to gateway");
                                        intent = new Intent(SplashActivity.this, DashboardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        safelyDispose(networkDisposable);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(int error, AVA88GatewayInfo gatewayInfo) {
                                        Log.d("WIFI", "WIFI CONNECTED!!! But has to connect gateway");
                                        safelyDispose(networkDisposable);
                                        DialogUtilities.showGatewayConnectionErrorDialog(SplashActivity.this);
                                    }
                                });
                            }
                            else{
                                Intent intent;
                                Log.d("WIFI", "WIFI CONNECTED!!! Has gateway!");
                                intent = new Intent(SplashActivity.this, DashboardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                safelyDispose(networkDisposable);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            }
                        }
                        else {
                            Log.d("WIFI", "WIFI DISCONNECTED!!!");
                            //DialogUtilities.showWifiNotConnectDialog(SplashActivity.this);
                            DialogUtilities.getNoWifiDialog(SplashActivity.this, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    SplashActivity.this.finish();
                                }
                            }).show();

                        }
                    }
                });



    }

    @Override
    protected void onStop() {

        super.onStop();
        Log.d("ACTIVITY", "STOPPING");
        safelyDispose(networkDisposable);
    }

    private void safelyDispose(Disposable... disposables) {
        for (Disposable subscription : disposables) {
            if (subscription != null && !subscription.isDisposed()) {
                subscription.dispose();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
