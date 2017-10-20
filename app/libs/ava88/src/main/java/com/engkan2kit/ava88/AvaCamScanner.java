package com.engkan2kit.ava88;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by Setsuna F. Seie on 28/04/2017.
 */

public class AvaCamScanner extends AsyncTask<AvaCamInfo,Void,ArrayList<AvaCamInfo>> {
    private int response = 0;
    private Context mContext;
    private AvaCamInfo infoToCompare;
    Boolean found = false;
    private AvaCamScannerListener mAvaCamScannerListener=null;

    public interface AvaCamScannerListener {
        void onAvaCamScannerDone(ArrayList<AvaCamInfo> avaCamInfos);
        void onAvaCamFound(AvaCamInfo mAvaCamInfo);
        void onAvaCamNotFound();
    }

    public AvaCamScanner(Context context, AvaCamScannerListener avaCamScannerListener ){
        this.mContext = context;
        this.mAvaCamScannerListener = avaCamScannerListener;
    }

    @Override
    protected ArrayList<AvaCamInfo> doInBackground(AvaCamInfo... params) {
        Log.d("AVA-Discovery","Checking Internet!");
        infoToCompare = params[0];
        ArrayList<AvaCamInfo> avaCamInfos = new ArrayList<>();
        ConnectivityManager connectionManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiCheck;
        wifiCheck = connectionManager.getActiveNetworkInfo();
        if (wifiCheck!=null) {
            Log.d("AVA-CAM Discovery","internet");
            //if wifi is connected, look for online gateway
            if (wifiCheck.getType() == ConnectivityManager.TYPE_WIFI) {
                Log.d("AVA-CAM Discovery","WIFI internet");
                //TODO: Search local network for valid AVA-88 gateway.
                DatagramSocket c=null;
                try {
                    //Open a random port to send the package
                    Log.d("AVA-Discovery",">>>Creating Datagram Socket");
                    c = new DatagramSocket(10000);
                    c.setBroadcast(true);
                    c.setSoTimeout(3000);

                    byte[] sendData = "WHOIS_AVA_CAM#".getBytes();

                    //Try the 255.255.255.255 first
                    try {
                        Log.d("AVA-CAM Discovery",">>>Creating Datagram Packet");
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 10000);
                        c.send(sendPacket);
                        Log.d("AVA-CAM Discovery", ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
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
                    Log.d("AVA-CAM Discovery",">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

                    //Check if the message is correct
                    String message = new String(receivePacket.getData()).trim();
                    if (message.contains("RE_WHOIS_AVA_CAM")) {
                        //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                        Log.d("AVA-CAM Discovery", message);
                        Log.d("AVA-CAM Discovery", "AVA Cam Discovered!" + receivePacket.getAddress());
                        String serverIP = receivePacket.getAddress().getHostAddress().toString();
                        //int macIndex=message.indexOf("mac=");
                        //String mac= message.substring(macIndex+4,message.indexOf('&',macIndex));
                        AvaCamInfo mAvaCamInfo = new AvaCamInfo(message + "&ipv4=" + serverIP + "&");
                        if (infoToCompare==null){
                            avaCamInfos.add(mAvaCamInfo);
                        }else {
                            if (infoToCompare.hardwareAddress.equals(mAvaCamInfo.hardwareAddress)) {
                                avaCamInfos.add(mAvaCamInfo);
                                found = true;
                                return avaCamInfos;
                            }
                        }


                    }
                    c.receive(receivePacket);
                    //We have a response
                    Log.d("AVA-CAM Discovery",">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());

                    //Check if the message is correct
                    message = new String(receivePacket.getData()).trim();
                    if (message.contains("RE_WHOIS_AVA_CAM")) {
                        //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                        Log.d("AVA-CAM Discovery", message);
                        Log.d("AVA-CAM Discovery", "AVA Cam Discovered!" + receivePacket.getAddress());
                        String serverIP = receivePacket.getAddress().getHostAddress().toString();
                        //int macIndex=message.indexOf("mac=");
                        //String mac= message.substring(macIndex+4,message.indexOf('&',macIndex));

                        AvaCamInfo mAvaCamInfo = new AvaCamInfo(message + "&ipv4=" + serverIP + "&");
                        if (infoToCompare==null){
                            avaCamInfos.add(mAvaCamInfo);
                        }else {
                            if (infoToCompare.hardwareAddress.equals(mAvaCamInfo.hardwareAddress)) {
                                avaCamInfos.add(mAvaCamInfo);
                                found = true;
                                return avaCamInfos;
                            }
                        }

                    }
                    c.close();
                } catch (IOException ex) {
                    Log.d("AVA-CAM Discovery", ex.toString());
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
        return avaCamInfos;
    }



    protected void onPostExecute(ArrayList<AvaCamInfo> avaCamInfos){
        //Scanner response.
        //IF mac length ==12 this is an IP query
        if (infoToCompare!=null){
            if (found)
                mAvaCamScannerListener.onAvaCamFound(avaCamInfos.get(0));
            else
                mAvaCamScannerListener.onAvaCamNotFound();
        }
        else {
            if (mAvaCamScannerListener != null) {
                Log.d("AVA Discovery", avaCamInfos.toString());
            }
            mAvaCamScannerListener.onAvaCamScannerDone(avaCamInfos);
        }

    }

}
