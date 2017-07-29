package com.tip2panel.smarthome.dashboard;

import android.support.annotation.Nullable;

import com.engkan2kit.ava88.ZNode;
import com.tip2panel.smarthome.BasePresenter;
import com.tip2panel.smarthome.BaseView;

import java.util.List;

/**
 * Created by Setsuna F. Seie on 11/07/2017.
 */

public interface DashboardContract {
    interface MvpView extends BaseView<MvpPresenter> {
        void showDeviceDetails(ZNode zNode);
        void showDevicesList(List<ZNode> zNodes);
        void isActive();
    }

    interface MvpPresenter extends BasePresenter {
        void changeValue(ZNode node, String zNodeValueKey, int instance);
        void loadDevices();
    }
}
