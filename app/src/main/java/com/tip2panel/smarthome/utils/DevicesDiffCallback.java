package com.tip2panel.smarthome.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.util.Log;

import com.engkan2kit.ava88.ZNode;
import com.engkan2kit.ava88.ZNodeValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Setsuna F. Seie on 24/01/2018.
 */

public class DevicesDiffCallback extends DiffUtil.Callback {
    protected final List<DeviceListItem> mOldItems;
    protected final List<DeviceListItem> mNewItems;


    public DevicesDiffCallback(@NonNull List<DeviceListItem> oldItems, @NonNull List<DeviceListItem> newItems) {
        this.mOldItems=oldItems;
        this.mNewItems=newItems;
    }

    @Override
    public int getOldListSize() {
        return mOldItems.size();
    }

    @Override
    public int getNewListSize() {
        return mNewItems.size();
    }

    @Override
    public boolean areItemsTheSame(final int oldItemPosition, final int newItemPosition) {
        //compare items mOldItems.get(oldItemPosition) and mNewItems.get(newItemPosition)
        return mOldItems.get(oldItemPosition).getId().equals(mNewItems.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(final int oldItemPosition, final int newItemPosition) {
        //compare item contents mOldItems.get(oldItemPosition) and mNewItems.get(newItemPosition)
        int falseCounter =0;
        ZNodeValue oldZNodeValue = mOldItems.get(oldItemPosition).getZNodeValue();
        ZNodeValue newZNodeValue = mNewItems.get(newItemPosition).getZNodeValue();
        Log.d("DIFF", "Items the same "+oldZNodeValue.getNodeId()+ " "+newZNodeValue.getNodeId());
        //Labels are not the same
        if (!oldZNodeValue.getValueLabel().equals(newZNodeValue.getValueLabel()))
        {
            falseCounter++;
            Log.d("DIFF","Items not the same label "+oldZNodeValue.getValueLabel()+ " and "+newZNodeValue.getValueLabel());
        }
        //values not the same
        if (!oldZNodeValue.getValue().equals(newZNodeValue.getValue()))
        {
            falseCounter++;
            Log.d("DIFF","Items not the same values "+oldZNodeValue.getValue()+ " and "+newZNodeValue.getValue());
        }
        //values not the same index
        if (oldZNodeValue.getValueIndex()!=newZNodeValue.getValueIndex())
        {
            falseCounter++;
            Log.d("DIFF","Items not the same indices");
        }
        if (oldZNodeValue.getInstance()!=newZNodeValue.getInstance())
        {
            falseCounter++;
            Log.d("DIFF","Items not the same instances");
        }

        return falseCounter>0?false:true; //if there is one mismatch above, contents not the same
    }


    @Nullable
    @Override
    public Object getChangePayload(final int oldItemPosition, final int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);

    }


}
