package com.tip2panel.smarthome.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.tip2panel.smarthome.R;
import com.tip2panel.smarthome.devices.DevicesActivity;
import com.tip2panel.smarthome.gateway.GatewayActivity;

import java.util.List;

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
                .setCancelable(false)
                .setPositiveButton(R.string.settings_capital, new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            }
                        })
                .setNegativeButton(R.string.dialog_not_now, negativeListener);
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
     * The dialog that shows gateway error, with listener.
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

    /**
     * The dialog that shows gateway invalid credentials, with listener.
     */
    public static AlertDialog
    getGatewayInvalidCredentialsErrorDialog(final Activity activity,
                                            final AVA88GatewayInfo ava88GatewayInfo,
                                            final GatewayLoginDialogCallback callback) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setTitle(R.string.msg_gateway_error)
                .setMessage(R.string.msg_invalid_gateway_credentials)
                .setCancelable(false).setPositiveButton(R.string.dialog_ok, new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                callback.onInvalidCredentials(ava88GatewayInfo);
                            }
                        });
        return dialogBuilder.create();
    }


    /**
     * The helper method to show gateway invalid credentials error alert dialog.
     */
    public static void showGatewayInvalidCredentialsErrorDialog(final Activity activity,
                                                                AVA88GatewayInfo ava88GatewayInfo,
                                                                GatewayLoginDialogCallback callback) {
        DialogUtilities.getGatewayInvalidCredentialsErrorDialog(activity,ava88GatewayInfo,callback).show();
    }


    public interface LocationDialogCallback{
        void onAddLocation(String location);
        void onDeleteLocation(String location);
    }

    public interface ChangeDeviceNameDialogCallback{
        void onChangeName(String deviceName);
    }

    public interface GatewayLoginDialogCallback{
        void onLogin(String user, String password);
        void onInvalidCredentials(AVA88GatewayInfo ava88GatewayInfo);
    }

    /**
     * The dialog for entering credentials for the gateway.
     */
    public static AlertDialog getGatewayLoginDialog(final Activity activity, @Nullable String user,
                                                    final GatewayLoginDialogCallback callback) {
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_gateway_login,null);
        final EditText usernameEditText = (EditText) view.findViewById(R.id.usernameEditText);
        final EditText passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);
        if(user!=null && user.length()>0){
            usernameEditText.setText(user);
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setTitle(R.string.dialog_login)
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_continue,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callback.onLogin(usernameEditText.getText().toString(),
                                        passwordEditText.getText().toString());
                                dialog.dismiss();
                            }
                        });
        return dialogBuilder.create();
    }

    /**
     * The helper method to show  entering credentials for the gateway dialog.
     */
    public static void showGatewayLoginDialog(final Activity activity, @Nullable String user, final GatewayLoginDialogCallback callback) {
        DialogUtilities.getGatewayLoginDialog(activity,user,callback).show();
    }




    /**
     * The dialog for adding new Location.
     */
    public static AlertDialog getAddLocationDialog(final Activity activity, final LocationDialogCallback callback) {
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_add_location,null);
        final EditText input = (EditText) view.findViewById(R.id.locationNameEditText);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setTitle(R.string.msg_new_location)
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_continue,
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
     * The helper method to show add location alert dialog.
     */
    public static void showAddLocationDialog(final Activity activity, final LocationDialogCallback callback) {
        DialogUtilities.getAddLocationDialog(activity,callback).show();
    }


    /**
     * The dialog that prompt to conflict of location name, with listener.
     */
    public static AlertDialog getLocationsConflictDialog(final Activity activity,String location) {

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_add_location_conflict,null);
        TextView dialogMessage = (TextView) view.findViewById(R.id.locationTextView);
        dialogMessage.setText("\""+location+ "\"");

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setTitle(R.string.msg_new_location_error)
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_ok, new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
        return dialogBuilder.create();
    }


    /**
     * The helper method to show Location Name Conflict alert dialog.
     */
    public static void showLocationsConflictDialog(final Activity activity, String location) {
        DialogUtilities.getLocationsConflictDialog(activity,location).show();
    }


    /**
     * The dialog that prompt to remove location, with listener.
     */
    public static AlertDialog getRemoveLocationsDialog(final Activity activity, final String location,
                                                       final LocationDialogCallback callback) {

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_delete_location,null);
        TextView dialogMessage = (TextView) view.findViewById(R.id.locationTextView);
        dialogMessage.setText("\""+location+ "\"");

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setTitle(R.string.remove_location_confirmation)
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_ok, new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                callback.onDeleteLocation(location);
                            }
                        })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });;
        return dialogBuilder.create();
    }


    /**
     * The helper method to show remove Location alert dialog.
     */
    public static void showRemoveLocationsDialog(final Activity activity, String location,
                                                 final LocationDialogCallback callback) {
        DialogUtilities.getRemoveLocationsDialog(activity,location, callback).show();
    }


    public interface ChangeLocationDialogCallback{
        void onChangeLocationOk(String location);
    }
    /**
     * The dialog that prompt to change location, with listener.
     */
    public static AlertDialog getChangeLocationsDialog(final Activity activity,
                                                       final List<String> locations,
                                                       final String location,
                                                       final ChangeLocationDialogCallback callback) {
        final ArrayAdapter arrayAdapter=new ArrayAdapter(activity,
                android.R.layout.simple_list_item_single_choice,
                locations);
        class SelectedLocation{
            private String selected="Ungrouped";
            public SelectedLocation(){

            }
            public SelectedLocation(String location){
                this.selected=location;
            }

            public void setSelected(String location){
                selected=location;
            }
            public String getSelected(){
                return selected;
            }
        }

        final SelectedLocation selectedLocation = new SelectedLocation(location);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setTitle(R.string.change_location_selection)
                .setCancelable(true)
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setSingleChoiceItems (arrayAdapter,
                        arrayAdapter.getPosition(location),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedLocation.setSelected(arrayAdapter.getItem(which).toString());

                    }
                })
                .setPositiveButton(R.string.dialog_ok, new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                callback.onChangeLocationOk(selectedLocation.getSelected());
                            }
                        })
                ;
        return dialogBuilder.create();
    }


    /**
     * The helper method to show change location alert dialog.
     */
    public static void showChangeLocationsDialog(final Activity activity,
                                                 List<String> locations,
                                                 String location,
                                                 final ChangeLocationDialogCallback callback) {
        DialogUtilities.getChangeLocationsDialog(activity,locations,location, callback).show();
    }


    /**
     * The dialog for changing Device name.
     */
    public static AlertDialog getChangeDeviceNameDialog(final Activity activity,String deviceId, String currentName, final ChangeDeviceNameDialogCallback callback) {
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_change_device_name,null);
        final EditText input = (EditText) view.findViewById(R.id.deviceNameEditText);
        input.setText(currentName);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setTitle(R.string.msg_device_name)
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_continue,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                callback.onChangeName(input.getText().toString());
                            }
                        });
        return dialogBuilder.create();
    }

    /**
     * The helper method to show changing Device name alert dialog.
     */
    public static void showChangeDeviceNameDialog(final Activity activity,String deviceId,String currentName, final ChangeDeviceNameDialogCallback callback) {
        DialogUtilities.getChangeDeviceNameDialog(activity,deviceId,currentName,callback).show();
    }


    /**
     * The dialog that shows Device Discovery Result.
     */
    public static AlertDialog getDeviceDiscoveryResultDialog(final Activity activity,final String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                .setTitle(R.string.dialog_device_discovery)
                .setMessage(message)
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
     * The helper method to show device discovery alert dialog.
     */
    public static void showDeviceDiscoveryResultDialog(final Activity activity, final String message) {
        DialogUtilities.getDeviceDiscoveryResultDialog(activity,message).show();
    }
}
