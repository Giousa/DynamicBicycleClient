package com.km1930.dynamicbicycleclient.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:16/10/1
 * Time:上午10:21
 */
public class SharedPreferencesUtil {

    private static SharedPreferences sp;

    private final static String SP_NAME = "config";

    /**
     * 保存String类型信息
     * @param context
     * @param key
     * @param value
     */
    public static void saveString(Context context, String key, String value){
        if (sp==null) {
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }


    /**
     * 获取String类型信息
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(Context context, String key, String defValue){
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }

}
