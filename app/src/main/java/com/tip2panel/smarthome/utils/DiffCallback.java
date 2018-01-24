package com.tip2panel.smarthome.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.engkan2kit.ava88.ZNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Setsuna F. Seie on 24/01/2018.
 */

public abstract class DiffCallback<ZN extends ZNode> extends DiffUtil.Callback {
    protected final List<ZN> mOldItems = new ArrayList<>();
    protected final List<ZN> mNewItems = new ArrayList<>();

    public void setItems(@NonNull final List<ZN> oldItems, @NonNull final List<ZN> newItems) {
        mOldItems.clear();
        mOldItems.addAll(oldItems);

        mNewItems.clear();
        mNewItems.addAll(newItems);
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
        return areItemsTheSame(
                mOldItems.get(oldItemPosition),
                mNewItems.get(newItemPosition)
        );
    }

    public abstract boolean areItemsTheSame(@NonNull final ZN oldItem, @NonNull final ZN newItem);

    @Override
    public boolean areContentsTheSame(final int oldItemPosition, final int newItemPosition) {
        return areContentsTheSame(
                mOldItems.get(oldItemPosition),
                mNewItems.get(newItemPosition)
        );
    }

    public abstract boolean areContentsTheSame(@NonNull final ZN oldItem, @NonNull final ZN newItem);

    @Nullable
    @Override
    public Object getChangePayload(final int oldItemPosition, final int newItemPosition) {
        final Object changePayload = getChangePayload(mOldItems.get(oldItemPosition), mNewItems.get(newItemPosition));
        if (changePayload == null) {
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
        return changePayload;
    }

    @Nullable
    public Object getChangePayload(@NonNull final ZN oldItem, @NonNull final ZN newItem) {
        return null;
    }
}
