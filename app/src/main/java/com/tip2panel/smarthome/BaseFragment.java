package com.tip2panel.smarthome;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.tip2panel.smarthome.utils.DialogUtilities;
import com.tip2panel.smarthome.utils.NetworkUtilities;

/**
 * Created by Setsuna F. Seie on 21/07/2017.
 */

public class BaseFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        NetworkUtilities.subcribeToNetworkEvents(getActivity().getApplicationContext(),
                new NetworkUtilities.NetworkEventsCallback() {
                    @Override
                    public void onWifiConnected() {
                        Log.d("WIFI", "WIFI CONNECTED!!!");
                    }

                    @Override
                    public void onWifiDisconnected() {
                        Log.d("WIFI", "WIFI DISCONNECTED!!!");
                        DialogUtilities.getNoWifiDialog(getActivity(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getActivity().finish();
                            }
                        }).show();
                    }

                    @Override
                    public void onDataConnected() {

                    }

                    @Override
                    public void onDataDisconnected() {

                    }
                });

    }
}
