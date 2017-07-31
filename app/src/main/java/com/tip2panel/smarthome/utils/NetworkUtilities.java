package com.tip2panel.smarthome.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ConnectivityPredicate;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import org.reactivestreams.Subscriber;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Setsuna F. Seie on 20/07/2017.
 */

public class NetworkUtilities {
    public static final String TAG = NetworkUtilities.class.getSimpleName();
    public static boolean enableLogs = true;
    public static boolean enableAlerts = false;
    private Disposable networkDisposable;
    public static void checkNetworkState(){

    }




    public static boolean isWifiOnline(Context context) {
        try {
            ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiCheck = connectionManager.getActiveNetworkInfo();
            return wifiCheck!=null && wifiCheck.getType() == ConnectivityManager.TYPE_WIFI;
        } catch (Exception ex) {
            if (enableLogs) Log.e(TAG, ex.toString());
        }
        return false;
    }

}
