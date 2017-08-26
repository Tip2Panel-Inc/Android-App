package com.tip2panel.smarthome.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.engkan2kit.ava88.AVA88Gateway;
import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.engkan2kit.ava88.AVA88GatewayScanner;
import com.engkan2kit.ava88.NodeLocation;
import com.engkan2kit.ava88.ZNode;
import com.tip2panel.smarthome.data.source.GatewayDataSource;
import com.tip2panel.smarthome.utils.NetworkUtilities;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Setsuna F. Seie on 11/07/2017.
 */

public class GatewayLocalDataSource implements GatewayDataSource {
    static final String TAG = GatewayLocalDataSource.class.getSimpleName();
    private static GatewayLocalDataSource INSTANCE;
    private AppDBHelper mAppDBHelper;
    private Context mContext;
    private AVA88GatewayScanner mAVA88GatewayScanner =null;
    private AVA88Gateway mAva88Gateway=null;

    // Prevent direct instantiation.
    private GatewayLocalDataSource(@NonNull Context context) {
        mContext = checkNotNull(context);
        mAppDBHelper = new AppDBHelper(context);

    }

    public static GatewayLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new GatewayLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void changeValue(ZNode node, String zNodeValueKey, int instance) {
        mAva88Gateway.changeValue(node, node.getZNodeValue(zNodeValueKey),
                instance, new AVA88Gateway.AVA88GatewayListener() {
            @Override
            public void onGetLocationsResponse(List<NodeLocation> locations) {

            }

            @Override
            public void onScanNodesDone(List<ZNode> nodes) {

            }

            @Override
            public void onValueChangeResponseListener(ZNode mZNode, Boolean isSuccessfull) {
                if(isSuccessfull){
                    Log.d("VALUE","Change successful");
                }
                else{
                    Log.d("VALUE","Change unsuccessful");
                }
            }

            @Override
            public void onInclusionExlusionProcessEnded(int status, @Nullable ZNode node) {

            }
        });
    }

    @Override
    public void connectGateway(final AVA88GatewayInfo ava88GatewayInfo,
                               @NonNull final GatewayConnectionCallback callback) {
            mAva88Gateway = new AVA88Gateway(ava88GatewayInfo);
            mAva88Gateway.connect(new AVA88Gateway.GatewayConnectionCallback() {
                @Override
                public void onSuccess() {
                    saveGateway(ava88GatewayInfo);
                    setActiveGateway(ava88GatewayInfo);
                    callback.onSuccess(ava88GatewayInfo);
                }

                @Override
                public void onFailure(int error) {
                    mAva88Gateway.disConnect();
                    mAva88Gateway=null;
                    callback.onFailure(error, ava88GatewayInfo);
                }
            });
    }

    @Override
    public void connectKnownGateway(final AVA88GatewayInfo ava88GatewayInfo, @NonNull final GatewayConnectionCallback callback) {
        checkGatewayOnline(ava88GatewayInfo, new CheckGatewayConnectedCallback() {
            @Override
            public void onGatewayOnline(AVA88GatewayInfo info) {
                Log.d(TAG, "Connected Known Gateway!");
                ava88GatewayInfo.ipv4Address=info.ipv4Address;
                mAva88Gateway = new AVA88Gateway(ava88GatewayInfo);
                mAva88Gateway.connect(new AVA88Gateway.GatewayConnectionCallback() {
                    @Override
                    public void onSuccess() {
                        saveGateway(ava88GatewayInfo);
                        setActiveGateway(ava88GatewayInfo);
                        callback.onSuccess(ava88GatewayInfo);
                    }

                    @Override
                    public void onFailure(int error) {
                        mAva88Gateway.disConnect();
                        mAva88Gateway=null;
                        callback.onFailure(error,ava88GatewayInfo);
                    }
                });
            }

            @Override
            public void onGatewayOffline() {
                callback.onFailure(AVA88Gateway.GatewayConnectionCallback.NOT_CONNECTED,ava88GatewayInfo);
            }
        });
    }

    @Override
    public void connectActiveGateway(@NonNull GatewayConnectionCallback callback) {
        final AVA88GatewayInfo ava88GatewayInfo = getActiveGateway();
        if (ava88GatewayInfo!=null)
            connectKnownGateway(ava88GatewayInfo,callback);
    }

    @Override
    public void checkGatewayOnline(AVA88GatewayInfo ava88GatewayInfo, @NonNull final CheckGatewayConnectedCallback callback) {
        if (mAVA88GatewayScanner!=null){
            mAVA88GatewayScanner.cancel(true);
            Log.d(TAG,"Stopping search before searching again");
        }
        Log.d(TAG,"Searching");
        mAVA88GatewayScanner= new AVA88GatewayScanner(mContext,new AVA88GatewayScanner.GatewayScannerListener(){
            @Override
            public void onGatewayScannerDone(ArrayList<AVA88GatewayInfo> gatewayIPs) {

            }

            @Override
            public void onGatewayFound(AVA88GatewayInfo mAVA88GatewayInfo) {
                callback.onGatewayOnline(mAVA88GatewayInfo);
            }

            @Override
            public void onGatewayNotFound() {
                callback.onGatewayOffline();
            }
        });
        mAVA88GatewayScanner.execute(ava88GatewayInfo);
    }

    @Override
    public boolean isGatewayOnline(AVA88GatewayInfo ava88GatewayInfo) {
        //call api to check if online
        if(ava88GatewayInfo!=null){
            //
        }
        else{

        }
        return false;
    }

    @Override
    public boolean isGatewayConnected(){
        if(mAva88Gateway != null)
            return mAva88Gateway.isConnected();
        else
            return false;
    }

    @Override
    public void getOwnedGateways(@NonNull LoadGatewaysCallback callback) {
        Log.d(TAG,"Querying Owned gateway!");
        AVA88GatewayInfo active = getActiveGateway();
        SQLiteDatabase database=mAppDBHelper.getWritableDatabase();

        String activeID=null;
        if(active !=null)
            activeID = active.hardwareAddress;

        List<AVA88GatewayInfo> gateways = new ArrayList<AVA88GatewayInfo>();

        Cursor cursor = database.query(AppDBHelper.TABLE_GATEWAY,
                AppDBHelper.allGatewayInfoColumns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                AVA88GatewayInfo ava88GatewayInfo = cursorToAVA88GatewayInfo(cursor);
                if (activeID!=null) {
                    if(activeID.equals(ava88GatewayInfo.hardwareAddress))
                        ava88GatewayInfo.active = true;
                }
                else
                    ava88GatewayInfo.active=false;
                gateways.add(ava88GatewayInfo);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }
        if(gateways.isEmpty()){
            callback.onDataNotAvailable();
        }
        else{
            callback.onGatewaysLoaded(gateways);
        }
        database.close();
    }

    @Override
    public void getAvailableGateways(@NonNull final LoadGatewaysCallback callback) {

        if (mAVA88GatewayScanner!=null){
            mAVA88GatewayScanner.cancel(true);
            Log.d(TAG,"Stopping search before searching again");
        }
        Log.d(TAG,"Searching");
        mAVA88GatewayScanner = new AVA88GatewayScanner(mContext,
                new AVA88GatewayScanner.GatewayScannerListener(){
                    @Override
                    public void onGatewayScannerDone(ArrayList<AVA88GatewayInfo> gatewayIPs) {
                        Log.d(TAG,"Searching Done");
                        if(gatewayIPs==null || gatewayIPs.isEmpty())
                            callback.onDataNotAvailable();
                        else
                            callback.onGatewaysLoaded(gatewayIPs);
                    }

                    @Override
                    public void onGatewayFound(AVA88GatewayInfo mAVA88GatewayInfo) {

                    }

                    @Override
                    public void onGatewayNotFound() {

                    }
        });
        mAVA88GatewayScanner.execute((AVA88GatewayInfo) null);
    }

    @Override
    public void getOwnedGateway(@NonNull String id, @NonNull GetOwnedGatewayCallback callback) {

    }

    @Override
    public void saveGateway(@NonNull AVA88GatewayInfo gateway) {
        SQLiteDatabase database=mAppDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppDBHelper.GATEWAY_ID, gateway.hardwareAddress);
        values.put(AppDBHelper.GATEWAY_IP, gateway.ipv4Address);
        values.put(AppDBHelper.GATEWAY_NAME, gateway.deviceName);
        values.put(AppDBHelper.GATEWAY_MODEL, gateway.deviceModel);
        values.put(AppDBHelper.GATEWAY_VERSION, gateway.deviceVersion);
        values.put(AppDBHelper.GATEWAY_USER, gateway.user);
        values.put(AppDBHelper.GATEWAY_PASSWORD, gateway.password);
        long insertId = database.insertWithOnConflict(AppDBHelper.TABLE_GATEWAY, null,
                values,SQLiteDatabase.CONFLICT_IGNORE);
        if (insertId == -1) {
            database.update(AppDBHelper.TABLE_GATEWAY,
                    values,AppDBHelper.GATEWAY_ID+"=?", new String[] {gateway.hardwareAddress});
        }
        database.close();
    }

    @Override
    public void getDevices(String location, final LoadDevicesCallback callback) {
        if(mAva88Gateway!=null && mAva88Gateway.isConnected()) {
            if (location != null & location.equals("Ungrouped"))
                location = " ";
            mAva88Gateway.fetchNodes(location, new AVA88Gateway.OnFetchNodesListener() {
                @Override
                public void onFetchNodesDone(final List<ZNode> nodes) {
                    callback.onDevicesLoaded(nodes);
                }
            });
        }
    }

    @Override
    public void getAllDevices(final LoadDevicesCallback callback) {
        if(mAva88Gateway!=null && mAva88Gateway.isConnected()) {
            mAva88Gateway.fetchNodes(new AVA88Gateway.OnFetchNodesListener() {
                @Override
                public void onFetchNodesDone(final List<ZNode> nodes) {
                    callback.onDevicesLoaded(nodes);
                }
            });
        }
    }

    @Override
    public void getLocations(final LocationsCallback callback) {
        if(mAva88Gateway!=null && mAva88Gateway.isConnected()) {
            Log.d(TAG, "Local repository source getting locations from AVA Gateway."
                    + "using Address: " + mAva88Gateway.getGatewayHost());
            mAva88Gateway.fetchLocations(new AVA88Gateway.LocationsCallback() {
                @Override
                public void onFetchLocationsDone(List<String> locations) {
                    Log.d(TAG, "Returning locations: " + locations.toArray(new String[0]));
                    callback.onLocationsLoaded(locations);
                }

                @Override
                public void onAddLocationsDone(String location) {

                }

                @Override
                public void onAddLocationsConflict(String location) {

                }

                @Override
                public void onLocationIdFound(int id) {

                }
            });
        }
    }

    @Override
    public void getLatestLog(final InclusionCallback callback) {
        mAva88Gateway.getSyslogLatest(new AVA88Gateway.InclusionCallback(){

            @Override
            public void onBusy(int action) {
                callback.onBusy(action);
            }

            @Override
            public void onCanceled(int action) {
                Log.d("DISCOVERY Source","CANCELLED");
                callback.onCanceled(action);
            }

            @Override
            public void onDone(final int action, final int nodeId) {
                Log.d("DISCOVERY Source","DONE");
                if(action == INCLUSION_ADDING || action == INCLUSION_FORCEADDING) {
                    mAva88Gateway.fetchNode(nodeId, new AVA88Gateway.OnFetchNodeListener() {

                        @Override
                        public void onFound(ZNode node) {
                            callback.onDone(action, node);
                        }

                        @Override
                        public void onNotFound(int id) {
                            ZNode zNode=new ZNode();
                            zNode.nodeID=nodeId;
                            zNode.nodeProductStr="Unknown";
                        }
                    });
                }
                else{
                    ZNode zNode=new ZNode();
                    zNode.nodeID=nodeId;
                    callback.onDone(action,zNode);
                }

            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
    }

    @Override
    public void checkNetworkState(Context context, CheckNetworkStateCallback callback) {
        if(NetworkUtilities.isWifiOnline(context)){
            callback.onWifiConnected();
        }
        else{
            callback.onWifiNotConnected();
        }
    }

    @Override
    public void addLocation(String location, final LocationsCallback callback) {
        mAva88Gateway.addLocation(location, new AVA88Gateway.LocationsCallback() {
            @Override
            public void onFetchLocationsDone(List<String> locations) {

            }

            @Override
            public void onAddLocationsDone(String location) {
                callback.onLocationsChanged();
            }

            @Override
            public void onAddLocationsConflict(String location) {
                callback.onLocationAddConflict(location);
            }

            @Override
            public void onLocationIdFound(int id) {

            }
        });
    }

    @Override
    public void changeNodeLocations(List<Integer> nodeIds, String newLocation,
                                    NodeChangeLocationsCallback callback) {
        for (int zNode:nodeIds) {
            mAva88Gateway.changeNodeLocation(zNode,newLocation);
        }
        callback.onNodeChangeLocationsDone();

    }

    @Override
    public void removeLocation(int id, LocationsCallback callback) {
        mAva88Gateway.removeLocation(id);
        callback.onLocationsChanged();
    }

    @Override
    public void removeLocation(String location, final LocationsCallback callback) {
        mAva88Gateway.getLocationId(location, new AVA88Gateway.LocationsCallback(){

            @Override
            public void onFetchLocationsDone(List<String> locations) {

            }

            @Override
            public void onAddLocationsDone(String location) {

            }

            @Override
            public void onAddLocationsConflict(String location) {

            }

            @Override
            public void onLocationIdFound(int id) {
                removeLocation(id, callback);
            }
        });
    }

    @Override
    public void startInclusion() {
        mAva88Gateway.startInclusion();
    }

    @Override
    public void startExclusion() {
        mAva88Gateway.startExclusion();
    }

    @Override
    public void cancelInclusionAction() {
        mAva88Gateway.cancelInclusionExclusion();
    }


    public boolean setActiveGateway(AVA88GatewayInfo gateway) {
        ContentValues values = new ContentValues();
        SQLiteDatabase database=mAppDBHelper.getWritableDatabase();
        values.put(AppDBHelper.GATEWAY_ACTIVE_ID, gateway.hardwareAddress);
        values.put("row", 0);
        long insertId = database.insertWithOnConflict(AppDBHelper.TABLE_ACTIVE_GATEWAY, null,
                values, SQLiteDatabase.CONFLICT_IGNORE);
        if (insertId == -1) {
            Log.d("DB","Storing " + gateway.ipv4Address);
            database.update(AppDBHelper.TABLE_ACTIVE_GATEWAY,values,"row=?", new String[] {"0"});
        }
        database.close();
        return true;
    }

    public AVA88GatewayInfo getActiveGateway() {
        SQLiteDatabase database=mAppDBHelper.getReadableDatabase();
        AVA88GatewayInfo ava88GatewayInfo=null;
        String active_gateway = "SELECT * FROM "
                + AppDBHelper.TABLE_GATEWAY
                +" INNER JOIN "
                + AppDBHelper.TABLE_ACTIVE_GATEWAY
                +" ON " +AppDBHelper.TABLE_GATEWAY
                + "._id=" + AppDBHelper.TABLE_ACTIVE_GATEWAY + "._id";

        Cursor cursor = database.rawQuery(active_gateway, null);
        if (cursor != null && cursor.moveToFirst()) {
            ava88GatewayInfo = cursorToAVA88GatewayInfo(cursor);

            // make sure to close the cursor
            cursor.close();
        }
        database.close();
        return ava88GatewayInfo;
    }

    public AVA88GatewayInfo getGateway(String id) {
        AVA88GatewayInfo ava88GatewayInfo=null;
        SQLiteDatabase database=mAppDBHelper.getReadableDatabase();
        Cursor cursor = database.query(AppDBHelper.TABLE_GATEWAY,
                AppDBHelper.allGatewayInfoColumns,
                AppDBHelper.GATEWAY_ID + "= \"" +id.trim() + "\"",null,null,null,null);
        if (cursor != null && cursor.moveToFirst()) {
            ava88GatewayInfo = cursorToAVA88GatewayInfo(cursor);

            // make sure to close the cursor
            cursor.close();
        }
        database.close();
        return ava88GatewayInfo;
    }

    private AVA88GatewayInfo cursorToAVA88GatewayInfo(Cursor cursor) {
        AVA88GatewayInfo ava88GatewayInfo = new AVA88GatewayInfo();
        ava88GatewayInfo.hardwareAddress = cursor.getString(0);
        ava88GatewayInfo.ipv4Address=cursor.getString(1);
        ava88GatewayInfo.deviceName=cursor.getString(2);
        ava88GatewayInfo.deviceModel=cursor.getString(3);
        ava88GatewayInfo.deviceVersion=cursor.getString(4);
        ava88GatewayInfo.user=cursor.getString(5);
        ava88GatewayInfo.password=cursor.getString(6);
        ava88GatewayInfo.isOwned=true;

        return ava88GatewayInfo;
    }

}
