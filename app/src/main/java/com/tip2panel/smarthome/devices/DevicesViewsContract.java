package com.tip2panel.smarthome.devices;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.engkan2kit.ava88.ZNode;
import com.engkan2kit.ava88.ZNodeValue;
import com.tip2panel.smarthome.BasePresenter;
import com.tip2panel.smarthome.BaseView;
import com.tip2panel.smarthome.utils.DeviceListItem;

import java.util.List;

/**
 * Created by Setsuna F. Seie on 11/07/2017.
 */

public interface DevicesViewsContract {
    interface ChildView{
        void onAttachToParentView(Fragment fragment);
        void showDeviceDetails(ZNode zNode);
        void showDevicesList(List<DeviceListItem> deviceListItems);
    }

    interface ParentView{
        void loadDevicesList(String location);
        void loadDeviceDetails(String id);
        void changeValue(ZNodeValue nodeValue);
        void changeDeviceName(ZNode zNode,String name);
        void changeDeviceLocation(ZNode zNode,String location);
        void setChildView(String location, ChildView childView);
        void addLocation(String location);
    }
}
