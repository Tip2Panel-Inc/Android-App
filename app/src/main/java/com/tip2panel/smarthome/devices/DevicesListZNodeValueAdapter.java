package com.tip2panel.smarthome.devices;

import android.content.Context;
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

import java.util.List;
import java.util.Map;

import yuku.ambilwarna.AmbilWarnaDialog;

import static java.lang.Math.min;

/**
 * Created by Setsuna F. Seie on 13/07/2017.
 */

public class DevicesListZNodeValueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG=DevicesListZNodeValueAdapter.class.getSimpleName();
    private List<ZNodeValue> mList;
    public interface DeviceListZNodeValueListener{
        void onDeviceListItemClick(ZNodeValue item);
        void onDeviceItemChangeValue(ZNodeValue item, String mZNodeValueKey, int instance);

    }

    private final DeviceListZNodeValueListener mListener;

    public DevicesListZNodeValueAdapter(List<ZNodeValue> list, DeviceListZNodeValueListener listener) {
        this.mList = list;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int DeviceType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.devices_item, parent, false);
        return new DeviceViewHolder(view);

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ZNodeValue object = mList.get(position);
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
            ZNodeValue object = mList.get(position);
            if (object != null) {
                return object.getValueIndex();
            }
        }
        return 0;
    }


    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        private FlexboxLayout mFlexboxLayout;
        private CheckBox mDeviceCheckBox;
        private TextView mDeviceName;
        private Context mContext;
        private boolean mShowCheckBox=false;


        public DeviceViewHolder(View itemView) {
            super(itemView);
            mDeviceName = (TextView) itemView.findViewById(R.id.deviceNameTextView);
            mFlexboxLayout = (FlexboxLayout) itemView.findViewById(R.id.deviceValueFlexboxLayout);
            mContext = itemView.getContext();

        }

        public void bind(final ZNodeValue item,final DeviceListZNodeValueListener listener){
            if (item != null) {

            }
        }
    }

}
