package com.tip2panel.smarthome.data.source.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.engkan2kit.ava88.AVA88GatewayInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Setsuna F. Seie on 03/05/2017.
 */

public class GatewaySQLiteSource {
    // Database fields
    private SQLiteDatabase database;
    AppDBHelper mAppDBHelper;
    private String[] allColumns= { AppDBHelper.GATEWAY_ID, AppDBHelper.GATEWAY_IP,
            AppDBHelper.GATEWAY_NAME,  AppDBHelper.GATEWAY_MODEL, AppDBHelper.GATEWAY_VERSION,AppDBHelper.GATEWAY_USER,AppDBHelper.GATEWAY_PASSWORD};

    public GatewaySQLiteSource(AppDBHelper mAppDBHelper) {
        this.mAppDBHelper =mAppDBHelper;
    }

    public void open(){
        database=mAppDBHelper.getWritableDatabase();
    }

    public void close(){
        database.close();
    }
    public boolean addGateway(AVA88GatewayInfo gateway) {
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
            database.update(AppDBHelper.TABLE_GATEWAY,values,AppDBHelper.GATEWAY_ID+"=?", new String[] {gateway.hardwareAddress});
        }
        return true;
    }

    public void deleteGateway(AVA88GatewayInfo gateway) {
        String id = gateway.hardwareAddress;
        System.out.println("Gateway deleted with id: " + id);
        database.delete(AppDBHelper.TABLE_GATEWAY, AppDBHelper.GATEWAY_ID
                + " = " + id, null);
    }

    public List<AVA88GatewayInfo> getAllGateways() {
        AVA88GatewayInfo active = getActiveGateway();
        String activeID=null;
        if(active !=null)
            activeID = active.hardwareAddress;

        List<AVA88GatewayInfo> gateways = new ArrayList<AVA88GatewayInfo>();

        Cursor cursor = database.query(AppDBHelper.TABLE_GATEWAY,
                allColumns, null, null, null, null, null);
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
        return gateways;
    }

    public boolean setActiveGateway(AVA88GatewayInfo gateway) {
        ContentValues values = new ContentValues();
        values.put(AppDBHelper.GATEWAY_ACTIVE_ID, gateway.hardwareAddress);
        values.put("row", 0);
        long insertId = database.insertWithOnConflict(AppDBHelper.TABLE_ACTIVE_GATEWAY, null,
                values,SQLiteDatabase.CONFLICT_IGNORE);
        if (insertId == -1) {
            Log.d("DB","Storing " + gateway.ipv4Address);
            database.update(AppDBHelper.TABLE_ACTIVE_GATEWAY,values,"row=?", new String[] {"0"});
        }
        return true;
    }

    public AVA88GatewayInfo getGateway(String id) {
        AVA88GatewayInfo ava88GatewayInfo=null;
        Cursor cursor = database.query(AppDBHelper.TABLE_GATEWAY,
                allColumns,AppDBHelper.GATEWAY_ID + "= \"" +id.trim() + "\"",null,null,null,null);
        if (cursor != null && cursor.moveToFirst()) {
            ava88GatewayInfo = cursorToAVA88GatewayInfo(cursor);

            // make sure to close the cursor
            cursor.close();
        }
        return ava88GatewayInfo;
    }

    public AVA88GatewayInfo getActiveGateway() {
        AVA88GatewayInfo ava88GatewayInfo=null;
        String active_gateway = "SELECT * FROM " + AppDBHelper.TABLE_GATEWAY +" INNER JOIN "+ AppDBHelper.TABLE_ACTIVE_GATEWAY+" ON " +AppDBHelper.TABLE_GATEWAY+ "._id=" + AppDBHelper.TABLE_ACTIVE_GATEWAY + "._id";

        Cursor cursor = database.rawQuery(active_gateway, null);
        if (cursor != null && cursor.moveToFirst()) {
            ava88GatewayInfo = cursorToAVA88GatewayInfo(cursor);

            // make sure to close the cursor
            cursor.close();
        }
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
