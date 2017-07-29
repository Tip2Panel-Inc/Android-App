package com.tip2panel.smarthome.gateway;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.engkan2kit.ava88.AVA88GatewayInfo;
import com.tip2panel.smarthome.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AVA88GatewayInfo} and makes a call to the
 * specified {@link GatewayItemListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class GatewayListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<AVA88GatewayInfo> mValues;
    private GatewayItemListener mGatewayItemListener;
    private static final int ITEM_TYPE_OWNED = 0;
    private static final int ITEM_TYPE_DISCOVERED = 1;


        public GatewayListAdapter(List<AVA88GatewayInfo> items, GatewayItemListener listener) {
        this.mValues = items;
        this.mGatewayItemListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int itemType) {
        View view;
        switch (itemType){
            case ITEM_TYPE_OWNED:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_gateway_owned_item, parent, false);
                return new OwnedGatewayViewHolder(view);
            case ITEM_TYPE_DISCOVERED:
                view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_gateway_available_item, parent, false);
                return new AvailableGatewayViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder  holder, int position) {
        AVA88GatewayInfo item = mValues.get(position);

        Log.d("GATEWAYListItem","MAC: "+mValues.get(position).hardwareAddress);
        Log.d("GATEWAYListItem","NAME: "+mValues.get(position).deviceName);
        Log.d("GATEWAYListItem","IP: "+mValues.get(position).ipv4Address);

        switch (getItemViewType(position)){
            case ITEM_TYPE_OWNED:
                ((OwnedGatewayViewHolder) holder).bind(item,mGatewayItemListener);
                break;
            case ITEM_TYPE_DISCOVERED:
                ((AvailableGatewayViewHolder) holder).bind(item,mGatewayItemListener);
                break;
        }
    }


    public class GatewayHeaderViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mLabelView;


        public GatewayHeaderViewHolder(View view) {
            super(view);
            mView = view;
            mLabelView = (TextView) view.findViewById(R.id.headerLabelTextView);

        }

        public void bind(final String label){
            mLabelView.setText(label);

        }


        @Override
        public String toString() {
            return super.toString() + " '" + mLabelView.getText() + "'";
        }
    }

    public class OwnedGatewayViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final ImageView mStatusImageView;
        public final TextView mMACView;

        public OwnedGatewayViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.gatewayNameTextView);
            mMACView = (TextView) view.findViewById(R.id.gatewayMacTextView);
            mStatusImageView = (ImageView) view.findViewById(R.id.gatewayStatusImageView);
        }

        public void bind(final AVA88GatewayInfo item, final GatewayItemListener listener){
            if (item != null) {
                String name = item.deviceName;
                if (name==null || name.length() ==0)
                    name=item.deviceModel;
                mNameView.setText(name);
                mMACView.setText(item.hardwareAddress);
                //Set icon for connected gateway
                mStatusImageView.setImageResource(R.drawable.ic_radio_button_checked);
                if(item.active)
                    mStatusImageView.setVisibility(View.VISIBLE);
                else
                    mStatusImageView.setVisibility(View.INVISIBLE);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v){
                        listener.onGatewayClick(item);
                    }
                });
            }
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mMACView.getText() + "'";
        }
    }

    public class AvailableGatewayViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mIPView;
        public final TextView mMACView;

        public AvailableGatewayViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.gatewayNameTextView);
            mMACView = (TextView) view.findViewById(R.id.gatewayMacTextView);
            mIPView = (TextView) view.findViewById(R.id.gatewayIPTextView);
        }

        public void bind(final AVA88GatewayInfo item, final GatewayItemListener listener){
            if (item != null) {
                String name = item.deviceName;
                if (name==null || name.length()==0)
                    name=item.deviceModel;
                mNameView.setText(name);
                mMACView.setText(item.hardwareAddress);
                mIPView.setText(item.ipv4Address);
                //TODO:Set icon and status
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v){
                        listener.onGatewayClick(item);
                    }
                });
            }
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIPView.getText() + "'";
        }
    }

    @Override
    public int getItemCount() {
        if (mValues == null)
            return 0;
        return mValues.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mValues != null) {
            AVA88GatewayInfo object = mValues.get(position);
            if (object != null) {
                if (object.isOwned){
                    return ITEM_TYPE_OWNED;
                }
                else{
                    return ITEM_TYPE_DISCOVERED;
                }
            }
        }
        return 0;
    }

    public interface GatewayItemListener {
        // TODO: Update argument type and name
        void onGatewayClick(AVA88GatewayInfo item);

    }


}
