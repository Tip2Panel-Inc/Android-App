package com.engkan2kit.ava88;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Setsuna F. Seie on 28/03/2017.
 */

public class ZNodeValue {
    private ZNode valueZNode;
    private String valueGenre;
    private String valueType;
    private String valueClass;
    private int valueIndex=0;
    private String valueLabel;
    private String valueUnits;
    private boolean valueReadOnly= false;
    private boolean valuePolled=false;
    private ArrayList<String> items=new ArrayList<>();
    private SparseArray<String> value= new SparseArray<>();


    public ZNodeValue(){
        items.clear();
    }
    public ZNodeValue(ZNode znode)
    {
        this.valueZNode=znode;
    }

    public ZNodeValue(int valueIndex){

        this.valueIndex=valueIndex;
    }

    public ZNodeValue(String valueClass, String valueGenre, String valueType, int valueInstance, int valueIndex, String nodeValue){

        this.valueClass = valueClass;
        this.valueGenre = valueGenre;
        this.valueType = valueType;
        this.valueIndex = valueIndex;
        this.value.append(valueInstance, nodeValue);
    }

    public void setValueGenre(String valueGenre){this.valueGenre=valueGenre;}
    public void setValueType(String valueType){this.valueType=valueType;}
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

    public void setValue(String nodeValue){this.value.put(1,nodeValue);}
    public void setValue(String nodeValue,int instance){this.value.put(instance,nodeValue);}

    public String getValueGenre( ){return this.valueGenre;}
    public String getValueType( ){return this.valueType;}
    public String getValueClass( ){return this.valueClass;}
    public int getValueIndex( ){return this.valueIndex;}
    public String getValueLabel( ){return this.valueLabel;}
    public String getValueUnits( ){return this.valueUnits;}
    public boolean getValueReadOnly( ){return this.valueReadOnly;}
    public boolean getValuePolled( ){return this.valuePolled;}
    public List<String> getItems( ){
        return this.items;
    }
    public String getValue(int instance){return value.get(instance);}
}
