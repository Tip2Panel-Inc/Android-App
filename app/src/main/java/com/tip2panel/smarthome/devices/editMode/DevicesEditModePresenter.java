package com.tip2panel.smarthome.devices.editMode;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.engkan2kit.ava88.ZNode;
import com.tip2panel.smarthome.dashboard.DashboardActivity;
import com.tip2panel.smarthome.dashboard.DashboardContract;
import com.tip2panel.smarthome.data.source.GatewayDataSource;
import com.tip2panel.smarthome.data.source.SmartHomeRepository;
import com.tip2panel.smarthome.gateway.GatewayActivity;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Setsuna F. Seie on 13/07/2017.
 */

public class DevicesEditModePresenter implements DevicesEditModeContract.MvpPresenter {

    private final SmartHomeRepository mSmartHomeRepository;
    private final static String TAG = DevicesEditModePresenter.class.getSimpleName();
    private final DevicesEditModeContract.MvpView mDeviceEditModeView;


    public DevicesEditModePresenter(@NonNull SmartHomeRepository smartHomeRepository,
                                    @NonNull DevicesEditModeContract.MvpView devicesPagerView){
        mSmartHomeRepository = checkNotNull(smartHomeRepository,
                "smartHomeRepository cannot be null!");
        mDeviceEditModeView = checkNotNull(devicesPagerView,"gatewayView cannot be null!");
        mDeviceEditModeView.setPresenter(this);
    }

    @Override
    public void start() {

    }


    @Override
    public void loadDevices(String location) {
        mSmartHomeRepository.getDevices( location,new GatewayDataSource.LoadDevicesCallback() {
            @Override
            public void onDevicesLoaded(List<ZNode> zNodes) {
                mDeviceEditModeView.showDevicesList(zNodes);
            }

            @Override
            public void onDataNotAvailable() {
                //TODO: show error message
            }
        });
    }

    @Override
    public void removeLocation(String location) {
        mSmartHomeRepository.removeLocation(location, new GatewayDataSource.LocationsCallback() {
            @Override
            public void onLocationsLoaded(List<String> locations) {

            }

            @Override
            public void onDataNotAvailable() {

            }

            @Override
            public void onLocationsChanged() {
                //finish then refresh Devices pager
                mDeviceEditModeView.jumpToDeviceActivity();
            }

            @Override
            public void onLocationAddConflict(String location) {

            }
        });
    }

    @Override
    public void changeNodesLocation(List<Integer> nodeIds, String newLocation) {
        mSmartHomeRepository.changeNodeLocations(nodeIds, newLocation,
                new GatewayDataSource.NodeChangeLocationsCallback() {
            @Override
            public void onNodeChangeLocationsDone() {
                mDeviceEditModeView.jumpToDeviceActivity();
            }
        });
    }

    @Override
    public void loadLocationListsForSelector(final String location) {
        mSmartHomeRepository.getLocations(new GatewayDataSource.LocationsCallback() {
            @Override
            public void onLocationsLoaded(List<String> locations) {
                mDeviceEditModeView.showChangeLocationSelector(locations, location);
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
    public boolean isGatewayConnected() {
        return mSmartHomeRepository.isGatewayConnected();
    }

    @Override
    public void connectToActiveGateway() {
        mSmartHomeRepository.connectActiveGateway(new GatewayDataSource.GatewayConnectionCallback() {
            @Override
            public void onSuccess(AVA88GatewayInfo ava88GatewayInfo) {
                mDeviceEditModeView.showGatewayConnected(ava88GatewayInfo);
            }

            @Override
            public void onFailure(int error, AVA88GatewayInfo gatewayInfo) {
                mDeviceEditModeView.showGatewayConnectionErrorDialog();
            }
        });
    }


}
