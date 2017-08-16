package com.tip2panel.smarthome.gateway;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.engkan2kit.ava88.AVA88Gateway;
import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.engkan2kit.ava88.AVA88GatewayScanner;
import com.tip2panel.smarthome.data.source.GatewayDataSource;
import com.tip2panel.smarthome.data.source.SmartHomeRepository;
import com.tip2panel.smarthome.gateway.GatewayContract.MvpPresenter;
import com.tip2panel.smarthome.utils.DialogUtilities;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Setsuna F. Seie on 11/07/2017.
 */

public class GatewayPresenter implements MvpPresenter {
    private final SmartHomeRepository mSmartHomeRepository;
    private final static String TAG = GatewayPresenter.class.getSimpleName();
    private AVA88GatewayScanner mAVA88GatewayScanner =null;
    private final GatewayContract.MvpView mGatewayView;
    private boolean gatewaySearching;

    public GatewayPresenter(@NonNull SmartHomeRepository smartHomeRepository,
                            @NonNull GatewayContract.MvpView gatewayView){
        mSmartHomeRepository = checkNotNull(smartHomeRepository,
                "smartHomeRepository cannot be null!");
        mGatewayView = checkNotNull(gatewayView,"gatewayView cannot be null!");
        mGatewayView.setPresenter(this);
    }

    @Override
    public void getAvailableGateways(Context context) {
        //TODO replace routines with repository
        Log.d(TAG,"Ask repository to load available gateways!");
        mSmartHomeRepository.getAvailableGateways(new GatewayDataSource.LoadGatewaysCallback() {
            @Override
            public void onGatewaysLoaded(List<AVA88GatewayInfo> gateways) {
                mGatewayView.showAvailableGateways(gateways);
                Log.d(TAG,"Loaded available gateways!");
            }

            @Override
            public void onDataNotAvailable() {
                //TODO put no gateway available icon in view
                Log.d(TAG,"No data for available gateways!");
            }
        });

    }

    @Override
    public void getOwnedGateways(Context context) {
        //TODO replace routines with repository
        Log.d(TAG,"Ask repository to load owned gateways!");
        mSmartHomeRepository.getOwnedGateways(new GatewayDataSource.LoadGatewaysCallback() {
            @Override
            public void onGatewaysLoaded(List<AVA88GatewayInfo> gateways) {
                mGatewayView.showOwnedGateways(gateways);
                Log.d(TAG,"Loaded available gateways!");
            }

            @Override
            public void onDataNotAvailable() {
                //TODO put no gateway available icon in view
                Log.d(TAG,"No data for available gateways!");
            }
        });

    }

    @Override
    public void removeOwnedGateways(Context context) {

    }

    @Override
    public void storeGateway(Context context, AVA88GatewayInfo ava88GatewayInfo) {

    }

    @Override
    public void connectGateway(final AVA88GatewayInfo ava88GatewayInfo) {
        Log.d(TAG,"Connecting to " + ava88GatewayInfo.hardwareAddress);
        if(ava88GatewayInfo.isOwned){
            //Check stored gateway info if still online and connect if yes.
            mSmartHomeRepository.connectKnownGateway(ava88GatewayInfo,
                    new GatewayDataSource.GatewayConnectionCallback() {
                        @Override
                        public void onSuccess(AVA88GatewayInfo ava88GatewayInfo) {
                            //Show toast that the gateway connected
                            mGatewayView.showGatewayConnected(ava88GatewayInfo);
                        }

                        @Override
                        public void onFailure(int error,AVA88GatewayInfo gatewayInfo) {
                            handleGatewayConnectionError(error,gatewayInfo);
                            //Show toast that the connection failed
                            Log.d(TAG,"Gateway Connection Failed!");
                        }
                    });
        }
        else{
            mSmartHomeRepository.connectGateway(ava88GatewayInfo,
                    new GatewayDataSource.GatewayConnectionCallback() {
                @Override
                public void onSuccess(AVA88GatewayInfo ava88GatewayInfo) {
                    //Show toast that the gateway connected
                    mGatewayView.showGatewayConnected(ava88GatewayInfo);
                }

                @Override
                public void onFailure(int error,AVA88GatewayInfo gatewayInfo) {
                    handleGatewayConnectionError(error,gatewayInfo);
                    //Show toast that the connection failed
                    Log.d(TAG,"Gateway Connection Failed!");
                }
            });


        }
    }

    void handleGatewayConnectionError(int error,AVA88GatewayInfo ava88GatewayInfo){
        switch (error){
            case AVA88Gateway.GatewayConnectionCallback.INVALID_CREDENTIALS:
                mGatewayView.showInvalidGatewayCredentialsDialog(ava88GatewayInfo);
                break;
            case AVA88Gateway.GatewayConnectionCallback.NOT_CONNECTED:
                mGatewayView.showGatewayNotOnlineDialog();
                break;
        }
    }

    @Override
    public void cancelGatewayScan(Context context) {
        if(mAVA88GatewayScanner!=null) {
            Log.d(TAG,"Stopping search");
            mAVA88GatewayScanner=null;

        }
    }

    @Override
    public void start() {

    }

    @Override
    public boolean isGatewayConnected() {
        return mSmartHomeRepository.isGatewayConnected();
    }

    @Override
    public void connectToActiveGateway() {
        mSmartHomeRepository.connectActiveGateway(new GatewayDataSource.GatewayConnectionCallback() {
            @Override
            public void onSuccess(AVA88GatewayInfo ava88GatewayInfo) {
                mGatewayView.showGatewayConnected(ava88GatewayInfo);
            }

            @Override
            public void onFailure(int error,AVA88GatewayInfo ava88GatewayInfo) {
                handleGatewayConnectionError(error,ava88GatewayInfo);
            }
        });
    }


}
