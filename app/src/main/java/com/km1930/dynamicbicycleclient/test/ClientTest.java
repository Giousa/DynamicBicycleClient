package com.km1930.dynamicbicycleclient.test;

import android.util.Log;

import com.km1930.dynamicbicycleclient.client.Client;
import com.km1930.dynamicbicycleclient.model.IntelDevice;

import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Description:
 * Author:Giousa
 * Date:2017/2/10
 * Email:65489469@qq.com
 */
public class ClientTest {

    private static Timer mTimer = null;
    private static TimerTask mTimerTask = null;
    private static boolean isPause = false;
    private static int delay = 5000;
    private static int period = 5000;
    private static Client mClient;

    public static void main(String[] args) {
        mClient = new Client("127.0.0.1");
        mClient.start();
        startTimer();
    }

    private static void startTimer(){
        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    sendToServer();
                    do {
                        try {
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

    private static int speed = 0;
    private static void sendToServer(){
        if(speed > 1000){
            speed = 0;
        }
        byte[] bytes = new IntelDevice.Builder().deviceId("UT01").speed(speed++).build().encode();
        System.out.println("bytes speed="+ Arrays.toString(bytes));
        try {
            IntelDevice intelDevice = IntelDevice.ADAPTER.decode(bytes);
            System.out.println("IntelDevice:"+intelDevice);
            System.out.println("IntelDevice  deviceId:"+intelDevice.deviceId);
            System.out.println("IntelDevice  speed:"+intelDevice.speed);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mClient.sendData(Arrays.toString(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
