package com.tip2panel.smarthome.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ConnectivityPredicate;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.tip2panel.smarthome.R;
import com.tip2panel.smarthome.dashboard.DashboardActivity;
import com.tip2panel.smarthome.data.source.SmartHomeRepository;
import com.tip2panel.smarthome.data.source.local.GatewayLocalDataSource;
import com.tip2panel.smarthome.devices.DevicesActivity;
import com.tip2panel.smarthome.gateway.GatewayFragment;
import com.tip2panel.smarthome.gateway.GatewayPresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BaseActivity extends AppCompatActivity{
    private GatewayPresenter mGatewayPresenter;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private View contentView;
    final Activity act=this;
    private Disposable networkDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.d("ACTIVITY", "STARTING");
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
                        }
                        else {
                            Log.d("WIFI", "WIFI DISCONNECTED!!!");

                            DialogUtilities.getNoWifiDialog(act, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    act.finish();
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
