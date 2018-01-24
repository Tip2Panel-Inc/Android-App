package com.tip2panel.smarthome.utils;

import android.support.annotation.NonNull;

import com.engkan2kit.ava88.ZNode;

/**
 * Created by Setsuna F. Seie on 24/01/2018.
 */

public class DiffCallbackZNode <ZN extends ZNode> extends DiffCallback<ZN> {

    @Override
    public boolean areItemsTheSame(@NonNull final ZN oldItem, @NonNull final ZN newItem) {
        return oldItem.nodeID == newItem.nodeID;
    }

    @Override
    public boolean areContentsTheSame(@NonNull final ZN oldItem, @NonNull final ZN newItem) {

        return true;
    }
}
