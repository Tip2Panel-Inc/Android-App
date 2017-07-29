package com.engkan2kit.ava88;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Setsuna F. Seie on 11/07/2017.
 */

public class AVA88GatewayManager {
    private final static String TAG = AVA88GatewayManager.class.getSimpleName();

    public static List<AVA88GatewayInfo> scan(){
        return scan(null);
    }

    public static List<AVA88GatewayInfo> scan(@Nullable AVA88GatewayInfo ava88GatewayInfo){
        Log.d(TAG,"Scanning Gateways in network");
        ArrayList<AVA88GatewayInfo> gatewayIps = new ArrayList<>();
        DatagramSocket c=null;
        try {
            //Open a random port to send the package
            Log.d(TAG,">>>Creating Datagram Socket");
            c = new DatagramSocket(10000);
            c.setBroadcast(true);
            c.setSoTimeout(3000);

            byte[] sendData = "WHOIS_AVA_ZWAVE#".getBytes();

            //Try the 255.255.255.255 first
            try {
                Log.d(TAG,">>>Creating Datagram Packet");
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 10000);
                c.send(sendPacket);
                Log.d(TAG, ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
            }
            catch (Exception e) {
            }

            //Wait for a response
            byte[] recvBuf = new byte[15000];
            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
            c.receive(receivePacket);
            //We have a response
            Log.d(TAG,">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

            //Check if the message is correct
            String message = new String(receivePacket.getData()).trim();
            if (message.contains("RE_WHOIS_AVA")) {
                //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                Log.d(TAG, message);
                Log.d(TAG, "AVA-88 Gateway Discovered!" + receivePacket.getAddress());
                String serverIP = receivePacket.getAddress().getHostAddress().toString();
                //int macIndex=message.indexOf("mac=");
                //String mac= message.substring(macIndex+4,message.indexOf('&',macIndex));
                AVA88GatewayInfo mAVA88GatewayInfo = new AVA88GatewayInfo(message + "&ipv4=" + serverIP + "&");
                if (ava88GatewayInfo==null){
                    gatewayIps.add(mAVA88GatewayInfo);
                }else {
                    if (ava88GatewayInfo.hardwareAddress.equals(mAVA88GatewayInfo.hardwareAddress)) {
                        gatewayIps.add(mAVA88GatewayInfo);
                        return gatewayIps;
                    }
                }
            }
            c.receive(receivePacket);
            //We have a response
            Log.d(TAG,">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

            //Check if the message is correct
            message = new String(receivePacket.getData()).trim();
            if (message.contains("RE_WHOIS_AVA")) {
                //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                Log.d(TAG,message);
                Log.d(TAG,"AVA-88 Gateway Discovered!" + receivePacket.getAddress());
                String serverIP = receivePacket.getAddress().getHostAddress().toString();
                //int macIndex=message.indexOf("mac=");
                //String mac= message.substring(macIndex+4,message.indexOf('&',macIndex));

                AVA88GatewayInfo mAVA88GatewayInfo = new AVA88GatewayInfo(message + "&ipv4=" + serverIP + "&");
                if (ava88GatewayInfo==null){
                    gatewayIps.add(mAVA88GatewayInfo);
                }else {
                    if (ava88GatewayInfo.hardwareAddress.equals(mAVA88GatewayInfo.hardwareAddress)) {
                        gatewayIps.add(mAVA88GatewayInfo);
                        return gatewayIps;
                    }
                }

            }
            c.close();
        } catch (IOException ex) {
            Log.d(TAG, ex.toString());
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

        return gatewayIps;
    }
}
