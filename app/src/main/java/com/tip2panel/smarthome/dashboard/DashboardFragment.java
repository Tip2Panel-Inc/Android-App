package com.tip2panel.smarthome.dashboard;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.engkan2kit.ava88.ZNode;
import com.engkan2kit.ava88.ZNodeValue;
import com.tip2panel.smarthome.BaseFragment;
import com.tip2panel.smarthome.R;
import com.tip2panel.smarthome.devices.DeviceListRecyclerViewAdapter;
import com.tip2panel.smarthome.devices.DevicesListAdapter;
import com.tip2panel.smarthome.gateway.GatewayContract;
import com.tip2panel.smarthome.gateway.GatewayFragment;
import com.tip2panel.smarthome.gateway.GatewayListAdapter;
import com.tip2panel.smarthome.utils.DeviceListItem;
import com.tip2panel.smarthome.utils.DialogUtilities;
import com.tip2panel.smarthome.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements DashboardContract.MvpView{
    private final static String TAG = DashboardFragment.class.getSimpleName();
    private DashboardContract.MvpPresenter mPresenter;
    private DeviceListRecyclerViewAdapter mDeviceListRecyclerViewAdapter;
    private RecyclerView mDevicesRecyclerView;
    private boolean pause =false;
    Handler handler = new Handler();
    private Runnable runnableCode=null;
    Handler gatewayCheckHandler = new Handler();
    private Runnable gatewayCheckRunnable=null;
    private View rootView;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mDeviceListRecyclerViewAdapter = new DeviceListRecyclerViewAdapter(new ArrayList<DeviceListItem>(),
                mDevicesItemListener,false);
    }

    DeviceListRecyclerViewAdapter.DeviceListItemListener mDevicesItemListener = new DeviceListRecyclerViewAdapter.DeviceListItemListener() {
        @Override
        public void onDeviceListCheckBoxChecked(String nodeId) {

        }

        @Override
        public void onDeviceListCheckBoxUnchecked(String nodeId) {

        }

        @Override
        public void onDeviceListItemClick(DeviceListItem item) {
            Log.d(TAG,"Device item Clicked!");
        }

        @Override
        public void onDeviceItemChangeValue(ZNodeValue item)
        {
            Log.d(TAG,"Device item Switch Clicked!");
            mPresenter.changeValue(item);
        }

        @Override
        public void onDeviceProductLongClick(final String deviceId, final String currentName) {
            DialogUtilities.showChangeDeviceNameDialog(getActivity(),deviceId, currentName,new DialogUtilities.ChangeDeviceNameDialogCallback() {
                @Override
                public void onChangeName(String deviceName) {
                    mPresenter.changeDeviceName(deviceId,deviceName);
                }
            });
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_dashboard, container, false);
        mDevicesRecyclerView = (RecyclerView) rootView.findViewById(R.id.devicesListRecyclerView);
        RecyclerView.ItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getContext().getDrawable(R.drawable.line_divider));
        mDevicesRecyclerView.addItemDecoration(dividerItemDecoration);
        mDevicesRecyclerView.setAdapter(mDeviceListRecyclerViewAdapter);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
        gatewayCheckRunnable = new Runnable() {
            @Override
            public void run() {
                if (!mPresenter.isGatewayConnected()){
                    mPresenter.connectToActiveGateway();
                    gatewayCheckHandler.postDelayed(gatewayCheckRunnable, 60000);
                }
            }
        };
        if(!mPresenter.isGatewayConnected()) {
            DialogUtilities.showGatewayConnectionErrorDialog(getActivity());
        }
        else {
            // Define the code block to be executed
            runnableCode = new Runnable() {
                @Override
                public void run() {
                    // Do something here on the main thread
                    mPresenter.loadDevices();
                    Log.d("Handlers", "Refresh lists");
                    // Repeat this the same runnable code block again another 2 seconds
                    handler.postDelayed(runnableCode, 5000);
                }
            };
        }

    }

    @Override
    public void showDeviceDetails(ZNode zNode) {

    }

    @Override
    public void showDevicesList(List<DeviceListItem> deviceListItems) {
        View v=rootView.findViewById(R.id.devicesListRecyclerView);
        if (v!=null){
            final List<DeviceListItem> items = new ArrayList<>();
            items.addAll(deviceListItems);

            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    mDeviceListRecyclerViewAdapter.updateDeviceListItems(items);
                    Log.d(TAG,"showing devices list");
                }
            });
        }
    }

    @Override
    public void isActive() {

    }

    @Override
    public void setPresenter(DashboardContract.MvpPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showGatewayConnected(AVA88GatewayInfo item) {
        Toast.makeText(getContext(),
                "Connected to " + item.hardwareAddress+
                        " @"+item.ipv4Address,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void showGatewayConnectionErrorDialog() {
        DialogUtilities.showGatewayConnectionErrorDialog(getActivity());
    }

    @Override
    public void onDestroy () {
        if (runnableCode!=null)
            handler.removeCallbacks(runnableCode);
        if(gatewayCheckRunnable!=null)
            gatewayCheckHandler.removeCallbacks(gatewayCheckRunnable);
        super.onDestroy ();

    }

    @Override
    public void onResume() {
        handler.post(runnableCode);
        gatewayCheckHandler.post(gatewayCheckRunnable);
        //handler.postDelayed(runnableCode,5000);
        //gatewayCheckHandler.postDelayed(gatewayCheckRunnable,60000);
        super.onResume();
    }

    @Override
    public void onPause() {
        pause = true;
        if (runnableCode!=null) {
            handler.removeCallbacks(runnableCode);
        }
        if (gatewayCheckRunnable!=null)
            gatewayCheckHandler.removeCallbacks(gatewayCheckRunnable);
        super.onPause();
    }


}
