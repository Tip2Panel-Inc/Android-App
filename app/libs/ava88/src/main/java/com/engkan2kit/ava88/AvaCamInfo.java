package com.engkan2kit.ava88;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Setsuna F. Seie on 22/04/2017.
 */

public class AvaCamInfo implements Parcelable {
    public String deviceVersion="0.0";
    public String hardwareAddress="";
    public String deviceModel="0000";
    public String ipv4Address="";
    public String deviceName="";
    public String user="";
    public String password="";
    public boolean isOwned=false;
    public boolean active=false;

    public AvaCamInfo(){

    }

    public AvaCamInfo(String hardwareAddress, String ipv4Address){
        this.hardwareAddress=hardwareAddress;
        this.ipv4Address=ipv4Address;
    }

    public AvaCamInfo(String serverResponse){
        String response=serverResponse.toLowerCase();
        int index=response.indexOf("mac=");
        if(index>-1)
            this.hardwareAddress = serverResponse.substring(index+4,serverResponse.indexOf('&',index)).trim();

        index=response.indexOf("version=");
        if(index>-1)
            this.deviceVersion=serverResponse.substring(index+8,serverResponse.indexOf('&',index));

        index=response.indexOf("model=");
        if(index>-1)
            this.deviceModel=serverResponse.substring(index+6,serverResponse.indexOf('&',index));

        index=response.indexOf("ipv4=");
        if(index>-1)
            this.ipv4Address=serverResponse.substring(index+5,serverResponse.indexOf('&',index)).trim();
        index=response.indexOf("name=");
        if(index>-1)
            this.deviceName=serverResponse.substring(index+5,serverResponse.indexOf('&',index)).trim();

    }

    public AvaCamInfo(Parcel parcel){
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

    public static final Creator<AvaCamInfo> CREATOR = new Creator<AvaCamInfo>() {
        public AvaCamInfo createFromParcel(Parcel in) {
            return new AvaCamInfo(in);
        }

        public AvaCamInfo[] newArray(int size) {

            return new AvaCamInfo[size];
        }

    };
}
