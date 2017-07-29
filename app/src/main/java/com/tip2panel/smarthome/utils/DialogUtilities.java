package com.tip2panel.smarthome.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import com.tip2panel.smarthome.R;
import com.tip2panel.smarthome.devices.DevicesActivity;
import com.tip2panel.smarthome.gateway.GatewayActivity;

/**
 * Created by Setsuna F. Seie on 20/07/2017.
 */

public class DialogUtilities {
    /**
     * The dialog that prompt to connect Internet, with listener.
     */
    public static AlertDialog getNoWifiDialog(final Activity activity,
                                              DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setTitle(R.string.msg_wifi_not_connected)
                .setMessage(R.string.msg_try_wifi_again)
                .setCancelable(false).setPositiveButton(R.string.settings_capital, new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            }
                        }).setNegativeButton(R.string.dialog_not_now, negativeListener);
        return dialogBuilder.create();
    }

    /**
     * The helper method to show Internet alert dialog and finish the activity.
     */
    public static void showWifiNotConnectDialog(final Activity activity) {
        DialogUtilities.getNoWifiDialog(activity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                activity.finish();
            }
        }).show();
    }

    /**
     * The dialog that prompt to connect Internet, with listener.
     */
    public static AlertDialog getGatewayConnectionErrorDialog(final Activity activity) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setTitle(R.string.msg_gateway_error)
                .setMessage(R.string.msg_gateway_not_found_try_again)
                .setCancelable(false).setPositiveButton(R.string.dialog_ok, new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (!(activity instanceof GatewayActivity)){
                                    Intent intent =
                                            new Intent(activity, GatewayActivity.class);
                                    if (intent!=null) {
                                        activity.startActivity(intent);
                                        activity.overridePendingTransition(0, 0);
                                        activity.finish();
                                    }
                                }
                            }
                        });
        return dialogBuilder.create();
    }


    /**
     * The helper method to show gateway error alert dialog.
     */
    public static void showGatewayConnectionErrorDialog(final Activity activity) {
        DialogUtilities.getGatewayConnectionErrorDialog(activity).show();
    }
}
