package com.km1930.dynamicbicycleclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.km1930.dynamicbicycleclient.client.Client;
import com.km1930.dynamicbicycleclient.model.DeviceValue;
import com.km1930.dynamicbicycleclient.model.TypeData;
import com.km1930.dynamicbicycleclient.serialndk.SerialManager;

/**
 * Description:
 * Author：Giousa
 * Date：2016/8/3
 * Email：giousa@chinayoutu.com
 */
public class TService extends Service implements SerialManager.SerialValueChangeListener, Client.ChannelChangeListener {

    private static final String TAG = TService.class.getSimpleName();
    private SerialManager mSerialManager;
    private Client mClient;
    private int mSeatId = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "============> TService.onBind");
        return null;
    }

    @Override
    public void onCreate() {
        openSerial();
        super.onCreate();
    }

    private void openSerial() {
        mSerialManager = SerialManager.getInstance();
        mSerialManager.openSerial();
        mSerialManager.setSerialValueChangeListener(this);

        mClient = new Client();
        mClient.start();
        mClient.setChannelChangeListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "============> TService.onStartCommand 重启服务");
        return START_STICKY;
    }

    @Override
    public void onSerialValueChangeListener(int speed, int angle) throws Exception {
        System.out.println("onSerialValueChangeListener  "+"speed"+speed+"  angle="+angle);
        DeviceValue s = new DeviceValue();
        s.setType(TypeData.CUSTOME);
        s.setSpeed(speed);
        s.setAngle(angle);
        s.setSeatId(mSeatId);
        mClient.sendData(s);
    }

    public void onDestroy() {

        if(mSerialManager != null){
            mSerialManager.closeSerial();
        }
        Intent localIntent = new Intent();
        localIntent.setClass(this, TService.class);
        this.startService(localIntent);
    }

    private short[] mNoweData = new short[9];

    @Override
    public void onChannelChangeListener(int resistance) {
        System.out.println("TSerivice resistance="+resistance);
        if (resistance >= 100) {
            resistance = 99;
        }

        mNoweData[0] = 0x55;
        mNoweData[1] = 0x02;
        mNoweData[2] = (short) resistance;
        mNoweData[3] = 0x00;
        mNoweData[4] = 0x00;
        mNoweData[5] = 0x00;
        mNoweData[6] = 0x00;
        mNoweData[7] = 0x00;
        mNoweData[8] = 0xAA;

        mSerialManager.writeSerial(mNoweData);
    }
}
