package com.tip2panel.smarthome.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.engkan2kit.ava88.ZNode;

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
        void onFailure(String message);
    }

    interface LoadDevicesCallback{
        void onDevicesLoaded(List<ZNode> zNodes);
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
    void changeValue(ZNode node, String zNodeValueKey, int instance);

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

    void checkNetworkState(Context context, final CheckNetworkStateCallback callback);

    void addLocation(String location,final LocationsCallback callback);

    void removeLocation(int id,final  LocationsCallback callback);
    void removeLocation(String location,final  LocationsCallback callback);
}
