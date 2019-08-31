package com.dckj.jiuzhu.module.util;

import android.util.Log;

public class LogUtils {

    private static final boolean DEBUG = true;

    private static final String APP_TAG = "jiuzhu";
    public static void i(String className,String method,String message){
        if (DEBUG){
            Log.i(APP_TAG, className + " " + method + " " + message);
        }
    }
}
