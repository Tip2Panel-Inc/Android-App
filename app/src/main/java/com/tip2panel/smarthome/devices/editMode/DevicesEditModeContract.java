package com.tip2panel.smarthome.devices.editMode;

import com.engkan2kit.ava88.ZNode;
import com.tip2panel.smarthome.BasePresenter;
import com.tip2panel.smarthome.BaseView;
import com.tip2panel.smarthome.utils.DeviceListItem;

import java.util.List;

/**
 * Created by Setsuna F. Seie on 11/07/2017.
 */

public interface DevicesEditModeContract {
    interface MvpView extends BaseView<MvpPresenter> {
        void showDevicesList(List<DeviceListItem> deviceListItems);
        void endEditing();
        void jumpToDeviceActivity();
        void showChangeLocationSelector(List<String> locations,String location);
    }

    interface MvpPresenter extends BasePresenter {
        void loadDevices(String location);
        void removeLocation(String location);
        void changeNodesLocation(List<Integer> nodeIds,String newLocation);
        void loadLocationListsForSelector(String location);
    }
}
