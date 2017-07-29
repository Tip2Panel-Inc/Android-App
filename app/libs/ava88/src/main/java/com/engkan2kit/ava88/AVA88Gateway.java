package com.engkan2kit.ava88;

import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

import com.burgstaller.okhttp.AuthenticationCacheInterceptor;
import com.burgstaller.okhttp.CachingAuthenticatorDecorator;
import com.burgstaller.okhttp.DispatchingAuthenticator;
import com.burgstaller.okhttp.digest.CachingAuthenticator;
import com.burgstaller.okhttp.digest.Credentials;
import com.burgstaller.okhttp.digest.DigestAuthenticator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Setsuna F. Seie on 20/03/2017.
 */
//test

public class AVA88Gateway{
    private String gatewayHost;
    private int gatewayPort;
    private int nodeCount;
    private int nodeId;
    private int homeId;
    private static boolean connected=false;

    private final OkHttpClient client;//=new OkHttpClient();

    public static final int COMMAND_BASE = 0;
    public static final int COMMAND_ZWAVE_LOG=1;
    public static final int COMMAND_ACCESS_LOG=2;
    public static final int COMMAND_ACCESS_ERROR_LOG=3;
    public static final int COMMAND_ALARM_LOG=4;
    public static final int COMMAND_UPDATE_LOG=5;
    public static final int COMMAND_CAMERA_LOG=6;
    public static final int COMMAND_RESET_CONTROLLER=7;
    public static final int COMMAND_SOFT_RESET_CONTROLLER=8;
    public static final int COMMAND_RESET_ALL=9;
    public static final int COMMAND_SHUTDOWN=10;
    public static final int COMMAND_SAVE=11;
    public static final int COMMAND_UPDATE=12;
    public static final int COMMAND_GET_GATEWAY_PRIVATE_IP=13;
    public static final int COMMAND_GET_IMAGE_LIST=14;
    public static final int COMMAND_HEAL_ALL=15;
    public static final int COMMAND_HEAL_SPECIFIC=16;
    public static final int COMMAND_LOAD_USER_ACCOUNT=17;
    public static final int COMMAND_MODIFY_ADMIN_PASSWORD=18;
    public static final int COMMAND_ADD_USER_ACCOUNT=19;
    public static final int COMMAND_REMOVE_USER_ACCOUNT=20;
    public static final int COMMAND_MODIFY_USER_PASSWORD=21;
    public static final int COMMAND_GET_CURRENT_ACCOUNT=22;
    public static final int COMMAND_RESET_ALL_ACCOUNT=23;
    public static final int COMMAND_DETAIL_NODE_STATE=24;
    public static final int COMMAND_TOPOLOGY_STATE=25;
    public static final int COMMAND_GET_SCENE_LIST=26;
    public static final int COMMAND_CREATE_SCENE=27;
    public static final int COMMAND_DELETE_SCENE=28;
    public static final int COMMAND_RUN_SCENE=29;
    public static final int COMMAND_LOAD_SCENE_SETTING=30;
    public static final int COMMAND_CHANGE_SCENE_LABEL=31;
    public static final int COMMAND_ADD_SCENE_SETTING=32;
    public static final int COMMAND_REMOVE_SCENE_VALUES=33;
    public static final int COMMAND_UPDATE_SCENE_VALUE=34;
    public static final int COMMAND_LOAD_SCENE_TRIGGER_LIST=35;
    public static final int COMMAND_ADD_SCENE_TRIGGER=36;
    public static final int COMMAND_REMOVE_SCENE_TRIGGER=37;
    public static final int COMMAND_UPDATE_SCENE_TRIGGER=38;
    public static final int COMMAND_TEST_SCENE_TRIGGER=39;
    public static final int COMMAND_LOAD_SCENE_SCHEDULE_LIST=40;
    public static final int COMMAND_ADD_NEW_SCHEDULE_FOR_WEEK=41;
    public static final int COMMAND_ADD_NEW_SCHEDULE_FOR_DAY=42;
    public static final int COMMAND_ADD_ONE_SCHEDULE=43;
    public static final int COMMAND_REMOVE_SCHEDULE=44;
    public static final int COMMAND_UPDATE_SCHEDULE=45;
    public static final int COMMAND_ADD_EVENT_RULE=46;
    public static final int COMMAND_MODIFY_EVENT_RULE=47;
    public static final int COMMAND_MODIFY_EVENT_RULE_LABEL=48;
    public static final int COMMAND_ENABLE_DISABLE_EVENT_RULE=49;
    public static final int COMMAND_REMOVE_EVENT_RULE=50;
    public static final int COMMAND_RUN_EVENT_RULE=51;
    public static final int COMMAND_GET_EVENT_RULE_LIST=52;
    public static final int COMMAND_LOAD_EVENT_RULE=53;
    public static final int COMMAND_SET_CURRENT_MODE=54;
    public static final int COMMAND_START_ADD_NODE_PROCESS=55;
    public static final int COMMAND_START_ADD_NODE_PROCESS_FORCED=56;
    public static final int COMMAND_START_REMOVE_NODE_PROCESS=57;
    public static final int COMMAND_CANCEL_ADD_REMOVE_NODE_PROCESS=58;
    public static final int COMMAND_CREATE_PRIMARY=59;
    public static final int COMMAND_ZWAVE_CONFIG_FROM_OTHER=60;
    public static final int COMMAND_DETECT_FAILED_NODE=61;
    public static final int COMMAND_REMOVE_FAILED_NODE=62;
    public static final int COMMAND_REPLACE_FAILED_NODE=63;
    public static final int COMMAND_TRANSFER_PRIMARY=64;
    public static final int COMMAND_REQUEST_NETWORK_UPDATE=65;
    public static final int COMMAND_REQUEST_NODE_NEIGHBOR_UPDATE=66;
    public static final int COMMAND_ASSIGN_RETURN_ROUTE=67;
    public static final int COMMAND_DELETE_ALL_RETURN_ROUTES=68;
    public static final int COMMAND_REQUEST_NODE_INFORMATION=69;
    public static final int COMMAND_SEND_ZWAVE_INFO_TO_SECONDARY=70;
    public static final int COMMAND_ADD_BUTTON=71;
    public static final int COMMAND_REMOVE_BUTTON=72;
    public static final int COMMAND_POLL_NODES_STATES=73;
    public static final int COMMAND_POLL_TARGET_NODE_STATE=74;
    public static final int COMMAND_GET_NODES_UPDATE_HISTORY=75;
    public static final int COMMAND_CHANGE_VALUE=76;
    public static final int COMMAND_CHANGE_VALUE_BUTTON=77;
    public static final int COMMAND_REFRESH_NODE_VALUE=78;
    public static final int COMMAND_REFRESH_NODE_CONFIG=79;
    public static final int COMMAND_SET_NODE_NAME=80;
    public static final int COMMAND_SET_NODE_LOCATION=81;
    public static final int COMMAND_SET_NODE_ICON=82;
    public static final int COMMAND_GET_GROUP_SETTING=83;
    public static final int COMMAND_SET_VALUE_POLL=84;
    public static final int COMMAND_LOAD_LOCATION_LIST=85;
    public static final int COMMAND_ADD_LOCATION=86;
    public static final int COMMAND_REMOVE_LOCATION=87;
    public static final int COMMAND_LOAD_NOTIFICATION_LIST=88;
    public static final int COMMAND_ADD_NEW_NOTIFICATION=89;
    public static final int COMMAND_REMOVE_NOTIFICATION=90;
    public static final int COMMAND_UPDATE_NOTIFICATION=91;
    public static final int COMMAND_TEST_NOTIFICATION=92;
    public static final int COMMAND_LOAD_CAMERA_LIST=93;
    public static final int COMMAND_ADD_CAMERA=94;
    public static final int COMMAND_REMOVE_CAMERA=95;
    public static final int COMMAND_UPDATE_CAMERA_SETTING=96;
    public static final int COMMAND_START_CAMERA_CONNECTION=97;
    public static final int COMMAND_STOP_CAMERA_CONNECTION=98;
    public static final SparseArray<String> command= new SparseArray<String>();
    static{
        command.put(COMMAND_BASE,"");
        command.put(COMMAND_ZWAVE_LOG,"");
        command.put(COMMAND_ACCESS_LOG,"");
        command.put(COMMAND_ACCESS_ERROR_LOG,"");
        command.put(COMMAND_LOAD_LOCATION_LIST,"/location_list.cgi?action=load");
        command.put(COMMAND_POLL_NODES_STATES,"/poll2.xml?reduced=1");
        command.put(COMMAND_POLL_TARGET_NODE_STATE,"/node_detail.cgi");
        command.put(COMMAND_CHANGE_VALUE,"/valuepost.html");
        command.put(COMMAND_CHANGE_VALUE_BUTTON,"/valuebutton.html");
        command.put(COMMAND_START_ADD_NODE_PROCESS,"/admpost.html");
        command.put(COMMAND_START_REMOVE_NODE_PROCESS,"/admpost.html");
        command.put(COMMAND_CANCEL_ADD_REMOVE_NODE_PROCESS,"/admpost.html");
        command.put(COMMAND_DETECT_FAILED_NODE,"/admpost.html");
        command.put(COMMAND_REMOVE_FAILED_NODE,"/admpost.html");
        command.put(COMMAND_REPLACE_FAILED_NODE,"/admpost.html");
        command.put(COMMAND_ADD_LOCATION,"/location_list.cgi");
        command.put(COMMAND_REMOVE_LOCATION,"/location_list.cgi");
    }


    public AVA88Gateway(AVA88GatewayInfo ava88GatewayInfo){
        this(ava88GatewayInfo.ipv4Address,5000,ava88GatewayInfo.user,ava88GatewayInfo.password);
    }
    public AVA88Gateway(String hostString,int port){
        this(hostString,port,"admin","admin");
    }

    public AVA88Gateway(String hostString,int port,String username,String password){
        this.gatewayHost=hostString;
        this.gatewayPort=port;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        final Map<String, CachingAuthenticator> authCache = new ConcurrentHashMap<>();

        Credentials credentials = new Credentials(username, password);
        final DigestAuthenticator digestAuthenticator = new DigestAuthenticator(credentials);

        // note that all auth schemes should be registered as lowercase!
        DispatchingAuthenticator authenticator = new DispatchingAuthenticator.Builder()
                .with("digest", digestAuthenticator)
                .build();

        client = builder
                .authenticator(new CachingAuthenticatorDecorator(authenticator, authCache))
                .addInterceptor(new AuthenticationCacheInterceptor(authCache))
                .build();
    }

    public void setGatewayHost(String gatewayHost){
        this.gatewayHost = gatewayHost;
    }

    public String getGatewayHost(){
        return gatewayHost;
    }

    public  ArrayList<ZNode> scanNodes(@Nullable String mLocation){
        ArrayList<ZNode> nodes= new ArrayList<>();
        MediaType mediaType= MediaType.parse("text/plain; charset=utf-8");
        //Use the all node state polling request
        Request request = new Request.Builder()
                .url("http://"+gatewayHost+":"+ gatewayPort+AVA88Gateway.command.get(COMMAND_POLL_NODES_STATES))
                .get()
                .build();

        try {

            Response response = client.newCall(request).execute();
            Log.d("NODES LIST", "Handling list of nodes");
            if (response.code() == 200) {
                Log.d("NODES LIST", "Response OK 200");
                try {
                    //request returns XML of all devices paired
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputStream is = response.body().byteStream();
                    Document nodesDocument = builder.parse(is);
                    nodesDocument.getDocumentElement().normalize();
                    // get all the nodes in XML by checking the name "node"
                    NodeList items = nodesDocument.getElementsByTagName("node");
                    Log.d("NODES LIST", "NODES: "+items.getLength());
                    for (int k = 0; k < items.getLength(); k++) {
                        Element item = (Element) items.item(k);
                        Log.d("NODES LIST",item.getAttribute("id") + " Detected");
                        //get the properties of the each node
                        int id=Integer.parseInt(item.getAttribute("id"));
                        String location = item.getAttribute("location")+"";
                        String btype= item.getAttribute("btype")+"";
                        String gtype= item.getAttribute("gtype")+"";
                        String name = item.getAttribute("name")+"";
                        String manufacturer = item.getAttribute("manufacturer")+"";
                        String product = item.getAttribute("product")+"";
                        String statusStr = item.getAttribute("status")+"";

                        //if the node is not the gateway, handle it
                        if(!btype.equalsIgnoreCase("Static Controller")) {
                            ZNode z = new ZNode(id,name, product, manufacturer);
                            z.nodeStatus = ZNodeStatus.deviceStatus.get(statusStr);
                            z.nodeStatusString = statusStr;
                            z.nodeBType=btype;
                            z.nodeGType=gtype;
                            z.nodeLocation=location;
                            if(mLocation==null || mLocation.equalsIgnoreCase(location.trim())) {
                                NodeList values;
                                int prod =ZNodeProduct.nodeZNodeProducttoInt(product);
                                Log.d("NODES LIST",""+prod);

                                switch (prod) {
                                    case ZNodeProduct.PIRSENSOR:
                                        Log.d("NODES LIST","A PIR SENSOR!!!");
                                        values = item.getElementsByTagName("value");
                                        for(int j=0;j<values.getLength();j++){
                                            Element value = (Element) values.item(j);
                                            //find the current value of index 0 and record it as the current state
                                            if(value.getAttribute("class").equalsIgnoreCase("ALARM") && value.getAttribute("index").equals("7")){
                                                z.defaultValue = new ZNodeValue(value.getAttribute("class"),
                                                        value.getAttribute("genre"),
                                                        value.getAttribute("type"),
                                                        1,
                                                        0,
                                                        value.getChildNodes().item(0).getNodeValue());
                                                Log.d("VALUE",""+z.defaultValue.getValue(1));
                                            }
                                        }
                                        break;
                                    case ZNodeProduct.APPLIANCEONOFF:
                                        Log.d("NODES LIST","A BINARY POWER SWITCH!!!");
                                        values = item.getElementsByTagName("value");
                                        for(int j=0;j<values.getLength();j++){
                                            Element value = (Element) values.item(j);
                                            //find the current value of index 0 and record it as the current state
                                            if(value.getAttribute("class").equalsIgnoreCase("SWITCH BINARY") && value.getAttribute("index").equals("0")){
                                                z.defaultValue = new ZNodeValue(value.getAttribute("class"),
                                                        value.getAttribute("genre"),
                                                        value.getAttribute("type"),
                                                        1,
                                                         0,
                                                        value.getChildNodes().item(0).getNodeValue());
                                                Log.d("VALUE",""+z.defaultValue.getValue(1));
                                            }
                                        }
                                        break;
                                    case ZNodeProduct.COLOURLED:
                                        Log.d("NODES LIST","A Colour LED!!!");
                                        values = item.getElementsByTagName("value");
                                        for(int j=0;j<values.getLength();j++){
                                            Element value = (Element) values.item(j);
                                            //find the current value of index 97 and record it as the current state
                                            if(value.getAttribute("class").equalsIgnoreCase("COLOR CONTROL") && value.getAttribute("index").equals("97")){
                                                z.defaultValue = new ZNodeValue(value.getAttribute("class"),
                                                        value.getAttribute("genre"),
                                                        value.getAttribute("type"),
                                                        1,
                                                        97,
                                                        value.getChildNodes().item(0).getNodeValue());
                                                Log.d("VALUE",""+z.defaultValue.getValue(1));
                                            }
                                        }
                                        break;

                                }
                                //add the found node in the list of found nodes.
                                nodes.add(z);
                                Log.d("NODES LIST", "Added Node: " + id);
                            }

                        }
                    }

                } catch (Exception e) {
                    // if an exception occurred, log it
                    e.printStackTrace();
                }

            }

        }
        catch(IOException e){
            e.printStackTrace();
        }
        return nodes;
        /*client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("SCAN NODES","SCAN FAIL!");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("SCAN NODES","SCAN OK");
                if(response.isSuccessful()){
                    Log.d("SCAN NODES",response.body().string());
                }
                mAVA88GatewayListener.onScanNodesDone(null);
            }
        });
        */
    }

    public void connect(){
        connected=true;
    }

    public boolean isConnected(){
        return connected;
    }
/*
    public ArrayList<String> fetchLocations(){
        int responseCode = 0;
        ArrayList<String> locations = new ArrayList<>();
        locations.add("Ungrouped");
        locations.add("Dashboard");
        MediaType mediaType= MediaType.parse("text/plain; charset=utf-8");
        Request request = new Request.Builder()
                .url("http://"+gatewayHost+":"+ gatewayPort+AVA88Gateway.command.get(COMMAND_LOAD_LOCATION_LIST))
                .get()
                .build();
        try {

            Response response = client.newCall(request).execute();
            Log.d("LOCATION LIST", "Handling list of locations");
            if (response.code() == 200) {
                Log.d("LOCATION LIST", "Response OK 200");
                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputStream is = response.body().byteStream();
                    Document locationDocument = builder.parse(is);
                    locationDocument.getDocumentElement().normalize();
                    // Request will return XML of locations stored in the gateway
                    NodeList items = locationDocument.getElementsByTagName("item");
                    Log.d("LOCATION LIST", "Locations: "+items.getLength());
                    for (int k = 0; k < items.getLength(); k++) {
                        Element item = (Element) items.item(k);
                        String location = item.getAttribute("location");
                        //add to the list of locations all found locations
                        locations.add(location);
                        Log.d("LOCATION LIST", location);
                    }

                } catch (Exception e) {
                    // if an exception occurred, log it
                    Log.e("LOCATION LIST", e.getMessage());
                }

            }

        }
        catch(IOException e){
            e.printStackTrace();
        }
        return locations;
    }

*/
    public interface LocationsCallback {
        void onFetchLocationsDone(List<String> locations);
        void onAddLocationsDone(String location);
        void onAddLocationsConflict(String location);
        void onLocationIdFound(int id);
    }

    public void getLocationId(final String location, final LocationsCallback callback){
        String body = "";
        String uri = AVA88Gateway.command.get(COMMAND_LOAD_LOCATION_LIST);
        Callback mCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("LOCATION LIST","Error fetching Locations!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("LOCATION LIST","Handling list of locations");
                int id=0;
                if(response.code()==200)
                {
                    Log.d("LOCATION LIST", "Response OK 200");
                    try {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        InputStream is = response.body().byteStream();
                        Document locationDocument = builder.parse(is);
                        locationDocument.getDocumentElement().normalize();
                        // Request will return XML of locations stored in the gateway
                        NodeList items = locationDocument.getElementsByTagName("item");
                        Log.d("LOCATION LIST", "Locations: " + items.getLength());
                        for (int k = 0; k < items.getLength(); k++) {
                            Element item = (Element) items.item(k);

                            String newLocation = item.getAttribute("location");
                            if(newLocation.trim().equals(location.trim())) {
                                try {
                                    id = Integer.parseInt(item.getAttribute("id"));
                                    callback.onLocationIdFound(id);
                                }
                                catch(NumberFormatException eo){
                                    eo.printStackTrace();
                                }
                            }
                        }

                    } catch (Exception e) {
                        // if an exception occurred, log it
                        Log.e("LOCATION LIST", e.getMessage());
                    }


                }
            }

        };
        sendCommandGet(uri,mCallback);
    }
    public void fetchLocations(final LocationsCallback callback){
        String body = "";
        String uri = AVA88Gateway.command.get(COMMAND_LOAD_LOCATION_LIST);
        Callback mCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("LOCATION LIST","Error fetching Locations!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("LOCATION LIST","Handling list of locations");
                final ArrayList<String> locations = new ArrayList<>();
                locations.add("Ungrouped");
                if(response.code()==200)
                {
                    Log.d("LOCATION LIST", "Response OK 200");
                    try {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        InputStream is = response.body().byteStream();
                        Document locationDocument = builder.parse(is);
                        locationDocument.getDocumentElement().normalize();
                        // Request will return XML of locations stored in the gateway
                        NodeList items = locationDocument.getElementsByTagName("item");
                        Log.d("LOCATION LIST", "Locations: " + items.getLength());
                        for (int k = 0; k < items.getLength(); k++) {
                            Element item = (Element) items.item(k);
                            String location = item.getAttribute("location");
                            //add to the list of locations all found locations
                            locations.add(location);
                            Log.d("LOCATION LIST", location);
                        }

                    } catch (Exception e) {
                        // if an exception occurred, log it
                        Log.e("LOCATION LIST", e.getMessage());
                    }
                    callback.onFetchLocationsDone(locations);

                }
            }

        };
        sendCommandGet(uri,mCallback);

    }

    public void addLocation(String location, final LocationsCallback callback){
        if (location.trim().equals("Ungrouped"))
        {
            callback.onAddLocationsConflict(location);
            return;
        }
        final String locationName = location.trim();
        String body = "action=add&name="+locationName+"";
        String uri = AVA88Gateway.command.get(COMMAND_ADD_LOCATION);
        //TODO: Add code for handling response
        Callback mCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               // mAVA88GatewayListener.onInclusionExlusionProcessEnded(4,null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int responseCode = response.code();
                if (responseCode==200){
                    callback.onAddLocationsDone(locationName);
                }
                else if(responseCode==409) { //Conflict Location already existing.
                    callback.onAddLocationsConflict(locationName);
                }
            }
        };
        sendCommand(uri,body,mCallback);
    }

    public void removeLocation(int id){
        String body = "action=remove&id="+id+"";
        String uri = AVA88Gateway.command.get(COMMAND_REMOVE_LOCATION);
        //TODO: Add code for handling response
        Callback mCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // mAVA88GatewayListener.onInclusionExlusionProcessEnded(4,null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // mAVA88GatewayListener.onInclusionExlusionProcessEnded(0,null);
            }
        };
        sendCommand(uri,body,mCallback);
    }

    public void changeNodeLocation(ZNode mZNode, String newLocationID){

    }

    public void changeNodeName(ZNode mZNode, String newName){

    }

    public void changeValue(final ZNode znode,
                            ZNodeValue zNodeValue,
                            int instance,
                            final AVA88GatewayListener callback){
        String body = znode.nodeID+"-"
                +zNodeValue.getValueClass()+"-"
                +zNodeValue.getValueGenre()+"-"
                +zNodeValue.getValueType()+"-"
                +instance+"-"
                +zNodeValue.getValueIndex()+"="
                +zNodeValue.getValue(instance);
        //TODO: Add code for sending and receiving request.
        String uri = AVA88Gateway.command.get(COMMAND_CHANGE_VALUE);
        Callback mCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onValueChangeResponseListener(znode,false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onValueChangeResponseListener(znode,true);
            }
        };
        sendCommand(uri,body,mCallback);

    }

    public void startInclusion(final AVA88GatewayListener callback){
        String body = "fun=addd";
        //TODO: Add code for sending and receiving request.
        String uri = AVA88Gateway.command.get(COMMAND_START_ADD_NODE_PROCESS);
        Callback mCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onInclusionExlusionProcessEnded(4,null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onInclusionExlusionProcessEnded(0,null);
            }
        };
        sendCommand(uri,body,mCallback);
    }

    public void startExclusion(final AVA88GatewayListener callback){
        String body = "fun=remd";
        String uri = AVA88Gateway.command.get(COMMAND_START_REMOVE_NODE_PROCESS);
        Callback mCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onInclusionExlusionProcessEnded(4,null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onInclusionExlusionProcessEnded(0,null);
            }
        };
        sendCommand(uri,body,mCallback);
    }

    public void cancelInclusionExclusion(final AVA88GatewayListener callback){
        String body = "fun=cancel";
        //TODO: Add code for sending and receiving request.
        String uri = AVA88Gateway.command.get(COMMAND_CANCEL_ADD_REMOVE_NODE_PROCESS);
        Callback mCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onInclusionExlusionProcessEnded(4,null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onInclusionExlusionProcessEnded(3,null);
            }
        };
        sendCommand(uri,body,mCallback);

    }

    private void sendCommand(String IP,int port, String uri,String body, Callback mCallback){
        sendCommand(IP,port, uri,POST_METHOD,body, mCallback);
    }

    private void sendCommand(String uri,String body,Callback mCallback){
        sendCommand(gatewayHost,gatewayPort, uri,POST_METHOD,body, mCallback);
    }

    private void sendCommandGet(String uri,Callback mCallback){
        sendCommand(gatewayHost,gatewayPort, uri,GET_METHOD,null, mCallback);
    }

    private final static String GET_METHOD = "get";
    private final static String POST_METHOD = "post";
    private void sendCommand(String IP,int port, String uri,String method,@Nullable String body, Callback mCallback){
        //TODO: Add code for sending and receiving request.
        MediaType mediaType= MediaType.parse("text/plain; charset=utf-8");

        Request request;
        if(method.equals("get")) {
            request = new Request.Builder()
                    .url("http://" + IP + ":" + port + uri)
                    .get()
                    .build();
        }
        else{
            request = new Request.Builder()
                    .url("http://" + IP + ":" + port + uri)
                    .post(RequestBody.create(mediaType, body))
                    .build();
        }
        client.newCall(request).enqueue(mCallback);
    }

    private Response sendCommandPolled(String IP,int port, String uri,String body) {
        //TODO: Add code for sending and receiving request.
        MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
        Request request = new Request.Builder()
                .url("http://" + IP + ":" + port + uri)
                .post(RequestBody.create(mediaType, body))
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return response;
    }


    private Response sendCommandPolled(String uri,String body){
        return sendCommandPolled(gatewayHost,gatewayPort, uri,body);
    }




    public class AVA88Command{
        public int commandType;
        public String paramBody;
        public String gatewayHostString;
        public int gatewayPort;
        public AVA88Command(int commandType, String paramBody, String gatewayHostString,int gatewayPort){
            this.commandType = commandType;
            this.paramBody = paramBody;
            this.gatewayHostString=gatewayHostString;
            if(gatewayPort==0){
                gatewayPort=80;
            }
            this.gatewayPort=gatewayPort;
        }
    }

    public interface OnFetchNodesListener{
       void onFetchNodesDone(List<ZNode> mZNodes);
    }

    public void fetchNodes(@Nullable String mLocation,final OnFetchNodesListener handler){
        fetchNodes(mLocation, false,  handler);
    }

    public void fetchNodes(final OnFetchNodesListener handler){
        fetchNodes(null, true,  handler);
    }

    private  void fetchNodes(@Nullable String mLocation, final boolean allDevices, final OnFetchNodesListener handler){
        ArrayList<ZNode> nodes= new ArrayList<>();
        String body = "location="+mLocation;
        boolean isOrphan=false;
        if (mLocation==null||mLocation.trim().length()==0|| mLocation.equals("Ungrouped")) {
            body=" ";
            isOrphan=true;
        }
        if (allDevices){
            isOrphan=false;
            body = " ";

        }
        final boolean getOnlyEmptyLocations=isOrphan;

        String uri = AVA88Gateway.command.get(COMMAND_POLL_TARGET_NODE_STATE);
        Callback mCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //TODO: Handle request error here
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Handle xml response here
                ArrayList<ZNode> nodes= new ArrayList<>();
                Log.d("NODES LIST", "Handling list of nodes");
                if (response.code() == 200) {
                    Log.d("NODES LIST", "Response OK 200");
                    try {

                        //request returns XML of all nodes matching the locations
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        InputStream is = response.body().byteStream();
                        Document nodesDocument = builder.parse(is);
                        nodesDocument.getDocumentElement().normalize();
                        // get all the nodes in XML by checking the name "node"
                        NodeList items = nodesDocument.getElementsByTagName("node");
                        Log.d("NODES LIST", "NODES: "+items.getLength());

                        //Process each values for each nodes
                        for (int k = 0; k < items.getLength(); k++) {
                            Element item = (Element) items.item(k);

                            //get the properties of the each node
                            int id=Integer.parseInt(item.getAttribute("id"));
                            String location = item.getAttribute("location")+"";
                            String btype= item.getAttribute("btype")+"";
                            String gtype= item.getAttribute("gtype")+"";
                            String name = item.getAttribute("name")+"";
                            String manufacturer = item.getAttribute("manufacturer")+"";
                            String product = item.getAttribute("product")+"";
                            String statusStr = item.getAttribute("status")+"";

                            //Build the Node Information
                            ZNode z = new ZNode(id,name, product, manufacturer);
                            try {
                                z.nodeStatus = ZNodeStatus.deviceStatus.get(statusStr);
                            }catch(NullPointerException ex){
                                ex.printStackTrace();
                            }
                            finally {
                                z.nodeStatus=0;
                            }
                            z.nodeStatusString = statusStr;
                            z.nodeBType=btype;
                            z.nodeGType=gtype;
                            z.nodeLocation=location;
                            boolean insertToNodeList=false;
                            if (getOnlyEmptyLocations) {
                                if (location.trim().length()==0)
                                    insertToNodeList=true;
                            }
                            else{
                                insertToNodeList=true;
                            }
                            if (insertToNodeList)  {
                                NodeList values;
                                int prod = ZNodeProduct.nodeZNodeProducttoInt(product);
                                Log.d("NODES LIST", item.getAttribute("id") + " with product id " + prod + " Detected");

                                values = item.getElementsByTagName("value");
                                Log.d("NODES LIST", "Values: " + values.getLength());
                                //iterate all values found in each node
                                for (int j = 0; j < values.getLength(); j++) {
                                    Element value = (Element) values.item(j);
                                    String nodeValueClass = value.getAttribute("class");
                                    String nodeValueGenre = value.getAttribute("genre");
                                    String nodeValueType = value.getAttribute("type");
                                    Log.d("NODES LIST", "Values: class=" + nodeValueClass + "instance " + value.getAttribute("instance"));
                                    int nodeValueInstance;
                                    int nodeValueIndex;
                                    try {
                                        nodeValueInstance = Integer.parseInt(value.getAttribute("instance"));
                                        nodeValueIndex = Integer.parseInt(value.getAttribute("index"));
                                    } catch (NumberFormatException ne) {
                                        ne.printStackTrace();
                                        nodeValueInstance = 1;
                                        nodeValueIndex = 0;
                                    }
                                    String nodeValueValue = "";
                                    if (value.getChildNodes().getLength() > 0)
                                        nodeValueValue = value.getChildNodes().item(0).getNodeValue();

                                    //create a value instance
                                    ZNodeValue myZNodeValue = new ZNodeValue(nodeValueClass,
                                            nodeValueGenre,
                                            nodeValueType,
                                            nodeValueInstance,
                                            nodeValueIndex,
                                            nodeValueValue);
                                    //insert the value to the node with key = class+index
                                    z.addZNodeValue(nodeValueClass + nodeValueIndex, myZNodeValue);
                                }

                                //add the found node in the list of found nodes.
                                nodes.add(z);
                                Log.d("NODES LIST", "Added Node: " + id);
                            }
                        }
                    } catch (Exception e) {
                        // if an exception occurred, log it
                        e.printStackTrace();
                    }
                    handler.onFetchNodesDone(nodes);
                }
            }
        };
        sendCommand(uri,body,mCallback);
    }

    //Interfaces from this Class
    public interface AVA88GatewayListener{
        int INCLUSION_SUCCESS=0;
        int INCLUSION_FAIL=1;
        int EXCLUSION_FAIL=2;
        int INCLUSION_EXCLUSION_CANCELLED=3;
        int INCLUSION_EXCLUSION_IO_ERROR=4;
        void onGetLocationsResponse(List<NodeLocation> locations);
        void onScanNodesDone(List<ZNode> nodes);
        void onValueChangeResponseListener(ZNode mZNode,Boolean isSuccessfull);
        void onInclusionExlusionProcessEnded(int status, @Nullable ZNode node);
    }

}
