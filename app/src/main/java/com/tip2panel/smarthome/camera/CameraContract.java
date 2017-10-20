package com.tip2panel.smarthome.camera;

import com.engkan2kit.ava88.AvaCamInfo;
import com.engkan2kit.ava88.ZNode;
import com.tip2panel.smarthome.BasePresenter;
import com.tip2panel.smarthome.BaseView;

import java.util.List;

/**
 * Created by Setsuna F. Seie on 11/07/2017.
 */

public interface CameraContract {
    interface MvpView extends BaseView<MvpPresenter> {
        void showCameraDetails(AvaCamInfo avaCamInfo);
        void showCameraList(List<AvaCamInfo> avaCamInfos);
        void isActive();
    }

    interface MvpPresenter extends BasePresenter {
        void playCamera(AvaCamInfo avaCamInfo);
        void loadCameras();
    }
}
