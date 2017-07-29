package com.engkan2kit.ava88;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Setsuna F. Seie on 28/04/2017.
 */

public class AVA88GatewayScanner extends AsyncTask<AVA88GatewayInfo,Void,ArrayList<AVA88GatewayInfo>> {
    private int response = 0;
    private Context mContext;
    private AVA88GatewayInfo infoToCompare;
    Boolean found = false;
    private GatewayScannerListener mGatewayScannerListener=null;

    public interface GatewayScannerListener {
        void onGatewayScannerDone(ArrayList<AVA88GatewayInfo> gatewayIPs);
        void onGatewayFound(AVA88GatewayInfo mAVA88GatewayInfo);
        void onGatewayNotFound();
    }

    public AVA88GatewayScanner(Context context,GatewayScannerListener gatewayScannerListener ){
        this.mContext = context;
        this.mGatewayScannerListener = gatewayScannerListener;
    }

    @Override
    protected ArrayList<AVA88GatewayInfo> doInBackground(AVA88GatewayInfo... params) {
        Log.d("AVA-Discovery","Checking Internet!");
        infoToCompare = params[0];
        ArrayList<AVA88GatewayInfo> gatewayIps = new ArrayList<>();
        ConnectivityManager connectionManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiCheck;
        wifiCheck = connectionManager.getActiveNetworkInfo();
        if (wifiCheck!=null) {
            Log.d("AVA-Discovery","internet");
            //if wifi is connected, look for online gateway
            if (wifiCheck.getType() == ConnectivityManager.TYPE_WIFI) {
                Log.d("AVA-Discovery","WIFI internet");
                //TODO: Search local network for valid AVA-88 gateway.
                DatagramSocket c=null;
                try {
                    //Open a random port to send the package
                    Log.d("AVA-Discovery",">>>Creating Datagram Socket");
                    c = new DatagramSocket(10000);
                    c.setBroadcast(true);
                    c.setSoTimeout(3000);

                    byte[] sendData = "WHOIS_AVA_ZWAVE#".getBytes();

                    //Try the 255.255.255.255 first
                    try {
                        Log.d("AVA-Discovery",">>>Creating Datagram Packet");
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 10000);
                        c.send(sendPacket);
                        Log.d("AVA Discovery", ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
                    }
                    catch (Exception e) {
                    }

                    //Wait for a response
                    byte[] recvBuf = new byte[15000];
                    DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                    c.receive(receivePacket);
                    if (isCancelled()){
                        c.close();
                        return null;
                    }
                    //We have a response
                    Log.d("AVA Discovery",">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

                    //Check if the message is correct
                    String message = new String(receivePacket.getData()).trim();
                    if (message.contains("RE_WHOIS_AVA")) {
                        //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                        Log.d("AVA Discovery", message);
                        Log.d("AVA Discovery", "AVA-88 Gateway Discovered!" + receivePacket.getAddress());
                        String serverIP = receivePacket.getAddress().getHostAddress().toString();
                        //int macIndex=message.indexOf("mac=");
                        //String mac= message.substring(macIndex+4,message.indexOf('&',macIndex));
                        AVA88GatewayInfo mAVA88GatewayInfo = new AVA88GatewayInfo(message + "&ipv4=" + serverIP + "&");
                        if (infoToCompare==null){
                            gatewayIps.add(mAVA88GatewayInfo);
                        }else {
                            if (infoToCompare.hardwareAddress.equals(mAVA88GatewayInfo.hardwareAddress)) {
                                gatewayIps.add(mAVA88GatewayInfo);
                                found = true;
                                return gatewayIps;
                            }
                        }


                    }
                    c.receive(receivePacket);
                    //We have a response
                    Log.d("AVA Discovery",">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

                    //Check if the message is correct
                    message = new String(receivePacket.getData()).trim();
                    if (message.contains("RE_WHOIS_AVA")) {
                        //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                        Log.d("AVA Discovery",message);
                        Log.d("AVA Discovery","AVA-88 Gateway Discovered!" + receivePacket.getAddress());
                        String serverIP = receivePacket.getAddress().getHostAddress().toString();
                        //int macIndex=message.indexOf("mac=");
                        //String mac= message.substring(macIndex+4,message.indexOf('&',macIndex));

                        AVA88GatewayInfo mAVA88GatewayInfo = new AVA88GatewayInfo(message + "&ipv4=" + serverIP + "&");
                        if (infoToCompare==null){
                            gatewayIps.add(mAVA88GatewayInfo);
                        }else {
                            if (infoToCompare.hardwareAddress.equals(mAVA88GatewayInfo.hardwareAddress)) {
                                gatewayIps.add(mAVA88GatewayInfo);
                                found = true;
                                return gatewayIps;
                            }
                        }

                    }
                    c.close();
                } catch (IOException ex) {
                    Log.d("AVA-Discovery", ex.toString());
                }
                finally {
                    if(c!=null){
                        try {
                            c.close();
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }


        }
        return gatewayIps;
    }



    protected void onPostExecute(ArrayList<AVA88GatewayInfo> gatewayIPs){
        //Scanner response.
        //IF mac length ==12 this is an IP query
        if (infoToCompare!=null){
            if (found)
                mGatewayScannerListener.onGatewayFound(gatewayIPs.get(0));
            else
                mGatewayScannerListener.onGatewayNotFound();
        }
        else {
            if (mGatewayScannerListener != null) {
                Log.d("AVA Discovery", gatewayIPs.toString());
            }
            mGatewayScannerListener.onGatewayScannerDone(gatewayIPs);
        }

    }

}
