package com.tip2panel.smarthome.gateway;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ConnectivityPredicate;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.tip2panel.smarthome.BaseFragment;
import com.tip2panel.smarthome.R;
import com.tip2panel.smarthome.utils.DialogUtilities;
import com.tip2panel.smarthome.utils.DividerItemDecoration;
import com.tip2panel.smarthome.utils.NetworkUtilities;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by Setsuna F. Seie on 11/07/2017.
 */

/**
 * Displays a list of {@link com.engkan2kit.ava88.AVA88GatewayInfo}. User can click on the gateway
 * to connect or see detail.
 */
public class GatewayFragment extends BaseFragment implements GatewayContract.MvpView{
    private final static String TAG = GatewayFragment.class.getSimpleName();
    private GatewayContract.MvpPresenter mPresenter;
    private GatewayListAdapter mOwnedGatewaysListAdapter;
    private GatewayListAdapter mAvailableGatewaysListAdapter;
    private RecyclerView mOwnedGatewaysRecyclerview;
    private RecyclerView mAvailableGatewaysRecyclerview;
    private View root;
    public GatewayFragment(){

    }

    public static GatewayFragment newInstance() {
        return new GatewayFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOwnedGatewaysListAdapter = new GatewayListAdapter(new ArrayList<AVA88GatewayInfo>(),
                mGatewayItemListener);
        mAvailableGatewaysListAdapter = new GatewayListAdapter(new ArrayList<AVA88GatewayInfo>(),
                mGatewayItemListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        root = inflater.inflate(R.layout.fragment_gateway, container, false);
        mOwnedGatewaysRecyclerview = (RecyclerView) root.findViewById(R.id.gateway_owned_list);
        mAvailableGatewaysRecyclerview = (RecyclerView) root.findViewById(R.id.gateway_available_list);
        RecyclerView.ItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getContext().getDrawable(R.drawable.line_divider));
        //set dividers for items
        mOwnedGatewaysRecyclerview.addItemDecoration(dividerItemDecoration);
        mAvailableGatewaysRecyclerview.addItemDecoration(dividerItemDecoration);

        //map button for rescanning
        final Button button = (Button) root.findViewById(R.id.discoverGatewayButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG,"Button Clicked!");
                mPresenter.getAvailableGateways(getContext());
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"Activity Created: Create gateway discovery");
        mPresenter.getOwnedGateways(getContext());
        mPresenter.getAvailableGateways(getContext());
        mPresenter.connectToActiveGateway();

    }



    @Override
    public void showConnectionSuccess() {

    }

    @Override
    public void showConnectionFail() {

    }

    @Override
    public void showAvailableGateways(List<AVA88GatewayInfo> gatewayIPs) {
        View v=getView().findViewById(R.id.gateway_available_list);
        if (v!=null){
            mAvailableGatewaysListAdapter = new GatewayListAdapter( gatewayIPs, mGatewayItemListener);
            mAvailableGatewaysRecyclerview.setAdapter(mAvailableGatewaysListAdapter);
            Log.d(TAG,"Updating Available list");
        }
        //TODO:Stop spinner here
    }

    @Override
    public void showOwnedGateways(List<AVA88GatewayInfo> gatewayIPs) {
        View v=getView().findViewById(R.id.gateway_owned_list);
        if (v!=null){
            mOwnedGatewaysListAdapter = new GatewayListAdapter( gatewayIPs, mGatewayItemListener);
            mOwnedGatewaysRecyclerview.setAdapter(mOwnedGatewaysListAdapter);
            Log.d(TAG,"Updating Owned list");
        }
        //TODO:Stop spinner here
    }

    @Override
    public void showNoAvailableGateway(boolean show) {

    }

    @Override
    public void ShowNoOwnedGateway(boolean show) {

    }

    @Override
    public void showGatewayConnected(AVA88GatewayInfo item) {
        Toast.makeText(getContext(),
                "Connected to " + item.hardwareAddress+
                        " @"+item.ipv4Address,
                Toast.LENGTH_LONG).show();
    }


    @Override
    public void isActive() {

    }

    @Override
    public void showGatewayDetails() {

    }

    @Override
    public void setPresenter(@NonNull GatewayContract.MvpPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showGatewayConnectionErrorDialog() {
        DialogUtilities.showGatewayConnectionErrorDialog(getActivity());
    }


    /**
     * Listener for clicks in the list of gateways.
     */
    GatewayListAdapter.GatewayItemListener mGatewayItemListener=
            new GatewayListAdapter.GatewayItemListener() {
                @Override
                public void onGatewayClick(AVA88GatewayInfo item) {
                    //TODO: Connect to this gateway info
                    mPresenter.connectGateway(item);
                }

            };
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */


}
