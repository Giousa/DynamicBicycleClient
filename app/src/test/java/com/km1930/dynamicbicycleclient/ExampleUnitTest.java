package com.km1930.dynamicbicycleclient;

import android.util.Log;

import com.km1930.dynamicbicycleclient.client.Client;
import com.km1930.dynamicbicycleclient.model.IntelDevice;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private boolean isPause = false;
    private static int delay = 5000;
    private static int period = 5000;
    private Client mClient;

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void startClient() throws Exception{
        mClient = new Client("127.0.0.1");
        mClient.start();
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
                    Log.d("UnitTest","timer start");
                    sendToServer();
                    do {
                        try {
                            Log.i("UnitTest", "sleep(5000)...");
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

    private void stopTimer(){

        Log.d("UnitTest","timer end");

        if(mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }

        if(mTimerTask != null){
            mTimerTask.cancel();
            mTimerTask = null;
        }

    }

}