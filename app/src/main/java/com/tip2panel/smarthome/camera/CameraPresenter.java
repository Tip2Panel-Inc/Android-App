package com.tip2panel.smarthome.camera;

import android.support.annotation.NonNull;

import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.engkan2kit.ava88.AvaCamInfo;
import com.engkan2kit.ava88.ZNode;
import com.tip2panel.smarthome.dashboard.DashboardContract;
import com.tip2panel.smarthome.data.source.GatewayDataSource;
import com.tip2panel.smarthome.data.source.SmartHomeRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Setsuna F. Seie on 13/07/2017.
 */

public class CameraPresenter implements CameraContract.MvpPresenter {

    private final SmartHomeRepository mSmartHomeRepository;
    private final static String TAG = CameraPresenter.class.getSimpleName();
    private final CameraContract.MvpView mCameraView;


    public CameraPresenter(@NonNull SmartHomeRepository smartHomeRepository,
                           @NonNull CameraContract.MvpView cameraView){
        mSmartHomeRepository = checkNotNull(smartHomeRepository,
                "smartHomeRepository cannot be null!");
        mCameraView = checkNotNull(cameraView,"cameraView cannot be null!");
        mCameraView.setPresenter(this);
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
                mCameraView.showGatewayConnected(ava88GatewayInfo);
            }

            @Override
            public void onFailure(int error, AVA88GatewayInfo gatewayInfo) {
                mCameraView.showGatewayConnectionErrorDialog();
            }

        });
    }


    @Override
    public void playCamera(AvaCamInfo avaCamInfo) {
        
    }

    @Override
    public void loadCameras() {

    }
}
