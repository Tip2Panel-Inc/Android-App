package com.tip2panel.smarthome.devices;

import android.support.annotation.NonNull;
import android.util.Log;

import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.engkan2kit.ava88.ZNode;
import com.engkan2kit.ava88.ZNodeValue;
import com.tip2panel.smarthome.data.source.GatewayDataSource;
import com.tip2panel.smarthome.data.source.SmartHomeRepository;
import com.tip2panel.smarthome.utils.DeviceListItem;
import com.tip2panel.smarthome.utils.DialogUtilities;


import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Setsuna F. Seie on 13/07/2017.
 */

public class DevicesPagerPresenter implements DevicesPagerContract.MvpPresenter {

    private final SmartHomeRepository mSmartHomeRepository;
    private final static String TAG = DevicesPagerPresenter.class.getSimpleName();
    private final DevicesPagerContract.MvpView mDevicesPagerView;


    public DevicesPagerPresenter(@NonNull SmartHomeRepository smartHomeRepository,
                            @NonNull DevicesPagerContract.MvpView devicesPagerView){
        mSmartHomeRepository = checkNotNull(smartHomeRepository,
                "smartHomeRepository cannot be null!");
        mDevicesPagerView = checkNotNull(devicesPagerView,"gatewayView cannot be null!");
        mDevicesPagerView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void changeValue(ZNodeValue zNodeValue) {
        mSmartHomeRepository.changeValue(zNodeValue);
    }

    @Override
    public void loadDevices(final String location) {
        mSmartHomeRepository.getDevices(location, new GatewayDataSource.LoadDevicesCallback() {
            @Override
            public void onDevicesLoaded(List<DeviceListItem> deviceListItems) {
                mDevicesPagerView.showDevicesList(deviceListItems,location);
            }

            @Override
            public void onDataNotAvailable() {
                //TODO: show error message
            }
        });
    }

    @Override
    public void loadPages() {
        Log.d(TAG, "Tell presenter to load pages");
        mSmartHomeRepository.getLocations(new GatewayDataSource.LocationsCallback() {
            @Override
            public void onLocationsLoaded(List<String> locations) {
                mDevicesPagerView.showPages(locations);
            }

            @Override
            public void onDataNotAvailable() {

            }

            @Override
            public void onLocationsChanged() {

            }

            @Override
            public void onLocationAddConflict(String location) {

            }

        });
    }

    @Override
    public void addLocation(String location) {
        mSmartHomeRepository.addLocation(location, new GatewayDataSource.LocationsCallback() {
            @Override
            public void onLocationsLoaded(List<String> locations) {

            }

            @Override
            public void onDataNotAvailable() {

            }

            @Override
            public void onLocationsChanged() {
                //refresh pages
                loadPages();
            }

            @Override
            public void onLocationAddConflict(String location) {
                //show conflict error
                mDevicesPagerView.showLocationAddConflictDialog(location);
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
