package com.tip2panel.smarthome.data.source.local;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Setsuna F. Seie on 13/06/2017.
 */

public class AppDBHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "tip2panel.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ACTIVE_GATEWAY = "active_gateway";
    public static final String GATEWAY_ACTIVE_ID = "_id";

    public static final String TABLE_GATEWAY = "gateway";
    public static final String GATEWAY_ID = "_id";
    public static final String GATEWAY_IP = "ip";
    public static final String GATEWAY_NAME = "name";
    public static final String GATEWAY_MODEL = "model";
    public static final String GATEWAY_VERSION = "version";
    public static final String GATEWAY_USER = "user";
    public static final String GATEWAY_PASSWORD = "password";

    public static final String TABLE_SUPPORTED_NODE = "supported_node";
    public static final String SUPPORTED_NODE_ID = "_id";
    public static final String SUPPORTED_NODE_BTYPE = "btype";
    public static final String SUPPORTED_NODE_GTYPE = "gtype";
    public static final String SUPPORTED_NODE_MANUFACTURER = "manufacturer";
    public static final String SUPPORTED_NODE_PRODUCT = "product";

    public static final String TABLE_SUPPORTED_COMMAND_CLASS = "supported_command_class";
    public static final String SUPPORTED_COMMAND_CLASS_ID = "_id";
    public static final String SUPPORTED_COMMAND_CLASS_GENRE = "genre";
    public static final String SUPPORTED_COMMAND_CLASS_TYPE = "type";
    public static final String SUPPORTED_COMMAND_CLASS_CLASS = "class";
    public static final String SUPPORTED_COMMAND_CLASS_INDEX = "index";
    public static final String SUPPORTED_COMMAND_CLASS_LABEL = "label";
    public static final String SUPPORTED_COMMAND_CLASS_UNITS= "units";
    public static final String SUPPORTED_COMMAND_CLASS_READONLY = "readonly";
    public static final String SUPPORTED_COMMAND_CLASS_POLLED = "polled";
    public static final String SUPPORTED_COMMAND_CLASS_VERSION = "version";

    public static String[] allGatewayInfoColumns= { AppDBHelper.GATEWAY_ID,
            AppDBHelper.GATEWAY_IP,AppDBHelper.GATEWAY_NAME,  AppDBHelper.GATEWAY_MODEL,
            AppDBHelper.GATEWAY_VERSION,AppDBHelper.GATEWAY_USER,AppDBHelper.GATEWAY_PASSWORD};

    public AppDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}