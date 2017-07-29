package com.tip2panel.smarthome.devices.editMode;

import com.engkan2kit.ava88.ZNode;
import com.tip2panel.smarthome.BasePresenter;
import com.tip2panel.smarthome.BaseView;

import java.util.List;

/**
 * Created by Setsuna F. Seie on 11/07/2017.
 */

public interface DevicesEditModeContract {
    interface MvpView extends BaseView<MvpPresenter> {
        void showDevicesList(List<ZNode> zNodes);
        void endEditing();
        void jumpToDeviceActivity();
    }

    interface MvpPresenter extends BasePresenter {
        void loadDevices(String location);
        void removeLocation(String location);
    }
}
