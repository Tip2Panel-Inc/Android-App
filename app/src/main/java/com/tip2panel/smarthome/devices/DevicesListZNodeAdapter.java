package com.tip2panel.smarthome.devices;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.engkan2kit.ava88.ZNode;
import com.engkan2kit.ava88.ZNodeProduct;
import com.engkan2kit.ava88.ZNodeValue;
import com.google.android.flexbox.FlexboxLayout;
import com.tip2panel.smarthome.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import yuku.ambilwarna.AmbilWarnaDialog;

import static java.lang.Math.min;

/**
 * Created by Setsuna F. Seie on 13/07/2017.
 */

public class DevicesListZNodeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG=DevicesListZNodeAdapter.class.getSimpleName();
    @NonNull
    private final ArrayList<ZNode> mList;
    private boolean mShowCheckBox=false;
    public interface DeviceListListener{
        void onDeviceListCheckBoxChecked(int nodeId);
        void onDeviceListCheckBoxUnchecked(int nodeId);
        void onDeviceListItemClick(ZNode item);
        void onDeviceItemChangeValue(ZNode item, String mZNodeValueKey, int instance);

    }

    private final DeviceListListener mListener;

    public DevicesListZNodeAdapter(ArrayList<ZNode> list, DeviceListListener listener) {
        this(list, listener,false);
    }

    public DevicesListZNodeAdapter(ArrayList<ZNode> list, DeviceListListener listener, boolean showCheckBox) {
        this.mList = list;
        this.mListener = listener;
        this.mShowCheckBox=showCheckBox;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int DeviceType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.devices_item, parent, false);
        return new DeviceViewHolder(view,mShowCheckBox);

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ZNode object = mList.get(position);
        ((DeviceViewHolder) holder).bind(object,mListener);

    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }
    @Override
    public int getItemViewType(int position) {
        if (mList != null) {
            ZNode object = mList.get(position);
            if (object != null) {
                return object.nodeProduct;
            }
        }
        return 0;
    }

    public void setItems(@NonNull final ArrayList<ZNode> items)
    {
        mList.clear();
        mList.addAll(items);
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        private FlexboxLayout mFlexboxLayout;
        private CheckBox mDeviceCheckBox;
        private TextView mDeviceName;
        private Context mContext;
        private boolean mShowCheckBox=false;

        public DeviceViewHolder(View itemView) {
            this(itemView,false);
        }

        public DeviceViewHolder(View itemView, boolean showCheckBox) {
            super(itemView);
            mDeviceCheckBox = (CheckBox) itemView.findViewById(R.id.deviceCheckBox);
            mDeviceName = (TextView) itemView.findViewById(R.id.deviceNameTextView);
            mContext = itemView.getContext();
            mShowCheckBox=showCheckBox;
        }

        public void bind(final ZNode item,final DeviceListListener listener){
            if (item != null) {
                String name = item.nodeName;
                if (name==null || name.length()==0)
                    if (item.nodeProductStr.length()>15)
                        name=item.nodeProductStr.substring(0,14);
                    else
                        name=item.nodeProductStr;
                name = name + " ("+item.nodeStatusString+")";
                mDeviceName.setText(name);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v){
                        listener.onDeviceListItemClick(item);
                    }
                });

                if(mShowCheckBox) {
                    mDeviceCheckBox.setVisibility(View.VISIBLE);
                    mDeviceCheckBox.setOnCheckedChangeListener(
                            new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            if(isChecked){
                                listener.onDeviceListCheckBoxChecked(item.nodeID);
                            }
                            else{
                                listener.onDeviceListCheckBoxUnchecked(item.nodeID);
                            }
                        }
                    });
                }
                else {
                    mDeviceCheckBox.setVisibility(View.GONE);
                }

            }
        }
    }

}
