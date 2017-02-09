package com.km1930.dynamicbicycleclient.serialndk;

/**
 * Description:
 * Author：Giousa
 * Date：2016/8/15
 * Email：giousa@chinayoutu.com
 */
public class Serial {
    public static native int OpenSerial(int num);
    public static native void SetSerialBaud(long baud);
    public static native int ReadSerialBuf(short[] ReadBuf,int ReadLen);
    public static native int WriteSerialBuf(short[] WriteBuf,int WriteLen);
    public static native int CloseSerial();

    static {
        System.loadLibrary("Serial");
    }
}
