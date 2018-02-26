package com.engkan2kit.ava88;

import java.util.HashMap;

/**
 * Created by engkan2kit on 2/16/18.
 */

public final class ZNodeValueClass {
    public static final int NOP                 =0x00;
    public static final String strNOP                   ="NOP";
    public static final int SWITCH_BINARY       =0x25;
    public static final String strSWITCH_BINARY         ="SWITCH BINARY";
    public static final int SWITCH_MULTILEVEL   =0x26;
    public static final String strSWITCH_MULTILEVEL     ="SWITCH MULTILEVEL";
    public static final int SENSOR_BINARY       =0x30;
    public static final String strSENSOR_BINARY         ="SENSOR BINARY";
    public static final int SENSOR_MULTILEVEL   =0x31;
    public static final String strSENSOR_MULTILEVEL     ="SENSOR_ MULTILEVEL";
    public static final int COLOR_CONTROL       =0x33;
    public static final String strCOLOR_CONTROL         ="COLOR CONTROL";
    public static final int ALARM               =0x71;
    public static final String strALARM                 ="ALARM";

    public final static HashMap<String,Integer> valueClass =new HashMap<>();
    static{
        valueClass.put(strNOP,NOP);
        valueClass.put(strSWITCH_BINARY,SWITCH_BINARY);
        valueClass.put(strSWITCH_MULTILEVEL,SWITCH_MULTILEVEL);
        valueClass.put(strSENSOR_BINARY,SENSOR_BINARY);
        valueClass.put(strSENSOR_MULTILEVEL,SENSOR_MULTILEVEL);
        valueClass.put(strCOLOR_CONTROL,COLOR_CONTROL);
        valueClass.put(strALARM,ALARM);
    }
}
