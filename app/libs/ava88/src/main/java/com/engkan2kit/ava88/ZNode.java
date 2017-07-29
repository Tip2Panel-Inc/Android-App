package com.engkan2kit.ava88;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Setsuna F. Seie on 20/03/2017.
 */

public class ZNode {

    //TODO: Set initial values

    public int nodeID;
    public String nodeBType;
    public String nodeGType;
    public String nodeName;
    public String nodeManufacturer;
    public String nodeProductStr;
    public int nodeProduct;
    public String nodeLocation;
    public String nodeStatusString;
    public int nodeStatus;
    public ZNodeValue defaultValue;
    public String defaultValueKey = "";
    public int defaultInstance =1;
    private int nodeGroup=0;

    private boolean nodeListening;
    private boolean nodeFrequent;
    private boolean nodeBeam;
    private boolean nodeRouting;
    private boolean nodeSecurity;
    private long nodeTime;

    private Map<String,ZNodeValue> nodeValues=new HashMap<String,ZNodeValue>();

    public ZNode(){
    }

    public ZNode(String nodeName){
        this.nodeName=nodeName;
    }

    public ZNode(String nodeName, String mNodeProductStr){
        this(nodeName);
        this.nodeProductStr=mNodeProductStr;
        this.nodeProduct=ZNodeProduct.nodeZNodeProducttoInt(mNodeProductStr);
    }

    public ZNode(String nodeName, String nodeProduct, String nodeManufacturer){
        this(nodeName, nodeProduct);
        this.nodeManufacturer=nodeManufacturer;

    }

    public ZNode(int nodeID,String nodeName, String nodeProduct, String nodeManufacturer) {
        this(nodeName, nodeProduct,nodeManufacturer);
        this.nodeID = nodeID;
    }

    //key = class+index
    public ZNodeValue getZNodeValue(String key){
        return nodeValues.get(key);
    }

    //key = class+index
    public void setZNodeValue(String key, String value){

    }

    //key = class+index
    public void addZNodeValue(String key,ZNodeValue mZNodeValue){
        nodeValues.put(key,mZNodeValue);
    }

    public int getState(){
        return nodeStatus;
    }

    public void setState(){

    }

}
