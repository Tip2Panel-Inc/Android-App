package com.tip2panel.smarthome.discovery;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.engkan2kit.ava88.ZNode;
import com.tip2panel.smarthome.R;
import com.tip2panel.smarthome.gateway.GatewayContract;
import com.tip2panel.smarthome.gateway.GatewayListAdapter;
import com.tip2panel.smarthome.utils.DialogUtilities;
import com.tip2panel.smarthome.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by Setsuna F. Seie on 11/07/2017.
 */

/**
 * Displays a list of {@link AVA88GatewayInfo}. User can click on the gateway
 * to connect or see detail.
 */
public class DiscoveryFragment extends Fragment implements DiscoveryContract.MvpView{
    private final static String TAG = DiscoveryFragment.class.getSimpleName();
    private DiscoveryContract.MvpPresenter mPresenter;
    private View root;
    private boolean fabStarted=false;
    Handler handler = new Handler();
    private Runnable runnableCode=null;
    public DiscoveryFragment(){

    }

    public static DiscoveryFragment newInstance() {
        return new DiscoveryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private FloatingActionButton inclusionFab;
    private TextView inclusionFabTextView;
    RadioGroup radioGroup;
    private RadioButton inclusionRadionButton;
    private RadioButton exclusionRadionButton;
    ProgressBar progressBar;
    DiscoveryCounter mDiscoveryCounter=null;
    private boolean inclusionMode=true;
    private boolean busy=false;
    private boolean progressBarShown=false;
    private boolean resultShown=true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        root = inflater.inflate(R.layout.fragment_device_discovery, container, false);

        //map button for rescanning
        inclusionFab = (FloatingActionButton) root.findViewById(R.id.inclusionFab);
        inclusionFabTextView = (TextView) root.findViewById(R.id.inclusionFabTextView);
        radioGroup = (RadioGroup) root.findViewById(R.id.radioGroup);
        inclusionRadionButton = (RadioButton) root.findViewById(R.id.inclusionModeRadioButton);
        exclusionRadionButton = (RadioButton) root.findViewById(R.id.exclusionModeRadioButton);
        progressBar = (ProgressBar)root.findViewById(R.id.progressBar);
        inclusionRadionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onModeRadioButtonClicked(v);
            }
        });
        exclusionRadionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onModeRadioButtonClicked(v);
            }
        });
        inclusionFabTextView.setText(getString(R.string.start));
        inclusionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startClicked();
            }
        });
        setInclusionMode(true);
        return root;
    }

    private void startClicked(){
        if(inclusionMode){
            if (busy)
                mPresenter.cancelAction();
            else
                mPresenter.startInclusion();
        }
        else{
            if (busy)
                mPresenter.cancelAction();
            else
                mPresenter.startExclusion();
        }
    }

    public void onModeRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.inclusionModeRadioButton:
                if (checked) {
                    inclusionMode = true;

                    Log.d("Radio", "inclusion");
                }
                break;
            case R.id.exclusionModeRadioButton:
                if (checked) {
                    inclusionMode = false;
                    Log.d("Radio", "exclusion");
                }
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"Activity Created: Create device discovery");
        runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                mPresenter.checkLatestLog();
                Log.d("Handlers", "Refresh lists");
                // Repeat this the same runnable code block again another 2 seconds
                handler.postDelayed(runnableCode, 2000);
            }
        };

    }


    @Override
    public void showGatewayConnected(final AVA88GatewayInfo item) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getContext(),
                        "Connected to " + item.hardwareAddress+
                                " @"+item.ipv4Address,
                        Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void setPresenter(@NonNull DiscoveryContract.MvpPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showGatewayConnectionErrorDialog() {
        DialogUtilities.showGatewayConnectionErrorDialog(getActivity());
    }


    @Override
    public void setFabTextCancel() {
        busy=true;
        resultShown = false;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                inclusionFabTextView.setText(getString(R.string.cancel));
            }
        });
    }

    @Override
    public void setFabTextStart() {
        busy=false;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                inclusionFabTextView.setText(getString(R.string.start));
            }
        });
    }

    public class DiscoveryCounter extends CountDownTimer {
        private int maxProgress;
        public DiscoveryCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.maxProgress=(int)millisInFuture/1000;
            progressBar.setMax(maxProgress);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) (millisUntilFinished/1000);
            progressBar.setProgress(maxProgress-progress);
        }

        @Override
        public void onFinish() {
            progressBar.setProgress(maxProgress);
        }

    }

    @Override
    public void showProgressbar(final boolean show) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(show && !progressBarShown){
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(0);
                    mDiscoveryCounter = new DiscoveryCounter(32000, 2000);
                    mDiscoveryCounter.start();
                    progressBarShown=true;
                }
                else if(!show){
                    if (mDiscoveryCounter!=null) {
                        progressBar.setVisibility(View.GONE);
                        mDiscoveryCounter.cancel();
                        mDiscoveryCounter=null;
                        progressBarShown = false;
                    }
                }
            }
        });
    }

    @Override
    public void enableRadioGroup(final boolean enable) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                inclusionRadionButton.setEnabled(enable);
                exclusionRadionButton.setEnabled(enable);
            }
        });
    }


    @Override
    public void showResultDialog(@Nullable final int action, @Nullable final ZNode zNode) {
        if(!resultShown) {
            resultShown = true;
            String message;
            switch (action) {
                case 1: //canceled adding
                    message = getString(R.string.msg_device_inclusion_canceled);
                    break;
                case 2: //canceled removing
                    message = getString(R.string.msg_device_exclusion_canceled);
                    break;
                case 20: //canceled force adding
                    message = getString(R.string.msg_device_inclusion_canceled);
                    break;
                case 101: //success adding
                    message = getString(R.string.msg_device_inclusion_success, zNode.nodeProductStr, zNode.nodeID);
                    break;
                case 102://success removing
                    message = getString(R.string.msg_device_exclusion_success, zNode.nodeID);
                    break;
                case 120://success force adding
                    message = getString(R.string.msg_device_inclusion_success, zNode.nodeProductStr, zNode.nodeID);
                    break;
                default: //others/fail
                    message = getString(R.string.msg_device_discovery_error);
            }
            final String dialogMessage = message;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    DialogUtilities.showDeviceDiscoveryResultDialog(getActivity(), dialogMessage);
                }
            });
        }

    }

    @Override
    public void setInclusionMode(final boolean mode) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mode)
                    inclusionRadionButton.setChecked(true);
                else
                    exclusionRadionButton.setChecked(true);
            }
        });

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
        if (runnableCode!=null) {
            handler.removeCallbacks(runnableCode);
        }
        super.onPause();
    }
}
