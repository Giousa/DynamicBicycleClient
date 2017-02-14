package com.km1930.dynamicbicycleclient.serialndk;

import android.os.Handler;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Description:
 * Author：Giousa
 * Date：2016/8/15
 * Email：giousa@chinayoutu.com
 */
public class SerialManager {

    private final String TAG = SerialManager.class.getSimpleName();
    private static SerialManager mSerialManager;
    private final Timer timer = new Timer();
    private TimerTask task;

    public static SerialManager getInstance() {
        Log.d("SerialManager", "getInstance");
        if (mSerialManager == null) {
            mSerialManager = new SerialManager();
        }

        return mSerialManager;

    }

    public interface SerialValueChangeListener{
        void onSerialValueChangeListener(int speed, int angle) throws Exception;
    }

    private SerialValueChangeListener mSerialValueChangeListener;

    public void setSerialValueChangeListener(SerialValueChangeListener serialValueChangeListener) {
        mSerialValueChangeListener = serialValueChangeListener;
    }

    private Handler mHandler = new Handler();
    private Runnable mTimerRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                readSerial();
                mHandler.postDelayed(mTimerRunnable, 200);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void openSerial() {
        Log.d(TAG, "openSerial");
        Serial.OpenSerial(4);
        setSerialBaud(555);
        mHandler.postDelayed(mTimerRunnable, 20);
    }

    public void closeSerial() {
        mHandler.removeCallbacks(mTimerRunnable);
        Serial.CloseSerial();

    }

    public void setSerialBaud(long baud) {
        Serial.SetSerialBaud(baud);
    }

    public void readSerial() throws Exception {

        short[] mShorts = new short[9];

        int serialBuf = Serial.ReadSerialBuf(mShorts, 9);
        Log.d(TAG, "serialBuf:" + serialBuf);

        if(serialBuf == 9){
            parseSerialData(mShorts);
        }
    }

    private int mSpeed = 0;
    private int mAngle = 15;

    private void parseSerialData(short[] mShorts) throws Exception {

        int speed = mShorts[2];
        speed = (Math.round(speed*100))/100;
        Log.d(TAG,"serialmanager speed="+speed);

        int angle = mShorts[3];

        if (mSpeed != speed || mAngle != angle) {
            mSpeed = speed;
            mAngle = angle;

            if(mSerialValueChangeListener != null){
                mSerialValueChangeListener.onSerialValueChangeListener(mSpeed,mAngle);
            }
        }

    }

    public void writeSerial(short[] WriteBuf) {
        Log.d(TAG,"writeSerial:"+WriteBuf[2]+"     length:"+WriteBuf.length);
        Serial.WriteSerialBuf(WriteBuf, WriteBuf.length);
    }
}