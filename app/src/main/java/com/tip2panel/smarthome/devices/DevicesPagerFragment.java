package com.tip2panel.smarthome.devices;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.engkan2kit.ava88.ZNode;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.tip2panel.smarthome.BaseFragment;
import com.tip2panel.smarthome.R;
import com.tip2panel.smarthome.utils.DialogUtilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Setsuna F. Seie on 13/07/2017.
 */

public class DevicesPagerFragment extends Fragment implements DevicesPagerContract.MvpView,
        DevicesViewsContract.ParentView{

    private final static String TAG = DevicesPagerFragment.class.getSimpleName();
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_PAGE = "page";
    private static final String ARG_TABS = "tabs";

    private DevicesPagerContract.MvpPresenter mPresenter;
    private DevicesViewsContract.ChildView mChildView;
    private Map<String,DevicesViewsContract.ChildView> mChildViews= new HashMap<>();
    private View rootFragmentView;
    private ViewPager viewPager;
    private SmartTabLayout viewPagerTab;
    private FragmentManager mChildFragmentManager;
    public DevicesPagerFragment(){

    }

    public static DevicesPagerFragment newInstance() {
        return new DevicesPagerFragment();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        rootFragmentView = inflater.inflate(R.layout.fragment_pager_devices, container, false);
        FragmentPagerItems pages = new FragmentPagerItems(getContext());
        viewPager = (ViewPager)rootFragmentView.findViewById(R.id.viewpager);
        viewPagerTab =
                (SmartTabLayout) rootFragmentView.findViewById(R.id.viewPagerTab);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled ( int position, float positionOffset, int positionOffsetPixels){
            }

            @Override
            public void onPageSelected ( int position){
                if (position == 0)
                    ((DevicesActivity) getActivity()).
                            setTitle(getString(R.string.devices)
                                    + "(" + getString(R.string.orphan) + ")");
                else {
                    ((DevicesActivity) getActivity()).setTitle(getString(R.string.devices));
                }
                Log.d("PAGER", " " + position);
            }

            @Override
            public void onPageScrollStateChanged ( int state){

            }
        });
        return rootFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(!mPresenter.isGatewayConnected()) {
            DialogUtilities.showGatewayConnectionErrorDialog(getActivity());
        }
        else {
            Log.d(TAG, "Activity Created: Create Devices Pager");
            mPresenter.loadPages();
        }
    }



    @Override
    public void showPages(List<String> pageTitles) {
        FragmentPagerItems pages = new FragmentPagerItems(getContext());
        int i=0;
        for (String pageTitle : pageTitles) {
            Bundle mBundle = new Bundle();
            mBundle.putString("location", pageTitle);
            Log.d(TAG,"Added Page "+pageTitle);
            pages.add(FragmentPagerItem.of(pageTitle, DevicesFragment.class,mBundle));
            i=i+1;
        }
        mChildFragmentManager = checkNotNull(getChildFragmentManager());
        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                mChildFragmentManager,pages);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewPager.setAdapter(adapter);
                viewPagerTab.setViewPager(viewPager);
            }


        });
    }

    @Override
    public void showDeviceDetails(ZNode zNode) {

    }

    @Override
    public void showDevicesList(List<ZNode> zNodes, String location) {
        DevicesViewsContract.ChildView childView = mChildViews.get(location);
        mChildFragmentManager = checkNotNull(getChildFragmentManager());
        Log.d(TAG,"Number of fragments = " +mChildFragmentManager.getFragments().size());
        if(childView!=null) {
            childView.showDevicesList(zNodes);
        }
    }

    @Override
    public void isActive() {

    }

    @Override
    public void showLocationAddConflictDialog(final String location) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogUtilities.showLocationsConflictDialog(getActivity(),location);
            }


        });

    }

    @Override
    public void setPresenter(@NonNull DevicesPagerContract.MvpPresenter presenter) {
        Log.d(TAG,"Presenter set!");
        mPresenter=checkNotNull(presenter);
    }

    @Override
    public void showGatewayConnected(AVA88GatewayInfo ava88GatewayInfo) {
        Toast.makeText(getContext(),
                "Connected to " + ava88GatewayInfo.hardwareAddress+
                        " @"+ava88GatewayInfo.ipv4Address,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void showGatewayConnectionErrorDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogUtilities.showGatewayConnectionErrorDialog(getActivity());
            }


        });
        DialogUtilities.showGatewayConnectionErrorDialog(getActivity());
    }

    public void setPage(int index){
        viewPager.setCurrentItem(index,true);

    }

    @Override
    public void loadDevicesList(String location) {
        mPresenter.loadDevices(location);
    }

    @Override
    public void loadDeviceDetails(String id) {

    }

    @Override
    public void changeValue(ZNode zNode, String zNodeValueKey, int instance) {
        mPresenter.changeValue(zNode,zNodeValueKey,instance);
    }

    @Override
    public void changeDeviceName(ZNode zNode, String name) {

    }

    @Override
    public void changeDeviceLocation(ZNode zNode, String location) {

    }

    @Override
    public void setChildView(String location, DevicesViewsContract.ChildView childView) {
        mChildViews.put(checkNotNull(location),checkNotNull(childView));
        Log.d(TAG,"Added Child Fragment "+location);
    }

    @Override
    public void addLocation(String location) {
        mPresenter.addLocation(location);
    }


}
