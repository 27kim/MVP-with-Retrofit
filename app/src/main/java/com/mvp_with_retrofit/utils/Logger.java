package com.mvp_with_retrofit.utils;

import android.util.Log;

import com.mvp_with_retrofit.BuildConfig;

public class Logger {
    public static void LOGV(String msg) {
        Logger(Log.VERBOSE, msg);
    }

    public static void LOGD(String msg) {
        Logger(Log.DEBUG, msg);
    }

    public static void LOGI(String msg) {
        Logger(Log.INFO, msg);
    }

    public static void LOGW(String msg) {
        Logger(Log.WARN, msg);
    }

    public static void LOGE(String msg) {
        Logger(Log.ERROR, msg);
    }

    private static void Logger(int priority, String msg) {
        if (BuildConfig.DEBUG) {
            StringBuilder msgBuilder = new StringBuilder();
            msgBuilder
                    .append("***SAMPLEAPP***")
                    .append("[").append(Thread.currentThread().getStackTrace()[4].getMethodName())
                    .append("()").append("]").append(" :: ").append(msg)
                    .append(" (").append(Thread.currentThread().getStackTrace()[4].getFileName()).append(":")
                    .append(Thread.currentThread().getStackTrace()[4].getLineNumber()).append(")");

            Log.println(priority, Thread.currentThread().getStackTrace()[4].getFileName().replace(".java", ""), msgBuilder.toString());
        }
    }
}
