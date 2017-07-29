package com.engkan2kit.ava88;

import android.util.SparseArray;

import java.util.HashMap;

/**
 * Created by Setsuna F. Seie on 27/04/2017.
 */

public class ZNodeProduct {

    public final static int AVACONTROL=0;
    public final static int APPLIANCEONOFF=1;
    public final static int PIRSENSOR=2;
    public final static int COLOURLED=3;


    public  final static String strAVACONTROL="avaControl-100";
    public final static String strAPPLIANCEONOFF="Appliance On/Off Module(Power Plug In Type)";
    public final static String strPIRSENSOR="Wireless PIR Motion Sensor 2(Temperature Sensor Built-In)";
    public final static String strCOLOURLED="Colour Led";

    public final static HashMap<String,Integer> deviceType =new HashMap<>();
    static{
        deviceType.put(strAVACONTROL,AVACONTROL);
        deviceType.put(strAPPLIANCEONOFF,APPLIANCEONOFF);
        deviceType.put(strPIRSENSOR,PIRSENSOR);
        deviceType.put(strCOLOURLED,COLOURLED);
    }

    public final static SparseArray<String> deviceTypeString =new SparseArray<>();
    static{
        deviceTypeString.put(AVACONTROL,strAVACONTROL);
        deviceTypeString.put(APPLIANCEONOFF,strAPPLIANCEONOFF);
        deviceTypeString.put(PIRSENSOR,strPIRSENSOR);
        deviceTypeString.put(COLOURLED,strCOLOURLED);
    }

    public  static int nodeZNodeProducttoInt(String productString){
        return deviceType.get(productString);
    }

    public static String nodeZNodeProducttoString(int product){
        return deviceTypeString.get(product);
    }

}
