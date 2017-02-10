package com.km1930.dynamicbicycleclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.km1930.dynamicbicycleclient.client.Client;
import com.km1930.dynamicbicycleclient.serialndk.SerialManager;
import com.km1930.dynamicbicycleclient.utils.SharedPreferencesUtil;
import com.km1930.dynamicbicycleclient.utils.UIUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Description:
 * Author：Giousa
 * Date：2016/8/3
 * Email：giousa@chinayoutu.com
 */
public class TService extends Service implements SerialManager.SerialSpeedChangeListener,
        SerialManager.SerialAngleChangeListener {

    private static final String TAG = TService.class.getSimpleName();
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private boolean isPause = false;
    private static int delay = 5000;
    private static int period = 5000;
    private static final int HOST_IP = 110;
    private static final int COACH_CONNECT = 111;
    private final static String SP_NAME = "CONFIG_IP";
    private String mConfigIP;
    private boolean achievedIP = false;
    private SerialManager mSerialManager;
    private Client mClient;
    private String mDeviceId = "UT01";


    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case HOST_IP:
                    Log.d(TAG,"HOST_IP"+HOST_IP);
                    if(!achievedIP){
                        achieveHostIP();
                    }
                    break;
                case COACH_CONNECT:
                    Log.d(TAG,"COACH_CONNECT"+COACH_CONNECT);
                    if(mConfigIP != null){
                        connectToServer(mConfigIP);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "============> TService.onBind");
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "============> TService.onCreate");
        achievedIP = false;
        mConfigIP = SharedPreferencesUtil.getString(UIUtils.getContext(), SP_NAME, "");
        openSerial();
        super.onCreate();
    }

    private void openSerial() {
//        mSerialManager = SerialManager.getInstance();
//        mSerialManager.openSerial();
//        mSerialManager.setSerialSpeedChangeListener(this);
//        mSerialManager.setSerialAngleChangeListener(this);
//        startTimer();

        connectToServer("192.168.0.108");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "============> TService.onStartCommand 重启服务");
//        flags = START_STICKY;
//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(TAG, "============> TService.onStart");
    }


    private void achieveHostIP() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"achieve host ip");
                int port = 9999;
                DatagramSocket ds = null;
                DatagramPacket dp = null;
                byte[] buf = new byte[1024];
                StringBuffer sbuf = new StringBuffer();
                try {
                    ds = new DatagramSocket(port);
                    dp = new DatagramPacket(buf, buf.length);
                    Log.d(TAG,"监听广播端口打开：");
                    ds.receive(dp);
                    ds.close();
                    int i;
                    for(i=0;i<1024;i++){
                        if(buf[i] == 0){
                            break;
                        }
                        sbuf.append((char) buf[i]);
                    }

                    if(sbuf != null){
                        String mConfigServerIP = sbuf.toString();
                        Log.d(TAG,"收到广播: "+mConfigServerIP);
                        if(!mConfigServerIP.equals(mConfigIP) && !mConfigServerIP.isEmpty()){
                            SharedPreferencesUtil.saveString(UIUtils.getContext(),SP_NAME,mConfigServerIP);
                            achievedIP =  true;
                        }
                        mConfigIP = mConfigServerIP;
                    }

                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }



    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "============> TService.onUnbind");
        return false;
    }

    public void onRebind(Intent intent) {
        Log.d(TAG, "============> TService.onRebind");
    }

    public void onDestroy() {
        Log.d(TAG, "============> TService.onDestroy");
        stopTimer();
        if(mSerialManager != null){
            mSerialManager.closeSerial();
        }
        Intent localIntent = new Intent();
        localIntent.setClass(this, TService.class); // 销毁时重新启动Service
        this.startService(localIntent);
    }

    private void connectToServer(String ip) {

        Log.d(TAG,"connectToServerIP:"+ip);
        mClient = new Client(ip);
        mClient.start();
//        stopTimer();
        startTimer();

    }

    private void startTimer(){
        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Log.d(TAG,"timer start");
                    sendToServer();
//                    sendMessage(HOST_IP);
//                    sendMessage(COACH_CONNECT);
                    do {
                        try {
                            Log.i(TAG, "sleep(5000)...");
                            Thread.sleep(delay);
                        } catch (InterruptedException e) {
                        }
                    } while (isPause);

                }
            };
        }

        if(mTimer != null && mTimerTask != null )
            mTimer.schedule(mTimerTask, delay, period);

    }

    private int speed = 0;
    private void sendToServer(){
        if(speed > 1000){
            speed = 0;
        }
//        mClient.sendData(Arrays.toString(bytes));
    }

    private void stopTimer(){

        Log.d(TAG,"timer end");

        if(mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }

        if(mTimerTask != null){
            mTimerTask.cancel();
            mTimerTask = null;
        }

    }

    public void sendMessage(int id){
        if (mHandler != null) {
            Message message = Message.obtain(mHandler, id);
            mHandler.sendMessage(message);
        }
    }



    @Override
    public void onSerialSpeedChanged(int speed) {
        Log.d(TAG, "onSerialSpeedChanged：" + speed);

    }

    @Override
    public void onSerialAngleChanged(int angle) {
        Log.d(TAG, "onSerialAngleChanged：" + angle);
    }
}
