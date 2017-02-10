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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "============> TService.onBind");
        return null;
    }

    @Override
    public void onCreate() {
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

        connectToServer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "============> TService.onStartCommand 重启服务");
        return START_STICKY;
    }

    public void onDestroy() {

        if(mSerialManager != null){
            mSerialManager.closeSerial();
        }
        Intent localIntent = new Intent();
        localIntent.setClass(this, TService.class); // 销毁时重新启动Service
        this.startService(localIntent);
    }

    private void connectToServer() {

        mClient = new Client();
        mClient.start();
    }

    private int speed = 0;
    private void sendToServer(){
        if(speed > 1000){
            speed = 0;
        }
//        mClient.sendData(Arrays.toString(bytes));
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
