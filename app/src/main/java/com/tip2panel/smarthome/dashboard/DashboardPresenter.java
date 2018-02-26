package com.tip2panel.smarthome.dashboard;

import android.support.annotation.NonNull;
import android.util.Log;

import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.engkan2kit.ava88.ZNode;
import com.engkan2kit.ava88.ZNodeValue;
import com.tip2panel.smarthome.data.source.GatewayDataSource;
import com.tip2panel.smarthome.data.source.SmartHomeRepository;
import com.tip2panel.smarthome.devices.DevicesPagerContract;
import com.tip2panel.smarthome.utils.DeviceListItem;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Setsuna F. Seie on 13/07/2017.
 */

public class DashboardPresenter implements DashboardContract.MvpPresenter {

    private final SmartHomeRepository mSmartHomeRepository;
    private final static String TAG = DashboardPresenter.class.getSimpleName();
    private final DashboardContract.MvpView mDevicesPagerView;


    public DashboardPresenter(@NonNull SmartHomeRepository smartHomeRepository,
                              @NonNull DashboardContract.MvpView devicesPagerView){
        mSmartHomeRepository = checkNotNull(smartHomeRepository,
                "smartHomeRepository cannot be null!");
        mDevicesPagerView = checkNotNull(devicesPagerView,"gatewayView cannot be null!");
        mDevicesPagerView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void changeValue(ZNode node, String zNodeValueKey, int instance) {
        mSmartHomeRepository.changeValue(node,zNodeValueKey,instance);
    }

    @Override
    public void changeValue(ZNodeValue nodeValue) {
        mSmartHomeRepository.changeValue(nodeValue);
    }

    @Override
    public void loadDevices() {
        mSmartHomeRepository.getAllDevices( new GatewayDataSource.LoadDevicesCallback() {
            @Override
            public void onDevicesLoaded(List<DeviceListItem> deviceListItems) {
                mDevicesPagerView.showDevicesList(deviceListItems);
            }

            @Override
            public void onDataNotAvailable() {
                //TODO: show error message
            }
        });
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
                mDevicesPagerView.showGatewayConnected(ava88GatewayInfo);
            }

            @Override
            public void onFailure(int error, AVA88GatewayInfo gatewayInfo) {
                mDevicesPagerView.showGatewayConnectionErrorDialog();
            }

        });
    }


}
