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
import com.tip2panel.smarthome.R;
import com.tip2panel.smarthome.devices.DevicesListAdapter;
import com.tip2panel.smarthome.gateway.GatewayContract;
import com.tip2panel.smarthome.gateway.GatewayFragment;
import com.tip2panel.smarthome.gateway.GatewayListAdapter;
import com.tip2panel.smarthome.utils.DialogUtilities;
import com.tip2panel.smarthome.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements DashboardContract.MvpView{
    private final static String TAG = DashboardFragment.class.getSimpleName();
    private DashboardContract.MvpPresenter mPresenter;
    private DevicesListAdapter mDevicesListAdapter;
    private RecyclerView mDevicesRecyclerView;
    private boolean pause =false;
    Handler handler = new Handler();
    private Runnable runnableCode=null;
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
            Log.d(TAG,"Device item Switch Clicked!");
            mPresenter.changeValue(item,mZNodeValueKey,instance);
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
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
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

    @Override
    public void showDeviceDetails(ZNode zNode) {

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
        super.onDestroy ();

    }

    @Override
    public void onResume() {
        handler.post(runnableCode);
        super.onResume();
    }

    @Override
    public void onPause() {
        pause = true;
        if (runnableCode!=null) {
            handler.removeCallbacks(runnableCode);
        }
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // show menu only when home fragment is selected
        inflater.inflate(R.menu.toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        // Add Widget selected from toolbar fragment
        if (id == R.id.action_add) {
            Toast.makeText(getActivity().getApplicationContext(), "Add Widget Selected!", Toast.LENGTH_LONG).show();
        }

        // Add Widget selected from toolbar fragment
        if (id == R.id.action_edit) {
            Toast.makeText(getActivity().getApplicationContext(), "Edit Selected!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
