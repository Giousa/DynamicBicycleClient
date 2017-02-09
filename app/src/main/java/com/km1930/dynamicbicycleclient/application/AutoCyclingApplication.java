package com.km1930.dynamicbicycleclient.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Created by liuenbao on 1/22/16.
 */
public class AutoCyclingApplication extends Application {

    private static final String LAST_VERSION_CODE = "last_version_code";

    private static final String TAG = AutoCyclingApplication.class.getSimpleName();
    //全局上下文环境
    private static Context mContext;
    //全局的handler
    private static Handler mHandler;
    //主线程
    private static Thread mMainThread;
    //主线程id
    private static int mMainThreadId;

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        mHandler = new Handler();

        //MyApplication运行在主线程中,所以拿当前线程对象即可
        mMainThread = Thread.currentThread();

        //主线程id,就是MyApplication(主线程)线程id,获取当前线程id
        mMainThreadId = android.os.Process.myTid();

//        exeShell();

    }

    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

//    private void exeShell() {
//        changeSerialPortMode("/dev/ttyS3", "777");
//        openSerialPort("/dev/ttyS3");
//    }
//
//    public void changeSerialPortMode(String serialPortName, String mode) {
//        try {
//            Process sh = Runtime.getRuntime().exec("/system/adbbin/su");
//
//            String changeModeCommand = "chmod " + mode + " " + serialPortName;
//            OutputStream os = sh.getOutputStream();
//
//            Log.d(TAG, "Chage mode command : " + changeModeCommand);
//
//            writeCommand(os, changeModeCommand);
//
//            String exitCommand = "exit";
//
//            Log.d(TAG, "Exit Comamnd 1");
//
//            writeCommand(os, exitCommand);
//
//            if (sh.waitFor() != 0) {
//                Log.d(TAG, "Fuck fuck fuck");
//            }
//
//            Log.d(TAG, "Done Done Done");
//
//            os.close();
//
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//    }
//
//    private void openSerialPort(String serialPortName) {
//        try {
//            SerialPort serialPort = new SerialPort(new File(serialPortName));
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    static void writeCommand(OutputStream os, String command) throws Exception {
//        os.write((command + "\n").getBytes("ASCII"));
//        os.flush();
//    }
}
