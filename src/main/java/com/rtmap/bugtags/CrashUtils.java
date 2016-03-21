package com.rtmap.bugtags;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TimeZone;

/**
 * Created by silver on 16-3-17.
 */
public class CrashUtils {

    public static void saveCrashInfoToSDCard(final Context context, Throwable throwable, Thread thread) {
        StringBuilder crashInfoStringBuilder = new StringBuilder();

        //获取Crash时间
        String crashTime = getCrashTime();
        crashInfoStringBuilder.append("------------------" + "\n");
        crashInfoStringBuilder.append(crashTime + "\n");
        crashInfoStringBuilder.append("------------------" + "\n");

        //获取Crash时设备及该App的基本信息
        LinkedHashMap<String, String> mobileParamsMap = getBaseInfo(context);
        for (Map.Entry<String, String> entry : mobileParamsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            crashInfoStringBuilder.append(key).append("=").append(value).append("\n");
        }
        crashInfoStringBuilder.append("------------------" + "\n");

        //获取导致Crash的时间
        String uncaughtException = getUncaughtException(throwable);
        crashInfoStringBuilder.append(uncaughtException + "\n");
        crashInfoStringBuilder.append("------------------" + "\n");


        final Map<String, String> paramsMap = new LinkedHashMap<>();
        paramsMap.put("crash", uncaughtException);
        paramsMap.put("date_time", crashTime);
        paramsMap.put("app_thread", thread.getName());
        paramsMap.put("exception_class", throwable.getClass().toString() + "");
        paramsMap.put("date_time", crashTime);
        paramsMap.putAll(mobileParamsMap);

        new Thread(new Runnable() {
            @Override
            public void run() {
                CrashHttp.uploadCrash(paramsMap, context);
            }
        }).start();

        System.out.println("crashInfo如下:" + "\n" + crashInfoStringBuilder.toString());
    }

    public void crashException(Throwable throwable) {


        if (throwable instanceof IOException) {

            if (throwable instanceof EOFException) {

            }

            if (throwable instanceof FileNotFoundException) {

            }

            if (throwable instanceof IntentFilter.MalformedMimeTypeException) {

            }

            if (throwable instanceof UnknownHostException) {

            }

        }


        if (throwable instanceof ClassNotFoundException) {

        }

        if (throwable instanceof CloneNotSupportedException) {

        }

        if (throwable instanceof RuntimeException) {

            if (throwable instanceof ArithmeticException) {

            }

            if (throwable instanceof ClassCastException) {

            }

            if (throwable instanceof IllegalArgumentException) {

            }

            if (throwable instanceof IndexOutOfBoundsException) {

            }

            if (throwable instanceof NoSuchElementException) {

            }

            if (throwable instanceof NullPointerException) {

            }

            if (throwable instanceof ActivityNotFoundException) {

            }

        }


    }


    /**
     * 获取设备及该App的基本信息
     */
    public static LinkedHashMap<String, String> getBaseInfo(Context context) {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        hashMap.put("app_id", CrashHandler.getInstance().getApp_id());
        hashMap.put("versionName", packageInfo.versionName);
        hashMap.put("versionCode", packageInfo.versionCode + "");

        hashMap.put("model", Build.MODEL + "(" + Build.MANUFACTURER + ")");
        hashMap.put("sdk_int", Build.VERSION.SDK_INT + "");
        hashMap.put("app_release", Build.VERSION.RELEASE + "");
        hashMap.put("product", Build.PRODUCT + "");

        return hashMap;
    }

    /**
     * 获取造成Crash的异常的具体信息
     */
    public static String getUncaughtException(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        String uncaughtException = stringWriter.toString();
        return uncaughtException;

    }

    /**
     * 获取Crash的时间
     */
    public static String getCrashTime() {
        String currentTime = "";
        long currentTimeMillis = System.currentTimeMillis();
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(timeZone);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date(currentTimeMillis);
        currentTime = simpleDateFormat.format(currentDate);
        return currentTime;
    }

}
