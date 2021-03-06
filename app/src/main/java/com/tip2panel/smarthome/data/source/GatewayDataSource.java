package com.tip2panel.smarthome.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.engkan2kit.ava88.ZNode;
import com.engkan2kit.ava88.ZNodeValue;
import com.tip2panel.smarthome.utils.DeviceListItem;

import java.util.List;

/**
 * Created by Setsuna F. Seie on 11/07/2017.
 */

public interface GatewayDataSource {
    interface LoadGatewaysCallback {
        void onGatewaysLoaded(List<AVA88GatewayInfo> gateways);

        void onDataNotAvailable();
    }

    interface GetOwnedGatewayCallback {
        void onOwnedGatewayLoaded(AVA88GatewayInfo gateways);

        void onDataNotAvailable();
    }

    interface GatewayConnectionCallback{
        void onSuccess(AVA88GatewayInfo ava88GatewayInfo);
        void onFailure(int error,AVA88GatewayInfo ava88GatewayInfo);
    }

    interface LoadDevicesCallback{
        void onDevicesLoaded(List<DeviceListItem> deviceListItems);
        void onDataNotAvailable();
    }

    interface LocationsCallback {
        void onLocationsLoaded(List<String> locations);
        void onDataNotAvailable();
        void onLocationsChanged();
        void onLocationAddConflict(String location);

    }


    interface CheckNetworkStateCallback{
        void onWifiConnected();
        void onWifiNotConnected();
        void onDataConnected();
        void onDataNotConnected();
    }

    interface CheckGatewayConnectedCallback{
        void onGatewayOnline(AVA88GatewayInfo ava88GatewayInfo);
        void onGatewayOffline();
    }

    interface NodeChangeLocationsCallback{
        void onNodeChangeLocationsDone();
    }

    interface InclusionCallback{
        int INCLUSION_ADDING=1;
        int INCLUSION_REMOVING=2;
        int INCLUSION_FORCEADDING=20;
        void onBusy(int action);
        void onCanceled(int action);
        void onDone(int action, ZNode zNode);
        void onFailure();
    }

    void changeValue(ZNode node, String zNodeValueKey, int instance);
    void changeValue(ZNodeValue nodeValue);

    void changeDeviceName(String deviceId, String name);

    void connectGateway(AVA88GatewayInfo ava88GatewayInfo,
                        @NonNull GatewayConnectionCallback callback);

    void connectKnownGateway(AVA88GatewayInfo ava88GatewayInfo,
                             @NonNull GatewayConnectionCallback callback);

    void connectActiveGateway(@NonNull GatewayConnectionCallback callback);

    void checkGatewayOnline(AVA88GatewayInfo ava88GatewayInfo,
                              @NonNull CheckGatewayConnectedCallback callback);
    boolean isGatewayOnline(@Nullable AVA88GatewayInfo ava88GatewayInfo);
    boolean isGatewayConnected();

    void getOwnedGateways(@NonNull LoadGatewaysCallback callback);

    void getAvailableGateways(@NonNull LoadGatewaysCallback callback);

    void getOwnedGateway(@NonNull String id, @NonNull GetOwnedGatewayCallback callback);

    void saveGateway(@NonNull AVA88GatewayInfo task);

    void getDevices(String location, final LoadDevicesCallback callback);

    void getAllDevices(final LoadDevicesCallback callback);

    void getLocations(final LocationsCallback callback);

    void getLatestLog(final InclusionCallback callback);

    void checkNetworkState(Context context, final CheckNetworkStateCallback callback);

    void addLocation(String location,final LocationsCallback callback);

    void changeNodeLocations(final List<Integer> nodeIds, final String newLocation,
                             NodeChangeLocationsCallback callback);

    void removeLocation(int id,final  LocationsCallback callback);
    void removeLocation(String location,final  LocationsCallback callback);

    void startInclusion();
    void startExclusion();
    void cancelInclusionAction();

}
