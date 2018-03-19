package com.tip2panel.smarthome.devices;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.engkan2kit.ava88.ZNodeValue;
import com.google.android.flexbox.FlexboxLayout;
import com.tip2panel.smarthome.R;
import com.tip2panel.smarthome.utils.DeviceListItem;
import com.tip2panel.smarthome.utils.DevicesDiffCallback;

import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

import static com.engkan2kit.ava88.ZNodeValueClass.NOP;
import static java.lang.Math.min;

/**
 * Created by Setsuna F. Seie on 13/07/2017.
 */

public class DeviceListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG=DeviceListRecyclerViewAdapter.class.getSimpleName();
    private List<DeviceListItem> mList = new ArrayList<>();
    private boolean mShowCheckBox=false;
    public interface DeviceListItemListener{
        void onDeviceListCheckBoxChecked(String deviceId);
        void onDeviceListCheckBoxUnchecked(String deviceId);
        void onDeviceListItemClick(DeviceListItem item);
        void onDeviceItemChangeValue(ZNodeValue item);

    }

    private final DeviceListItemListener mListener;

    public DeviceListRecyclerViewAdapter(List<DeviceListItem> list, DeviceListItemListener listener,boolean showCheckBox) {
        this.mList.addAll( list);
        this.mListener = listener;
        this.mShowCheckBox=showCheckBox;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int deviceType) {
        LayoutInflater mInflater = LayoutInflater.from ( parent.getContext () );

        switch (deviceType)
        {
            case NOP:
                ViewGroup productViewGroup = (ViewGroup) mInflater.inflate ( R.layout.devices_item, parent, false );
                ProductViewHolder productViewHolder = new ProductViewHolder(productViewGroup,mShowCheckBox);
                return productViewHolder;
            case 0x2500:
                ViewGroup switchBinaryViewGroup = (ViewGroup) mInflater.inflate ( R.layout.devices_item_value_switch_binary, parent, false );
                SwitchBinaryViewHolder switchBinaryViewHolder = new SwitchBinaryViewHolder(switchBinaryViewGroup);
                return switchBinaryViewHolder;
            case 0x2600:
                ViewGroup switchMultilevelViewGroup = (ViewGroup) mInflater.inflate ( R.layout.devices_item_value_switch_multilevel, parent, false );
                SwitchMultilevelViewHolder switchMultilevelViewHolder = new SwitchMultilevelViewHolder(switchMultilevelViewGroup);
                return switchMultilevelViewHolder;
            case 0x3000:
                ViewGroup sensorBinaryViewGroup = (ViewGroup) mInflater.inflate(R.layout.devices_item_value_binary_sensor,parent,false);
                SensorBinaryViewHolder sensorBinaryViewHolder = new SensorBinaryViewHolder(sensorBinaryViewGroup);
                return sensorBinaryViewHolder;
            case 0x3100:
                ViewGroup sensorMultilevelViewGroup = (ViewGroup) mInflater.inflate(R.layout.devices_item_value_multilevel_sensor,parent,false);
                SensorMultilevelViewHolder sensorMultilevelViewHolder = new SensorMultilevelViewHolder(sensorMultilevelViewGroup);
                return sensorMultilevelViewHolder;
            case 0x3361:
                ViewGroup colorControlViewGroup = (ViewGroup) mInflater.inflate (R.layout.devices_item_value_color_control,parent,false);
                ColorControlViewHolder colorControlViewHolder = new ColorControlViewHolder(colorControlViewGroup);
                return colorControlViewHolder;

            case 0x7107:
                ViewGroup alarmViewGroup = (ViewGroup) mInflater.inflate(R.layout.devices_item_value_alarm,parent,false);
                AlarmViewHolder alarmViewHolder = new AlarmViewHolder(alarmViewGroup);
                return alarmViewHolder;

            default:
                ViewGroup defaultViewGroup = (ViewGroup) mInflater.inflate ( R.layout.devices_item, parent, false );
                ProductViewHolder defaultViewHolder = new ProductViewHolder(defaultViewGroup);
                return defaultViewHolder;
        }

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DeviceListItem object = mList.get(position);
        switch (holder.getItemViewType())
        {
            case NOP:
                ((ProductViewHolder) holder).bind(object,mListener);
                break;
            case 0x2500:
                ((SwitchBinaryViewHolder) holder).bind(object,mListener);
                break;
            case 0x2600:
                ((SwitchMultilevelViewHolder) holder).bind(object,mListener);
                break;
            case 0x3000:
                ((SensorBinaryViewHolder) holder).bind(object,mListener);
                break;
            case 0x3100:
                ((SensorMultilevelViewHolder) holder).bind(object,mListener);
                break;
            case 0x3361:
                ((ColorControlViewHolder) holder).bind(object,mListener);
                break;
            case 0x7107:
                ((AlarmViewHolder) holder).bind(object,mListener);
                break;
            default:
                ((ProductViewHolder) holder).bind(object,mListener);

        }


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
            DeviceListItem object = mList.get(position);
            if (object != null) {
                return object.getType();
            }
        }
        return 0;
    }

    public void updateDeviceListItems(List<DeviceListItem> newDeviceList)
    {
        final DevicesDiffCallback diffCallback = new DevicesDiffCallback(this.mList,newDeviceList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mList.clear();
        this.mList.addAll(newDeviceList);
        diffResult.dispatchUpdatesTo(this);
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
            //mFlexboxLayout = (FlexboxLayout) itemView.findViewById(R.id.deviceValueFlexboxLayout);
            mContext = itemView.getContext();

        }

        public void bind(final DeviceListItem item,final DeviceListItemListener listener){
            if (item != null) {

            }
        }
    }

    //View Holder for Product or ZNode
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private CheckBox mProductCheckBox;
        private TextView mProductNameTextView;
        private TextView mProductStatusTextView;
        private ImageView mProductStatusImageView;
        private Context mContext;
        private boolean mShowCheckBox=false;


        public ProductViewHolder(View itemView) {this(itemView,false);}
        public ProductViewHolder(View itemView, boolean showCheckBox) {
            super(itemView);
            mProductCheckBox = (CheckBox) itemView.findViewById(R.id.deviceCheckBox);
            mProductCheckBox.setVisibility(View.GONE);
            mProductNameTextView = (TextView) itemView.findViewById(R.id.deviceNameTextView);
            mProductStatusTextView = (TextView) itemView.findViewById(R.id.deviceStatusTextView);
            mProductStatusImageView=(ImageView) itemView.findViewById(R.id.deviceStatusImageView);
            mContext = itemView.getContext();
            mShowCheckBox=showCheckBox;

        }

        public void bind(final DeviceListItem item,final DeviceListItemListener listener){
            if (item != null) {
                String name = item.getZNodeValue().getValueLabel()+"";
                mProductNameTextView.setText(name);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v){
                        listener.onDeviceListItemClick(item);
                    }
                });
                String status = item.getZNodeValue().getValue()+"";
                mProductStatusTextView.setText(status);
                Log.d(TAG,"Product Label");
                if(mShowCheckBox) {
                    mProductCheckBox.setVisibility(View.VISIBLE);
                    mProductCheckBox.setOnCheckedChangeListener(
                            new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView,
                                                             boolean isChecked) {
                                    if(isChecked){
                                        listener.onDeviceListCheckBoxChecked(item.getId());
                                    }
                                    else{
                                        listener.onDeviceListCheckBoxUnchecked(item.getId());
                                    }
                                }
                            });
                }
                else {
                    mProductCheckBox.setVisibility(View.GONE);
                }
            }
        }
    }

    //ViewHolder for Switch Binary index 0
    public static class SwitchBinaryViewHolder extends RecyclerView.ViewHolder {
        private FlexboxLayout mFlexboxLayout;
        private TextView mBinarySwitchName;
        private Switch mBinarySwitch;
        private Context mContext;


        public SwitchBinaryViewHolder(View itemView) {
            super(itemView);
            mBinarySwitchName = (TextView) itemView.findViewById(R.id.valueNameTextView);
            mBinarySwitch = (Switch) itemView.findViewById(R.id.binarySwitch);
            //mFlexboxLayout = (FlexboxLayout) itemView.findViewById(R.id.deviceValueFlexboxLayout);
            mContext = itemView.getContext();

        }

        public void bind(final DeviceListItem item,final DeviceListItemListener listener){
            if (item != null) {
                String valueLabel = item.getZNodeValue().getValueLabel();
                if (valueLabel==null || valueLabel.length() ==0)
                {
                    mBinarySwitchName.setText("Switch");
                }
                else
                {
                    mBinarySwitchName.setText(valueLabel);
                }

                if (item.getZNodeValue().getValue(1).equals("True"))
                    mBinarySwitch.setChecked(true);
                else
                    mBinarySwitch.setChecked(false);
                Log.d(TAG,"Switch Binary");
                mBinarySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ZNodeValue zNodeValue= item.getZNodeValue();
                    String val = "False";
                    if (isChecked)
                        val = "True";
                    zNodeValue.setValue(val, 1);
                    //item.addZNodeValue(item.defaultValueKey,zNodeValue);
                    //<value genre="user" type="bool" class="SWITCH BINARY" instance="1" index="0" label="Switch" units="" readonly="false" polled="false">False</value>
                    listener.onDeviceItemChangeValue(zNodeValue);
                    }
                });
            }
        }
    }

    //ViewHolder for Switch Multilevel index 0
    public static class SwitchMultilevelViewHolder extends RecyclerView.ViewHolder {
        private FlexboxLayout mFlexboxLayout;
        private TextView mSwitchMultilevelName;
        private SeekBar mSwitchMultilevelSeekBar;
        private Context mContext;


        public SwitchMultilevelViewHolder(View itemView) {
            super(itemView);
            mSwitchMultilevelName = (TextView) itemView.findViewById(R.id.valueNameTextView);
            mSwitchMultilevelSeekBar = (SeekBar) itemView.findViewById(R.id.switchMultilevelSeekBar);
            mContext = itemView.getContext();

        }

        public void bind(final DeviceListItem item,final DeviceListItemListener listener){
            if (item != null) {
                String valueLabel = item.getZNodeValue().getValueLabel();
                if (valueLabel==null || valueLabel.length() ==0)
                {
                    mSwitchMultilevelName.setText("Level");
                }
                else
                {
                    mSwitchMultilevelName.setText(valueLabel);
                }

                int switchValue=0;
                try{
                    String val = item.getZNodeValue().getValue(1);
                    Log.d("LEVEL","Switch value in str: " + switchValue);
                    switchValue = Integer.parseInt(val);
                }
                catch (NumberFormatException e){
                    e.printStackTrace();
                    switchValue=0;
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                    switchValue=0;
                }

                int switchValueMax=0;
                try{
                    String val = item.getZNodeValue().getValueMax();
                    switchValueMax = Integer.parseInt(val);
                }
                catch (NumberFormatException e){
                    e.printStackTrace();
                    switchValueMax=0;
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                    switchValueMax=0;
                }

                int switchValueMin=0;
                try{
                    String val = item.getZNodeValue().getValueMin();
                    switchValueMin = Integer.parseInt(val);
                }
                catch (NumberFormatException e){
                    e.printStackTrace();
                    switchValueMin=0;
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                    switchValueMin=0;
                }

                mSwitchMultilevelSeekBar.setMax(switchValueMax);
                //mSwitchMultilevelSeekBar.setMin(switchValueMin);
                mSwitchMultilevelSeekBar.setProgress(switchValue);
                Log.d("LEVEL","Switch value in int: " + switchValue);
                mSwitchMultilevelSeekBar.setOnSeekBarChangeListener(
                        new SeekBar.OnSeekBarChangeListener(){

                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                ZNodeValue zNodeValue= item.getZNodeValue();
                                zNodeValue.setValue(String.valueOf(progress), 1);
                                listener.onDeviceItemChangeValue(zNodeValue);

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        }
                );
                Log.d(TAG,"Switch Multilevel");
            }
        }
    }

    //ViewHolder for Alarm Index 7
    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        private TextView mAlarmLabelTextView;
        private TextView mAlarmValueTextView;
        private ImageView mProductStatusImageView;
        private Context mContext;



        public AlarmViewHolder(View itemView) {
            super(itemView);
            mAlarmLabelTextView = (TextView) itemView.findViewById(R.id.valueNameTextView);
            mAlarmValueTextView = (TextView) itemView.findViewById(R.id.alarmValueTextView);
            mProductStatusImageView=(ImageView) itemView.findViewById(R.id.alarmStatusImageView);
            mContext = itemView.getContext();

        }

        public void bind(final DeviceListItem item,final DeviceListItemListener listener){
            if (item != null) {
                String name = item.getZNodeValue().getValueLabel()+"";
                mAlarmLabelTextView.setText(name);
                Log.d(TAG,"Alarm Label "+name);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v){
                        listener.onDeviceListItemClick(item);
                    }
                });

                String PIRValue=item.getZNodeValue().getValue();
                String PIRCode=PIRValue.substring(3,5);
                Log.d(TAG,PIRCode + " PIR -> " +PIRValue);
                switch(PIRCode){
                    case "01":
                        mAlarmValueTextView.setText(mContext.getString(R.string.intrusion));
                        mProductStatusImageView.setImageResource(R.drawable.ic_warning_red);
                        break;
                    case "02":
                        mAlarmValueTextView.setText(mContext.getString(R.string.intrusion));
                        mProductStatusImageView.setImageResource(R.drawable.ic_warning_red);
                        break;
                    case "03":
                        mAlarmValueTextView.setText(mContext.getString(R.string.tampering));
                        mProductStatusImageView.setImageResource(R.drawable.ic_warning_red);
                        break;
                    case "04":
                        mAlarmValueTextView.setText(mContext.getString(R.string.tampering));
                        mProductStatusImageView.setImageResource(R.drawable.ic_warning_red);
                        break;
                    case "05":
                        mAlarmValueTextView.setText(mContext.getString(R.string.glass_break));
                        mProductStatusImageView.setImageResource(R.drawable.ic_info);
                        break;
                    case "06":
                        mAlarmValueTextView.setText(mContext.getString(R.string.glass_break));
                        mProductStatusImageView.setImageResource(R.drawable.ic_info);
                        break;
                    case "07":
                        mAlarmValueTextView.setText(mContext.getString(R.string.motion_detected));
                        mProductStatusImageView.setImageResource(R.drawable.ic_warning_red);
                        break;
                    case "08":
                        mAlarmValueTextView.setText(mContext.getString(R.string.motion_detected));
                        mProductStatusImageView.setImageResource(R.drawable.ic_warning_red);
                        break;
                    default:
                        mAlarmValueTextView.setText(mContext.getString(R.string.no_event));
                        mProductStatusImageView.setImageResource(R.drawable.ic_mood);
                }

            }
        }
    }

    //ViewHolder for Binary Sensor
    public static class SensorBinaryViewHolder extends RecyclerView.ViewHolder {
        private TextView mSensorLabelTextView;
        private TextView mSensorValueTextView;
        private ImageView mSensorStatusImageView;
        private Context mContext;



        public SensorBinaryViewHolder(View itemView) {
            super(itemView);
            mSensorLabelTextView = (TextView) itemView.findViewById(R.id.valueNameTextView);
            mSensorValueTextView = (TextView) itemView.findViewById(R.id.sensorBinaryTextView);
            mSensorStatusImageView=(ImageView) itemView.findViewById(R.id.sensorBinaryStatusImageView);
            mContext = itemView.getContext();

        }

        public void bind(final DeviceListItem item,final DeviceListItemListener listener){
            if (item != null) {
                String name = item.getZNodeValue().getValueLabel()+"";
                mSensorLabelTextView.setText(name);
                Log.d(TAG,"Sensor Binary Label "+name);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v){
                        listener.onDeviceListItemClick(item);
                    }
                });

                String sensorValue=item.getZNodeValue().getValue();

                if(sensorValue.equalsIgnoreCase("true"))
                {
                    mSensorValueTextView.setText(mContext.getString(R.string.strTrue));
                    mSensorStatusImageView.setImageResource(R.drawable.ic_radio_button_checked);
                    mSensorStatusImageView.setColorFilter(R.color.colorSensorCheckedBackground);
                }
                else
                {
                    mSensorValueTextView.setText(mContext.getString(R.string.strFalse));
                    mSensorStatusImageView.setImageResource(R.drawable.ic_radio_button_unchecked);
                    mSensorStatusImageView.setColorFilter(R.color.colorSensorUnCheckedBackground);
                }




            }
        }
    }

    //ViewHolder for Multilevel Sensor
    public static class SensorMultilevelViewHolder extends RecyclerView.ViewHolder {
        private TextView mSensorLabelTextView;
        private TextView mSensorValueTextView;
        private ImageView mSensorStatusImageView;
        private ImageView mValueImageView;
        private Context mContext;



        public SensorMultilevelViewHolder(View itemView) {
            super(itemView);
            mValueImageView = (ImageView) itemView.findViewById(R.id.valueImageView);
            mSensorLabelTextView = (TextView) itemView.findViewById(R.id.valueNameTextView);
            mSensorValueTextView = (TextView) itemView.findViewById(R.id.sensorMultilevelTextView);
            mSensorStatusImageView=(ImageView) itemView.findViewById(R.id.sensorMultilevelStatusImageView);
            mContext = itemView.getContext();

        }

        public void bind(final DeviceListItem item,final DeviceListItemListener listener){
            if (item != null) {
                String name = item.getZNodeValue().getValueLabel()+"";
                mSensorLabelTextView.setText(name);
                Log.d(TAG,"Sensor Binary Label "+name);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v){
                        listener.onDeviceListItemClick(item);
                    }
                });

                String sensorValue=item.getZNodeValue().getValue();

                mSensorValueTextView.setText(sensorValue + " " + item.getZNodeValue().getValueUnits());


            }
        }
    }

    //ViewHolder for Color Control Index 97
    public static class ColorControlViewHolder extends RecyclerView.ViewHolder {
        private FlexboxLayout mFlexboxLayout;
        private TextView mRGBWSwitchViewHolderName;
        private Context mContext;
        private Button mColorPicker;
        private Switch mRGBSwitch;
        private static int index = 97;
        private static int instance = 1;

        public ColorControlViewHolder(View itemView) {
            super(itemView);
            mRGBWSwitchViewHolderName = (TextView) itemView.findViewById(R.id.valueNameTextView);
            //mFlexboxLayout = (FlexboxLayout) itemView.findViewById(R.id.deviceValueFlexboxLayout);
            mColorPicker = (Button) itemView.findViewById(R.id.colorPickerButton);
            mRGBSwitch = (Switch) itemView.findViewById(R.id.binarySwitch);
            mContext = itemView.getContext();

        }

        public void bind(final DeviceListItem item,final DeviceListItemListener listener){
            if (item != null) {
                String valueLabel = item.getZNodeValue().getValueLabel();
                if (valueLabel==null || valueLabel.length() ==0)
                {
                    mRGBWSwitchViewHolderName.setText("Color");
                }
                else
                {
                    mRGBWSwitchViewHolderName.setText(valueLabel);
                }
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
                //The Key for Default value for COLOURLED is COLOR CONTROL97
                //item.defaultValueKey="COLOR CONTROL97";
                //item.defaultInstance=1;
                ZNodeValue COLOURLEDzNodeValue = item.getZNodeValue();
                String val = COLOURLEDzNodeValue.getValue(instance);
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


                if ((R+G+B+W)>0)
                    mRGBSwitch.setChecked(true);
                else
                    mRGBSwitch.setChecked(false);

                mRGBSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ZNodeValue COLOURLEDzNodeValue = item.getZNodeValue();
                        if(isChecked){
                            if(!colorState.isColorState()) {
                                COLOURLEDzNodeValue.setValue("0x00 0x00 0x00 0xFF", instance);
                                mColorPicker.setBackgroundColor(0xffffffff);

                                listener.onDeviceItemChangeValue(COLOURLEDzNodeValue);
                            }
                            Log.d("COLORSTATE","checked!");
                        }
                        else{
                            Log.d("COLORSTATE","unchecked!");
                            colorState.setColorState(false);
                            COLOURLEDzNodeValue.setValue("0x00 0x00 0x00 0x00", instance);
                            mColorPicker.setBackgroundColor(0xff000000);
                            listener.onDeviceItemChangeValue(COLOURLEDzNodeValue);
                        }
                        Log.d("COLORSTATE","State: " + colorState.isColorState());

                    }
                });


                final float scale = itemView.getContext().getResources().getDisplayMetrics().density;
                mColorPicker.setPadding(0,(int)(16* scale + 0.5f),(int)(10* scale + 0.5f),(int)(10* scale + 0.5f));
                int width=((int)(40* scale + 0.5f));
                int height=((int)(20* scale + 0.5f));
                //ConstraintLayout.LayoutParams params=  new ConstraintLayout.LayoutParams(width,height);
                //mColorPicker.setLayoutParams(params);
                mColorPicker.setBackgroundColor(initialColor);
                mColorPicker.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(final View view) {
                        //initialColor is the initially-selected color to be shown in the rectangle on the left of the arrow.
                        String delims = "[ ]+";
                        final ZNodeValue COLOURLEDzNodeValue = item.getZNodeValue();
                        String val = COLOURLEDzNodeValue.getValue(instance);
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
                                listener.onDeviceItemChangeValue(COLOURLEDzNodeValue);
                            }

                        });
                        dialog.show();


                    }
                });
            }
        }
    }
}
