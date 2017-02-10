package com.km1930.dynamicbicycleclient.test;

import com.km1930.dynamicbicycleclient.client.Client;
import com.km1930.dynamicbicycleclient.model.DeviceValue;
import com.km1930.dynamicbicycleclient.model.TypeData;

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
    private static int delay = 100;
    private static int period = 100;
    private static Client mClient;

    public static void main(String[] args) {
        mClient = new Client();
        mClient.start();
//        startTimer();
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
        DeviceValue s = new DeviceValue();
        s.setType(TypeData.CUSTOME);
        s.setSpeed(speed++);
        s.setAngle(15);
        s.setDeviceName("UT01");
        try {
            mClient.sendData(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
