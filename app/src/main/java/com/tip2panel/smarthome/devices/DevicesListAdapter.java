package com.tip2panel.smarthome.devices;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.tip2panel.smarthome.R;

import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

import static java.lang.Math.min;

/**
 * Created by Setsuna F. Seie on 13/07/2017.
 */

public class DevicesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG=DevicesListAdapter.class.getSimpleName();
    private List<ZNode> mList;
    public interface DeviceListListener{
        void onDeviceListItemClick (ZNode item);
        void onDeviceItemChangeValue(ZNode item, String mZNodeValueKey, int instance);

    }

    private final DeviceListListener mListener;

    public DevicesListAdapter(List<ZNode> list,DeviceListListener listener) {
        this.mList = list;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int DeviceType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.devices_list_item, parent, false);
        return new DeviceViewHolder(view);

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


    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLinearLayout;
        private CheckBox mDeviceCheckBox;
        private TextView mDeviceName;
        private Context mContext;

        public DeviceViewHolder(View itemView) {
            super(itemView);
            mDeviceCheckBox = (CheckBox) itemView.findViewById(R.id.deviceCheckBox);
            mDeviceCheckBox.setVisibility(View.INVISIBLE);
            mDeviceName = (TextView) itemView.findViewById(R.id.deviceNameTextView);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.itemCommandsLinearLayout);
            mContext = itemView.getContext();
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
                switch(item.nodeProduct) {
                    case ZNodeProduct.APPLIANCEONOFF:
                        Switch mDeviceSwitch = new Switch(itemView.getContext());
                        mDeviceSwitch.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));

                        //The Key for Default value for APPLIANCEONOFF is SWITCH BINARY0
                        item.defaultValueKey="SWITCH BINARY0";
                        item.defaultInstance=1;
                        ZNodeValue zNodeValue= item.getZNodeValue(item.defaultValueKey);
                        Log.d(TAG, "APPLIANCE ON/OFF");
                        if (zNodeValue.getValue(item.defaultInstance).equalsIgnoreCase("True"))
                            mDeviceSwitch.setChecked(true);
                        else
                            mDeviceSwitch.setChecked(false);

                        mDeviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                ZNodeValue zNodeValue= item.getZNodeValue(item.defaultValueKey);
                                String val = "False";
                                if (isChecked)
                                    val = "True";
                                zNodeValue.setValue(val, item.defaultInstance);
                                item.addZNodeValue(item.defaultValueKey,zNodeValue);
                                //<value genre="user" type="bool" class="SWITCH BINARY" instance="1" index="0" label="Switch" units="" readonly="false" polled="false">False</value>
                                listener.onDeviceItemChangeValue(item, item.defaultValueKey, item.defaultInstance);
                            }
                        });
                        mLinearLayout.addView(mDeviceSwitch);
                        break;
                    case ZNodeProduct.PIRSENSOR:
                        ImageView mDeviceIcon = new ImageView(itemView.getContext());
                        TextView mDeviceInfo = new TextView(itemView.getContext());
                        mDeviceInfo.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        mDeviceIcon.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));

                        //The Key for Default value for PIRSENSOR is SWITCH BINARY0
                        item.defaultValueKey="ALARM7";
                        item.defaultInstance=1;
                        String PIRValue=item.getZNodeValue("ALARM7").getValue(item.defaultInstance);
                        String PIRCode=PIRValue.substring(3,5);
                        Log.d(TAG,PIRCode + " PIR -> " +PIRValue);
                        switch(PIRCode){
                            case "01":
                                mDeviceInfo.setText(mContext.getString(R.string.intrusion));
                                mDeviceIcon.setImageResource(R.drawable.ic_warning_red);
                                break;
                            case "02":
                                mDeviceInfo.setText(mContext.getString(R.string.intrusion));
                                mDeviceIcon.setImageResource(R.drawable.ic_warning_red);
                                break;
                            case "03":
                                mDeviceInfo.setText(mContext.getString(R.string.tampering));
                                mDeviceIcon.setImageResource(R.drawable.ic_warning_red);
                                break;
                            case "04":
                                mDeviceInfo.setText(mContext.getString(R.string.tampering));
                                mDeviceIcon.setImageResource(R.drawable.ic_warning_red);
                                break;
                            case "05":
                                mDeviceInfo.setText(mContext.getString(R.string.glass_break));
                                mDeviceIcon.setImageResource(R.drawable.ic_info);
                                break;
                            case "06":
                                mDeviceInfo.setText(mContext.getString(R.string.glass_break));
                                mDeviceIcon.setImageResource(R.drawable.ic_info);
                                break;
                            case "07":
                                mDeviceInfo.setText(mContext.getString(R.string.motion_detected));
                                mDeviceIcon.setImageResource(R.drawable.ic_warning_red);
                                break;
                            case "08":
                                mDeviceInfo.setText(mContext.getString(R.string.motion_detected));
                                mDeviceIcon.setImageResource(R.drawable.ic_warning_red);
                                break;
                            default:
                                mDeviceInfo.setText(mContext.getString(R.string.no_event));
                                mDeviceIcon.setImageResource(R.drawable.ic_mood);
                        }
                        mLinearLayout.addView(mDeviceInfo);
                        mLinearLayout.addView(mDeviceIcon);
                        break;
                    case ZNodeProduct.COLOURLED:
                        final Button mColorPicker = new Button(itemView.getContext());
                        final Switch mRGBSwitch = new Switch(itemView.getContext());
                        class ColorState {
                            private boolean mColorState =false;
                            public ColorState(){

                            }
                            public boolean isColorState(){
                                return mColorState;
                            }
                            public void setColorState(boolean color){
                                this.mColorState=color;
                            }
                        }
                        final ColorState colorState= new ColorState();
                        //w=white;
                        //c=color;
                        //b=black;
                        Log.d(TAG, "COLOUR LED");
                        //The Key for Default value for COLOURLED is SWITCH BINARY0
                        item.defaultValueKey="COLOR CONTROL97";
                        item.defaultInstance=1;
                        ZNodeValue COLOURLEDzNodeValue = item.getZNodeValue(item.defaultValueKey);
                        String val = COLOURLEDzNodeValue.getValue(item.defaultInstance);
                        String delims = "[ ]+";
                        String[] tokens = val.split(delims);
                        int R = Integer.decode(tokens[0]);
                        int G = Integer.decode(tokens[1]);
                        int B = Integer.decode(tokens[2]);
                        int W = Integer.decode(tokens[3]);
                        R=min(R+W,255);
                        G=min(G+W,255);
                        B=min(B+W,255);
                        final int initialColor= 0xff000000 |  R<<16 | G<<8 | B;

                        mRGBSwitch.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));

                        if ((R+G+B+W)>0)
                            mRGBSwitch.setChecked(true);
                        else
                            mRGBSwitch.setChecked(false);

                        mRGBSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                ZNodeValue COLOURLEDzNodeValue = item.getZNodeValue(item.defaultValueKey);
                                if(isChecked){
                                    if(!colorState.isColorState()) {
                                        COLOURLEDzNodeValue.setValue("0x00 0x00 0x00 0xFF", item.defaultInstance);
                                        mColorPicker.setBackgroundColor(0xffffffff);
                                        item.addZNodeValue(item.defaultValueKey,COLOURLEDzNodeValue);
                                        listener.onDeviceItemChangeValue(item, item.defaultValueKey,item.defaultInstance);
                                    }
                                    Log.d("COLORSTATE","checked!");
                                }
                                else{
                                    Log.d("COLORSTATE","unchecked!");
                                    colorState.setColorState(false);
                                    COLOURLEDzNodeValue.setValue("0x00 0x00 0x00 0x00", item.defaultInstance);
                                    mColorPicker.setBackgroundColor(0xff000000);
                                    item.addZNodeValue(item.defaultValueKey,COLOURLEDzNodeValue);
                                    listener.onDeviceItemChangeValue(item, item.defaultValueKey,item.defaultInstance);
                                }
                                Log.d("COLORSTATE","State: " + colorState.isColorState());

                            }
                        });



                        /*
                        mRGBSwitch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ZNodeValue COLOURLEDzNodeValue = item.getZNodeValue(item.defaultValueKey);
                                if (mRGBSwitch.isChecked()){
                                    mRGBSwitch.setChecked(true);
                                    COLOURLEDzNodeValue.setValue("0x00 0x00 0x00 0xFF", item.defaultInstance);
                                    mColorPicker.setBackgroundColor(0xffffffff);
                                }
                                else {
                                    mRGBSwitch.setChecked(false);
                                    COLOURLEDzNodeValue.setValue("0x00 0x00 0x00 0x00", item.defaultInstance);
                                    mColorPicker.setBackgroundColor(0xff000000);
                                }
                                item.addZNodeValue(item.defaultValueKey,COLOURLEDzNodeValue);
                                listener.onDeviceItemChangeValue(item, item.defaultValueKey,item.defaultInstance);
                            }
                        });
                        */

                        final float scale = itemView.getContext().getResources().getDisplayMetrics().density;
                        mColorPicker.setPadding(0,(int)(16* scale + 0.5f),(int)(10* scale + 0.5f),(int)(10* scale + 0.5f));
                        int width=((int)(40* scale + 0.5f));
                        int height=((int)(20* scale + 0.5f));
                        ViewGroup.LayoutParams params=  new ViewGroup.LayoutParams(width,height);
                        mColorPicker.setLayoutParams(params);
                        mColorPicker.setBackgroundColor(initialColor);
                        mColorPicker.setOnClickListener(new Button.OnClickListener() {

                            @Override
                            public void onClick(final View view) {
                                //initialColor is the initially-selected color to be shown in the rectangle on the left of the arrow.
                                String delims = "[ ]+";
                                final ZNodeValue COLOURLEDzNodeValue = item.getZNodeValue(item.defaultValueKey);
                                String val = COLOURLEDzNodeValue.getValue(item.defaultInstance);
                                String[] tokens = val.split(delims);
                                int RR = Integer.decode(tokens[0]);
                                int GG = Integer.decode(tokens[1]);
                                int BB = Integer.decode(tokens[2]);
                                int WW = Integer.decode(tokens[3]);
                                //R=min(R+W,255);
                                //G=min(G+W,255);
                                //B=min(B+W,255);
                                final int initColor= (WW<<24) |  (RR<<16) | (GG<<8) | BB;
                                AmbilWarnaDialog dialog = new AmbilWarnaDialog(view.getContext(), initColor,true, new AmbilWarnaDialog.OnAmbilWarnaListener() {

                                    // Executes, when user click Cancel button
                                    @Override
                                    public void onCancel(AmbilWarnaDialog dialog){
                                    }

                                    // Executes, when user click OK button
                                    @Override
                                    public void onOk(AmbilWarnaDialog dialog, int color) {
                                        int W=(color>>24) & 0xff;
                                        int R = (color>>16) & 0xff;
                                        int G = (color>>8) & 0xff;
                                        int B = color & 0xff;

                                        Toast.makeText(view.getContext(), "Selected Color : " + String.format("R:0x%X", (byte)R) + " " + String.format("G:0x%X", (byte)G) + " " + String.format("B:0x%X", (byte)B) + " " +String.format("W:0x%X",  (byte)W) , Toast.LENGTH_LONG).show();
                                        COLOURLEDzNodeValue.setValue( String.format("0x%X", (byte)R) + " " + String.format("0x%X", (byte)G) + " " + String.format("0x%X", (byte)B) + " " +String.format("0x%X", (byte)W),1);

                                        item.addZNodeValue(item.defaultValueKey,COLOURLEDzNodeValue);
                                        R=min(R+W,255);
                                        G=min(G+W,255);
                                        B=min(B+W,255);
                                        Log.d("COLOR",R +" "+ G  +" " +B  +" " + W);
                                        mColorPicker.setBackgroundColor(0xff000000 |  R<<16 | G<<8 | B);

                                        if ((R+G+B+W)>0){
                                            colorState.setColorState(true);
                                            mRGBSwitch.setChecked(true);
                                        }
                                        else
                                            mRGBSwitch.setChecked(false);
                                        listener.onDeviceItemChangeValue(item, item.defaultValueKey,item.defaultInstance);
                                    }

                                });
                                dialog.show();


                            }
                        });
                        mLinearLayout.addView(mRGBSwitch);
                        mLinearLayout.addView(mColorPicker);
                        break;
                }
            }
        }
    }

    public static class DecIncViewHolder extends RecyclerView.ViewHolder {
        private CheckBox mDeviceCheckBox;
        private TextView mDeviceName;

        public DecIncViewHolder(View itemView) {
            super(itemView);
            mDeviceCheckBox = (CheckBox) itemView.findViewById(R.id.deviceCheckBox);
            mDeviceCheckBox.setVisibility(View.INVISIBLE);
            mDeviceName = (TextView) itemView.findViewById(R.id.deviceNameTextView);

        }

        public void bind(final ZNode item,final DeviceListListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v){
                    listener.onDeviceListItemClick(item);
                }
            });
        }
    }
}
