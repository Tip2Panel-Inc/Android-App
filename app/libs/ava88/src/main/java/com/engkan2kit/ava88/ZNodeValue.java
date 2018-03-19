package com.engkan2kit.ava88;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Setsuna F. Seie on 28/03/2017.
 */

public class ZNodeValue {
    private ZNode valueZNode;
    private int nodeId;
    private String valueGenre;
    private String valueType;
    private int valueClassId;
    private String valueClass;
    private int valueIndex=0;
    private String valueLabel;
    private String valueUnits;
    private boolean valueReadOnly= false;
    private boolean valuePolled=false;
    private String valueMax;
    private String valueMin;
    private ArrayList<String> items=new ArrayList<>();
    private SparseArray<String> value= new SparseArray<>();
    private int instance;
    private String val;


    public ZNodeValue(){
        items.clear();
    }
    public ZNodeValue(ZNode znode)
    {
        this.valueZNode=znode;
        this.nodeId=znode.nodeID;
    }

    public ZNodeValue(int valueIndex){

        this.valueIndex=valueIndex;
    }

    public ZNodeValue(String valueClass, String valueGenre, String valueType, int valueInstance, int valueIndex, String nodeValue){

        this.valueClass = valueClass;
        try {
            this.valueClassId = ZNodeValueClass.valueClass.get(valueClass);
        }
        catch(NullPointerException ex)
        {
            this.valueClassId=-1;
            ex.printStackTrace();
        }
        this.valueGenre = valueGenre;
        this.valueType = valueType;
        this.valueIndex = valueIndex;
        this.value.append(valueInstance, nodeValue);
        this.instance=valueInstance;
        this.val=nodeValue;
    }

    public void setNodeId(int nodeId){this.nodeId=nodeId;}
    public void setValueGenre(String valueGenre){this.valueGenre=valueGenre;}
    public void setValueType(String valueType){this.valueType=valueType;}
    public void setValueClassId(int valueClassId){this.valueClassId=valueClassId;}
    public void setValueClass(String valueClass){this.valueClass=valueClass;}
    public void setValueIndex(int valueIndex){this.valueIndex=valueIndex;}
    public void setValueLabel(String valueLabel){this.valueLabel=valueLabel;}
    public void setValueUnits(String valueUnits){this.valueUnits=valueUnits;}
    public void setValueReadOnly(boolean valueReadOnly){this.valueReadOnly=valueReadOnly;}
    public void setValuePolled(boolean valuePolled){this.valuePolled=valuePolled;}
    public void addItem(String item){
        items.add(item);
    }
    public void setItems(List<String> items){
        this.items=new ArrayList<>(items);
    }
    public void setValueMax(String valueMax){this.valueMax=valueMax;}
    public void setValueMin(String valueMin){this.valueMin=valueMin;}

    public void setValue(String nodeValue)
    {
        this.value.put(1,nodeValue);
        this.val = nodeValue;
    }
    public void setValue(String nodeValue,int instance)
    {
        this.value.put(instance,nodeValue);
        this.instance=instance;
        this.val=nodeValue;
    }

    public int getNodeId( ){return this.nodeId;}
    public String getValueGenre( ){return this.valueGenre;}
    public String getValueType( ){return this.valueType;}
    public String getValueClass( ){return this.valueClass;}
    public int getValueClassId( ){return this.valueClassId;}
    public int getValueIndex( ){return this.valueIndex;}
    public String getValueLabel( ){return this.valueLabel;}
    public String getValueUnits( ){return this.valueUnits;}
    public boolean getValueReadOnly( ){return this.valueReadOnly;}
    public boolean getValuePolled( ){return this.valuePolled;}
    public List<String> getItems( ){
        return this.items;
    }
    public String getValue(int instance){return value.get(instance);}
    public int getInstance(){return instance;}
    public String getValue(){return val;}
    public String getValueMax(){ return valueMax;}
    public String getValueMin(){ return valueMin;}
}
