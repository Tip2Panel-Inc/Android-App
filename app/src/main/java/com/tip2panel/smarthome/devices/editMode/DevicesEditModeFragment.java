package com.tip2panel.smarthome.devices.editMode;


import android.app.Activity;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.engkan2kit.ava88.ZNode;
import com.engkan2kit.ava88.ZNodeValue;
import com.tip2panel.smarthome.BaseFragment;
import com.tip2panel.smarthome.R;
import com.tip2panel.smarthome.dashboard.DashboardActivity;
import com.tip2panel.smarthome.dashboard.DashboardContract;
import com.tip2panel.smarthome.devices.DeviceListRecyclerViewAdapter;
import com.tip2panel.smarthome.devices.DevicesActivity;
import com.tip2panel.smarthome.devices.DevicesListAdapter;
import com.tip2panel.smarthome.gateway.GatewayActivity;
import com.tip2panel.smarthome.utils.DeviceListItem;
import com.tip2panel.smarthome.utils.DialogUtilities;
import com.tip2panel.smarthome.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevicesEditModeFragment extends Fragment implements DevicesEditModeContract.MvpView{
    public static final String ARGUMENT_EDIT_MODE = "location";
    private final static String TAG = DevicesEditModeFragment.class.getSimpleName();
    private DevicesEditModeContract.MvpPresenter mPresenter;
    private DeviceListRecyclerViewAdapter mDeviceListRecyclerViewAdapter;
    private RecyclerView mDevicesRecyclerView;
    private boolean pause =false;
    Handler handler = new Handler();
    private Runnable runnableCode=null;
    private View rootView;
    private String mLocation;
    private List<Integer> nodeIds = new ArrayList<>();
    public DevicesEditModeFragment() {
        // Required empty public constructor

    }

    public static DevicesEditModeFragment newInstance() {
        return new DevicesEditModeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mLocation=(String) getArguments().get(ARGUMENT_EDIT_MODE);
        mDeviceListRecyclerViewAdapter = new DeviceListRecyclerViewAdapter(new ArrayList<DeviceListItem>(),
                mDeviceListItemListener,true);
    }

    DeviceListRecyclerViewAdapter.DeviceListItemListener mDeviceListItemListener = new DeviceListRecyclerViewAdapter.DeviceListItemListener() {

        @Override
        public void onDeviceListCheckBoxChecked(String nodeId) {
            nodeIds.add(new Integer(nodeId));
            Log.d(TAG,"add to nodes to be modified " + new Integer(nodeId));
        }

        @Override
        public void onDeviceListCheckBoxUnchecked(String nodeId) {
            nodeIds.remove(new Integer(nodeId));
            Log.d(TAG,"removed to nodes to be modified " + new Integer(nodeId));
        }

        @Override
        public void onDeviceListItemClick(DeviceListItem item) {
            Log.d(TAG,"Device item Clicked!");
        }

        @Override
        public void onDeviceItemChangeValue(ZNodeValue item)
        {
            Log.d(TAG,"Device item Switch Clicked!");
            //mPresenter.changeValue(item);
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_devices_edit_mode, container, false);
        TextView locationTextView = (TextView) rootView.findViewById(R.id.editModeLocationTextView);
        locationTextView.setText(mLocation);
        mDevicesRecyclerView = (RecyclerView) rootView.findViewById(R.id.devicesListRecyclerView);
        RecyclerView.ItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getContext().getDrawable(R.drawable.line_divider));
        mDevicesRecyclerView.addItemDecoration(dividerItemDecoration);
        mDevicesRecyclerView.setAdapter(mDeviceListRecyclerViewAdapter);
        Button changeNodesLocationButton = (Button) rootView.findViewById(R.id.moveDeviceButton);
        changeNodesLocationButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.loadLocationListsForSelector(mLocation);

                    }
                }
        );
        Button removeLocationButton = (Button) rootView.findViewById(R.id.deleteLocationButton);
        if(!mLocation.equals("Ungrouped")) { //allow deletion of location if not ungrouped

            removeLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //show confirmation dialog
                    DialogUtilities.showRemoveLocationsDialog(getActivity(), mLocation,
                            new DialogUtilities.LocationDialogCallback() {
                                @Override
                                public void onAddLocation(String location) {

                                }

                                @Override
                                public void onDeleteLocation(String location) {
                                    mPresenter.removeLocation(location);
                                }
                            });
                }
            });
        }
        else
        {
            removeLocationButton.setVisibility(View.INVISIBLE);
        }
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mPresenter.loadDevices(mLocation);
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
    public void endEditing() {
        Intent intent = new Intent();
        intent.putExtra(ARGUMENT_EDIT_MODE,mLocation);
        getActivity().setResult(Activity.RESULT_OK,intent);
        getActivity().finish();
    }

    @Override
    public void jumpToDeviceActivity() {
        Intent intent = new Intent(getActivity(), DevicesActivity.class);
        if (intent!=null) {
            startActivity(intent);
            //getActivity().overridePendingTransition(0, 0);
            getActivity().finish();
        }
    }

    @Override
    public void showChangeLocationSelector(final List<String> locations, final String location) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                DialogUtilities.showChangeLocationsDialog(getActivity(),locations, location,
                        new DialogUtilities.ChangeLocationDialogCallback() {
                            @Override
                            public void onChangeLocationOk(String newLocation) {
                                if(newLocation.equals("Ungrouped")){
                                    newLocation = " ";
                                }
                                mPresenter.changeNodesLocation(nodeIds,newLocation);
                                Log.d(TAG,"Apply change locations!");
                            }
                        });
            }
        });

    }

    @Override
    public void setPresenter(DevicesEditModeContract.MvpPresenter presenter) {
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // show menu only when home fragment is selected
        inflater.inflate(R.menu.devices_edit_mode, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Edit mode done!
        if (id == R.id.action_devices_edit_mode_done) {
            Toast.makeText(getActivity().getApplicationContext(), "Done Editing!", Toast.LENGTH_LONG).show();
            endEditing();

        }

        return super.onOptionsItemSelected(item);
    }
}
