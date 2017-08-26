package com.tip2panel.smarthome.discovery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.engkan2kit.ava88.AVA88Gateway;
import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.engkan2kit.ava88.AVA88GatewayScanner;
import com.engkan2kit.ava88.ZNode;
import com.tip2panel.smarthome.data.source.GatewayDataSource;
import com.tip2panel.smarthome.data.source.SmartHomeRepository;
import com.tip2panel.smarthome.gateway.GatewayContract;
import com.tip2panel.smarthome.gateway.GatewayContract.MvpPresenter;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Setsuna F. Seie on 11/07/2017.
 */

public class DiscoveryPresenter implements DiscoveryContract.MvpPresenter {
    private final SmartHomeRepository mSmartHomeRepository;
    private final static String TAG = DiscoveryPresenter.class.getSimpleName();
    private final DiscoveryContract.MvpView mDiscoveryView;

    public DiscoveryPresenter(@NonNull SmartHomeRepository smartHomeRepository,
                              @NonNull DiscoveryContract.MvpView discoveryView){
        mSmartHomeRepository = checkNotNull(smartHomeRepository,
                "smartHomeRepository cannot be null!");
        mDiscoveryView = checkNotNull(discoveryView,"gatewayView cannot be null!");
        mDiscoveryView.setPresenter(this);
    }



    @Override
    public void start() {

    }

    @Override
    public boolean isGatewayConnected() {
        return false;
    }

    @Override
    public void connectToActiveGateway() {

    }


    @Override
    public void startInclusion() {
        mSmartHomeRepository.startInclusion();
    }

    @Override
    public void startExclusion() {
        mSmartHomeRepository.startExclusion();
    }

    @Override
    public void cancelAction() {
        mSmartHomeRepository.cancelInclusionAction();
    }

    @Override
    public void checkLatestLog() {
        mSmartHomeRepository.getLatestLog(new GatewayDataSource.InclusionCallback() {
            @Override
            public void onBusy(int action) {
                mDiscoveryView.showProgressbar(true);
                mDiscoveryView.setFabTextCancel();
                if(action==GatewayDataSource.InclusionCallback.INCLUSION_REMOVING)
                    mDiscoveryView.setInclusionMode(false);
                else{
                    mDiscoveryView.setInclusionMode(true);
                }
                mDiscoveryView.enableRadioGroup(false);
            }

            @Override
            public void onCanceled(int action) {
                Log.d("DISCOVERY","CANCELLED");
                mDiscoveryView.showResultDialog(action,null);
                mDiscoveryView.setFabTextStart();
                mDiscoveryView.showProgressbar(false);
                mDiscoveryView.enableRadioGroup(true);
            }

            @Override
            public void onDone(int action, ZNode zNode) {
                Log.d("DISCOVERY","DONE");
                mDiscoveryView.showResultDialog(action+100,zNode);
                mDiscoveryView.setFabTextStart();
                mDiscoveryView.showProgressbar(false);
                mDiscoveryView.enableRadioGroup(true);
            }

            @Override
            public void onFailure() {
                Log.d("DISCOVERY","FAILED");
                mDiscoveryView.showResultDialog(0,null);
                mDiscoveryView.setFabTextStart();
                mDiscoveryView.showProgressbar(false);
                mDiscoveryView.enableRadioGroup(true);
            }
        });
    }
}
