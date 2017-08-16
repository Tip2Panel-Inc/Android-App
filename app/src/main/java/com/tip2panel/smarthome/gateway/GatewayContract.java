package com.tip2panel.smarthome.gateway;

import android.content.Context;

import com.engkan2kit.ava88.AVA88Gateway;
import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.engkan2kit.ava88.AVA88GatewayScanner;
import com.tip2panel.smarthome.BasePresenter;
import com.tip2panel.smarthome.BaseView;
import java.util.List;

/**
 * Created by Setsuna F. Seie on 11/07/2017.
 */

public interface GatewayContract {
    interface MvpView extends BaseView<MvpPresenter> {
        void showConnectionSuccess();
        void showConnectionFail();
        void showAvailableGateways(List<AVA88GatewayInfo> gatewayIPs);
        void showOwnedGateways(List<AVA88GatewayInfo> gatewayIPs);
        void showGatewayNotOnlineDialog();
        void showInvalidGatewayCredentialsDialog(AVA88GatewayInfo ava88GatewayInfo);
        void showGatewayLoginDialog(AVA88GatewayInfo ava88GatewayInfo);
        void showNoAvailableGateway(boolean show);
        void ShowNoOwnedGateway(boolean show);
        void isActive();
        void showGatewayDetails();
    }

    interface MvpPresenter extends BasePresenter {
        void getAvailableGateways(Context context);
        void getOwnedGateways(Context context);
        void removeOwnedGateways(Context context);
        void storeGateway(Context context, AVA88GatewayInfo ava88GatewayInfo);
        void connectGateway(AVA88GatewayInfo ava88GatewayInfo);
        void cancelGatewayScan(Context context);
    }
}
