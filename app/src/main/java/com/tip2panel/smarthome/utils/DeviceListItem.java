package com.tip2panel.smarthome.utils;

import com.engkan2kit.ava88.ZNode;
import com.engkan2kit.ava88.ZNodeValue;

import java.util.List;

/**
 * Created by engkan2kit on 2/16/18.
 */

public class DeviceListItem {

    private String mDeviceItemId;
    private int mItemType;
    private ZNodeValue mZNodeValue;

    public DeviceListItem()
    {

    }

    public DeviceListItem(String deviceItemId, int itemType, ZNodeValue zNodeValue)
    {
        this.mDeviceItemId=deviceItemId;
        this.mItemType=itemType;
        this.mZNodeValue=zNodeValue;
    }

    public void setId(String deviceItemId){this.mDeviceItemId=deviceItemId;}
    public void setType(int itemType){this.mItemType=itemType;}
    public void setZNodeValue(ZNodeValue zNodeValue){this.mZNodeValue=zNodeValue;}

    public String getId(){return mDeviceItemId;}
    public int getType(){return mItemType;}
    public ZNodeValue getZNodeValue(){return mZNodeValue;}

}
