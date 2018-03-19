package com.tip2panel.smarthome.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.engkan2kit.ava88.ZNode;
import com.engkan2kit.ava88.ZNodeValue;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Setsuna F. Seie on 11/07/2017.
 */

public class SmartHomeRepository implements GatewayDataSource {
    final static String TAG=SmartHomeRepository.class.getSimpleName();
    private static SmartHomeRepository INSTANCE = null;

    private final GatewayDataSource mGatewayLocalDataSource;

    private SmartHomeRepository (@NonNull GatewayDataSource gatewayLocalDataSource) {
        mGatewayLocalDataSource = checkNotNull(gatewayLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param gatewayLocalDataSource  the device storage data source
     * @return the {@link SmartHomeRepository} instance
     */
    public static SmartHomeRepository getInstance(GatewayDataSource gatewayLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new SmartHomeRepository(gatewayLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance(){INSTANCE = null;}


    @Override
    public void changeValue(ZNode node, String zNodeValueKey, int instance) {
        mGatewayLocalDataSource.changeValue(node,zNodeValueKey,instance);
    }

    @Override
    public void changeValue(ZNodeValue nodeValue) {
        mGatewayLocalDataSource.changeValue(nodeValue);
    }

    @Override
    public void changeDeviceName(String deviceId, String name) {
        mGatewayLocalDataSource.changeDeviceName(deviceId,name);
    }

    @Override
    public void connectGateway(AVA88GatewayInfo ava88GatewayInfo, @NonNull GatewayConnectionCallback callback) {
        mGatewayLocalDataSource.connectGateway(ava88GatewayInfo,callback);
    }

    @Override
    public void connectKnownGateway( AVA88GatewayInfo ava88GatewayInfo, @NonNull GatewayConnectionCallback callback) {
        mGatewayLocalDataSource.connectKnownGateway(ava88GatewayInfo,callback);
    }

    @Override
    public void connectActiveGateway(@NonNull GatewayConnectionCallback callback) {
        mGatewayLocalDataSource.connectActiveGateway(callback);
    }

    @Override
    public void checkGatewayOnline(AVA88GatewayInfo ava88GatewayInfo, @NonNull CheckGatewayConnectedCallback callback) {
        mGatewayLocalDataSource.checkGatewayOnline(ava88GatewayInfo,callback);
    }

    @Override
    public boolean isGatewayOnline(AVA88GatewayInfo ava88GatewayInfo) {
        return mGatewayLocalDataSource.isGatewayOnline(ava88GatewayInfo);
    }

    @Override
    public boolean isGatewayConnected(){
        return mGatewayLocalDataSource.isGatewayConnected();
    }

    @Override
    public void getOwnedGateways(@NonNull LoadGatewaysCallback callback) {
        Log.d(TAG,"Ask Local data source to load owned gateways!");
        mGatewayLocalDataSource.getOwnedGateways(callback);
    }

    @Override
    public void getAvailableGateways(@NonNull LoadGatewaysCallback callback) {
        Log.d(TAG,"Ask Local data source to load available gateways!");
        mGatewayLocalDataSource.getAvailableGateways(callback);
    }

    @Override
    public void getOwnedGateway(@NonNull String id, @NonNull GetOwnedGatewayCallback callback) {

    }

    @Override
    public void saveGateway(@NonNull AVA88GatewayInfo ava88GatewayInfo) {
            mGatewayLocalDataSource.saveGateway(ava88GatewayInfo);
    }

    @Override
    public void getDevices(String location, final LoadDevicesCallback callback) {
        mGatewayLocalDataSource.getDevices(location,callback);
    }

    @Override
    public void getAllDevices(final LoadDevicesCallback callback) {
        mGatewayLocalDataSource.getAllDevices(callback);
    }

    @Override
    public void getLocations(final LocationsCallback callback) {
        Log.d(TAG, "Tell SmartHome Repository to getLoations.");
        mGatewayLocalDataSource.getLocations(callback);
    }

    @Override
    public void getLatestLog(InclusionCallback callback) {
        mGatewayLocalDataSource.getLatestLog(callback);
    }

    @Override
    public void checkNetworkState(Context context, CheckNetworkStateCallback callback) {
        mGatewayLocalDataSource.checkNetworkState(context, callback);
    }

    @Override
    public void addLocation(String location, LocationsCallback callback) {
        mGatewayLocalDataSource.addLocation(location,callback);
    }

    @Override
    public void changeNodeLocations(List<Integer> nodeIds, String newLocation,
                                    NodeChangeLocationsCallback callback) {
        mGatewayLocalDataSource.changeNodeLocations(nodeIds,newLocation,callback);
    }

    @Override
    public void removeLocation(int id, LocationsCallback callback) {
        mGatewayLocalDataSource.removeLocation(id,callback );
    }

    @Override
    public void removeLocation(String location, LocationsCallback callback) {
        mGatewayLocalDataSource.removeLocation(location, callback);
    }

    @Override
    public void startInclusion() {
        mGatewayLocalDataSource.startInclusion();
    }

    @Override
    public void startExclusion() {
        mGatewayLocalDataSource.startExclusion();
    }

    @Override
    public void cancelInclusionAction() {
        mGatewayLocalDataSource.cancelInclusionAction();
    }

}
