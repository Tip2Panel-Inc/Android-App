package com.tip2panel.smarthome.devices;

import android.support.annotation.Nullable;

import com.engkan2kit.ava88.ZNode;
import com.engkan2kit.ava88.ZNodeValue;
import com.tip2panel.smarthome.BasePresenter;
import com.tip2panel.smarthome.BaseView;
import com.tip2panel.smarthome.utils.DeviceListItem;

import java.util.List;

/**
 * Created by Setsuna F. Seie on 11/07/2017.
 */

public interface DevicesPagerContract {
    interface MvpView extends BaseView<MvpPresenter> {
        void showPages(List<String> pageTitles);
        void showDeviceDetails(ZNode zNode);
        void showDevicesList(List<DeviceListItem> deviceListItems, @Nullable String Location);
        void isActive();
        void showLocationAddConflictDialog(String location);
    }

    interface MvpPresenter extends BasePresenter {
        void changeValue(ZNodeValue nodeValue);
        void loadDevices(final String Location);
        void loadPages();
        void addLocation(String location);
    }
}
