package com.engkan2kit.ava88;

import java.util.HashMap;

/**
 * Created by Setsuna F. Seie on 27/04/2017.
 */

public final class ZNodeStatus {
    public final static int AWAKE=4;
    public final static int SLEEPING=3;
    public final static int READY=2;
    public final static int OKAY=1;
    public final static int UNKNOWN=0;
    public final static int DEAD=-1;
    public final static int ERROR=-2;


    public final static String strUNKNOWN="Unknown";
    public final static String strOKAY="Okay";
    public final static String strERROR="Error";
    public final static String strDEAD="Dead";
    public final static String strSLEEPING="Sleeping";
    public final static String strREADY="Ready";
    public final static String strAWAKE="Awake";

    public final static HashMap<String,Integer> deviceStatus =new HashMap<>();
    static{
        deviceStatus.put(strOKAY,OKAY);
        deviceStatus.put(strERROR,ERROR);
        deviceStatus.put(strDEAD,DEAD);
        deviceStatus.put(strSLEEPING,SLEEPING);
        deviceStatus.put(strREADY,READY);
        deviceStatus.put(strAWAKE,AWAKE);
        deviceStatus.put(strUNKNOWN,UNKNOWN);
    }
}
