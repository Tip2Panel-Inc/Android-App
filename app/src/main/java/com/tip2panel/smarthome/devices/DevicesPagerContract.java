package com.tip2panel.smarthome.devices;

import android.support.annotation.Nullable;

import com.engkan2kit.ava88.ZNode;
import com.tip2panel.smarthome.BasePresenter;
import com.tip2panel.smarthome.BaseView;

import java.util.List;

/**
 * Created by Setsuna F. Seie on 11/07/2017.
 */

public interface DevicesPagerContract {
    interface MvpView extends BaseView<MvpPresenter> {
        void showPages(List<String> pageTitles);
        void showDeviceDetails(ZNode zNode);
        void showDevicesList(List<ZNode> zNodes,@Nullable String Location);
        void isActive();
    }

    interface MvpPresenter extends BasePresenter {
        void changeValue(ZNode node, String zNodeValueKey, int instance);
        void loadDevices(final String Location);
        void loadPages();
    }
}
