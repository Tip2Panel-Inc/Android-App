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
import com.tip2panel.smarthome.R;
import com.tip2panel.smarthome.dashboard.DashboardActivity;
import com.tip2panel.smarthome.dashboard.DashboardContract;
import com.tip2panel.smarthome.devices.DevicesActivity;
import com.tip2panel.smarthome.devices.DevicesListAdapter;
import com.tip2panel.smarthome.gateway.GatewayActivity;
import com.tip2panel.smarthome.utils.DialogUtilities;
import com.tip2panel.smarthome.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevicesEditModeFragment extends Fragment implements DevicesEditModeContract.MvpView{
    public static final String ARGUMENT_EDIT_MODE = "location";
    private final static String TAG = DevicesEditModeFragment.class.getSimpleName();
    private DevicesEditModeContract.MvpPresenter mPresenter;
    private DevicesListAdapter mDevicesListAdapter;
    private RecyclerView mDevicesRecyclerView;
    private boolean pause =false;
    Handler handler = new Handler();
    private Runnable runnableCode=null;
    private View rootView;
    private String mLocation;

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
        mDevicesListAdapter = new DevicesListAdapter(new ArrayList<ZNode>(),
                mDevicesItemListener);
    }

    DevicesListAdapter.DeviceListListener mDevicesItemListener = new DevicesListAdapter.DeviceListListener() {
        @Override
        public void onDeviceListItemClick(ZNode item) {
            Log.d(TAG,"Device item Clicked!");
        }

        @Override
        public void onDeviceItemChangeValue(ZNode item, String mZNodeValueKey, int instance) {

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

        if(!mLocation.equals("Ungrouped")) { //allow deletion of location if not ungrouped
            Button removeLocationButton = (Button) rootView.findViewById(R.id.deleteLocationButton);
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
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mPresenter.loadDevices(mLocation);
    }

    @Override
    public void showDevicesList(List<ZNode> zNodes) {
        View v=rootView.findViewById(R.id.devicesListRecyclerView);
        if (v!=null){
            mDevicesListAdapter = new DevicesListAdapter(zNodes, mDevicesItemListener);
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    mDevicesRecyclerView.setAdapter(mDevicesListAdapter);
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
