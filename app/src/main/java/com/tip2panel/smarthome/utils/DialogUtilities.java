package com.tip2panel.smarthome.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;

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


    public interface LocationDialogCallback{
        void onAddLocation(String location);
    }

    /**
     * The dialog for adding new Location.
     */
    public static AlertDialog getAddLocationDialog(final Activity activity, final LocationDialogCallback callback) {
        final EditText input = new EditText(activity);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setTitle(R.string.msg_new_location)
                .setView(input)
                .setMessage(R.string.msg_location_examples)
                .setCancelable(false).setPositiveButton(R.string.dialog_continue,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                callback.onAddLocation(input.getText().toString());
                            }
                        });
        return dialogBuilder.create();
    }

    /**
     * The helper method to show gateway error alert dialog.
     */
    public static void showAddLocationDialog(final Activity activity, final LocationDialogCallback callback) {
        DialogUtilities.getAddLocationDialog(activity,callback).show();
    }


    /**
     * The dialog that prompt to connect Internet, with listener.
     */
    public static AlertDialog getLocationsConflictDialog(final Activity activity,String location) {
        TextView dialogMessage = new TextView(activity);
        dialogMessage.setText("\""+location+ "\"");
        dialogMessage.setGravity(Gravity.CENTER_HORIZONTAL);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setTitle(R.string.msg_new_location_error)
                .setView(dialogMessage)
                .setMessage(R.string.msg_location_exists)
                .setCancelable(false).setPositiveButton(R.string.dialog_ok, new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
        return dialogBuilder.create();
    }


    /**
     * The helper method to show gateway error alert dialog.
     */
    public static void showLocationsConflictDialog(final Activity activity, String location) {
        DialogUtilities.getLocationsConflictDialog(activity,location).show();
    }
}
