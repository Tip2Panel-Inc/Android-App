package com.tip2panel.smarthome.devices;


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
import android.widget.Toast;

import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.engkan2kit.ava88.ZNode;
import com.tip2panel.smarthome.R;
import com.tip2panel.smarthome.devices.editMode.DevicesEditModeActivity;
import com.tip2panel.smarthome.devices.editMode.DevicesEditModeFragment;
import com.tip2panel.smarthome.utils.DialogUtilities;
import com.tip2panel.smarthome.utils.DividerItemDecoration;

import java.util.List;

/**
 * Created by Setsuna F. Seie on 11/07/2017.
 */

/**
 * Displays a list of {@link AVA88GatewayInfo}. User can click on the gateway
 * to connect or see detail.
 */
public class DevicesFragment extends Fragment implements DevicesViewsContract.ChildView {
    private final static String TAG = DevicesFragment.class.getSimpleName();
    private final static String LOCATION = "location";
    private DevicesViewsContract.ParentView mParentView;
    private DevicesListAdapter mDeviceListAdapter;
    private RecyclerView mDevicesRecyclerview;
    private View rootView;
    private String mLocation=" ";
    private boolean pause =false;
    Handler handler = new Handler();
    private Runnable runnableCode=null;
    public DevicesFragment(){

    }

    public static DevicesFragment newInstance() {
        return new DevicesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocation=getArguments().getString(LOCATION);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_devices, container, false);
        mDevicesRecyclerview = (RecyclerView) rootView.findViewById(R.id.devicesListRecyclerView);
        RecyclerView.ItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getContext().getDrawable(R.drawable.line_divider));
        //set dividers for items
        mDevicesRecyclerview.addItemDecoration(dividerItemDecoration);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"Page Created: Create devices location:"+mLocation);
        //onAttachToParentView(getParentFragment());
        // Define the code block to be executed
        runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                onAttachToParentView(getParentFragment());
                Log.d("Handlers", "Called on Page "+mLocation);
                // Repeat this the same runnable code block again another 2 seconds
                handler.postDelayed(runnableCode, 5000);
            }
        };
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // show menu only when home fragment is selected
        inflater.inflate(R.menu.toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //mPresenter.result(requestCode, resultCode);
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
            //Show add New locations Dialog
            DialogUtilities.showAddLocationDialog(getActivity(), new DialogUtilities.LocationDialogCallback() {
                @Override
                public void onAddLocation(String location) {
                    mParentView.addLocation(location);
                }

                @Override
                public void onDeleteLocation(String location) {

                }
            });
        }

        // Add Widget selected from toolbar fragment
        if (id == R.id.action_edit) {
            Intent intent = new Intent(getActivity(), DevicesEditModeActivity.class);
            intent.putExtra(DevicesEditModeFragment.ARGUMENT_EDIT_MODE,mLocation);
            startActivityForResult(intent, DevicesEditModeActivity.REQUEST_EDIT_MODE);
        }



        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAttachToParentView(Fragment fragment) {
        try{
            mParentView = (DevicesViewsContract.ParentView) fragment;
            mParentView.setChildView(mLocation,this);
            mParentView.loadDevicesList(mLocation);
        }catch(ClassCastException e){
            throw new ClassCastException(
                    fragment.toString() + " must implement DevicesViewsContract.ParentView"
            );
        }
    }


    @Override
    public void showDeviceDetails(ZNode zNode) {

    }

    @Override
    public void showDevicesList(List<ZNode> zNodes) {
        View v=rootView.findViewById(R.id.devicesListRecyclerView);
        if (v!=null){
            mDeviceListAdapter = new DevicesListAdapter(zNodes, mDevicesItemListener);
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    mDevicesRecyclerview.setAdapter(mDeviceListAdapter);
                    Log.d(TAG,"showing devices list");
                }
            });

        }
    }


    DevicesListAdapter.DeviceListListener mDevicesItemListener = new DevicesListAdapter.DeviceListListener() {
        @Override
        public void onDeviceListCheckBoxChecked(int nodeId) {

        }

        @Override
        public void onDeviceListCheckBoxUnchecked(int nodeId) {

        }

        @Override
        public void onDeviceListItemClick(ZNode item) {
            Log.d(TAG,"Device item Clicked!");
        }

        @Override
        public void onDeviceItemChangeValue(ZNode item, String mZNodeValueKey, int instance) {
            Log.d(TAG,"Device item Switch Clicked!");
            mParentView.changeValue(item,mZNodeValueKey,instance);
        }

    };


    @Override
    public void onDestroy () {
        Log.d("Handlers", "onDestroy on Page "+mLocation);
        if (runnableCode!=null)
            handler.removeCallbacks(runnableCode);
        super.onDestroy ();

    }

    @Override
    public void onResume() {
        Log.d("Handlers", "onResume on Page "+mLocation);
        handler.post(runnableCode);
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("Handlers", "onPause on Page "+mLocation);
        pause = true;
        if (runnableCode!=null) {
            handler.removeCallbacks(runnableCode);
        }
        super.onPause();
    }


}
