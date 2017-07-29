package com.engkan2kit.ava88;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Setsuna F. Seie on 22/04/2017.
 */

public class AVA88GatewayInfo implements Parcelable {
    public String deviceVersion="0.0";
    public String hardwareAddress="";
    public String deviceModel="0000";
    public String ipv4Address="";
    public String deviceName="";
    public String user="";
    public String password="";
    public boolean isOwned=false;
    public boolean active=false;

    public AVA88GatewayInfo(){

    }

    public AVA88GatewayInfo(String hardwareAddress, String ipv4Address){
        this.hardwareAddress=hardwareAddress;
        this.ipv4Address=ipv4Address;
    }

    public AVA88GatewayInfo(String serverResponse){
        int index=serverResponse.indexOf("mac=");
        if(index>-1)
            this.hardwareAddress = serverResponse.substring(index+4,serverResponse.indexOf('&',index)).trim();

        index=serverResponse.indexOf("version=");
        if(index>-1)
            this.deviceVersion=serverResponse.substring(index+8,serverResponse.indexOf('&',index));

        index=serverResponse.indexOf("model=");
        if(index>-1)
            this.deviceModel=serverResponse.substring(index+6,serverResponse.indexOf('&',index));

        index=serverResponse.indexOf("ipv4=");
        if(index>-1)
            this.ipv4Address=serverResponse.substring(index+5,serverResponse.indexOf('&',index)).trim();
        index=serverResponse.indexOf("name=");
        if(index>-1)
            this.deviceName=serverResponse.substring(index+5,serverResponse.indexOf('&',index)).trim();

    }

    public AVA88GatewayInfo(Parcel parcel){
        this.hardwareAddress=parcel.readString();
        this.deviceVersion=parcel.readString();
        this.deviceModel=parcel.readString();
        this.ipv4Address=parcel.readString();
        this.deviceName=parcel.readString();
        this.user=parcel.readString();
        this.password=parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceVersion);
        dest.writeString(this.hardwareAddress);
        dest.writeString(this.deviceModel);
        dest.writeString(this.ipv4Address);
        dest.writeString(this.deviceName);
        dest.writeString(this.user);
        dest.writeString(this.password);
    }

    public static final Parcelable.Creator<AVA88GatewayInfo> CREATOR = new Parcelable.Creator<AVA88GatewayInfo>() {
        public AVA88GatewayInfo createFromParcel(Parcel in) {
            return new AVA88GatewayInfo(in);
        }

        public AVA88GatewayInfo[] newArray(int size) {

            return new AVA88GatewayInfo[size];
        }

    };
}
