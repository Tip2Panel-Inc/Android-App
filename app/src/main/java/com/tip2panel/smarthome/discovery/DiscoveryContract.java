package com.tip2panel.smarthome.discovery;

import android.support.annotation.Nullable;

import com.engkan2kit.ava88.ZNode;
import com.tip2panel.smarthome.BasePresenter;
import com.tip2panel.smarthome.BaseView;


/**
 * Created by Setsuna F. Seie on 21/08/2017.
 */

public interface DiscoveryContract {
    interface MvpView extends BaseView<DiscoveryContract.MvpPresenter> {
        void setFabTextCancel();
        void setFabTextStart();
        void showProgressbar(boolean show);
        void enableRadioGroup(boolean enable);
        void showResultDialog(@Nullable final int action, @Nullable final ZNode zNode);
        void setInclusionMode(boolean mode);
    }

    interface MvpPresenter extends BasePresenter {
        void startInclusion();
        void startExclusion();
        void cancelAction();
        void checkLatestLog();
    }
}
