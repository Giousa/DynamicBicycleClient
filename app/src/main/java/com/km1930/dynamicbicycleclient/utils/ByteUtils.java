package com.km1930.dynamicbicycleclient.utils;

import java.util.Arrays;

/**
 * 负责在byte和十六进制字符串之间进行转换的一个工具
 *
 * @author lwq
 */
public class ByteUtils {

    private static final char[] CHARS = new char[] { '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    /**
     * 把字节数组转换成十六进制字符串，两个字符表示一个字节。
     *
     * @param b
     * @return
     */
    public static String getHexString(byte[] b) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[ i] & 0xFF);
            if (hex.length() == 1) {
                builder.append("0");
            }
            builder.append(hex);
        }
        String result = builder.toString();
        result = result.toUpperCase();
        return result;
    }

    /**
     * 把十六进制字符串还原成，字符串中每两个字符表示一个字节
     *
     * @param hexString
     * @return
     */
    public static byte[] getByteArray(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            byte b = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            if(b < 0){
                d[i] = (byte) (128+b);
            }else{
                d[i] = b;
            }
//            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
//            if(d[i]<0){
//                d[i] = (byte) (256+d[i]);
//            }
        }
        return d;
    }

    private static byte charToByte(char c) {
        for (int i = 0; i < CHARS.length; i++) {
            if (c == CHARS[i]) {
                return (byte) i;
            }
        }
        return Byte.MIN_VALUE;
    }

    public static void main(String[] args) {
        byte[] bs = new byte[]{1, 2, 3, 4, -1};
        String str = getHexString(bs);
        System.out.println(str);
        byte[] b = getByteArray(str);
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i]);
        }
        System.out.println();
        byte[] test = new byte[1];
        test[0] = 15;
        System.out.println("测试:"+getHexString(test));
        System.out.println("测试:"+ Arrays.toString(getByteArray(getHexString(test))));
    }
}
